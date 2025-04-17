package shop.example.service;

import shop.example.dto.request.orther.MailBody;

public interface MailService {
    void sendOTP(MailBody mailBody);

}
