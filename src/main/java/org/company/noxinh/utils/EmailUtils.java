package org.company.noxinh.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * The type Email utils.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailUtils {
    private final JavaMailSender mailSender;

    /**
     * Send otp email boolean.
     *
     * @param email the email
     * @param otp   the otp
     * @return the boolean
     */
    public boolean sendOtpEmail(String email, String otp) {
        String subject = "Xác thực OTP";
        String emailContent = "<html><body>"
                + "<h2>Chào mừng bạn đến với Nơ xinh!</h2>"
                + "<p>Xin chào " + email + ",</p>"
                + "<p>Đây là phản hồi yêu cầu đặt lại mật khẩu. Mã OTP của bạn để xác minh tài khoản là: "
                + "<strong>" + otp + "</strong>.</p>"
                + "<p>Vui lòng sử dụng mã OTP này để tiến hành quá trình đặt lại mật khẩu của bạn của bạn.</p>"
                + "<p>Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ thêm, hãy liên hệ với đội ngũ hỗ trợ của chúng tôi.</p>"
                + "<p>Trân trọng,<br/>Nơ xinh</p>"
                + "</body></html>";
        boolean isSuccess = false;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(emailContent, true);
            log.info("Email {} has been sending ", email);
            mailSender.send(mimeMessage);
            isSuccess = true;
        } catch (Exception ex) {
            log.error("Error occurred while sending verify email: {}", ex.getMessage());
        }
        return isSuccess;
    }
}
