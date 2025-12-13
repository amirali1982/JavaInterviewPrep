package com.interview.regtech;

public record RuleResult(boolean isValid, String errorMessage) {
    public static RuleResult success() {
        return new RuleResult(true, null);
    }

    public static RuleResult failure(String message) {
        return new RuleResult(false, message);
    }
}
