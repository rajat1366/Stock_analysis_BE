package com.StockAnalysis;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@SpringBootTest
class StockAnalysisApplicationTests {

	@Test
	void contextLoads() {
		try {
			Stock stock = YahooFinance.get("SBIN.NS");
			double price = stock.getQuote(true).getPrice().doubleValue();
			stock.print();
			System.out.println(price);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
