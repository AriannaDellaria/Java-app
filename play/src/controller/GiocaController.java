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

    //chiude la scena 
    @FXML
    void closeButton(MouseEvent event) {
        Stage stage = (Stage) close.getScene().getWindow(); //prende la scena corrente
        stage.close(); 
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
        	// Parent è il primo nodo (BorderPane)
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Login.fxml")); //il loader carica il file dell'interfaccia successiva

            Stage scenaCorrente = (Stage) start.getScene().getWindow(); //prende la scena corrente
            
            //Crea la nuova scena
            //Modifica la scena corrente
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) { 
            System.out.println("Errore nel caricamento della schermata successiva!");
        }
    }
}