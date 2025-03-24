package com.zz.cb.service;

import com.zz.cb.config.SupplierStateMachinePersister;
import com.zz.cb.event.SupplierTimeoutEvent;
import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class SupplierStateTransitionService {

    @Setter(onMethod_ = @Autowired)
    private StateMachine<SupplierStatus, SupplierEvent> stateMachine;
    @Setter(onMethod_ = @Autowired)
    private SupplierStateMachinePersister persister;

    public synchronized SupplierStatus sendEvent(Long supplierId, SupplierEvent event) {
        try {
            stateMachine = persister.restore(stateMachine, supplierId);
            boolean accepted = stateMachine.sendEvent(event);
            if (accepted) {
                persister.persist(stateMachine, supplierId);
                return stateMachine.getState().getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @EventListener
    public void handleSupplierTimeout(SupplierTimeoutEvent event) {
        sendEvent(event.getSupplierId(), SupplierEvent.TIMEOUT);
    }
}
