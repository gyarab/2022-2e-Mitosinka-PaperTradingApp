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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.out;

public class HelloController {

    @FXML
    public Button buyButton;
    @FXML
    public Label portfolioLabel;
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
    @FXML
    public Button yesButton;
    @FXML
    public Button noButton;
    Double Money = null;
    Scraper scraping = new Scraper();
    String cryptoCurrency = "";
    Double amount = 0.0;
    Double currentPrice;


    //Tato funkce zobrazí informace o aktuálním portfoliu uživatele. Pokud uživatel ještě nemá žádnou kryptoměnu v portfoliu, zobrazí se zpráva "Portfolio: None".
    // Pokud má uživatel nějakou kryptoměnu v portfoliu, zobrazí se informace o kryptoměně, počtu tokenů a současné hodnotě tohoto portfolia v dolarech.
    @FXML
    public void onPortfolioButtonClick() throws IOException {
        checkCurrency();
        if (cryptoCurrency == null || cryptoCurrency.equals("")) {
            portfolioLabel.setText("Portfolio: None");
        } else {
            portfolioLabel.setText("Portfolio: " + cryptoCurrency + "-" + Math.round(amount * 100.0) / 100.0 + "-" + Math.round(amount * scraping.currentPrice(cryptoCurrency) * 100.0) / 100.0 + "$");
        }
    }


    //Tato funkce načte informace o aktuálním zůstatku účtu z textového souboru "Balance.txt" a zobrazí je na obrazovce v Labelu "balance".
    public double accountBalance() {
        String data = "";

        File balanceFile = new File("Balance.txt");

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
        Money = Double.parseDouble(data);

        out.println("Current balance is: " + Money + "$");

        balance.setText("Account balance: " + Money + "$");

        return Money;

    }


    //Tato funkce uloží informace o nákupu kryptoměny (název měny a počet tokenů) do textového souboru "Data.txt".
    public void saveCurrency() {
        Path dataPath = Paths.get("Data.txt");
        while (true) {
            try {
                Files.writeString(dataPath, searchBar.getText() + "-" + amount, StandardCharsets.UTF_8);
                break;

            } catch (IOException ex) {
                out.println("Error, failed to save data.");
            }
        }
    }

    //Tato funkce aktualizuje zůstatek účtu po provedení nákupu kryptoměny. Nejprve zavolá funkci accountBalance() pro načtení aktuálního zůstatku účtu,
    // poté od tohoto zůstatku odečte cenu kryptoměny a nový zůstatek uloží zpět do souboru "Balance.txt".
    public void updateBalanceOnBuy() {
        Path dataPath = Paths.get("Balance.txt");
        accountBalance();
        Money = Money - Double.parseDouble(amountBar.getText());
        while (true) {
            try {
                Files.writeString(dataPath, String.valueOf(Money), StandardCharsets.UTF_8);
                break;

            } catch (IOException ex) {
                out.println("Error, failed to save data.");
            }
        }
    }


    //Tato funkce aktualizuje zůstatek účtu po provedení prodeje kryptoměny. Nejprve zavolá funkci accountBalance() pro načtení aktuálního zůstatku účtu,
    // poté přičte cenu kryptoměny a nový zůstatek uloží zpět do souboru "Balance.txt".
    public void updateBalanceOnSell() throws IOException {
        Path dataPath = Paths.get("Balance.txt");
        accountBalance();
        checkCurrency();

        Money = Money + Double.parseDouble(amountBar.getText());

        while (true) {
            try {
                Files.writeString(dataPath, String.valueOf(Money), StandardCharsets.UTF_8);
                break;

            } catch (IOException ex) {
                out.println("Error, failed to save data.");
            }
        }
    }

    //Tato funkce zkontroluje, zda uživatel již nakoupil nějakou kryptoměnu a pokud ano, načte její název a počet tokenů a název z textového souboru "Data.txt".
    public void checkCurrency() {
        String data = "";
        File balanceFile = new File("Data.txt");

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
        if (!data.equals("")) {
            String[] dataString = data.split("-");
            cryptoCurrency = dataString[0];
            amount = Double.parseDouble(dataString[1]);

        }
    }

    //Po kliknutí na tlačítko "Buy" tato funkce skryje tlačítka pro nákup a prodej, pole pro množství, zkontroluje platnost zadaného množství a zjistí cenu zadané kryptoměny.
    // Poté funkce ověří, zda má uživatel dostatečný zůstatek na účtu a pokud ano, přidá kryptoměnu do portfolia a aktualizuje zůstatek. Na konci uloží změny v portfoliu.
    @FXML
    public void onBuyButtonClick() throws IOException {
        buyButton.setVisible(false);
        sellButton.setVisible(false);
        amountBar.setVisible(false);
        amountLabel.setVisible(false);
        currentPrice = scraping.currentPrice(searchBar.getText());
        if (amountBar.getText() == null || !amountBar.getText().matches("[0-9]+")) {
            errorMessage.setVisible(true);
            errorMessage.setText("Invalid amount");
            amountBar.setVisible(false);
        }
        accountBalance();
        checkCurrency();
        out.println(Money);
        if (Double.parseDouble(amountBar.getText()) > Money) {
            errorMessage.setVisible(true);
            amountBar.setVisible(false);
            errorMessage.setText("Insufficient balance.");
        } else {
            amount = amount + Double.parseDouble(amountBar.getText()) / scraping.currentPrice(searchBar.getText());
            errorMessage.setText("Bought " + Double.parseDouble(amountBar.getText()) / currentPrice + " of " + searchBar.getText() + " at market price: " + currentPrice + "$");
            portfolioLabel.setText("Portfolio: " + cryptoCurrency + "-" + Math.round(amount * 100.0) / 100.0 + "-" + Math.round(amount * currentPrice * 100.0) / 100.0 + "$");
            errorMessage.setVisible(true);
            saveCurrency();
            updateBalanceOnBuy();
            accountBalance();
        }

    }

