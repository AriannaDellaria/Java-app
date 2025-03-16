package controller;

import java.io.IOException;
import javafx.scene.control.Label;
import dati.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class RiordinaLivelliController {
	@FXML
    private Button close, indietro, base, medio, avanzato;

	@FXML
	private Label utente, erroreEsercizi; 
	
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
    
    
    @FXML
    public void initialize() {
        utente.setText(utenteCorrente.getUsername());    
    }
    
    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow(); 
        stage.close(); 
    }

    @FXML
    void entrata(MouseEvent event) {
        Button bottone = (Button) event.getSource();

        if (bottone.getId().equals("base")) { // Il base si colora sempre
            bottone.setStyle("-fx-background-color:  #fede77; -fx-border-color: #F9943B");
        }
        else if (bottone.getId().equals("medio") && utenteCorrente.getPg3() >= 0.33) {
            bottone.setStyle("-fx-background-color:  #fede77; -fx-border-color:  #F9943B"); // colore per "medio"
        }
        else if (bottone.getId().equals("avanzato") && utenteCorrente.getPg3() >= 0.66) {
            bottone.setStyle("-fx-background-color:  #fede77; -fx-border-color:  #F9943B"); // colore per "avanzato"
        }
    }

    @FXML
    void uscita(MouseEvent event) {
        Button bottone = (Button) event.getSource();

        if (bottone.getId().equals("base")) { // Il base si colora sempre
            bottone.setStyle("-fx-background-color: white; -fx-border-color:  #F9943B; -fx-border-width: 2");
        }
        // Ritorna al colore di default solo se aveva il colore cambiato
        else if (bottone.getId().equals("medio") && utenteCorrente.getPg3() >= 0.33) {
            bottone.setStyle("-fx-background-color: white; -fx-border-color:  #F9943B; -fx-border-width: 2"); // Ritorna al colore di default
        }
        else if (bottone.getId().equals("avanzato") && utenteCorrente.getPg3() >= 0.66) {
            bottone.setStyle("-fx-background-color: white; -fx-border-color:   #F9943B; -fx-border-width: 2");
        }
    }

    @FXML
    void paginaPrecedente(MouseEvent event) {
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));

            Stage scenaCorrente = (Stage) indietro.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente!");
        }
    }

    @FXML
    void paginaSuccessiva(MouseEvent event) {
        Button bottoneCliccato = (Button) event.getSource();
        
        String nomeFileFXML = "";

        switch (bottoneCliccato.getId()) {
            case "base":
            	nomeFileFXML = "/application/RiordinaBase.fxml";
                break;
            case "medio":
            	if(utenteCorrente.getPg3() >= 0.33) { 
            		nomeFileFXML = "/application/RiordinaMedio.fxml";
            	}
            	else { 
            		erroreEsercizi.setVisible(true); 
            		return;
            	}
                break;
            case "avanzato":
            	if(utenteCorrente.getPg3() >= 0.66) { 
            		nomeFileFXML = "/application/RiordinaAvanzato.fxml";
            	}
            	else { 
            		erroreEsercizi.setVisible(true); 
            		return;
            	}
                break;
            default:
                System.out.println("Nessun livello corrispondente trovato!");
                return;
        }

        try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource(nomeFileFXML));

            Stage scenaCorrente = (Stage) bottoneCliccato.getScene().getWindow();
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
            e.printStackTrace(); 
        }
    }
    
    @FXML
    void popUpUtente() {
    	    try {
    	        Stage paginaCorrente = (Stage) utente.getScene().getWindow();

    	        Parent popUp = FXMLLoader.load(getClass().getResource("/application/PopUpUtente.fxml"));
    	        
    	        Stage popUpStage = new Stage();
    	        popUpStage.setScene(new Scene(popUp));
    	        popUpStage.initModality(Modality.WINDOW_MODAL); 
    	        popUpStage.initOwner(paginaCorrente); 
    	        popUpStage.show(); 
    	    } catch (NullPointerException | IOException e) {
    	        System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
    	    }   
	}

}

