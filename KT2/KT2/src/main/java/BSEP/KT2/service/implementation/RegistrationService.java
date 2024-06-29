package BSEP.KT2.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import BSEP.KT2.dto.ClientRegistrationRequestCreationDto;
import BSEP.KT2.dto.ClientRegistrationRequestRejectionDto;
import BSEP.KT2.dto.ClientRegistrationRequestViewDto;
import BSEP.KT2.dto.UserActivationDto;
import BSEP.KT2.model.ClientRegistrationRequest;
import BSEP.KT2.model.User;
import BSEP.KT2.model.enums.ClientRegistrationRequestStatus;
import BSEP.KT2.repository.ClientRegistrationRequestRepository;
import BSEP.KT2.repository.UserRepository;
import BSEP.KT2.security.encryption.IEncryptor;
import BSEP.KT2.security.hash.IHasher;
import BSEP.KT2.security.hash.ISaltGenerator;
import BSEP.KT2.security.reCaptcha.IReCaptchaVerifier;
import BSEP.KT2.service.IRegistrationService;
import BSEP.KT2.utility.email.IEmailSender;
import BSEP.KT2.utility.email.templates.RegistrationRequestAcceptanceEmailTemplate;
import BSEP.KT2.utility.email.templates.RegistrationRequestRejectionEmailTemplate;
import jakarta.persistence.EntityExistsException;

@Service
public class RegistrationService implements IRegistrationService{
    @Autowired
    private ClientRegistrationRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ISaltGenerator saltGenerator;

    @Autowired
    private IHasher hasher;

    @Autowired
    private IEncryptor encryptor;

    @Autowired
    private IEmailSender emailSender;

    @Autowired
    private IReCaptchaVerifier captchaVerifier;
    
    @Override
    public void requestClientRegistration(ClientRegistrationRequestCreationDto requestDto) {
        List<ClientRegistrationRequest> previousRequests = requestRepository.findAllByEmail(requestDto.getEmail());
        List<ClientRegistrationRequest> conflictingRequests = previousRequests.stream()
            .filter(r -> r.isRecentlyRejected()).collect(Collectors.toList());
        if (!conflictingRequests.isEmpty()) {
            throw new EntityExistsException("You must wait at least 1 week after your previous sign up request has been rejected.");
        }

        User conflictingUser = userRepository.findByUsername(requestDto.getEmail()).orElse(null);
        if(conflictingUser != null) {
            throw new EntityExistsException("User with this email already exists.");
        }

        if(!captchaVerifier.verify(requestDto.getReCaptchaResponse())) {
            throw new SecurityException("Invalid reCAPTCHA answer.");
        }

        ClientRegistrationRequest request = new ClientRegistrationRequest(requestDto);

        request.setPasswordSalt(saltGenerator.generate());
        String hashedPassword = hasher.hashSalted(request.getPassword(), request.getPasswordSalt());
        request.setPassword(hashedPassword);

        requestRepository.save(request);
    }

    @Override
    public void acceptClientRegistration(int requestId) {
        ClientRegistrationRequest request = requestRepository.findById(requestId).orElseThrow();

        request.accept();
        User user = new User(request);
        requestRepository.save(request);
        userRepository.save(user);

        List<ClientRegistrationRequest> requestsByEmail = requestRepository.findAllByEmail(request.getEmail());
        List<ClientRegistrationRequest> conflictingRequests = requestsByEmail.stream()
            .filter(r -> r.getId() != requestId && r.getStatus().equals(ClientRegistrationRequestStatus.PENDING)).collect(Collectors.toList());
        for (ClientRegistrationRequest conflictingRequest : conflictingRequests) {
            conflictingRequest.invalidate();
            requestRepository.save(conflictingRequest);
        }

        LocalDateTime expirationDateTime = LocalDateTime.now().plusDays(7);
        String message =  "" + user.getId() + "|" + expirationDateTime.toString();
        String token = encryptor.encrypt(message);
        String signature = hasher.hashHmac(token);

        String activationLinkTemplate = "http://localhost:4200/account-activation?token=%s&signature=%s";
        String activationLink = String.format(activationLinkTemplate, token, signature);

        String title = RegistrationRequestAcceptanceEmailTemplate.generateTitle();
        String emailContent = RegistrationRequestAcceptanceEmailTemplate.generateText(activationLink);
        emailSender.sendHtml(user.getUsername(), title, emailContent);
    }

    @Override
    public void rejectClientRegistration(int requestId, ClientRegistrationRequestRejectionDto rejection) {
        ClientRegistrationRequest request = requestRepository.findById(requestId).orElseThrow();

        request.reject(rejection.getReason());
        requestRepository.save(request);

        String title = RegistrationRequestRejectionEmailTemplate.generateTitle();
        String emailContent = RegistrationRequestRejectionEmailTemplate.generateText(rejection.getReason());
        emailSender.sendHtml(request.getEmail(), title, emailContent);
    }

    @Override
    public void activateUser(UserActivationDto activation) {
        activation.setToken(activation.getToken().replace(' ', '+'));
        String message = encryptor.decrypt(activation.getToken());
        String[] messageParts = message.split("\\|");
        int userId = Integer.parseInt(messageParts[0]);
        LocalDateTime expiration = LocalDateTime.parse(messageParts[1]);

        String trueSignature = hasher.hashHmac(activation.getToken());
        trueSignature = trueSignature.replace('+', ' ');
        activation.setSignature(activation.getSignature().replace('+', ' '));
        boolean isSignatureValid = activation.getSignature().equals(trueSignature);
        if(!isSignatureValid) {
            throw new SecurityException("Invalid activation link.");
        }
        
        if(expiration.isBefore(LocalDateTime.now())) {
            throw new SecurityException("Activation link has expired.");
        }

        User user = userRepository.findById(userId).orElseThrow();
        user.activate();
        userRepository.save(user);
    }

    public List<ClientRegistrationRequestViewDto> getAllClientRegistrationRequests(){
        List<ClientRegistrationRequest> requests = requestRepository.findAll();
        List<ClientRegistrationRequest> validSortedRequests = requests.stream()
            .filter(r -> !r.getStatus().equals(ClientRegistrationRequestStatus.INVALID)).collect(Collectors.toList());

        Comparator<ClientRegistrationRequest> byDateDescending = Comparator.comparing(ClientRegistrationRequest::getDateTime).reversed();
        validSortedRequests.sort(byDateDescending);

        List<ClientRegistrationRequestViewDto> requestDtos = new ArrayList<>();
        for (ClientRegistrationRequest request : validSortedRequests) {
            ClientRegistrationRequestViewDto requestDto = new ClientRegistrationRequestViewDto(request);
            requestDtos.add(requestDto);
        }

        return requestDtos;
    }
}
