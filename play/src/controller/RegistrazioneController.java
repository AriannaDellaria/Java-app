package controller;

import java.io.*;
import java.util.Objects;
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

public class RegistrazioneController {

	@FXML
    private TextField nomeUtente,cognomeUtente,usernameUtente,textField;

    @FXML
    private Label erroreCompilazioneRegistrazione, errorePassword, usernameEsistente;

    @FXML
    private PasswordField passwordField, passwordUtente;

    @FXML
    private Button salvaButton, close, indietro;

    @FXML
    private ToggleButton toggle;

    private File file = new File("utenti.csv"); 

    // Caricamento immagini
    private final Image occhioAperto = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/occhioAperto.png")));
    private final Image occhioChiuso = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/occhioChiuso.png")));
    
    @FXML
    void closeButton(MouseEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();  
        stage.close(); 
    }

    @FXML
    void colorChangeBasic(MouseEvent event) {
    	salvaButton.setStyle("-fx-background-color: white; -fx-border-color: #2AAA4A; -fx-border-width: 2px;");
    }
    
    @FXML
    void colorChangeGreen(MouseEvent event) {
    	salvaButton.setStyle("-fx-background-color: #A2D8A3;-fx-border-color: #2AAA4A"); 
    }
    
    @FXML
    void paginaPrecedente(MouseEvent event) {
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));

            Stage scenaCorrente = (Stage) indietro.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();
        } catch (IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
        }
    }
    
    @FXML
    private void initialize() {
        //crea e imposta l'icona iniziale (occhio chiuso)
        ImageView icon = new ImageView(occhioAperto);
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        toggle.setGraphic(icon); 
      
        //metodo che consente di sincronizzare passwordField e textField
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
        	 passwordField.setText(newValue);
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!toggle.isSelected()) {
                    textField.setText(newValue);
                }
        });
        
        nomeUtente.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                cognomeUtente.requestFocus();
            }
        });  
        
        cognomeUtente.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                usernameUtente.requestFocus();
            }
        });  
        
        usernameUtente.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                passwordUtente.requestFocus();
            }
        });  
    
        passwordUtente.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                passwordField.requestFocus(); 
            }
        });  
    }
    
    @FXML
    void salvaEContinua(MouseEvent event) { 
    	//recupera i dati scritti nei vari campi
    	String nome = nomeUtente.getText().trim();
        String cognome = cognomeUtente.getText().trim();
        String username = usernameUtente.getText().trim();
        String password = passwordUtente.getText().trim();
        String confermaPassword = passwordField.getText().trim(); 
        
        //se uno dei campi è vuoto viene segnalato l'errore all'utente 
        boolean vuoto = false; 
        if (nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || password.isEmpty() || confermaPassword.isEmpty()) {
        	erroreCompilazioneRegistrazione.setVisible(true); 
        	vuoto = true; 
        }else { 
        	erroreCompilazioneRegistrazione.setVisible(false); 
        }
       
        //se la password ha un numero di caratteri inferiore a 8, o le password inserite non corrispondono viene segnalato l'errore all'utente
        boolean uguali = false; 
        if(password.length() <= 7 || !password.equals(confermaPassword)) { 
        	errorePassword.setVisible(true); 
        	uguali = true; 
        }else { 
        	errorePassword.setVisible(false); 
        }
        
        //se l'username è già esistente, viene segnalato l'errore all'utente
        boolean trovato = false; 
        File file = new File("utenti.csv");	
        try (BufferedReader reader = new BufferedReader(new FileReader("utenti.csv"))) {
   		 String linea;
            while ((linea = reader.readLine()) != null) {
                String[] dati = linea.split(",");
                
                if (dati[2].equals(username)) {
                	trovato = true;
                	usernameEsistente.setVisible(true);
                	break;
                }
            }
        
            if(trovato == false) { 
            	usernameEsistente.setVisible(false);
            }
            
        } catch(IOException e) {
            System.out.println("Errore nella lettura del file! " + e.getMessage()); 
        }
          
        //se non vengono segnalati errori, la registrazione viene completata e i dati vengono salvati su un file 
        //i punteggi relativi agli esercizi vengono inizializzati a 0
        if(vuoto == false && uguali == false && trovato == false) {
    	   try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
               writer.println(nome + "," + cognome + "," + username + "," + password + "," + 0.0 + "," + 0.0 + "," + 0.0);
    	   } catch(IOException e) {
           		System.out.println("Errore nel salvataggio! " + e.getMessage());
           }
           
    	   //se la registra è stata completata con successo, l'utente torna alla pagina del login e viene mostrato il popUp
    	   try {    
				Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));

	            Stage scenaCorrente = (Stage) salvaButton.getScene().getWindow();

	            Scene vecchiaScena = new Scene(scenaPrecedente);
	            scenaCorrente.setScene(vecchiaScena);
	            scenaCorrente.show();
	            
	            Parent popUp = FXMLLoader.load(getClass().getResource("/application/PopUp.fxml")); 

	            Stage popUpStage = new Stage();
	            popUpStage.setScene(new Scene(popUp));
	            popUpStage.initModality(Modality.WINDOW_MODAL); 
	            popUpStage.initOwner(scenaCorrente); 
	            popUpStage.show();      
	        } catch (NullPointerException | IOException e) {
	            System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
	        }
        }  	
    }
    
    @FXML
    void visibilita(MouseEvent event) {
        if(toggle.isSelected()) { 
        	 passwordField.setVisible(false); 
        	 ImageView icon1 = new ImageView(occhioChiuso);
        	 icon1.setFitWidth(20);
             icon1.setFitHeight(20);
        	 toggle.setGraphic(icon1); 
        	 textField.setVisible(true);
        }else { 
        	passwordField.setVisible(true); 
       	 	ImageView icon2 = new ImageView(occhioAperto);
       	 	icon2.setFitWidth(20);
            icon2.setFitHeight(20);
       	 	toggle.setGraphic(icon2); 
       	 	textField.setVisible(false);
        }
    }
}