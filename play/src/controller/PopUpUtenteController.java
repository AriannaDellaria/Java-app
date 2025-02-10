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
	    private Button close;

	    @FXML
	    private Label cognome;

	    @FXML
	    private Button home;

	    @FXML
	    private Button logout;

	    @FXML
	    private Label modPass;

	    @FXML
	    private Label nome;

	    @FXML
	    private Label p1;

	    @FXML
	    private Label p2;

	    @FXML
	    private Label p3;

	    @FXML
	    private Label username;
	    
	    private Stage finestraPrincipale; 

	    
	    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
	    Utente utenteCorrente = sessioneGioco.getUtenteLoggato(); 

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


	    @FXML
	    void esci(MouseEvent event) {
	    	try {
	    		
	    		 // Carica la scena per il popup di logout
	            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/PopUpLogout.fxml"));

	            // Ottieni il riferimento alla finestra del popup corrente (quella che contiene il bottone "esci")
	            Stage stagePopupCorrente = (Stage) logout.getScene().getWindow();
	            
	            // Crea una nuova finestra (popup) per il nuovo popup di logout
	            Stage nuovoPopup = new Stage();
	            
	            // Crea una nuova scena con il nuovo popup
	            Scene nuovaScena = new Scene(scenaSuccessiva);
	            nuovoPopup.setScene(nuovaScena);
	            
	            // Imposta la finestra corrente come "genitore" del nuovo popup
	            nuovoPopup.initOwner(stagePopupCorrente); // Il nuovo popup sarà sopra il popup corrente
	            
	            // Imposta la modalità su `WINDOW_MODAL` per evitare interazione con la finestra principale
	            nuovoPopup.initModality(Modality.WINDOW_MODAL);

	            // Mostra il nuovo popup sopra quello corrente
	            nuovoPopup.show();
	  
	            } catch (NullPointerException | IOException e) {
	                System.out.println("Errore nel caricamento della schermata successiva!");
	            }
		}

	    @FXML
	    void modificaPassword(MouseEvent event) {
	    	try {
	    		
	    		 // Carica la scena per il popup di logout
	            Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/PopUpModificaPassword.fxml"));

	            // Ottieni il riferimento alla finestra del popup corrente (quella che contiene il bottone "esci")
	            Stage stagePopupCorrente = (Stage) modPass.getScene().getWindow();
	            
	            // Crea una nuova finestra (popup) per il nuovo popup di logout
	            Stage nuovoPopup = new Stage();
	            
	            // Crea una nuova scena con il nuovo popup
	            Scene nuovaScena = new Scene(scenaSuccessiva);
	            nuovoPopup.setScene(nuovaScena);
	            
	            // Imposta la finestra corrente come "genitore" del nuovo popup
	            nuovoPopup.initOwner(stagePopupCorrente); // Il nuovo popup sarà sopra il popup corrente
	            
	            // Imposta la modalità su `WINDOW_MODAL` per evitare interazione con la finestra principale
	            nuovoPopup.initModality(Modality.WINDOW_MODAL);

	            // Mostra il nuovo popup sopra quello corrente
	            nuovoPopup.show();
	  
	            } catch (NullPointerException | IOException e) {
	                System.out.println("Errore nel caricamento della schermata successiva!");
	            }
	    }

	    @FXML
	    void tornaAllaHome(MouseEvent event) {
	    	 try {
	    		 	// Ottieni il riferimento al palco del popup
	    	        Stage popUpStage = (Stage) home.getScene().getWindow();
	    	        
	    	        // Chiudi il popup
	    	        popUpStage.close();
	    	        
	    	        // Usa la finestra principale già impostata
	    	        Stage finestraPrincipale = this.finestraPrincipale;

	    	        // Carica la scena "Home"
	    	        Parent scenaSuccessiva = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));

	    	        // Crea una nuova scena
	    	        Scene nuovaScena = new Scene(scenaSuccessiva);

	    	        // Imposta la nuova scena nella finestra principale esistente
	    	        finestraPrincipale.setScene(nuovaScena);
	    	        finestraPrincipale.show();
	    	 	} catch (NullPointerException | IOException e) {
			        System.out.println("Errore nel caricamento della schermata successiva!" + e.getMessage());
			    }      
	    }
	    
	    
	    public void initialize() {
            username.setText("Ciao " + utenteCorrente.getUsername()); 
            nome.setText(utenteCorrente.getNome()); 
            cognome.setText(utenteCorrente.getCognome()); 
            p1.setText((utenteCorrente.getPg1() * 100) + "%"); 
            p2.setText((utenteCorrente.getPg2() * 100) + "%"); 
            p3.setText((utenteCorrente.getPg3() * 100) + "%"); 
	    }
}


