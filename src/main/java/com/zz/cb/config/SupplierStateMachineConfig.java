package com.zz.cb.config;

import com.zz.cb.model.SupplierEvent;
import com.zz.cb.model.SupplierStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class SupplierStateMachineConfig extends StateMachineConfigurerAdapter<SupplierStatus, SupplierEvent> {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisStateMachineContextRepository<SupplierStatus, SupplierEvent> redisStateMachineContextRepository() {
        return new RedisStateMachineContextRepository<>(redisConnectionFactory);
    }

    @Bean
    public StateMachinePersist<SupplierStatus, SupplierEvent, String> stateMachinePersist(
            RedisStateMachineContextRepository<SupplierStatus, SupplierEvent> repository) {
        return new RepositoryStateMachinePersist<>(repository);
    }

    @Bean
    public StateMachineRuntimePersister<SupplierStatus, SupplierEvent, String> stateMachineRuntimePersister(
            StateMachinePersist<SupplierStatus, SupplierEvent, String> stateMachinePersist) {
        return new StateMachinePersisterAdapter<>(stateMachinePersist);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SupplierStatus, SupplierEvent> states) throws Exception {
        states
                .withStates()
                .initial(SupplierStatus.ONLINE)
                .states(EnumSet.allOf(SupplierStatus.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<SupplierStatus, SupplierEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(new StateMachineListenerAdapter<>());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SupplierStatus, SupplierEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SupplierStatus.ONLINE)
                .target(SupplierStatus.OFFLINE)
                .event(SupplierEvent.MANUAL_OFFLINE)
                .and()
                .withExternal()
                .source(SupplierStatus.ONLINE)
                .target(SupplierStatus.UNHEALTHY)
                .event(SupplierEvent.UNHEALTHY)
                .and()
                .withExternal()
                .source(SupplierStatus.UNHEALTHY)
                .target(SupplierStatus.ONLINE)
                .event(SupplierEvent.RECOVERED)
                .and()
                .withExternal()
                .source(SupplierStatus.UNHEALTHY)
                .target(SupplierStatus.ONLINE)
                .event(SupplierEvent.MANUAL_ONLINE)
                .and()
                .withExternal()
                .source(SupplierStatus.OFFLINE)
                .target(SupplierStatus.ONLINE)
                .event(SupplierEvent.MANUAL_ONLINE)
                .and()
                .withExternal()
                .source(SupplierStatus.OFFLINE)
                .target(SupplierStatus.ONLINE)
                .event(SupplierEvent.RECOVERED)
                .and()
                .withInternal()
                .source(SupplierStatus.UNHEALTHY)
                .timer(60000) // 60 seconds = 1 minute
                .action(context -> context.getStateMachine().sendEvent(SupplierEvent.TIMEOUT))
                .and()
                .withExternal()
                .source(SupplierStatus.UNHEALTHY)
                .target(SupplierStatus.OFFLINE)
                .event(SupplierEvent.TIMEOUT);
    }
}