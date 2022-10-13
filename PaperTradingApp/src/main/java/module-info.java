module com.example.papertradingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;


    opens com.example.papertradingapp to javafx.fxml;
    exports com.example.papertradingapp;
}