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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class ModificaPasswordController {

    @FXML
    private Button close;

    @FXML
    private Button conferma;

    @FXML
    private PasswordField nuovaPassword;

    @FXML
    private PasswordField confermaPassword;
    
    @FXML
    private ToggleButton toggle; 

    @FXML
    private TextField textField;
    
    @FXML
    private Text errore;
    
    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow();  
        stage.close(); 
    }

    @FXML
    void colorChangeBasic(MouseEvent event) {
    	close.setStyle("");
    }

    @FXML
    void colorChangeRed(MouseEvent event) {
    	close.setStyle("-fx-background-color: red;");
    }

    // Ottieni l'utente loggato dalla sessione Singleton
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
    
        
      // Caricamento immagini (metti i file in /resources/images/)
        private final Image occhioAperto = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/occhioAperto.png")));
        private final Image occhioChiuso = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/occhioChiuso.png")));

        @FXML
        private void initialize() {
            // Crea e imposta l'icona iniziale (occhio chiuso)
            ImageView icon = new ImageView(occhioAperto);
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            toggle.setGraphic(icon); 
        }
        
        @FXML
        void visibilita(MouseEvent event) {
            if(toggle.isSelected()) { 
            	confermaPassword.setVisible(false); 
            	 ImageView icon1 = new ImageView(occhioChiuso);
            	 icon1.setFitWidth(20);
                 icon1.setFitHeight(20);
            	 toggle.setGraphic(icon1); 
            	 textField.setText(confermaPassword.getText()); 
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


        @FXML
        private void impostaPassword() {
        	String x = nuovaPassword.getText(); 
        	String y = confermaPassword.getText(); 
        	
        	List<String> righe = new ArrayList<>();
            boolean trovato = false;
        	
        	if(!x.equals(y) || x.length() <= 7 || x.equals(utenteCorrente.getPassword())){
        		errore.setVisible(true); 
        	}
        	
        	else { 
        		// Leggi il file e aggiorna l'utente
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

                // Riscrivi il file con i dati aggiornati
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String riga : righe) {
                        writer.write(riga);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.out.println("Errore nella scrittura del file: " + e.getMessage());
                }
                
	        	 try {
	                 Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
	
	                 Stage scenaCorrente = (Stage)conferma.getScene().getWindow();
	                 
	                 Scene nuovaScena = new Scene(scenaSuccessiva);
	                 scenaCorrente.setScene(nuovaScena);
	                 scenaCorrente.show();
	                 
	             } catch (NullPointerException | IOException e) {
	                 System.out.println("Errore nel caricamento della schermata successiva!");
	             } 
        	}
        }
}
    
    

