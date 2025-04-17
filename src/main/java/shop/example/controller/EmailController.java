//package shop.example.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import shop.example.service.MailService;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping("api/v1/email")
//public class EmailController {
//
//    private final MailService emailService;
//
//    @GetMapping("/send-email")
//    public void send(@RequestParam String emailTo) {
//        log.info("Sending email to {}", emailTo);
//        emailService.sendMail(emailTo);
//    }
//}
