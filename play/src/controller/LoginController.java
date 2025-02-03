package controller;

import java.io.IOException;
import java.util.Scanner;

import dati.Utente;

import java.io.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class LoginController {

    @FXML
    private Button close;

    @FXML
    private Button indietro;
    
    @FXML
    private Label signIn;
    
    @FXML
    private Button login;
    
    @FXML
    private PasswordField passwordUtente;
    
    @FXML
    private TextField usernameUtente;
    
    @FXML
    private Label erroreCompilazioneLogin;
    
    @FXML
    private Label erroreDatiLogin;
    

    @FXML
    void colorChangeYellow(MouseEvent event) {
    	indietro.setStyle("-fx-background-color: yellow;");	
    }
    
    @FXML
    void colorChangeRed(MouseEvent event) {
    	close.setStyle("-fx-background-color: red;");
    }

    @FXML
    void colorChangeBasic(MouseEvent event) {
    	indietro.setStyle("");
    	close.setStyle("");
    }
    
    @FXML
    void closeButton(MouseEvent event) {
        Stage stage = (Stage) close.getScene().getWindow(); 
        stage.close(); 
    }

    @FXML
    void paginaPrecedente(MouseEvent event) {
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Gioca.fxml"));

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
    void accesso(MouseEvent event) {
    
        String username = usernameUtente.getText();
        String password = passwordUtente.getText();
        
    	if(username.isEmpty() || password.isEmpty()) {
    		erroreCompilazioneLogin.setVisible(true); 
        	return;	
    	}
        		
    	if(!autenticazione(username, password)) {
    		erroreDatiLogin.setVisible(true); 
    		usernameUtente.clear();
            passwordUtente.clear();
    	    return;
    	}else{
    		// Login riuscito, crea l'oggetto Utente e salva nella sessione
    	    Utente utente = new Utente(username, password);
    	    SessioneGioco sessioneGioco = SessioneGioco.getInstance(); // Ottieni la sessione Singleton
    	    sessioneGioco.setUtenteLoggato(utente);
    	    utente.recuperaSalvataggio();

            //passa alla schermata succcessiva
            try {
                Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Home.fxml")); //da modificare nome filefxml

                Stage scenaCorrente = (Stage) login.getScene().getWindow();
                
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
    }
    
    private boolean autenticazione(String username, String password) {
    	try {
    		Scanner scf = new Scanner(new File("utenti.csv"));

    		while(scf.hasNextLine()) {
    			String riga = scf.nextLine();
    			String[] tokens = riga.split(",");
    			String usernameFile = tokens[2];
    			String passwordFile = tokens[3];
    			
    			if (usernameFile.equals(username) && passwordFile.equals(password)) {
                    return true; // Login riuscito
                }
    		}
    		scf.close();
    		
    	}catch(IOException e) {
        	System.out.println("Errore nella lettura del file");
        }
    	return false;
    }
    
    @FXML
    void registrazione(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Registrazione.fxml"));

            Stage scenaCorrente = (Stage) signIn.getScene().getWindow();

            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.setFullScreen(true);
            scenaCorrente.setFullScreenExitHint("");
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva!");
        }
    }
}

//ciao a tutti