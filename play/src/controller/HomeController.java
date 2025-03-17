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
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class HomeController {

    @FXML
    private Button close, indietro;
    
    @FXML
    private ProgressBar pb1, pb2, pb3;

    @FXML
    private Label primoEsercizio, secondoEsercizio, terzoEsercizio, utente, recensione, tutteLeRecensioni;
  
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();  //getIstance() -> restituisce una e una sola istanza della classe
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato(); //recupera i dati dell'utente che ha effettuato il login
    
    
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
    void grande(MouseEvent event) {
    	primoEsercizio.setStyle("-fx-font-size: 32px; -fx-font-family: 'Georgia'; -fx-font-weight: bold;");
    }
    @FXML
    void grande1(MouseEvent event) {
    	secondoEsercizio.setStyle("-fx-font-size: 32px; -fx-font-family: 'Georgia'; -fx-font-weight: bold;");
    }
    @FXML
    void grande2(MouseEvent event) {
    	terzoEsercizio.setStyle("-fx-font-size: 32px; -fx-font-family: 'Georgia'; -fx-font-weight: bold;");
    }
    @FXML
    void piccolo(MouseEvent event) {
    	primoEsercizio.setStyle("-fx-font-size: 30px; -fx-font-family: 'Georgia'; -fx-font-weight: bold;"); 
    }
    @FXML
    void piccolo1(MouseEvent event) {
    	secondoEsercizio.setStyle("-fx-font-size: 30px; -fx-font-family: 'Georgia'; -fx-font-weight: bold;");
    }
    @FXML
    void piccolo2(MouseEvent event) {
    	terzoEsercizio.setStyle("-fx-font-size: 30px; -fx-font-family: 'Georgia'; -fx-font-weight: bold;");
    }
    
    

    @FXML
    void paginaPrecedente(MouseEvent event) {
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));

            Stage scenaCorrente = (Stage) indietro.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();
            sessioneGioco.setUtenteLoggato(null);
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente! " +e.getMessage());
        }
    }
 
    
    @FXML
    void paginaPrimoEsercizio(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/DefinizioniLivelli.fxml"));

            Stage scenaCorrente = (Stage)secondoEsercizio.getScene().getWindow();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
        }
    }
     

    @FXML
    void paginaSecondoEsercizio(MouseEvent event) {
    	  try {
              Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/OutputLivelli.fxml"));

              Stage scenaCorrente = (Stage)secondoEsercizio.getScene().getWindow();
              
              Scene nuovaScena = new Scene(scenaSuccessiva);
              scenaCorrente.setScene(nuovaScena);
              scenaCorrente.show();
          } catch (NullPointerException | IOException e) {
              System.out.println("Errore nel caricamento della schermata successiva!" + e.getMessage());
          }
    }

    
    @FXML
    void paginaTerzoEsercizio(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/RiordinaLivelli.fxml"));

            Stage scenaCorrente = (Stage)secondoEsercizio.getScene().getWindow();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva! " +e.getMessage());
        }
    }

    @FXML
    void scriviRecensione(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/ScriviRecensione.fxml"));

            Stage scenaCorrente = (Stage)recensione.getScene().getWindow();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva! " +e.getMessage());
        }
    }
    
    @FXML
    void apriRecensioni(MouseEvent event)  { 
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Recensioni.fxml"));

            Stage scenaCorrente = (Stage)tutteLeRecensioni.getScene().getWindow();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva! " +e.getMessage());
        }
    }
    
    @FXML
    public void initialize() {
        aggiornaProgressBar();
        utente.setText(utenteCorrente.getUsername()); 
        
    }

    private void aggiornaProgressBar() {
        double progresso1 = utenteCorrente.getPg1(); //recupera il punteggio pg1 -> (pg indica il punteggio globale)
        double progresso2 = utenteCorrente.getPg2(); 
        double progresso3 = utenteCorrente.getPg3(); 

        pb1.setProgress(progresso1); //aggiorna la progress bar pb1
        pb2.setProgress(progresso2); 
        pb3.setProgress(progresso3);  
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
    	        e.printStackTrace(); 
    	    }   
	}
}



    
