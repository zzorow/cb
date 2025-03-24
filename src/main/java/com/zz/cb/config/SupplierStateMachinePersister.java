package com.zz.cb.config;

import com.zz.cb.model.Supplier;
import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import com.zz.cb.repository.SupplierRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;


@Component
public class SupplierStateMachinePersister implements StateMachinePersister<SupplierStatus, SupplierEvent, Long> {

    @Setter(onMethod_ = @Autowired)
    private SupplierRepository supplierRepository;

    @Override
    public void persist(StateMachine<SupplierStatus, SupplierEvent> stateMachine, Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
        supplier.setStatus(stateMachine.getState().getId());
        supplierRepository.save(supplier);
    }

    @Override
    public StateMachine<SupplierStatus, SupplierEvent> restore(StateMachine<SupplierStatus, SupplierEvent> stateMachine, Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
        stateMachine.getExtendedState().getVariables().put("supplierId", supplierId);
        stateMachine.getExtendedState().getVariables().put("supplier", supplier);
        stateMachine.stop();
        stateMachine.getStateMachineAccessor().doWithAllRegions(function -> function.resetStateMachine(new DefaultStateMachineContext<>(supplier.getStatus(), null, null, null)));
        stateMachine.start();
        return stateMachine;
    }
}
