package controller;

import java.io.IOException;

import dati.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class PrevediOutputLivelliController {

    @FXML
    private Button close;

    @FXML
    private Button indietro;
    
    @FXML
    private Button livelloAvanzato1;

    @FXML
    private Button livelloBase1;

    @FXML
    private Button livelloMedio1;
    
    @FXML
    private Label utente; 
    
    // Ottieni l'utente loggato dalla sessione Singleton
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
    
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
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));

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
        // Riconosci il bottone che ha generato l'evento
        Button bottoneCliccato = (Button) event.getSource();
        
        // variabile per il file fxml
        String nomeFileFXML = "";

        // Sceglie il file fxml in base al bottone cliccato che trova tramite il suo id
        switch (bottoneCliccato.getId()) {
            case "livelloBase1":
            	nomeFileFXML = "/application/OutputBase.fxml";
                break;
            case "livelloMedio1":
            	nomeFileFXML = "/application/OutputMedio.fxml";
                break;
            case "livelloAvanzato1":
            	nomeFileFXML = "/application/OutputAvanzato.fxml";
                break;
            default:
                System.out.println("Nessun livello corrispondente trovato!");
                return;
        }

        // Carica la scena successiva
        try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource(nomeFileFXML));

            Stage scenaCorrente = (Stage) bottoneCliccato.getScene().getWindow();
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.setFullScreen(true);
            scenaCorrente.setFullScreenExitHint("");
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva!");
            e.printStackTrace();
        }
    }
    
    
    @FXML
    void popUpUtente() {
    	    try {
    	        // Ottieni il palco (Stage) della finestra principale
    	        Stage stagePrincipale = (Stage) utente.getScene().getWindow();

    	        // Carica il pop-up
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/PopUpUtente.fxml"));
    	        Parent popUp = loader.load();
    	        
    	        // Crea una nuova finestra (Stage) per il pop-up
    	        Stage popUpStage = new Stage();
    	        popUpStage.setScene(new Scene(popUp));

    	        // Modalità per evitare l'interazione con la finestra principale
    	        popUpStage.initModality(Modality.WINDOW_MODAL); 
    	        popUpStage.initOwner(stagePrincipale); // La finestra principale è il proprietario del pop-up

    	        popUpStage.show();  // Mostra il pop-up
    	    }
    	    catch (NullPointerException | IOException e) {
    	        System.out.println("Errore nel caricamento della schermata successiva!");
    	    }   
	}

}