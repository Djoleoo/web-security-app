package BSEP.KT2.utility.email.templates;

import java.io.IOException;

public class PasswordlessLoginEmailTemplate extends EmailTemplate{

    public static String generateTitle() {
        String title = "Passwordless sign in request accepted";
        return title;
    }

    public static String generateText(String loginLink) {
        String filePath = "src/main/resources/templates/passwordlessLoginEmailTemplate.html";
        try {
            String htmlContent = readHtmlTemplate(filePath);
            htmlContent = replaceVariable(htmlContent, "login_link", loginLink);
            return htmlContent;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return "";
    }
}
