package controller;

import java.io.*;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegistrazioneController {

	   @FXML
	    private Button close;

	    @FXML
	    private TextField cognomeUtente;

	    @FXML
	    private Button indietro;

	    @FXML
	    private TextField nomeUtente;

	    @FXML
	    private TextField passwordUtente;

	    @FXML
	    private Button salvaButton;

	    @FXML
	    private TextField usernameUtente;
    
	    @FXML
	    private Label erroreCompilazioneRegistrazione;
	    
	    @FXML
	    private Label errorePassword; 
	    
	    private File file = new File("utenti.csv"); 

    @FXML
    void closeButton(MouseEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();  
        stage.close(); 
    }

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
    	salvaButton.setStyle("");
    }
     
     @FXML
     void colorChangeGreen(MouseEvent event) {
    	 salvaButton.setStyle("-fx-background-color: #0fcc58;");
     }
     
     @FXML
     void colorChangeBasic2(MouseEvent event) {
     	salvaButton.setStyle("");
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
        } catch (IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente!");
        }
    }
    
    @FXML
    void salvaEContinua(MouseEvent event) throws IOException { 
    
    	String nome = nomeUtente.getText();
        String cognome = cognomeUtente.getText();
        String username = usernameUtente.getText();
        String password = passwordUtente.getText();
        
        if (nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || password.isEmpty()) {
        	erroreCompilazioneRegistrazione.setVisible(true); 
        	return; // Ferma l'esecuzione se ci sono errori
        }
       
        else if(password.length() <= 8) { 
        	errorePassword.setVisible(true); 
        	return;
        }
        
        else {
        	if(saveToFile(nome, cognome, username, password)) {
				nomeUtente.clear();
				cognomeUtente.clear();
				usernameUtente.clear();
				passwordUtente.clear();
				erroreCompilazioneRegistrazione.setVisible(false); 
				errorePassword.setVisible(false);
				
				try {
		           
					Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));

		            Stage scenaCorrente = (Stage) salvaButton.getScene().getWindow();

		            Scene vecchiaScena = new Scene(scenaPrecedente);
		            scenaCorrente.setScene(vecchiaScena);
		            scenaCorrente.setFullScreen(true);
		            scenaCorrente.setFullScreenExitHint("");
		            scenaCorrente.show();
		            
		            Parent popUp = FXMLLoader.load(getClass().getResource("/application/PopUp.fxml")); 

		            Stage popUpStage = new Stage();
		            popUpStage.setScene(new Scene(popUp));
		            
		            popUpStage.initModality(Modality.WINDOW_MODAL); //non permette all'utente di interagire con ila finestra principale
		            popUpStage.initOwner(scenaCorrente); //mantiene il popup in primo piano e lo chiude se viene chiusa la scena genitore
		            popUpStage.show();
		            
		        } catch (NullPointerException | IOException e) {
		            System.out.println("Errore nel caricamento della schermata successiva!");
		        }
			}else{
				erroreCompilazioneRegistrazione.setText("Username già esistente.");
			    erroreCompilazioneRegistrazione.setVisible(true);
			    return;
			}
        }
        
    }
        
    // Metodo per salvare i dati nel file
     boolean saveToFile(String nome, String cognome, String username, String password){
    	 
    	if (usernameGiaPreso(username)) {
             return false; // Username già esistente
        } 
    	
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) { //scrive sul file
            writer.write(nome + "," + cognome + "," + username + "," + password + "," + 0.0 + "," + 0.0 + "," + 0.0);
            writer.println(); // Aggiungi una nuova riga
        }
        catch(IOException e) {
        	System.out.println("Errore nel salvataggio");
        }
        return true;
    }

    private boolean usernameGiaPreso(String username){
    	
    	if (!file.exists()) {
            return false; // Se il file non esiste, l'username è disponibile
        }
        try {
        	Scanner scf = new Scanner(new File("utenti.csv"));
            while (scf.hasNextLine()) {
                String riga = scf.nextLine();
                String[] tokens = riga.split(",");
                if (tokens[2].equals(username)) { 
                    return true; // Username già esistente
                }  
            }
           scf.close(); 
        }catch (IOException e) {
            System.out.println("errore lettura file, user");
        }
        return false; // Username disponibile
    }   
}



