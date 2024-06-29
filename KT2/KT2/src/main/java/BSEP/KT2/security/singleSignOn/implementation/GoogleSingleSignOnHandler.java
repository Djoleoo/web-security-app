package BSEP.KT2.security.singleSignOn.implementation;

import BSEP.KT2.security.singleSignOn.IGoogleSingleSignOnHandler;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.json.JsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleSingleSignOnHandler implements IGoogleSingleSignOnHandler{
    @Value("${spring.security.oauth2.client.registration.google.client-id}") private String CLIENT_ID; 

    public Payload verify(String token) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        
         GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(token);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        if (idToken != null) {
            Payload userInfo = idToken.getPayload();
            return userInfo;
        } else {
            throw new SecurityException("Invalid Google sign in token.");
        }
    }
}
