package com.zz.cb;

import com.zz.cb.model.SupplierStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

@SpringBootTest
class CbApplicationTests {

    @Autowired
    private StateMachineFactory<SupplierStatus, String> stateMachineFactory;

    @Test
    public void testStateMachine() {
        StateMachine<SupplierStatus, String> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.start();

        // 模拟发送消息
        stateMachine.sendEvent("APPROVE");

        // 检查状态
        assert(stateMachine.getState().getId() == SupplierStatus.APPROVED);

        // 模拟另一个消息
        stateMachine.sendEvent("REJECT");

        // 再次检查状态
        assert(stateMachine.getState().getId() == SupplierStatus.REJECTED);
    }

}
