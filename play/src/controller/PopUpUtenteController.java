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

public class PopUpUtenteController {

    @FXML
    private Button close, home, logout;

    @FXML
    private Label cognome, modPass, nome, p1, p2, p3, username;
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato(); 
  
    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow(); 
        stage.close(); 
    }
    
    @FXML
    void colorChangeGreen(MouseEvent event) {
    	home.setStyle("-fx-background-color: #A2D8A3;-fx-border-color: #2AAA4A"); 
    }

    @FXML
    void colorChangeRed(MouseEvent event) {
    	logout.setStyle("-fx-background-color: #FFC8AE;-fx-border-color: #f64c4c"); 
    }
    
    @FXML
    void colorChangeBasic(MouseEvent event) {
    	home.setStyle("-fx-background-color: white; -fx-border-color: #2AAA4A; -fx-border-width: 2px;");
    	logout.setStyle("-fx-background-color: white; -fx-border-color: #f64c4c; -fx-border-width: 2px;");
    }

    @FXML
    void esci(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/PopUpLogout.fxml"));
            
            Stage popUpUtente = (Stage) logout.getScene().getWindow();
            
            Stage popUpLogout = new Stage();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            popUpLogout.setScene(nuovaScena);
            popUpLogout.initModality(Modality.WINDOW_MODAL);
            popUpLogout.initOwner(popUpUtente);
            popUpLogout.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
            e.printStackTrace(); 
        }
	}

    @FXML
    void modificaPassword(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/PopUpModificaPassword.fxml"));

            Stage popUpUtente = (Stage) modPass.getScene().getWindow();
            
            Stage nuovoPopUp = new Stage();
            
            Scene nuovaScena = new Scene(scenaSuccessiva);
            nuovoPopUp.setScene(nuovaScena);
            nuovoPopUp.initModality(Modality.WINDOW_MODAL);
            nuovoPopUp.initOwner(popUpUtente); 
            nuovoPopUp.show();
            } catch (NullPointerException | IOException e) {
                System.out.println("Errore nel caricamento della schermata successiva!");
            }
    }

    @FXML
    void tornaAllaHome(MouseEvent event) {
    	 try {
    	        Stage popUpUtente = (Stage) home.getScene().getWindow();
    	        popUpUtente.close();
    	        
    	        Stage genitore = (Stage) popUpUtente.getOwner(); 
    	        Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));
    	        Scene nuovaScena = new Scene(scenaSuccessiva);
    	        genitore.setScene(nuovaScena);
    	        genitore.show();
    	 	} catch (NullPointerException | IOException e) {
		        System.out.println("Errore nel caricamento della schermata successiva!" + e.getMessage());
		    }      
    }
    
    //imposta i dati dell'utente corrente (nome, cognome, punteggi relativi agli esercizi in percentuale)
    public void initialize() {
        username.setText("Ciao " + utenteCorrente.getUsername()); 
        nome.setText(utenteCorrente.getNome()); 
        cognome.setText(utenteCorrente.getCognome()); 
        p1.setText((int)(utenteCorrente.getPg1() * 100) + "%"); 
        p2.setText((int)(utenteCorrente.getPg2() * 100) + "%"); 
        p3.setText((int)(utenteCorrente.getPg3() * 100) + "%"); 
    }
}


