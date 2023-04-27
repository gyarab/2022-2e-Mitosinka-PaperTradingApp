package com.example.papertradingverze2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.NoSuchElementException;

public class Scraper {

    private static final String webURL = "https://coinmarketcap.com/currencies/";
    private Elements elements;

    public double currentPrice (String cryptoCurrencyName) throws IOException {
        String CryptoCurrencyNameUrl = cryptoCurrencyName+"/";

        Document document = Jsoup.connect(webURL+CryptoCurrencyNameUrl).get();

        elements = document.getElementsByClass("priceValue");

        if (elements.size() == 0) {
            throw new NoSuchElementException("Could not find price element");
        }

        String priceStr = elements.first().text().replaceAll("[^0-9.]+", "");
        double price = Double.parseDouble(priceStr);
        return price;
    }
}