package com.booknest.notificationservice;

import com.booknest.notificationservice.event.LoanPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @KafkaListener(topics = "notificationTopic")
    public void handle(LoanPlacedEvent event) {
        int totalBooks = event.getItems() == null ? 0
                : event.getItems().stream()
                        .mapToInt(item -> item.getQuantity() == null ? 0 : item.getQuantity())
                        .sum();
        log.info("Received loan confirmation for member {} - loan {} ({} book(s): {}). "
                        + "Sending confirmation & due-date reminder.",
                event.getMemberId(), event.getLoanNumber(), totalBooks, event.getItems());
    }
}
