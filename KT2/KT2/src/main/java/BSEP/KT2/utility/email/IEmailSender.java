package BSEP.KT2.utility.email;

public interface IEmailSender {
    public void send(String to, String subject, String text);
    public void sendHtml(String to, String subject, String htmlContent);
}
