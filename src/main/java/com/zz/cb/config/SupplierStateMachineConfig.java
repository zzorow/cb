package com.zz.cb.config;

import com.zz.cb.action.OfflineAction;
import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "supplierStateMachine")
public class SupplierStateMachineConfig extends StateMachineConfigurerAdapter<SupplierStatus, SupplierEvent> {

    @Setter(onMethod_ = @Autowired)
    private SupplierStateMachinePersister supplierStateMachinePersister;

    @Override
    public void configure(StateMachineStateConfigurer<SupplierStatus, SupplierEvent> states) throws Exception {
        states
                .withStates()
                .initial(SupplierStatus.ONLINE)
                .states(EnumSet.allOf(SupplierStatus.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SupplierStatus, SupplierEvent> transitions) throws Exception {
        transitions
                .withExternal().source(SupplierStatus.ONLINE).target(SupplierStatus.UNHEALTHY).event(SupplierEvent.UNHEALTHY)
                .and()
                .withExternal().source(SupplierStatus.ONLINE).target(SupplierStatus.OFFLINE).event(SupplierEvent.MANUAL_OFFLINE)
                .and()
                .withInternal().source(SupplierStatus.UNHEALTHY).action(offlineAction()).timerOnce(10000)
                .and()
                .withExternal().source(SupplierStatus.UNHEALTHY).target(SupplierStatus.ONLINE).event(SupplierEvent.MANUAL_ONLINE)
                .and()
                .withExternal().source(SupplierStatus.UNHEALTHY).target(SupplierStatus.OFFLINE).event(SupplierEvent.MANUAL_OFFLINE)
                .and()
                .withExternal().source(SupplierStatus.OFFLINE).target(SupplierStatus.ONLINE).event(SupplierEvent.MANUAL_ONLINE)
                .and()
                .withExternal().source(SupplierStatus.UNHEALTHY).target(SupplierStatus.OFFLINE).event(SupplierEvent.TIMEOUT);
    }

    @Bean
    public OfflineAction offlineAction(){
        return new OfflineAction();
    }

    @Bean
    public StateMachinePersister<SupplierStatus, SupplierEvent, Long> persister(){
        return supplierStateMachinePersister;
    }
}
