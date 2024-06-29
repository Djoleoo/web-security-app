package BSEP.KT2.utility.email.templates;

import java.io.IOException;

public class AccountRecoveryEmailTemplate extends EmailTemplate{

    public static String generateTitle() {
        String title = "Account recovery";
        return title;
    }

    public static String generateText(String passwordResetLink) {
        String filePath = "src/main/resources/templates/accountRecoveryEmailTemplate.html";
        try {
            String htmlContent = readHtmlTemplate(filePath);
            htmlContent = replaceVariable(htmlContent, "password_reset_link", passwordResetLink);
            return htmlContent;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return "";
    }
}
