package com.zz.cb.mq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.cb.model.SupplierStatus;
import com.zz.cb.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SupplierKafkaListener {

    private final ObjectMapper objectMapper;
    private final SupplierService supplierService;

    public SupplierKafkaListener(ObjectMapper objectMapper, SupplierService supplierService) {
        this.objectMapper = objectMapper;
        this.supplierService = supplierService;
    }

    @KafkaListener(topics = "supplier-events", groupId = "supplier-group")
    public void consume(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            Long supplierId = jsonNode.get("supplierId").asLong();
            String event = jsonNode.get("event").asText();

            SupplierStatus newStatus = supplierService.updateSupplierStatus(supplierId, event);
            System.out.println("Updated supplier " + supplierId + " status to " + newStatus);
        } catch (IOException e) {
            System.err.println("Error parsing Kafka message: " + e.getMessage());
        }
    }
}