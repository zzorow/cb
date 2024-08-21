package com.zz.cb.config;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.StateMachineInterceptor;

class StateMachinePersisterAdapter<S, E, T> implements StateMachineRuntimePersister<S, E, T> {
    private final StateMachinePersist<S, E, T> persist;

    public StateMachinePersisterAdapter(StateMachinePersist<S, E, T> persist) {
        this.persist = persist;
    }

    @Override
    public StateMachineInterceptor<S, E> getInterceptor() {
        return null; // 如果需要，可以在这里实现拦截器逻辑
    }

    @Override
    public void write(StateMachineContext<S, E> context, T contextObj) throws Exception {
        persist.write(context, contextObj);
    }

    @Override
    public StateMachineContext<S, E> read(T contextObj) throws Exception {
        return persist.read(contextObj);
    }
}