    //Podobně jako onBuyButtonClick(), tato funkce skryje tlačítka pro nákup a prodej, pole pro množství, kontroluje platnost zadaného množství a zjistí cenu zadané kryptoměnyu.
    // Poté funkce ověřuje, zda uživatel vlastní dostatek kryptoměny k prodeji a pokud ano, odečte kryptoměnu z portfolia a aktualizuje zůstatek. Na konci uloží změny v portfoliu.
    @FXML
    public void onSellButtonClick() throws IOException {
        buyButton.setVisible(false);
        sellButton.setVisible(false);
        amountBar.setVisible(false);
        amountLabel.setVisible(false);
        currentPrice = scraping.currentPrice(searchBar.getText());
        if (amountBar.getText() == null || !amountBar.getText().matches("[0-9]+")) {
            errorMessage.setVisible(true);
            errorMessage.setText("Invalid amount");
            amountBar.setVisible(false);
        }
        accountBalance();
        if (Double.parseDouble(amountBar.getText()) / scraping.currentPrice(searchBar.getText()) > amount) {
            errorMessage.setVisible(true);
            amountBar.setVisible(false);
            errorMessage.setText("Insufficient balance");
        } else {
            amount = amount - Double.parseDouble(amountBar.getText()) / scraping.currentPrice(searchBar.getText());
            errorMessage.setText("Sold " + Double.parseDouble(amountBar.getText()) / currentPrice + " of " + searchBar.getText() + " at market price: " + currentPrice + "$");
            portfolioLabel.setText("Portfolio: " + cryptoCurrency + "-" + Math.round(amount * 100.0) / 100.0 + "-" + Math.round(amount * currentPrice * 100.0) / 100.0 + "$");
            errorMessage.setVisible(true);
            saveCurrency();
            updateBalanceOnSell();
            accountBalance();
        }
    }

    //Tato funkce se spustí, když uživatel začne psát název kryptoměny v poli pro hledání. Funkce nejprve zjistí aktuální cenu zadané kryptoměny a uloží ji do proměnné.
    // Poté kontroluje, zda uživatel vlastní již nějakou kryptoměnu a pokud ano, zobrazí zprávu s dotazem, zda chce tuto kryptoměnu přepsat. Pokud uživatel nemá vlastněnou žádnou kryptoměnu,
    // funkce zobrazí tlačítka pro nákup a prodej a pole pro zadání množství.
    @FXML
    public void onSearchBarAction() throws IOException {
        currentPrice = scraping.currentPrice(searchBar.getText());

        out.println(currentPrice);

        checkCurrency();

        out.println(cryptoCurrency);

        if (cryptoCurrency == "" || cryptoCurrency == null || cryptoCurrency.equals(searchBar.getText())) {
            amountBar.setVisible(true);
            amountLabel.setVisible(true);
            yesButton.setVisible(false);
            noButton.setVisible(false);
            errorMessage.setVisible(false);
            priceLabel.setVisible(true);
            priceLabel.setText("Current price of " + searchBar.getText() + " is " + currentPrice + "$");
            priceLabel.setAlignment(Pos.CENTER);
            buyButton.setText("Buy");
            sellButton.setText("Sell");
            buyButton.setVisible(true);
            sellButton.setVisible(true);

            amountBar.setVisible(true);
            amountLabel.setVisible(true);
        } else {
            amountBar.setVisible(false);
            amountLabel.setVisible(false);
            buyButton.setVisible(false);
            sellButton.setVisible(false);
            priceLabel.setVisible(false);
            errorMessage.setText("You already own 1 cryptocurrency, do you want to overwrite it?");
            errorMessage.setVisible(true);
            yesButton.setVisible(true);
            noButton.setVisible(true);
        }

    }


    //Tato funkce se spustí, když uživatel potvrdí, že chce přepsat svou vlastněnou kryptoměnu. Funkce vymaže veškeré údaje v souboru "Data.txt".
    @FXML
    public void onYesButtonClick() {
        yesButton.setVisible(false);
        noButton.setVisible(false);
        Path dataPath = Paths.get("Data.txt");

        while (true) {
            try {
                Files.writeString(dataPath, "", StandardCharsets.UTF_8);
                errorMessage.setText("Data has been overwritten successfully, please restart the application");
                errorMessage.setVisible(true);
                break;

            } catch (IOException ex) {
                out.println("Error, failed to save data.");
            }
        }

    }

    //Tato funkce se spustí, když uživatel odmítne přepsat svou vlastněnou kryptoměnu. Funkce zobrazí zprávu, aby uživatel zadal název kryptoměny, kterou vlastní.
    @FXML
    public void onNoButtonClick() {
        yesButton.setVisible(false);
        noButton.setVisible(false);
        errorMessage.setText("Please enter the cryptocurrency you own.");
        errorMessage.setVisible(true);
    }

    //Tato funkce spustí funkci accountBalance
    @FXML
    public void onCheckBalanceButtonClick() {
        accountBalance();
    }


}