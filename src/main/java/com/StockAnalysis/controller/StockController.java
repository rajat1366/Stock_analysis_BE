package com.StockAnalysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.StockAnalysis.Bean.StrategyResult;
import com.StockAnalysis.services.StockService;
import com.StockAnalysis.services.TradingStrategy.Strategy;
import com.StockAnalysis.services.TradingStrategy.StrategyFactory;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    StockService stockService;
    @Autowired
    StrategyFactory strategyFactory;

    @GetMapping("/price/{stockName}")
    public ResponseEntity<?> getStockData(@PathVariable(value = "stockName") String stockName) {
        try {
            Stock stock = YahooFinance.get(stockName);
            stock.print();
            return ResponseEntity.ok(stock.getQuote());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/data/{name}/{from}")
    public ResponseEntity<?> getStockHistoricalDataFromToPresent(@PathVariable(value = "name") String stockName,
            @PathVariable(value = "from") Integer from) {

        try {

            Calendar fromCal = Calendar.getInstance();
            fromCal.set(Calendar.YEAR, from);

            Stock stock = stockService.getStockDataFromToPresent(stockName, fromCal, Interval.DAILY);

            return ResponseEntity.ok(stock);
        } catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/data/{name}/{from}/{to}")
    public ResponseEntity<?> getStockData(@PathVariable(value = "name") String stockName,
            @PathVariable(value = "from") Integer from, @PathVariable(value = "to") Integer to) {

        try {

            Calendar fromCal = Calendar.getInstance();
            fromCal.set(Calendar.YEAR, from);
            Calendar toCal = Calendar.getInstance();
            // toCal.set(Calendar.YEAR, to);
            toCal.setTime(new Date());
            Stock stock = stockService.getStockDataFromTo(stockName, fromCal, toCal, Interval.DAILY);

            return ResponseEntity.ok(stock);
        } catch (Exception e) {

            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/strategy/{strategyName}/{stock}/{from}/{to}/{capital}")
    public ResponseEntity<?> applyStrategy(@PathVariable(value = "stock") String stockName,
            @PathVariable(value = "strategyName") String strategyName,
            @PathVariable(value = "from") Integer from, @PathVariable(value = "to") Integer to,
            @PathVariable(value = "capital") Double intialCapital,
            @RequestParam(defaultValue = "true") boolean details) {
        try {

            Calendar fromCal = Calendar.getInstance();
            fromCal.set(Calendar.YEAR, from);
            Calendar toCal = Calendar.getInstance();
            toCal.set(Calendar.YEAR, to);

            Stock stock = stockService.getStockDataFromTo(stockName, fromCal, toCal, Interval.DAILY);
            Strategy strategy = strategyFactory.getStrategy(strategyName);
            StrategyResult strategyResult = strategy.applyStrategy(stockName, stock.getHistory(), intialCapital);
            if (!details) {
                strategyResult.setTransactions(new ArrayList<>());
            }
            return ResponseEntity.ok(strategyResult);
        } catch (Exception e) {
            // System.out.println(e.getStackTrace());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
