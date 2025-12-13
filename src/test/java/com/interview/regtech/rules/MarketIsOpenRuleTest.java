package com.interview.regtech.rules;

import com.interview.regtech.RuleResult;
import com.interview.regtech.TransferContext;
import com.interview.regtech.provider.MarketProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarketIsOpenRuleTest {

    @Mock
    private MarketProvider marketProvider;

    @Test
    void shouldFailWhenMarketClosed() {
        MarketIsOpenRule rule = new MarketIsOpenRule(marketProvider);
        TransferContext context = new TransferContext("s", "r", "A", 1);

        when(marketProvider.isMarketOpen()).thenReturn(false);

        RuleResult result = rule.validate(context);

        assertFalse(result.isValid());
        assertEquals("Market is currently closed", result.errorMessage());
    }

    @Test
    void shouldPassWhenMarketOpen() {
        MarketIsOpenRule rule = new MarketIsOpenRule(marketProvider);
        TransferContext context = new TransferContext("s", "r", "A", 1);

        when(marketProvider.isMarketOpen()).thenReturn(true);

        RuleResult result = rule.validate(context);

        assertTrue(result.isValid());
    }
}
