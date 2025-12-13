package com.interview.regtech.rules;

import com.interview.regtech.Rule;
import com.interview.regtech.RuleResult;
import com.interview.regtech.TransferContext;
import com.interview.regtech.repository.PortfolioRepository;

public class ReceiverExistsRule implements Rule<TransferContext> {
    private final PortfolioRepository portfolioRepository;

    public ReceiverExistsRule(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public RuleResult validate(TransferContext context) {
        if (portfolioRepository.findById(context.receiverId()).isPresent()) {
            return RuleResult.success();
        }
        return RuleResult.failure("Receiver portfolio not found");
    }
}
