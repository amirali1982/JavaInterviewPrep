package com.interview.regtech;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegTechEngineTest {

    @Mock
    private Rule<TransferContext> rule1;
    @Mock
    private Rule<TransferContext> rule2;

    @Test
    void shouldPassWhenAllRulesPass() {
        RegTechEngine engine = new RegTechEngine();
        engine.addRule(rule1);
        engine.addRule(rule2);

        TransferContext context = new TransferContext("s", "r", "A", 1);

        when(rule1.validate(context)).thenReturn(RuleResult.success());
        when(rule2.validate(context)).thenReturn(RuleResult.success());

        RuleResult result = engine.validate(context);

        assertTrue(result.isValid());
        verify(rule1).validate(context);
        verify(rule2).validate(context);
    }

    @Test
    void shouldFailFastWhenFirstRuleFails() {
        RegTechEngine engine = new RegTechEngine();
        engine.addRule(rule1);
        engine.addRule(rule2);

        TransferContext context = new TransferContext("s", "r", "A", 1);

        when(rule1.validate(context)).thenReturn(RuleResult.failure("Fail 1"));

        RuleResult result = engine.validate(context);

        assertFalse(result.isValid());
        assertEquals("Fail 1", result.errorMessage());
        // Verify rule2 was NOT called
        verify(rule2, never()).validate(any());
    }
}
