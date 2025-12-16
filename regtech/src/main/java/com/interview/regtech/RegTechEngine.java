package com.interview.regtech;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class RegTechEngine {
    private final List<Rule<TransferContext>> rules = new ArrayList<>();

    public void addRule(Rule<TransferContext> rule) {
        rules.add(rule);
    }

    /**
     * Demonstrate Java SPI (Service Provider Interface).
     * Automatically discovers implementations of Rule declared in
     * META-INF/services.
     */
    @SuppressWarnings("unchecked")
    public void loadRulesFromSPI() {
        ServiceLoader<Rule> loader = ServiceLoader.load(Rule.class);
        for (Rule<?> rule : loader) {
            // Unchecked cast because ServiceLoader is generic-erased mostly or harder to
            // type strictly with Rule<T>
            // capturing wildcard. For interview demo, this is acceptable.
            rules.add((Rule<TransferContext>) rule);
            System.out.println("Loaded SPI Rule: " + rule.getClass().getSimpleName());
        }
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
