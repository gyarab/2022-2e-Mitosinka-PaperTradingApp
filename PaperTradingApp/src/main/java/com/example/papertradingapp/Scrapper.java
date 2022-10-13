package com.example.papertradingapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Scrapper {

    private static final String webURL = "https://coinmarketcap.com/currencies/";
    Elements elements;

    public void currentPrice(String cryptoCurrencyName){
        String CryptoCurrencyNameUrl = cryptoCurrencyName+"/";

        Document document;


            try{
                document = Jsoup.connect(webURL+CryptoCurrencyNameUrl).get();

            } catch (IOException e) {
                System.out.println("Could not find entered cryptocurrency");
                return;
            }
            elements = document.getElementsByClass("priceValue");

    }



}
