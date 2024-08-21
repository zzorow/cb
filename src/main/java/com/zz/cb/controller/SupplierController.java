package com.zz.cb.controller;

import com.zz.cb.model.Supplier;
import com.zz.cb.model.SupplierStatus;
import com.zz.cb.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestParam Long supplierId, @RequestParam String name) {
        Supplier supplier = supplierService.createSupplier(supplierId, name);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping("/{supplierId}/event")
    public ResponseEntity<SupplierStatus> sendEvent(@PathVariable Long supplierId, @RequestParam String event) {
        SupplierStatus newStatus = supplierService.updateSupplierStatus(supplierId, event);
        return ResponseEntity.ok(newStatus);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<Supplier> getSupplier(@PathVariable Long supplierId) {
        return supplierService.getSupplier(supplierId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}