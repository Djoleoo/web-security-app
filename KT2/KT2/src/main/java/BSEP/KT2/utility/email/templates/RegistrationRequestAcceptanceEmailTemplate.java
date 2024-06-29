package BSEP.KT2.utility.email.templates;

import java.io.IOException;

public class RegistrationRequestAcceptanceEmailTemplate extends EmailTemplate{

    public static String generateTitle() {
        String title = "Sign up request accepted";
        return title;
    }

    public static String generateText(String activationLink) {
        String filePath = "src/main/resources/templates/registrationRequestAcceptanceEmailTemplate.html";
        try {
            String htmlContent = readHtmlTemplate(filePath);
            htmlContent = replaceVariable(htmlContent, "activation_link", activationLink);
            return htmlContent;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return "";
    }
}
