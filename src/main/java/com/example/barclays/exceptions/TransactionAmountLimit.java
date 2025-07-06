package com.example.barclays.exceptions;

public class TransactionAmountLimit extends Exception{
    public TransactionAmountLimit(String errorMessage) {
        super(errorMessage);
    }
}
