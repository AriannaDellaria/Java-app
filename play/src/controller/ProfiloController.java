package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sessione.SessioneGioco;
import dati.Utente;

public class ProfiloController {

    @FXML
    private Label labelNomeUtente;
    
    private SessioneGioco sessioneGioco;
    
    public void setSessioneGioco(SessioneGioco sessioneGioco) {
        this.sessioneGioco = sessioneGioco;
    }
    
    public void initialize() {
    	// Recupera l'utente loggato dalla sessione
    	Utente utente = sessioneGioco.getUtenteLoggato(); // Usa sessioneGioco per recuperare i dati
        if (utente != null) {
        	// Aggiorna la label con il nome dell'utente loggato
        	labelNomeUtente.setText(utente.getUsername());
        }    
    }
}
