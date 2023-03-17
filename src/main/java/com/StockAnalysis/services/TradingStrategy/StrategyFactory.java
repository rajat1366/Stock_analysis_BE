package com.StockAnalysis.services.TradingStrategy;

import org.springframework.stereotype.Service;

@Service
public class StrategyFactory {

    public Strategy getStrategy(String strategyName) {
        // right now only 1 strategy.
        if (strategyName.equals("S1")) {
            Strategy strategy = new Strategy1();
            return strategy;
        } else if (strategyName.equals("S2")) {
            return new Strategy2();
        }
        return new Strategy1();

    }
}
