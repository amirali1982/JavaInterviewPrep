package com.interview.regtech.rules;

import com.interview.portfolio.Portfolio;
import com.interview.regtech.RuleResult;
import com.interview.regtech.TransferContext;
import com.interview.regtech.repository.PortfolioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiverExistsRuleTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Test
    void shouldFailWhenReceiverNotFound() {
        ReceiverExistsRule rule = new ReceiverExistsRule(portfolioRepository);
        TransferContext context = new TransferContext("sender", "receiver", "AAPL", 10);

        when(portfolioRepository.findById("receiver")).thenReturn(Optional.empty());

        RuleResult result = rule.validate(context);

        assertFalse(result.isValid());
        assertEquals("Receiver portfolio not found", result.errorMessage());
    }

    @Test
    void shouldPassWhenReceiverExists() {
        ReceiverExistsRule rule = new ReceiverExistsRule(portfolioRepository);
        TransferContext context = new TransferContext("sender", "receiver", "AAPL", 10);

        when(portfolioRepository.findById("receiver")).thenReturn(Optional.of(new Portfolio()));

        RuleResult result = rule.validate(context);

        assertTrue(result.isValid());
    }
}
