package BSEP.KT2.utility.email.templates;

import java.io.IOException;

public class TemporaryPasswordEmailTemplate extends EmailTemplate{
    public static String generateTitle() {
        String title = "Google sign up successful";
        return title;
    }

    public static String generateText(String password) {
        String filePath = "src/main/resources/templates/temporaryPasswordEmailTemplate.html";
        try {
            String htmlContent = readHtmlTemplate(filePath);
            htmlContent = replaceVariable(htmlContent, "password", password);
            return htmlContent;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return "";
    }
}
