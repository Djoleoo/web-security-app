package BSEP.KT2.security.singleSignOn;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

public interface IGoogleSingleSignOnHandler {
    public Payload verify(String token);
}
