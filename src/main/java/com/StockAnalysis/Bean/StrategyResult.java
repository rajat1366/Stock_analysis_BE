package com.StockAnalysis.Bean;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StrategyResult {

    private String stockName;
    private Double capitalNow;
    private List<Transaction> transactions;

}
