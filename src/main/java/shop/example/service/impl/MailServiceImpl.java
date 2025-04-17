package shop.example.service.impl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shop.example.dto.request.orther.MailBody;
import shop.example.service.MailService;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    protected String emailFrom;


    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10,
            10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    @Override
    public void sendOTP(MailBody mailBody) {
        executor.execute(()->{
            try{
                MimeMessage message= mailSender.createMimeMessage();
                MimeMessageHelper helper= new MimeMessageHelper(message,true,"UTF-8");
                helper.setFrom(emailFrom);
                helper.setTo(mailBody.getEmailTo());
                helper.setText(mailBody.getContent());
                helper.setSubject(mailBody.getSubject());
                mailSender.send(message);
                log.info("Email has been sent to verify OTP");
                executor.shutdown();
            } catch (MessagingException e) {
                log.error("send email failed:{} ", e.getMessage());
            }
        });
    }


    @KafkaListener(topics = "${kafka.inbound-topic}")
    public void listen(@Payload MailBody mailBody){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(emailFrom);
            helper.setTo(mailBody.getEmailTo());
            helper.setSubject("Chào mừng bạn đến với cửa hàng tiện lợi!");
            helper.setText("Xin chào " + mailBody.getFullName() + "\n" +
                    """
                            Chúng tôi rất vui mừng chào đón bạn trở thành nhân viên của cửa hàng tiện lợi.
                            Hãy kiểm tra thông tin đăng nhập và bắt đầu công việc ngay hôm nay nhé!
                            Thông tin hỗ trợ:
                            📧 Email: example@gmail.com
                            📞 Hotline: 09383746***
                            """
            );
            mailSender.send(message);
            log.info("Email has been sent successfully");
        } catch (MessagingException e) {
            log.error("error occurred while send email:{} ", e.getMessage());
        }
    }
}
