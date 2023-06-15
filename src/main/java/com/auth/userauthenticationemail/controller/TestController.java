package com.auth.userauthenticationemail.controller;

import com.auth.userauthenticationemail.service.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private KafkaProducer kafkaProducer;

    public TestController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message){
        kafkaProducer.sendMessage(message);
        return  ResponseEntity.ok("message sent");
    }
}
