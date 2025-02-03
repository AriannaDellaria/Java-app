package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GiocaController {

    @FXML
    private Button close;

    @FXML
    private Label start;

    @FXML
    void closeButton(MouseEvent event) {
        Stage stage = (Stage) close.getScene().getWindow(); //riprende la scena e lo stage 
        stage.close(); //chiude la scena 
    }
    
    @FXML
    void colorChangeRed(MouseEvent event) {
    	close.setStyle("-fx-background-color: red;");
    }

    @FXML
    void colorChangeBasic(MouseEvent event) {
    	close.setStyle("");
    }

    @FXML
    void startGame(MouseEvent event) {
        try {
        	// Parent è il tipo della variabile, è il primo nodo
        	//il loader carica il file dell'interfaccia corrispondente
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));

            // event.getSource fa riferimento al label
            Stage scenaCorrente = (Stage) start.getScene().getWindow();
            
            //Modifica la scena corrente
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.setFullScreen(true);
            scenaCorrente.setFullScreenExitHint("");
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
        	System.out.println(e.getMessage()); 
            System.out.println("Errore nel caricamento della schermata successiva!");
        }
    }
}