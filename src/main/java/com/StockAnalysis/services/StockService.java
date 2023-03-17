package com.StockAnalysis.services;

import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.*;

@Service
public class StockService {

    public Stock getStockDataFromTo(String tickerName, Calendar from, Calendar to, Interval interval)
            throws IOException {
        Stock stock = YahooFinance.get(tickerName, from, to, interval);
        return stock;
    }

    public Stock getStockDataFromToPresent(String tickerName, Calendar from, Interval interval) throws IOException {
        Stock stock = YahooFinance.get(tickerName, from, interval);
        return stock;
    }
}
