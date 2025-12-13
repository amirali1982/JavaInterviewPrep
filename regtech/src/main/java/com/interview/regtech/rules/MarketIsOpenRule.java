package com.interview.regtech.rules;

import com.interview.regtech.Rule;
import com.interview.regtech.RuleResult;
import com.interview.regtech.TransferContext;
import com.interview.regtech.provider.MarketProvider;

public class MarketIsOpenRule implements Rule<TransferContext> {
    private final MarketProvider marketProvider;

    public MarketIsOpenRule(MarketProvider marketProvider) {
        this.marketProvider = marketProvider;
    }

    @Override
    public RuleResult validate(TransferContext context) {
        if (marketProvider.isMarketOpen()) {
            return RuleResult.success();
        }
        return RuleResult.failure("Market is currently closed");
    }
}
