package com.hyper.quoteoftheday;

public class QuotesModel {
    private String quote;

    public QuotesModel(){

    }

    public QuotesModel(String quote) {
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
