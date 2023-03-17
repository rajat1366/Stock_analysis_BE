package com.StockAnalysis.Bean;

import com.StockAnalysis.util.TransactionTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private TransactionTypeEnum transactionType;
    private Double profitLoss;
    private Double capitalNow;
    private Double drawDown;
    private Calendar date;
    private int redCount;
    private int greenCount;
    private Double open;
    private Double close;
    private Double quantity;

}
