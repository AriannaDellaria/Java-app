package controller;

import java.io.IOException;
import java.util.Objects;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class LoginController {

    @FXML
    private Button close, indietro, login;
    
    @FXML
    private Label signIn, erroreDatiLogin, passwordDimenticata;
     
    @FXML
    private TextField usernameUtente, textField;
    
    @FXML
    private PasswordField passwordUtente;
    
    @FXML
    private ToggleButton toggle;
    
    @FXML
    private ImageView immagine; 
 
    //carica e inizializza le immagini 
    private final Image occhioAperto = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/occhioAperto.png")));
    private final Image occhioChiuso = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/occhioChiuso.png")));
    
    @FXML
    void closeButton(MouseEvent event) {
        Stage stage = (Stage) close.getScene().getWindow(); 
        stage.close(); 
    }
    
    @FXML
    void colorChangeGreen(MouseEvent event) {
    	login.setStyle("-fx-background-color: #A2D8A3;-fx-border-color: #2AAA4A"); 
    }
    
    @FXML
    void colorChangeBasic(MouseEvent event) {
    	login.setStyle("-fx-background-color: white; -fx-border-color: #2AAA4A; -fx-border-width: 2px;");
    }
    
    //torna alla pagina precedente 
    @FXML
    void paginaPrecedente(MouseEvent event) {
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Gioca.fxml"));

            Stage scenaCorrente = (Stage) indietro.getScene().getWindow(); //recupera la pagina in cui si trova il botton

            Scene vecchiaScena = new Scene(scenaPrecedente); //imposta la scena con la pagina precedente
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente! " +e.getMessage());
        }
    }
    
    //initialize -> metodo che viene chiamato automaticamente da javaFX per inizializzare gli elementi dell'interfaccia
    @FXML
    private void initialize() {
        ImageView icon = new ImageView(occhioAperto); //ImageView -> consente di visualizzare l'immagine nell'interfaccia 
        icon.setFitWidth(20); //larghezza
        icon.setFitHeight(20); //altezza 
        toggle.setGraphic(icon);
       
        //metodo che consente di sincronizzare passwordField e textField
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
        	 passwordUtente.setText(newValue);
        });
        
        passwordUtente.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!toggle.isSelected()) {
                textField.setText(newValue);
            }
        });
        
        //se l'utente si trova in passwordField, e preme enter da tastiera, viene richiamato il metodo accesso 
        passwordUtente.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                accesso(null);
            }
        });
        
        //se l'utente si trova in textField, e preme enter da tastiera, viene richiamato il metodo accesso 
        textField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                accesso(null);
            }
        });
        
        //quando premi "Enter" in usernameUtente, passa al campo passwordUtente
        usernameUtente.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                passwordUtente.requestFocus(); //sposta il focus sulla password
            }
        });  
    }
    
    @FXML
    void accesso(MouseEvent event) {
    	//vengono recuperati username e password scritti nei textField
        String username = usernameUtente.getText().trim(); 
        String password = passwordUtente.getText().trim();
        
        //se uno dei due campi è vuoto, viene segnalato l'errore all'utente
    	if(username.isEmpty() || password.isEmpty()) {
    		erroreDatiLogin.setText("Attenzione! Tutti i campi devono essere compilati");
    		erroreDatiLogin.setVisible(true); 
        	return;	
    	}
        
    	//se l'autenticazione non è andata a buon fine, viene segnalato all'utente
    	if(!autenticazione(username, password)) {
    		erroreDatiLogin.setText("Attenzione! Username o password errati"); 
    		erroreDatiLogin.setVisible(true); 
    		usernameUtente.clear(); //pulisce il campo dell'username 
            passwordUtente.clear(); //pulisce il campo della password
            textField.clear(); 
    	    return;
    	}
    	
    	else{
    		//login riuscito, crea l'oggetto Utente e salva nella sessione
    	    Utente utente = new Utente(username, password);
    	    //getIstance() -> metodo che restituisce una e una sola istanza della classe 
    	    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    	    sessioneGioco.setUtenteLoggato(utente);
    	    utente.recuperaSalvataggio();

            //passa alla schermata succcessiva
            try {
                Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Home.fxml")); 

                Stage scenaCorrente = (Stage) login.getScene().getWindow();
                
                Scene nuovaScena = new Scene(scenaSuccessiva);
                scenaCorrente.setScene(nuovaScena);
                scenaCorrente.show();
            } catch (NullPointerException | IOException e) {
                System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
            }
    	}
    }
    
    //metodo che consente di verificare se password e username corrispondono a quelle salvate nel file al momento della registrazione 
    private boolean autenticazione(String username, String password) {
    	try {
    		Scanner scf = new Scanner(new File("utenti.csv"));
    		while(scf.hasNextLine()) {
    			String riga = scf.nextLine();
    			String[] tokens = riga.split(",");
    			String usernameFile = tokens[2];
    			String passwordFile = tokens[3];
    			
    			if (usernameFile.equals(username) && passwordFile.equals(password)) {
                    return true;
                }
    		}
    		scf.close();	
    	} catch(IOException e) {
        	System.out.println("Errore nella lettura del file " + e.getMessage());
        }
    	return false;
    }
    
    //passa alla schermata di registrazione
    @FXML
    void registrazione(MouseEvent event) {
    	try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Registrazione.fxml"));

            Stage scenaCorrente = (Stage) signIn.getScene().getWindow();

            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
        }
    }
   
    //il bottone consente di visualizzare o meno la password inserita dall'utente
    @FXML
    void visibilita(MouseEvent event) {
        if(toggle.isSelected()) { 
        	 passwordUtente.setVisible(false); 
        	 ImageView icon1 = new ImageView(occhioChiuso);
        	 icon1.setFitWidth(20);
             icon1.setFitHeight(20);
        	 toggle.setGraphic(icon1);  
        	 textField.setVisible(true);
        } else { 
        	passwordUtente.setVisible(true); 
       	 	ImageView icon2 = new ImageView(occhioAperto);
       	 	icon2.setFitWidth(20);
            icon2.setFitHeight(20);
       	 	toggle.setGraphic(icon2); 
       	 	textField.setVisible(false);
        }
    }
    
    //passa alla popUp che permette di recuperare la password 
    @FXML
    void recuperaPassword(MouseEvent event) {
    	try {
	        Stage stagePrincipale = (Stage) passwordDimenticata.getScene().getWindow();
	        
	        Parent popUp =  FXMLLoader.load(getClass().getResource("/application/PopUpRecuperaPassword.fxml")); //carica il popUp
	       
	        Stage popUpStage = new Stage();
	        popUpStage.setScene(new Scene(popUp));
	        popUpStage.initModality(Modality.WINDOW_MODAL); //non permette l'interazione con la finestra sottostante
	        popUpStage.initOwner(stagePrincipale); //la finestra sottostante è il genitore del popUp
	        popUpStage.show();
    	} catch (NullPointerException | IOException e) {
	        System.out.println("Errore nel caricamento della schermata successiva!" + e.getMessage());
	    }  
    }
}
