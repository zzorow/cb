package com.zz.cb.action;

import com.zz.cb.event.SupplierTimeoutEvent;
import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
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

    @Override
    public void execute(StateContext context) {
        Long supplierId = (Long)context.getExtendedState().getVariables().get("supplierId");
        context.getStateMachine().sendEvent(SupplierEvent.TIMEOUT);
        int i = 0;
    }
}
