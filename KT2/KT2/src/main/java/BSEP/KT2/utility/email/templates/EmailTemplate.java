package BSEP.KT2.utility.email.templates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class EmailTemplate {
    protected static String readHtmlTemplate(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    protected static String replaceVariable(String template, String variable, String value) throws IOException {
        String target = String.format("${%s}", variable);
        if (!template.contains(target)) {
            return template;
        }

        String replacedTemplate = template.replace(target, value);
        return replacedTemplate;
    }
}
