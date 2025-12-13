package com.interview.regtech;

import java.util.ArrayList;
import java.util.List;

public class RegTechEngine {
    private final List<Rule<TransferContext>> rules = new ArrayList<>();

    public void addRule(Rule<TransferContext> rule) {
        rules.add(rule);
    }

    public RuleResult validate(TransferContext context) {
        for (Rule<TransferContext> rule : rules) {
            RuleResult result = rule.validate(context);
            if (!result.isValid()) {
                return result;
            }
        }
        return RuleResult.success();
    }
}
