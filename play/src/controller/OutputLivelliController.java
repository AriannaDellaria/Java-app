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

public class OutputLivelliController {

    @FXML
    private Button close, indietro, base, medio, avanzato;
    
    @FXML
    private Label utente,erroreEsercizi; 

    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
    
    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow(); 
        stage.close(); 
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
            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        utente.setText(utenteCorrente.getUsername());    
    }
    
    //l'utente può accedere agli esercizi di livello successivo solo se ha completato quelli del livello precedente 
    //se l'esercizio è sbloccato il colore del bottone cambia 
    @FXML
    void entrata(MouseEvent event) {
        Button bottone = (Button) event.getSource();

        if (bottone.getId().equals("base")) { // Il base si colora sempre
            bottone.setStyle("-fx-background-color: #ADD9F4; -fx-border-color: #2379be");
        }
        else if (bottone.getId().equals("medio") && utenteCorrente.getPg2() >= 0.33) {
            bottone.setStyle("-fx-background-color: #ADD9F4; -fx-border-color: #2379be");
        }
        else if (bottone.getId().equals("avanzato") && utenteCorrente.getPg2() >= 0.66) {
            bottone.setStyle("-fx-background-color: #ADD9F4; -fx-border-color: #2379be");
        }
    }

    @FXML
    void uscita(MouseEvent event) {
        Button bottone = (Button) event.getSource();

        if (bottone.getId().equals("base")) { 
            bottone.setStyle("-fx-background-color: white; -fx-border-color: #2379be; -fx-border-width: 2");
        }
        else if (bottone.getId().equals("medio") && utenteCorrente.getPg2() >= 0.33) {
            bottone.setStyle("-fx-background-color: white; -fx-border-color: #2379be; -fx-border-width: 2"); 
        }
        else if (bottone.getId().equals("avanzato") && utenteCorrente.getPg2() >= 0.66) {
            bottone.setStyle("-fx-background-color: white; -fx-border-color:  #2379be; -fx-border-width: 2");
        }
    }


    //metodo che consente di bloccare l'accesso ai livelli successivi, tramite il punteggio
    //se si prova a accedere a un livello ancora bloccato viene segnalato un errore 
    @FXML
    void paginaSuccessiva(MouseEvent event) {
        Button bottoneCliccato = (Button) event.getSource();
        
        String nomeFileFXML = "";
        switch (bottoneCliccato.getId()) { //getId() -> recupera l'fx:id del bottone cliccato 
            case "base": 
            	nomeFileFXML = "/application/OutputBase.fxml";
                break;
            case "medio":
            	if(utenteCorrente.getPg2() >= 0.33) { 
            		nomeFileFXML = "/application/OutputMedio.fxml";
            	}
            	else { 
            		erroreEsercizi.setVisible(true); 
            		return;
            	}
                break;
            case "avanzato":
            	if(utenteCorrente.getPg2() >= 0.66) { 
            		nomeFileFXML = "/application/OutputAvanzato.fxml";
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
        }
    }  
    
    @FXML
    void popUpUtente() {
	    try {
	        Stage paginaPrincipale = (Stage) utente.getScene().getWindow();

	        Parent popUp = FXMLLoader.load(getClass().getResource("/application/PopUpUtente.fxml"));
	        
	        Stage popUpStage = new Stage();
	        popUpStage.setScene(new Scene(popUp));
	        popUpStage.initModality(Modality.WINDOW_MODAL); 
	        popUpStage.initOwner(paginaPrincipale); 
	        popUpStage.show();     	    
	    } catch (NullPointerException | IOException e) {
	        System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
	    }   
	}
}