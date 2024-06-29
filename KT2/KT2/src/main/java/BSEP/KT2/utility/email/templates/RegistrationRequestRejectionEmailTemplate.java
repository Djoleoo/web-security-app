package BSEP.KT2.utility.email.templates;

import java.io.IOException;

public class RegistrationRequestRejectionEmailTemplate extends EmailTemplate{

    public static String generateTitle() {
        String title = "Sign up request rejected";
        return title;
    }

    public static String generateText(String reason) {
        String filePath = "src/main/resources/templates/registrationRequestRejectionEmailTemplate.html";
        try {
            String htmlContent = readHtmlTemplate(filePath);
            htmlContent = replaceVariable(htmlContent, "reason", reason);
            return htmlContent;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return "";
    }
}
