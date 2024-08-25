package com.zz.cb.service;

import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
public class SupplierStateMachineService {

    private final StateMachineFactory<SupplierStatus, SupplierEvent> stateMachineFactory;

    @Autowired
    public SupplierStateMachineService(StateMachineFactory<SupplierStatus, SupplierEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public SupplierStatus sendEvent(SupplierStatus currentStatus, SupplierEvent event) {
        StateMachine<SupplierStatus, SupplierEvent> stateMachine = stateMachineFactory.getStateMachine();

        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma -> sma.resetStateMachineReactively(
                                new DefaultStateMachineContext<>(currentStatus, null, null, null))
                        .block());
        stateMachine.startReactively().block();

        boolean result = stateMachine.sendEvent(event);

        return result ? stateMachine.getState().getId() : currentStatus;
    }
}