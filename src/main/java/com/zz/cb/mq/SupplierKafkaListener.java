package com.zz.cb.mq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import com.zz.cb.service.SupplierService;
import org.springframework.kafka.annotation.KafkaListener;
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
            SupplierEvent event = SupplierEvent.valueOf(jsonNode.get("event").asText());

            SupplierStatus newStatus = supplierService.updateSupplierStatus(supplierId, event);
            System.out.println("Updated supplier " + supplierId + " status to " + newStatus);
        } catch (IOException e) {
            System.err.println("Error parsing Kafka message: " + e.getMessage());
        }
    }
}