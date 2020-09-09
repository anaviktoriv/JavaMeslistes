package com.anavidev.meslistes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Hi");

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

       primaryStage.setTitle("Meslistes");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
