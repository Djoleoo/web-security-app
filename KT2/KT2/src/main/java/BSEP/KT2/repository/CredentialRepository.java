package BSEP.KT2.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.warrenstrange.googleauth.ICredentialRepository;

import BSEP.KT2.model.User;
import BSEP.KT2.security.encryption.IEncryptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class CredentialRepository implements ICredentialRepository {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IEncryptor encryptor;

    private final Map<String, UserTOTP> usersKeys = new HashMap<String, UserTOTP>();

    @Override
    public String getSecretKey(String userName) {
        User user = userRepository.findByUsername(userName).orElse(null);

        if(user != null) {
            String key = encryptor.decrypt(user.getKey2FactorAuthentication());
            return key;
        }
        return "";
    }

    @Override
    public void saveUserCredentials(String userName,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {
        usersKeys.put(userName, new UserTOTP(userName, secretKey, validationCode, scratchCodes));
    }

    public UserTOTP getUser(String username) {
        return usersKeys.get(username);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UserTOTP {
        private String username;
        private String secretKey;
        private int validationCode;
        private List<Integer> scratchCodes;
    }
}
