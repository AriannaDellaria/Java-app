
package controller; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import dati.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class PopUpRecuperaPasswordController {

    @FXML
    private Button annulla, continua;

    @FXML
    private Label registrati;

    @FXML
    private TextField username;

    @FXML
    private Text usernameNonEsistente;

    //tramite l'username il metodo recupera gli altri dati utente per consentire la successiva modifica della password 
    //il tutto attraverso la lettura del file 'utenti.csv'
    @FXML
    void procediAllaModifica(MouseEvent event) {
    	String x = username.getText(); 
        File file = new File("utenti.csv");	
        boolean trovato = false; 
        
    	try(BufferedReader reader = new BufferedReader(new FileReader("utenti.csv"))) {
    		 String linea;
             while ((linea = reader.readLine()) != null) {
                 String[] dati = linea.split(",");
                 
                 if (dati[2].equals(x)) {
                	trovato = true;
                	String password = dati[3]; 
                	 
                	Utente utente = new Utente(x, password);
             	    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
             	    sessioneGioco.setUtenteLoggato(utente);
             	    utente.recuperaSalvataggio();
	             	    
	                    try {
	                         Stage popUp = (Stage) continua.getScene().getWindow();
	                         popUp.close();
	                         
	                         Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/ModificaPassword.fxml"));
	                         
	                         Stage scenaCorrente = (Stage) popUp.getOwner(); //prende il "genitore" della scena corrente (Login)                     
	                         Scene nuovaScena = new Scene(scenaSuccessiva);
	                         scenaCorrente.setScene(nuovaScena);
	                         scenaCorrente.show();
	              	    } catch (NullPointerException | IOException e) {
	              	        System.out.println("Errore nel caricamento della schermata successiva!" + e.getMessage());
	              	    }     
                     break; 
                 }
             }
    	 }catch (NullPointerException | IOException e) {
       	    System.out.println("Errore nel caricamento della schermata successiva!" + e.getMessage());
         }
    	
    	if(!trovato) { 
    		usernameNonEsistente.setVisible(true); 
    		registrati.setVisible(true); 
    	 }	
     }
    
    @FXML
    void registrazione(MouseEvent event) {
    	try {
            Stage popUp = (Stage) registrati.getScene().getWindow();
            popUp.close();
            
            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Registrazione.fxml"));
            
            Stage scenaCorrente = (Stage) popUp.getOwner(); //prende il "genitore" della scena corrente (Login)                     
            Scene nuovaScena = new Scene(scenaSuccessiva);
            scenaCorrente.setScene(nuovaScena);
            scenaCorrente.show();
 	    } catch (NullPointerException | IOException e) {
 	        System.out.println("Errore nel caricamento della schermata successiva!" + e.getMessage());
 	    }        
    }

    
    @FXML
    void tornaIndietro(MouseEvent event) {
    	try {
            Stage scenaCorrente = (Stage) annulla.getScene().getWindow();
            scenaCorrente.close();  
        } catch (NullPointerException e) {
            System.out.println("Errore nel caricamento della schermata precedente!");
        }
    }

}
