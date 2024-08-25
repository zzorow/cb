package com.zz.cb.service;

import com.zz.cb.model.Supplier;
import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import com.zz.cb.repository.SupplierRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierService {
    @Setter(onMethod_ = @Autowired)
    private SupplierRepository supplierRepository;
    @Setter(onMethod_ = @Autowired)
    private SupplierStateTransitionService supplierStateTransitionService;

    public Supplier createSupplier(Long supplierId, String name) {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierId);
        supplier.setName(name);
        supplier.setStatus(SupplierStatus.OFFLINE);  // 假设初始状态为 OFFLINE
        return supplierRepository.save(supplier);
    }

    public SupplierStatus updateSupplierStatus(Long supplierId, SupplierEvent event) {
        return supplierStateTransitionService.sendEvent(supplierId, event);
    }

    public Optional<Supplier> getSupplier(Long supplierId) {
        return supplierRepository.findBySupplierId(supplierId);
    }
}