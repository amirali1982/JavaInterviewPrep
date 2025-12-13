package com.interview.regtech.rules;

import com.interview.portfolio.Portfolio;
import com.interview.regtech.Rule;
import com.interview.regtech.RuleResult;
import com.interview.regtech.TransferContext;
import com.interview.regtech.repository.PortfolioRepository;
import java.util.Map;

public class SenderHasBalanceRule implements Rule<TransferContext> {
    private final PortfolioRepository portfolioRepository;

    public SenderHasBalanceRule(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public RuleResult validate(TransferContext context) {
        return portfolioRepository.findById(context.senderId())
                .map(portfolio -> {
                    Integer currentQty = portfolio.getHoldings().get(context.stockSymbol());
                    if (currentQty == null || currentQty < context.quantity()) {
                        return RuleResult.failure("Insufficient balance for symbol: " + context.stockSymbol());
                    }
                    return RuleResult.success();
                })
                .orElse(RuleResult.failure("Sender portfolio not found"));
    }
}
