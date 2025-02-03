package controller;

import java.io.IOException;

import dati.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class HomeController {

    @FXML
    private Button close;

    @FXML
    private Button indietro;
    
    @FXML
    private ProgressBar pb1;

    @FXML
    private ProgressBar pb2;

    @FXML
    private ProgressBar pb3;
    
    @FXML
    private Label primoEsercizio;

    @FXML
    private Label secondoEsercizio;

    @FXML
    private Label terzoEsercizio;
    
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
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));

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
    void paginaPrimoEsercizio(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/DefinizioniLivelli.fxml"));

            Stage scenaCorrente = (Stage)secondoEsercizio.getScene().getWindow();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.setFullScreen(true);
            scenaCorrente.setFullScreenExitHint("");
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva!");
        }
    }
     

    @FXML
    void paginaSecondoEsercizio(MouseEvent event) {
    	  try {
              Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/PrevediOutputLivelli.fxml"));

              Stage scenaCorrente = (Stage)secondoEsercizio.getScene().getWindow();
              
              Scene nuovaScena = new Scene(scenaSuccessiva);
              scenaCorrente.setScene(nuovaScena);
              scenaCorrente.setFullScreen(true);
              scenaCorrente.setFullScreenExitHint("");
              scenaCorrente.show();
          } catch (NullPointerException | IOException e) {
              System.out.println("Errore nel caricamento della schermata successiva!");
          }
    }

    
    @FXML
    void paginaTerzoEsercizio(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/OrdinaCodiceLivelli.fxml"));

            Stage scenaCorrente = (Stage)secondoEsercizio.getScene().getWindow();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.setFullScreen(true);
            scenaCorrente.setFullScreenExitHint("");
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva!");
        }
    }

    @FXML
    public void initialize() {
        aggiornaProgressBar();
    }

    private void aggiornaProgressBar() {
    	 // Recupera i punteggi
        double progresso1 = utenteCorrente.getPg1(); // Recupera pg1
        double progresso2 = utenteCorrente.getPg2(); // Recupera pg2
        double progresso3 = utenteCorrente.getPg3(); // Recupera pg3 (aggiunto per esempio)

        // Imposta i valori nelle ProgressBar corrispondenti
        pb1.setProgress(progresso1); // Aggiorna la ProgressBar per pg1
        pb2.setProgress(progresso2); // Aggiorna la ProgressBar per pg2
        pb3.setProgress(progresso3); // Aggiorna la ProgressBar per pg3 (se hai una ProgressBar per pg3) 	 
    }
    
    
}
