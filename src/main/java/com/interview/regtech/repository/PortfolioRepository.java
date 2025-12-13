package com.interview.regtech.repository;

import com.interview.portfolio.Portfolio;
import java.util.Optional;

public interface PortfolioRepository {
    Optional<Portfolio> findById(String id);
}
