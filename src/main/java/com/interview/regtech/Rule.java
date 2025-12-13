package com.interview.regtech;

public interface Rule<T> {
    RuleResult validate(T context);
}
