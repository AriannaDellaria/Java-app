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
    private Label utente; 

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
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
        }
    }

    @FXML
    void paginaSuccessiva(MouseEvent event) {
        Button bottoneCliccato = (Button) event.getSource();
        
        String nomeFileFXML = "";

        switch (bottoneCliccato.getId()) {
            case "base":
            	nomeFileFXML = "/application/OutputBase.fxml";
                break;
            case "medio":
            	nomeFileFXML = "/application/OutputMedio.fxml";
                break;
            case "avanzato":
            	nomeFileFXML = "/application/OutputAvanzato.fxml";
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