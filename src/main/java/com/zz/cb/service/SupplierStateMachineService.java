package com.zz.cb.service;

import com.zz.cb.model.SupplierStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
public class SupplierStateMachineService {

    private final StateMachineFactory<SupplierStatus, String> stateMachineFactory;

    @Autowired
    public SupplierStateMachineService(StateMachineFactory<SupplierStatus, String> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public SupplierStatus sendEvent(SupplierStatus currentStatus, String event) {
        StateMachine<SupplierStatus, String> stateMachine = stateMachineFactory.getStateMachine();

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