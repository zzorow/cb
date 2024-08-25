package com.zz.cb.event;

public class SupplierTimeoutEvent {
    private final Long supplierId;

    public SupplierTimeoutEvent(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() {
        return supplierId;
    }
}