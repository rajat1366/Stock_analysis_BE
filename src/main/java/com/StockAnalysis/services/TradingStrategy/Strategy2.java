package com.StockAnalysis.services.TradingStrategy;

import java.util.ArrayList;
import java.util.List;
import com.StockAnalysis.Bean.StrategyResult;
import com.StockAnalysis.Bean.Transaction;
import com.StockAnalysis.util.TransactionTypeEnum;
import yahoofinance.histquotes.HistoricalQuote;

public class Strategy2 implements Strategy {
    public StrategyResult applyStrategy(String stockName, List<HistoricalQuote> history, Double intialCapital) {
        List<Transaction> transactions = new ArrayList<>();

        // int lookBackDays = 8;
        // int candleRange = 5;
        // Double percentageOverPast = 0.04;
        // Double avgDiff = 5.0;
        // Double currCapitalAmount = intialCapital;

        int lookBackDays = 5;
        int candleRange = 4;
        Double percentageOverPast = 0.04;
        Double avgDiff = 5.0;
        Double currCapitalAmount = intialCapital;

        int greenCandleCount = 0, redCandleCount = 0;
        Double redSum = 0.0, greenSum = 0.0;
        if (history.size() > 10) {
            // previous 5 candle count

            for (int i = lookBackDays - candleRange; i < lookBackDays; i++) {
                HistoricalQuote hQ = history.get(i);
                Double open = hQ.getOpen().doubleValue(), close = hQ.getClose().doubleValue();
                if (open < close) {
                    greenCandleCount++;
                    greenSum += (open - close);
                } else {
                    redCandleCount++;
                    redSum += (close - open);
                }
            }

            for (int i = lookBackDays; i < history.size(); i++) {
                Transaction transaction = tradeCheck(history, i, currCapitalAmount, greenCandleCount, redCandleCount,
                        redSum, greenSum, lookBackDays, percentageOverPast, avgDiff);
                if (transaction != null) {
                    currCapitalAmount = transaction.getCapitalNow();
                    transactions.add(transaction);
                }
                HistoricalQuote hqCurr = history.get(i);
                Double open = hqCurr.getOpen().doubleValue(), close = hqCurr.getClose().doubleValue();

                if (open < close) {
                    greenCandleCount++;
                    greenSum += (open - close);
                } else {
                    redCandleCount++;
                    redSum += (close - open);
                }
                HistoricalQuote hqremove = history.get(i - candleRange);
                open = hqremove.getOpen().doubleValue();
                close = hqCurr.getClose().doubleValue(); // hq.curr
                if (open < close) {
                    greenCandleCount--;
                    greenSum -= (open - close);
                } else {
                    redCandleCount--;
                    redSum -= (close - open);
                }

            }
            //
        }

        return new StrategyResult(stockName, currCapitalAmount, transactions);
    }

    public Transaction tradeCheck(List<HistoricalQuote> history, int currInd, Double currCapitalAmount,
            int greenCandleCount, int redCandleCount, Double redSum, Double greenSum, int lookBackDays,
            Double percentageOverPast, Double avgDiff) {

        Double brookeageFee = 100.0;
        HistoricalQuote lastCandle = history.get(currInd - 1);
        Double lastCandleOpen = lastCandle.getOpen().doubleValue(),
                lastCandleClose = lastCandle.getClose().doubleValue();
        HistoricalQuote futureCandle = history.get(currInd);
        Double futureOpen = futureCandle.getOpen().doubleValue(), futureClose = futureCandle.getClose().doubleValue();

        HistoricalQuote tenDayBack = history.get(currInd - lookBackDays);
        Double tenDayBackClose = tenDayBack.getClose().doubleValue();

        Double redAvg = (redCandleCount > 0) ? redSum / (double) redCandleCount : 0.0;
        Double greenAvg = (greenCandleCount > 0) ? greenSum / (double) greenCandleCount : 0.0;

        if (futureOpen < currCapitalAmount) {
            if ((greenCandleCount > redCandleCount) && (Math.abs(greenAvg - redAvg) > avgDiff)
                    && (lastCandleClose > ((tenDayBackClose) + tenDayBackClose * percentageOverPast))) {
                // we go for buy

                Transaction t = new Transaction(TransactionTypeEnum.BUY, 0.0, 0.0, 0.0, futureCandle.getDate(),
                        redCandleCount,
                        greenCandleCount, futureOpen, futureClose, 0.0);
                t.setProfitLoss(futureClose - futureOpen);
                t.setQuantity(Math.floor(currCapitalAmount / futureOpen));
                t.setCapitalNow(currCapitalAmount + (t.getProfitLoss() * t.getQuantity()));

                return t;
            }
            if ((redCandleCount > greenCandleCount) && (Math.abs(greenAvg - redAvg) > avgDiff)
                    && (lastCandleClose < ((tenDayBackClose) - tenDayBackClose * percentageOverPast))) {
                // we go for sell

                Transaction t = new Transaction(TransactionTypeEnum.SELL, 0.0, 0.0, 0.0, futureCandle.getDate(),
                        redCandleCount,
                        greenCandleCount, futureOpen, futureClose, 0.0);
                t.setProfitLoss(futureOpen - futureClose);
                t.setQuantity(Math.floor(currCapitalAmount / futureOpen));
                t.setCapitalNow(currCapitalAmount + (t.getProfitLoss() * t.getQuantity()));
                return t;
            }
        }

        return null;
    }
}
