package com.StockAnalysis.services.TradingStrategy;

import java.util.List;
import com.StockAnalysis.Bean.StrategyResult;
import yahoofinance.histquotes.HistoricalQuote;

public interface Strategy {
    public StrategyResult applyStrategy(String stockName, List<HistoricalQuote> history, Double intialCapital);
}
