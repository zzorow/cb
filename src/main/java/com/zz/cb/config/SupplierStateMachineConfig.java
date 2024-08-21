package com.zz.cb.config;

import com.zz.cb.model.SupplierStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
@EnableStateMachineFactory
public class SupplierStateMachineConfig extends StateMachineConfigurerAdapter<SupplierStatus, String> {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisStateMachineContextRepository<SupplierStatus, String> redisStateMachineContextRepository() {
        return new RedisStateMachineContextRepository<>(redisConnectionFactory);
    }

    @Bean
    public StateMachinePersist<SupplierStatus, String, String> stateMachinePersist(
            RedisStateMachineContextRepository<SupplierStatus, String> repository) {
        return new RepositoryStateMachinePersist<>(repository);
    }

    @Bean
    public StateMachineRuntimePersister<SupplierStatus, String, String> stateMachineRuntimePersister(
            StateMachinePersist<SupplierStatus, String, String> stateMachinePersist) {
        return new StateMachinePersisterAdapter<>(stateMachinePersist);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SupplierStatus, String> states) throws Exception {
        states
                .withStates()
                .initial(SupplierStatus.OFFLINE)
                .state(SupplierStatus.ONLINE)
                .state(SupplierStatus.BUSY);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SupplierStatus, String> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SupplierStatus.OFFLINE)
                .target(SupplierStatus.ONLINE)
                .event("GO_ONLINE")
                .and()
                .withExternal()
                .source(SupplierStatus.ONLINE)
                .target(SupplierStatus.OFFLINE)
                .event("GO_OFFLINE")
                .and()
                .withExternal()
                .source(SupplierStatus.ONLINE)
                .target(SupplierStatus.BUSY)
                .event("BECOME_BUSY")
                .and()
                .withExternal()
                .source(SupplierStatus.BUSY)
                .target(SupplierStatus.ONLINE)
                .event("BECOME_AVAILABLE");
    }
}