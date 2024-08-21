package com.zz.cb.job;

import com.zz.cb.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SupplierStatusCheckJob {

    @Autowired
    private SupplierService supplierService;

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void checkSupplierStatus() {
        // Implement logic to check supplier status and update if necessary
    }
}