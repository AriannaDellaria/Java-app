package controller;

import java.io.IOException;
import dati.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class PopUpLogoutController {

    @FXML
    private Button annulla, conferma;
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();  //getIstance() -> restituisce una e una sola istanza della classe
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato(); //recupera i dati dell'utente che ha effettuato il login
    
    @FXML
    void colorChangeGreen(MouseEvent event) {
    	conferma.setStyle("-fx-background-color: #A2D8A3;-fx-border-color: #2AAA4A"); 
    }
    
    @FXML
    void colorChangeRed(MouseEvent event) {
    	annulla.setStyle("-fx-background-color: #FFC8AE;-fx-border-color: #f64c4c"); 
    }
    
    @FXML
    void colorChangeBasic(MouseEvent event) {
    	conferma.setStyle("-fx-background-color: white; -fx-border-color: #2AAA4A; -fx-border-width: 2px;");
    	annulla.setStyle("-fx-background-color: white; -fx-border-color: #f64c4c; -fx-border-width: 2px;");
    }
    
    @FXML
    void procediAlLogout(MouseEvent event) {
    	try {
            Stage popUpCorrente = (Stage) conferma.getScene().getWindow();
            Stage popUpUtente = (Stage) popUpCorrente.getOwner(); //viene recuperato il genitore (la scena che ha generato il popUp)  
            Stage genitore = (Stage) popUpUtente.getOwner(); //genitore del genitore del popUpCorrente
            
            //vengono chiusi i genitori
            genitore.close();
            popUpUtente.close();
            
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
            Scene nuovaScena = new Scene(scenaSuccessiva);
            popUpCorrente.setScene(nuovaScena);
            popUpCorrente.show();  
            sessioneGioco.setUtenteLoggato(null);
 	    } catch (NullPointerException | IOException e) {
 	        System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
 	    }   
    }

    //chiude il popUp
    @FXML
    void tornaIndietro(MouseEvent event) {
    	try {
           Stage scenaCorrente = (Stage) annulla.getScene().getWindow();
           scenaCorrente.close();     
        } catch (NullPointerException e) {
            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
        }
    }
}
