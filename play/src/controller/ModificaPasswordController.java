package controller; 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import dati.Utente;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class ModificaPasswordController {

    @FXML
    private Button close, conferma, login;

    @FXML
    private PasswordField nuovaPassword, confermaPassword;
    
    @FXML
    private ToggleButton toggle; 

    @FXML
    private TextField textField;
    
    @FXML
    private Text errore;
     
    @FXML
    private Label utente;
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
    
    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow();  
        stage.close(); 
    }

    @FXML
    void colorChangeGreen(MouseEvent event) {
    	conferma.setStyle("-fx-background-color: #A2D8A3;-fx-border-color: #2AAA4A"); 
    }
    
    @FXML
    void colorChangeBasic(MouseEvent event) {
    	conferma.setStyle("-fx-background-color: white; -fx-border-color: #2AAA4A; -fx-border-width: 2px;");
    	login.setStyle("-fx-background-color: white; -fx-border-color: #77358f; -fx-border-width: 2px;");
    	
    }
    @FXML
    void colorChangePurple(MouseEvent event) {
    	login.setStyle("-fx-background-color: #c990f3;-fx-border-color: #77358f"); 
    }

    //carica e inizializza le immagini     
    private final Image occhioAperto = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/occhioAperto.png")));
    private final Image occhioChiuso = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/occhioChiuso.png")));

    @FXML
    private void initialize() {
    	 utente.setText(utenteCorrente.getUsername());
    	ImageView icon = new ImageView(occhioAperto); //ImageView -> consente di visualizzare l'immagine nell'interfaccia 
        icon.setFitWidth(20);//larghezza
        icon.setFitHeight(20);//altezza
        toggle.setGraphic(icon);
        
        //metodo che consente di sincronizzare passwordField e textField
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
        	 confermaPassword.setText(newValue);
        });
        
        confermaPassword.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!toggle.isSelected()) {
                    textField.setText(newValue);
                }
        });
        
        nuovaPassword.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                confermaPassword.requestFocus();
            }
        });  
    }
    
    @FXML
    private void impostaPassword() {
    	String x = nuovaPassword.getText(); 
    	String y = confermaPassword.getText(); 
    	
    	List<String> righe = new ArrayList<>();
        boolean trovato = false;
    	
        //se le password non corrispondano 
        //se la nuova password è uguale alla precedente e se ha meno di 8 caratteri mostra l'errore corrispondente 
    	if(!x.equals(y) || x.length() <= 7 || x.equals(utenteCorrente.getPassword())){
    		errore.setVisible(true); 
    	}
    	//trova l'utente loggato in quel momento e modifica la password nel file 
    	else { 
            File file = new File("utenti.csv");
            try (BufferedReader reader = new BufferedReader(new FileReader("utenti.csv"))) {
                String linea;

                while ((linea = reader.readLine()) != null) {
                    String[] dati = linea.split(",");
                    if (dati[2].equals(utenteCorrente.getUsername())) {
                    	dati[3] = y;  
                        trovato = true;
                    }
                    righe.add(String.join(",", dati));
                }
            } catch (IOException e) {
                System.out.println("Errore nella lettura del file: " + e.getMessage());
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String riga : righe) {
                    writer.write(riga);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Errore nella scrittura del file: " + e.getMessage());
            }
            
            //se la modifica è andata a buon fine, l'utente torna al login e viene mostrato il popUp
        	try {
                Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));

                 Stage scenaCorrente = (Stage)conferma.getScene().getWindow();
                 
                 Scene nuovaScena = new Scene(scenaSuccessiva);
                 scenaCorrente.setScene(nuovaScena);
                 scenaCorrente.show(); 
                 Parent popUp = FXMLLoader.load(getClass().getResource("/application/PopUpPasswordModificataConSuccesso.fxml")); 

 	             Stage popUpStage = new Stage();
 	             popUpStage.setScene(new Scene(popUp));
 	             popUpStage.initModality(Modality.WINDOW_MODAL); 
 	             popUpStage.initOwner(scenaCorrente); 
 	             popUpStage.show();
 	            sessioneGioco.setUtenteLoggato(null);
            } catch (NullPointerException | IOException e) {
                 System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
	        } 
        }
    }
    
    @FXML
    void tornaAlLogin(MouseEvent event) {
        try {
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
            Scene nuovaScena = new Scene(scenaSuccessiva);
            Stage scenaCorrente = (Stage) login.getScene().getWindow();
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
            sessioneGioco.setUtenteLoggato(null);
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
        }
    }

    @FXML
    void visibilita(MouseEvent event) {
        if(toggle.isSelected()) { 
        	confermaPassword.setVisible(false); 
        	 ImageView icon1 = new ImageView(occhioChiuso);
        	 icon1.setFitWidth(20);
             icon1.setFitHeight(20);
        	 toggle.setGraphic(icon1); 
        	 textField.setVisible(true);
        }
        else { 
        	confermaPassword.setVisible(true); 
       	 	ImageView icon2 = new ImageView(occhioAperto);
       	 	icon2.setFitWidth(20);
            icon2.setFitHeight(20);
       	 	toggle.setGraphic(icon2); 
       	 	textField.setVisible(false);
        }
    }   
}
    
    

