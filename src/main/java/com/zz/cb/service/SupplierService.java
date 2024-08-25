package com.zz.cb.service;

import com.zz.cb.model.Supplier;
import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import com.zz.cb.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierStateMachineService stateMachineService;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository, SupplierStateMachineService stateMachineService) {
        this.supplierRepository = supplierRepository;
        this.stateMachineService = stateMachineService;
    }

    public Supplier createSupplier(Long supplierId, String name) {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierId);
        supplier.setName(name);
        supplier.setStatus(SupplierStatus.OFFLINE);  // 假设初始状态为 OFFLINE
        return supplierRepository.save(supplier);
    }

    public SupplierStatus updateSupplierStatus(Long supplierId, SupplierEvent event) {
        Supplier supplier = supplierRepository.findBySupplierId(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        SupplierStatus newStatus = stateMachineService.sendEvent(supplier.getStatus(), event);
        supplier.setStatus(newStatus);
        supplierRepository.save(supplier);

        return newStatus;
    }

    public Optional<Supplier> getSupplier(Long supplierId) {
        return supplierRepository.findBySupplierId(supplierId);
    }
}