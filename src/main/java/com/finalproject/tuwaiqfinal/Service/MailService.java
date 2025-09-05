package com.finalproject.tuwaiqfinal.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from:}")
    private String from;
    @Value("${mail.brand:}")
    private String brand;
    @Value("${mail.welcome.subject:salat - صالات}")
    private String subjectFmt;

    public void sendHtml(String from, String to, String subject, String htmlBody) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(mime, false, "UTF-8");
            if (from != null && !from.isBlank()) h.setFrom(from);
            h.setTo(to);
            h.setSubject(subject);
            h.setText(htmlBody, true); // true = HTML
            mailSender.send(mime);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    private static final String WELCOME_HTML = """
            <!doctype html>
            <html lang="en" dir="ltr">
            <head>
              <meta charset="utf-8"><meta name="viewport" content="width=device-width">
              <meta name="color-scheme" content="light dark"><meta name="supported-color-schemes" content="light dark">
              <title>Welcome</title>
              <style>@media (max-width:600px){.container{width:100%!important}.p-24{padding:16px!important}.btn{display:block!important;width:100%!important}}</style>
            </head>
            <body style="margin:0;background:#f6f9fc;font-family:Arial,Helvetica,sans-serif;">
              <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background:#f6f9fc;">
                <tr>
                  <td align="center" style="padding:24px;">
                    <table role="presentation" class="container" width="600" cellpadding="0" cellspacing="0"
                           style="width:600px;background:#ffffff;border-radius:12px;box-shadow:0 1px 2px rgba(0,0,0,.05);overflow:hidden;">
                      <tr>
                        <td align="center" style="padding:20px 24px;background:#0f172a;color:#fff;font-weight:bold;font-size:18px;">
                         🎉 صــالات 🎉
                        </td>
                      </tr>
                      <tr>
                        <td class="p-24" style="padding:24px;">
                          <h1 style="margin:0 0 12px;font-size:22px;color:#0f172a;"> {{name}},👋هلا فيك</h1>
                          <p style="margin:0 0 16px;line-height:1.6;color:#334155;">
            وصلنا طلب حجز جديد من صالات 🎉 ترى كل التفاصيل موجودة عندك بالموقع ارجع وتابعها من هناك.😉🤍
                                     </p>
                                     <table role="presentation" cellpadding="0" cellspacing="0" style="margin:20px 0;">
                            <tr>
                              <td align="center" bgcolor="#2563eb" style="border-radius:8px;">
                                <a href="{{ctaUrl}}" class="btn"
                                   style="display:inline-block;padding:12px 20px;color:#fff;text-decoration:none;font-weight:bold;border-radius:8px;">
                           حجوزاتي
                                </a>
                              </td>
                            </tr>
                          </table>
                          <p style="margin:0 0 16px;line-height:1.6;color:#64748b;">
                            إذا خلّصت حجزك، لا تنسى تقيّمنا 🙏 رأيك يهمّنا<br>
                            <a href="{{ctaUrl}}" style="color:#2563eb;word-break:break-all;">{{ctaUrl}}</a>
                          </p>
                          <hr style="border:none;border-top:1px solid #e2e8f0;margin:24px 0;">
                          <p style="margin:0;color:#94a3b8;font-size:12px;line-height:1.6;">
                            لو عندك مشكلة رد على الايميل وخل الباقي علينا🙏
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td align="center" style="padding:16px;background:#f8fafc;color:#94a3b8;font-size:12px;">
                          © موقع صالات —جميع الحقوق محفوظة
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """;

    public void sendWelcomeHtml(String to, String name, String ctaUrl) {
        String safeBrand = org.springframework.web.util.HtmlUtils.htmlEscape(brand);
        String safeName = org.springframework.web.util.HtmlUtils.htmlEscape(name == null ? "" : name);
        String safeUrl = org.springframework.web.util.HtmlUtils.htmlEscape(ctaUrl == null ? "" : ctaUrl);

        String html = MailService.WELCOME_HTML
                .replace("{{brand}}", safeBrand)
                .replace("{{name}}", safeName)
                .replace("{{ctaUrl}}", safeUrl);

        String subject = String.format(subjectFmt, brand);
        sendHtml(from, to, subject, html);
    }


    public void sendWithoutAttachment(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    public void sendWithAttachment(String from,
                                   String to,
                                   String subject,
                                   String htmlBody,
                                   byte[] attachment,
                                   String attachmentFilename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.addAttachment(attachmentFilename, () -> new java.io.ByteArrayInputStream(attachment));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
