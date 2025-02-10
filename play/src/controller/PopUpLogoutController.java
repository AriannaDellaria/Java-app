package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUpLogoutController {

    @FXML
    private Button annulla;

    @FXML
    private Button conferma;

    
    @FXML
    void procediAlLogout(MouseEvent event) {
    	try {
    		// Ottieni il riferimento al palco del popup di logout
            Stage stageLogout = (Stage) conferma.getScene().getWindow();
            
            // Ottieni il riferimento alla finestra principale (Home) da cui è stato aperto il popup
            Stage stagePopUpUtente = (Stage) stageLogout.getOwner(); // La finestra principale (Home) è il "genitore" del popup
            
            // Chiudi la finestra principale di Home
            stagePopUpUtente.close();
            stageLogout.close();
            
            // Carica la scena di Login
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
            
            // Ottieni il riferimento alla finestra principale (Home) per cambiarle la scena
            Stage scenaCorrente = (Stage) stagePopUpUtente.getOwner();  // La finestra principale è il "genitore" del popup di "utente"
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.setFullScreen(true);
            scenaCorrente.setFullScreenExitHint("");
            scenaCorrente.show();  // Mostra la finestra di login
 	    }
 	    catch (NullPointerException | IOException e) {
 	        System.out.println("Errore nel caricamento della schermata successiva!");
 	    }   
    }

    @FXML
    void tornaIndietro(MouseEvent event) {
    	try {
            Stage scenaCorrente = (Stage) annulla.getScene().getWindow();
           scenaCorrente.close();     
        } catch (NullPointerException e) {
            System.out.println("Errore nel caricamento della schermata precedente!");
        }
    }

}
