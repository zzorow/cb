package com.zz.cb.action;

import com.zz.cb.model.Supplier;
import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import com.zz.cb.repository.SupplierRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class OfflineAction implements Action<SupplierStatus, SupplierEvent> {
    @Setter(onMethod_ = @Autowired)
    private ApplicationEventPublisher eventPublisher;
    @Setter(onMethod_ = @Autowired)
    private SupplierRepository supplierRepository;

    @Override
    public void execute(StateContext context) {
        Long supplierId = (Long)context.getExtendedState().getVariables().get("supplierId");
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
        supplier.setStatus(SupplierStatus.OFFLINE);
        supplierRepository.save(supplier);
    }
}
