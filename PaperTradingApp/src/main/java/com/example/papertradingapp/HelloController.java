package com.example.papertradingapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static java.lang.System.err;
import static java.lang.System.out;

public class HelloController {

    public HashMap<String, Double> portfolio = new HashMap<>();
    public ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

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

    public String currencyAndAmount1;

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

    @FXML
    public void onCloseSettingsButtonClick(){
        Group items = new Group(balance, balanceButton, searchBar, searchCurrencyLabel);
        items.setVisible(true);
        Group settingsItems = new Group(darkModeButton);
        settingsItems.setVisible(false);
    }

    @FXML
    public void onDarkModeButtonClick(){

        background.setStyle(("-fx-background-color: #282828"));
        sellButton.setStyle(("-fx-background-color: #282828"));
    }


    @FXML
    public void onSettingsButtonClick(){
        Group items = new Group(sellButton, balance, balanceButton, searchBar, priceLabel, amountBar, amountLabel, portfolioChart, errorMessage, searchCurrencyLabel);
        items.setVisible(false);
        Group settingsItems = new Group(darkModeButton, closeSettingsButton);
        settingsItems.setVisible(true);

    }


    @FXML
    public void onSellButtonClick() throws IOException {
        double pricePerToken;
        String temp;

        Scrapper scraping = new Scrapper();

        String data = "";
        double totalAmountOfMoney;
        String currentMoney;

        File dataFile = new File("Data.txt"); //file data
        Path dataPath = Paths.get("Data.txt"); //data path
        //creating data file, where all user data will be stored
        boolean result;
        try {
            result = dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Data reading
        try {
            File file = new File("Data.txt");
            FileReader reader = new FileReader(file);
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
        String[] dataString = data.split("\\s+"); //splitting data into multiple characters by space

        //adding array values to each string
        currentMoney = dataString[0];
        String[] temp1;
        String currency;
        String amountOfTokensYouOwn1;
        double amountOfTokensYouOwn;
        if (dataString.length > 1) {
            currencyAndAmount1 = dataString[1];
            temp1 = currencyAndAmount1.split("-");
            currency = temp1[0];
            amountOfTokensYouOwn1 = temp1[1];
            amountOfTokensYouOwn = Double.parseDouble(amountOfTokensYouOwn1);

            String seekedCurrency = searchBar.getText();
            scraping.currentPrice(seekedCurrency);

            if (dataString.length > 0 && seekedCurrency != currency) {
                errorMessage.setText("You dont have any of this currency");
                sellButton.setVisible(false);
            }

            errorMessage.setVisible(false);

            temp = scraping.elements.text();
            String currentPrice = temp;
            currentPrice = currentPrice.replace("$", "");
            currentPrice = currentPrice.replace(",", "");

            pricePerToken = Double.parseDouble(currentPrice);
            String sellingAmount = amountBar.getText();

            double sellAmount = Double.parseDouble(sellingAmount);
            double sellAmountOfTokens = sellAmount / pricePerToken;
            if (sellAmountOfTokens > amountOfTokensYouOwn) {
                errorMessage.setText("Entered amount is higher then your current amount of tokens");
            }

            //out.println("you have right now: "+amountOfTokens+" tokens of "+seekedCurrency);
            totalAmountOfMoney = Double.parseDouble(currentMoney);
            totalAmountOfMoney = totalAmountOfMoney + sellAmount;
            currentMoney = Double.toString(totalAmountOfMoney);
            balance.setText("Account balance: "+currentMoney+"$");

            //rewriting data in dataFile
            if (sellAmountOfTokens < amountOfTokensYouOwn) {
                while (true) {
                    try {
                        Files.writeString(dataPath, currentMoney + " " + seekedCurrency + "-" + (amountOfTokensYouOwn - sellAmountOfTokens), StandardCharsets.UTF_8);
                        break;

                    } catch (IOException ex) {
                        out.println("Error, failed to save data.");
                    }
                }
            } else {
                while (true) {
                    try {
                        Files.writeString(dataPath, currentMoney, StandardCharsets.UTF_8);
                        break;

                    } catch (IOException ex) {
                        out.println("Error, failed to save data.");
                    }
                }
            }


        } else {
            errorMessage.setText("Not enough searched currency.");
            errorMessage.setVisible(true);
        }

        buyButton.setVisible(false);
        sellButton.setVisible(false);

        amountLabel.setVisible(false);
        amountBar.setVisible(false);

    }


    @FXML
    public void onBuyButtonClick() {
        double pricePerToken;
        String temp;
        double amountOfTokens;
        String currencyAndAmount1;

        Scrapper scraping = new Scrapper();

        String data = "";
        double totalAmountOfMoney;
        String currentMoney;

        File dataFile = new File("Data.txt"); //file data
        Path dataPath = Paths.get("Data.txt"); //data path

        //creating data file, where all user data will be stored
        boolean result;
        try {
            result = dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Data reading
        try {
            File file = new File("Data.txt");
            FileReader reader = new FileReader(file);
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
        String[] dataString = data.split("\\s+"); //splitting data into multiple characters by space

        //adding array values to each string
        currentMoney = dataString[0];
        String[] temp1;
        String currency;
        String amountOfTokensYouOwn1;
        if (dataString.length > 1) {
            currencyAndAmount1 = dataString[1];
            temp1 = currencyAndAmount1.split("-");
            currency = temp1[0];
            amountOfTokensYouOwn1 = temp1[1];

            Double amountOfTokensYouOwn = Double.parseDouble(amountOfTokensYouOwn1);

        }

        totalAmountOfMoney = Double.parseDouble(currentMoney);


        String seekedCurrency = searchBar.getText();
        scraping.currentPrice(seekedCurrency);

        Double buyingAmount = Double.valueOf(amountBar.getText());
        if (buyingAmount > totalAmountOfMoney) {
            //out.println("Entered amount is bigger then your current portfolio.");
            //out.println("Please enter valid amount, your current portfolio is: ");
            errorMessage.setText("Not enough balance, current balance is: " + currentMoney);
            errorMessage.setVisible(true);
            buyingAmount = Double.valueOf(amountBar.getText());
        }

        temp = scraping.elements.text();
        String currentPrice = temp;
        currentPrice = currentPrice.replace("$", "");
        currentPrice = currentPrice.replace(",", "");

        pricePerToken = Double.parseDouble(currentPrice);

        amountOfTokens = buyingAmount / pricePerToken;


        pieChartData.add(new PieChart.Data(seekedCurrency, amountOfTokens));
        portfolioChart.setData(pieChartData);
        portfolioChart.setVisible(true);

        //out.println("you have right now: "+amountOfTokens+" tokens of "+seekedCurrency);

        Double moneyLeft = totalAmountOfMoney - buyingAmount;
        currentMoney = Double.toString(moneyLeft);

        portfolio.put(seekedCurrency, amountOfTokens);
        pieChartData.add(new PieChart.Data(seekedCurrency, amountOfTokens));
        balance.setText("Account balance: "+currentMoney+"$");
        //rewriting data in dataFile
        while (true) {
            try {
                Files.writeString(dataPath, currentMoney + " " + seekedCurrency + "-" + amountOfTokens, StandardCharsets.UTF_8);
                break;

            } catch (IOException ex) {
                out.println("Error, failed to save data.");
            }
        }
        buyButton.setVisible(false);
        sellButton.setVisible(false);

        amountLabel.setVisible(false);
        amountBar.setVisible(false);

    }


    @FXML
    public void onSearchBarAction() {
        double pricePerToken;
        String temp;
        double amountOfTokens;


        Scrapper scraping = new Scrapper();

        String data = "";
        double totalAmountOfMoney;
        String currentMoney;

        errorMessage.setVisible(false);
        File dataFile = new File("Data.txt"); //file data


        //creating data file, where all user data will be stored
        boolean result;
        try {
            result = dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Data reading
        try {
            File file = new File("Data.txt");
            FileReader reader = new FileReader(file);
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
        String[] dataString = data.split("\\s+"); //splitting data into multiple characters by space

        //adding array values to each string
        currentMoney = dataString[0];


        totalAmountOfMoney = Double.parseDouble(currentMoney);


        //out.print("Enter cryptocurrency you would like to buy: ");
        String seekedCurrency = searchBar.getText();
        scraping.currentPrice(seekedCurrency);
        //out.print("Enter amount of money you would like to invest in: ");
        /*Double buyingAmount = sc.nextDouble();
        while(buyingAmount>totalAmountOfMoney){
            out.println("Entered amount is bigger then your current portfolio.");
            out.println("Please enter valid amount, your current portfolio is: "+);
            buyingAmount = sc.nextDouble();
        }*/

        temp = scraping.elements.text();
        String currentPrice = temp;
        currentPrice = currentPrice.replace("$", "");
        currentPrice = currentPrice.replace(",", "");


        out.println(currentPrice);
        pricePerToken = Double.parseDouble(currentPrice);

        //amountOfTokens = buyingAmount/pricePerToken;

        //out.println("you have right now: "+amountOfTokens+" tokens of "+seekedCurrency);

        //Double moneyLeft = totalAmountOfMoney-buyingAmount;
        //currentMoney=Double.toString(moneyLeft);
        seekedCurrency = Character.toUpperCase(seekedCurrency.charAt(0)) + seekedCurrency.substring(1);
        if (seekedCurrency.equals("bnb")) {
            seekedCurrency = "BNB";
        }
        priceLabel.setVisible(true);
        priceLabel.setText("Current price of " + seekedCurrency + " is " + currentPrice + "$");
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

    @FXML
    public void accountBalance() {


        String data = "";
        double totalAmountOfMoney;
        String currentMoney;

        File dataFile = new File("Data.txt"); //file data
        Path dataPath = Paths.get("Data.txt"); //data path

        //creating data file, where all user data will be stored
        boolean result;
        try {
            result = dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Data reading
        try {
            File file = new File("Data.txt");
            FileReader reader = new FileReader(file);
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
        String[] dataString = data.split("\\s+"); //splitting data into multiple characters by space

        //adding array values to each string
        currentMoney = dataString[0];


        totalAmountOfMoney = Double.parseDouble(currentMoney);
        out.println("Current balance is: " + totalAmountOfMoney + "$");

        balance.setText("Account balance: " + totalAmountOfMoney + "$");


    }


}