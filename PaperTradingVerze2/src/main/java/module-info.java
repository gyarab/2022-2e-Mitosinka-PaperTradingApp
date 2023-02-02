module com.example.papertradingverze2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;


    opens com.example.papertradingverze2 to javafx.fxml;
    exports com.example.papertradingverze2;
}