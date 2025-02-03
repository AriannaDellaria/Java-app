package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class OutputAvanzatoController {

    @FXML
    private Button close;

    @FXML
    private Button indietro;
    
    @FXML
    private Button next;

    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow(); 
        stage.close(); 
    }

    @FXML
    void colorChangeBasic(MouseEvent event) {
    	indietro.setStyle("");
    	close.setStyle("");
    }

    @FXML
    void colorChangeRed(MouseEvent event) {
    	close.setStyle("-fx-background-color: red;");
    }

    @FXML
    void colorChangeYellow(MouseEvent event) {
    	indietro.setStyle("-fx-background-color: yellow;");	
    }

    @FXML
    void paginaPrecedente(MouseEvent event) {
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/PrevediOutputLivelli.fxml"));

            Stage scenaCorrente = (Stage) indietro.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.setFullScreen(true);
            scenaCorrente.setFullScreenExitHint("");
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente!");
        }
    }
    
    @FXML
    void paginaSuccessiva(MouseEvent event) {
        try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/PrevediOutputLivelli.fxml")); //da modificare nome filefxml

            Stage scenaCorrente = (Stage) next.getScene().getWindow();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.setFullScreen(true);
            scenaCorrente.setFullScreenExitHint("");
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva!");
        }
    }

}
