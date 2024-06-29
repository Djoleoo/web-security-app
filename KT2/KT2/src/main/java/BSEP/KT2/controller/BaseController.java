package BSEP.KT2.controller;

import jakarta.servlet.http.HttpServletRequest;

public class BaseController {
    public String generateSuccessResponse(int statusCode, String message) {
        String bodyFormat = "{ \"statusCode\": \"%d\", \"message\": \"%s\" }";
        String body = String.format(bodyFormat, statusCode, message);
        return body;
    }

    public String generateErrorResponse(int statusCode, String error) {
        String bodyFormat = "{ \"statusCode\": \"%d\", \"error\": \"%s\" }";
        String body = String.format(bodyFormat, statusCode, error);
        return body;
    }

    public String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }
}
