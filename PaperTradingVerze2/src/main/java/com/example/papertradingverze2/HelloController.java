package com.example.papertradingverze2;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Double.parseDouble;
import static java.lang.System.out;

public class HelloController {

    String seekedCurrency = "";

    Scrapper scraping = new Scrapper();

    Double currentPrice;
    @FXML
    public Button buyButton;

    @FXML
    public Button sellButton;

    @FXML
    public Label balance;

    @FXML
    public Button balanceButton;

    @FXML
    public TextField searchBar;

    @FXML
    public Label priceLabel;

    @FXML
    public TextField amountBar;

    @FXML
    public Label amountLabel;

    @FXML
    public PieChart portfolioChart;

    @FXML
    public Label errorMessage;

    @FXML
    public Button settingsButton;

    @FXML
    public Pane background;

    @FXML
    public Label searchCurrencyLabel;

    @FXML
    public Button darkModeButton;

    @FXML
    public Button closeSettingsButton;


    public double seekCurrencyPrice(String seekedCurrency){
        scraping.currentPrice(seekedCurrency);
        String price = scraping.elements.text();
        price = price.replace("$", "");
        price = price.replace(",", "");
        String[] currentPrice0 = price.split("\\s+");
        price = currentPrice0[0];
        double d = Double.parseDouble(price);
        return d;
    }

    @FXML
    public void onSearchBarAction(){
        currentPrice = seekCurrencyPrice((searchBar.getText()));
        out.println(currentPrice);

        priceLabel.setVisible(true);
        priceLabel.setText("Current price of " + searchBar.getText() + " is " + currentPrice + "$");
        priceLabel.setAlignment(Pos.CENTER);
        buyButton.setVisible(true);
        sellButton.setVisible(true);

        amountBar.setVisible(true);
        amountLabel.setVisible(true);
    }
    @FXML
    public void onCheckBalanceButtonClick() {
        accountBalance();
    }

    public double accountBalance() {
        String data = "";

        File balanceFile = new File("Balance.txt"); //file data

        //Data reading
        try {
            FileReader reader = new FileReader(balanceFile);
            BufferedReader bfreader = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = bfreader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            data = sb.toString();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Double totalAmountOfMoney = Double.parseDouble(data);

        out.println("Current balance is: " + totalAmountOfMoney + "$");

        balance.setText("Account balance: " + totalAmountOfMoney + "$");

        return totalAmountOfMoney;

    }


}