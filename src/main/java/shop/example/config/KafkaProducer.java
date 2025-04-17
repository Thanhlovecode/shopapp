package shop.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import shop.example.dto.request.orther.MailBody;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    public void send(String topic, MailBody mailBody){
        kafkaTemplate.send(topic,mailBody);
    }
}
