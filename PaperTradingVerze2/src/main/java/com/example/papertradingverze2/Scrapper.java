package com.example.papertradingverze2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Scrapper {

    private static final String webURL = "https://coinmarketcap.com/currencies/";
    Elements elements;

    public String currentPrice(String cryptoCurrencyName){
        String CryptoCurrencyNameUrl = cryptoCurrencyName+"/";

        Document document = null;


        try{
            document = Jsoup.connect(webURL+CryptoCurrencyNameUrl).get();

        } catch (IOException e) {
            System.out.println("Could not find entered cryptocurrency");
            
        }
        elements = document.getElementsByClass("priceValue");
        return CryptoCurrencyNameUrl;
    }



}
