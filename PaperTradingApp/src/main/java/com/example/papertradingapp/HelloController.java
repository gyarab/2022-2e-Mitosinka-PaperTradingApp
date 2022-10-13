package com.example.papertradingapp;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

public class HelloController{

    public HashMap<String, Double> portfolio = new HashMap<>();
    public ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML
    public Button buyButton;

    @FXML
    public Button sellButton;

    @FXML
    public Label balance;

    @FXML
    public Button button;

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
    public void onSellButtonClick(){
        double pricePerToken;
        String temp;
        double amountOfTokens;

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

        totalAmountOfMoney = Double.parseDouble(currentMoney);
        String seekedCurrency = searchBar.getText();
        scraping.currentPrice(seekedCurrency);

        Double buyingAmount = Double.parseDouble(amountBar.getText());
        if(buyingAmount>totalAmountOfMoney){
            //out.println("Entered amount is bigger then your current portfolio.");
            //out.println("Please enter valid amount, your current portfolio is: ");
            errorMessage.setVisible(true);
            errorMessage.setText("Insufficient balance");
            buyingAmount = Double.parseDouble(amountBar.getText());
        }
        errorMessage.setVisible(false);

        temp = scraping.elements.text();
        String currentPrice = temp;
        currentPrice = currentPrice.replace("$", "");
        currentPrice = currentPrice.replace(",","");

        pricePerToken = Double.parseDouble(currentPrice);

        amountOfTokens = buyingAmount/pricePerToken;

        //out.println("you have right now: "+amountOfTokens+" tokens of "+seekedCurrency);


        Double moneyLeft = totalAmountOfMoney-buyingAmount;
        currentMoney=Double.toString(moneyLeft);

        //rewriting data in dataFile
        while(true) {
            try {
                Files.writeString(dataPath, currentMoney, StandardCharsets.UTF_8);
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
    public void onBuyButtonClick(){
        double pricePerToken;
        String temp;
        double amountOfTokens;


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



        totalAmountOfMoney = Double.parseDouble(currentMoney);



        String seekedCurrency = searchBar.getText();
        scraping.currentPrice(seekedCurrency);

        Double buyingAmount = Double.valueOf(amountBar.getText());
        while(buyingAmount>totalAmountOfMoney){
            //out.println("Entered amount is bigger then your current portfolio.");
            //out.println("Please enter valid amount, your current portfolio is: ");
            buyingAmount = Double.valueOf(amountBar.getText());
        }

        temp = scraping.elements.text();
        String currentPrice = temp;
        currentPrice = currentPrice.replace("$", "");
        currentPrice = currentPrice.replace(",","");

        pricePerToken = Double.parseDouble(currentPrice);

        amountOfTokens = buyingAmount/pricePerToken;

        //out.println("you have right now: "+amountOfTokens+" tokens of "+seekedCurrency);

        Double moneyLeft = totalAmountOfMoney-buyingAmount;
        currentMoney=Double.toString(moneyLeft);

        portfolio.put(seekedCurrency, amountOfTokens);
        pieChartData.add(new PieChart.Data(seekedCurrency, amountOfTokens));

        portfolioChart.setData(pieChartData);


        //rewriting data in dataFile
        while(true) {
            try {
                Files.writeString(dataPath, currentMoney, StandardCharsets.UTF_8);
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
    public void onSearchBarAction(){
        double pricePerToken;
        String temp;
        double amountOfTokens;


        Scrapper scraping = new Scrapper();

        String data = "";
        double totalAmountOfMoney;
        String currentMoney;

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
        currentPrice = currentPrice.replace(",","");


        out.println(currentPrice);
        pricePerToken = Double.parseDouble(currentPrice);

        //amountOfTokens = buyingAmount/pricePerToken;

        //out.println("you have right now: "+amountOfTokens+" tokens of "+seekedCurrency);

        //Double moneyLeft = totalAmountOfMoney-buyingAmount;
        //currentMoney=Double.toString(moneyLeft);
        seekedCurrency = Character.toUpperCase(seekedCurrency.charAt(0)) + seekedCurrency.substring(1);
        if(seekedCurrency.equals("bnb")){
            seekedCurrency = "BNB";
        }
        priceLabel.setVisible(true);
        priceLabel.setText("Current price of "+seekedCurrency+" is "+currentPrice+"$");
        priceLabel.setAlignment(Pos.CENTER);
        buyButton.setVisible(true);
        sellButton.setVisible(true);

        amountBar.setVisible(true);
        amountLabel.setVisible(true);


    }

    @FXML
    public void onCheckBalanceButtonClick(){
        accountBalance();
    }

    @FXML
    public void accountBalance(){


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
        out.println("Current balance is: "+totalAmountOfMoney+"$");

        balance.setText("Account balance: "+totalAmountOfMoney+ "$");

        //rewriting data in dataFile
        while(true) {
            try {
                Files.writeString(dataPath, currentMoney, StandardCharsets.UTF_8);
                break;

            } catch (IOException ex) {
                out.println("Error, failed to save data.");
            }
        }



    }


}