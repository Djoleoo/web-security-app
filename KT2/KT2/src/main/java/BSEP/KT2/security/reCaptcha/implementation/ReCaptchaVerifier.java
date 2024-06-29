package BSEP.KT2.security.reCaptcha.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import BSEP.KT2.security.reCaptcha.IReCaptchaVerifier;

@Component
public class ReCaptchaVerifier implements IReCaptchaVerifier {
    @Value("${application.re-captcha.secret-key}") private String reCaptchaSecret;

    private final RestTemplate restTemplate;

    public ReCaptchaVerifier(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean verify(String reCaptchaResponse) {
        String url = "https://www.google.com/recaptcha/api/siteverify?secret={secret}&response={response}";
        ResponseEntity<ReCaptchaResponse> responseEntity = restTemplate.postForEntity(url, null, ReCaptchaResponse.class, reCaptchaSecret, reCaptchaResponse);
        ReCaptchaResponse response = responseEntity.getBody();
        return response != null && response.isSuccess();
    }
}