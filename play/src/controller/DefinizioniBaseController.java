package controller;

	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Scanner;
	import dati.Utente;
	import domanda.DomandaDefinizioni;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.ComboBox;
	import javafx.scene.control.Label;
	import javafx.scene.input.MouseEvent;
	import javafx.stage.Modality;
	import javafx.stage.Stage;
	import sessione.SessioneGioco;
	
	public class DefinizioniBaseController {

	    @FXML
	    private Button close;

	    @FXML
	    private ComboBox<String> ComboBox1;

	    @FXML
	    private ComboBox<String> ComboBox2;

	    @FXML
	    private ComboBox<String> ComboBox3;

	    @FXML
	    private ComboBox<String> ComboBox4;

	    @FXML
	    private ComboBox<String> ComboBox5;

	    
	    @FXML
	    private Label domanda1;

	    @FXML
	    private Label domanda2;

	    @FXML
	    private Label domanda3;

	    @FXML
	    private Label domanda4;

	    @FXML
	    private Label domanda5;

	    @FXML
	    private Button indietro;
	    
	    @FXML
	    private Button terminaCorreggi;
	    
	    private ArrayList<DomandaDefinizioni> domandeDefinizioni;
	    
	 // Ottieni l'utente loggato dalla sessione Singleton
	    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
	    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();

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
	    void colorChangeRed(MouseEvent event) {
	    	close.setStyle("-fx-background-color: red;");
	    }

	    @FXML
	    void colorChangeYellow(MouseEvent event) {
	    	indietro.setStyle("-fx-background-color: yellow;");	
	    }

	    @FXML
	    void paginaPrecedente(MouseEvent event) {
	    	try {
	            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/DefinizioniLivelli.fxml"));

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
	    public void initialize() {
	    	if (domanda1 == null || domanda2 == null || domanda3 == null || domanda4 == null || domanda5 == null) {
	            System.out.println("Alcune etichette non sono state inizializzate correttamente.");
	        } else {
	            letturaDaFile();  // Solo se le etichette sono correttamente inizializzate
	        }
	    }
	  
	    void letturaDaFile() {
	        domandeDefinizioni = new ArrayList<>(); // Inizializza la lista

	        try {
	            Scanner scf = new Scanner(new File("DomandeDefinizioniBase.txt"));
	            String domanda = "";
	            String risposta = "";
	            String codice= "";
	            ArrayList<String> opzioni = new ArrayList<>();
	            
	            while (scf.hasNextLine()) {
	                String line = scf.nextLine().trim();

	                if (line.startsWith("opzioni:")) {
	                	codice = "";
	                    opzioni.clear(); // Pulisce la lista delle opzioni
	                    while (scf.hasNextLine()) {
	                        line = scf.nextLine().trim();
	                        if (line.equals("////")) {
	                            break; // Fine della domanda
	                        }
	                        domanda += line + '\n';
	                        opzioni.add(line);  // Aggiungi le opzioni
	                    }
	                }
	                
	                if (line.startsWith("domanda:")) {
	                    domanda = scf.nextLine(); // Prendi la riga successiva per la domanda
	                    domanda += line + '\n';
	                }
	                
	                if (line.startsWith("risposta:")) {
	                    risposta = scf.nextLine(); // Prendi la riga successiva per la risposta
	                }
	                
	                
	                if (line.equals("****")) {
	                    // Quando trovi il separatore, crea una nuova domanda
	                    if (!domanda.isEmpty() && !risposta.isEmpty()) {
	                        DomandaDefinizioni D = new DomandaDefinizioni(domanda, opzioni, risposta);
	                        domandeDefinizioni.add(D);

	                        // Reset delle variabili per la prossima domanda
	                        domanda = "";
	                        opzioni = new ArrayList<>();
	                        risposta = "";
	                    }
	                }
	            }
	            scf.close();

	            // Aggiorna le label con i dati delle domande
	           aggiornaLabel();
	        } 
	            catch (IOException e) {
	            System.out.println("Errore nella lettura del file: " + e.getMessage());
	        }
	    }
	    
	    void aggiornaLabel() {
	        for (int i = 0; i < domandeDefinizioni.size(); i++) {
	            DomandaDefinizioni domanda = domandeDefinizioni.get(i);

	            // Usa un if o switch per aggiornare le label
	            switch (i) {
                case 0:
                    domanda1.setText(domanda.getTestoDomanda());
                    ComboBox1.getItems().setAll(domanda.getOpzioni()); // Imposta le opzioni nel ComboBox
                    break;
                case 1:
                    domanda2.setText(domanda.getTestoDomanda());
                    ComboBox2.getItems().setAll(domanda.getOpzioni());
                    break;
                case 2:
                    domanda3.setText(domanda.getTestoDomanda());
                    ComboBox3.getItems().setAll(domanda.getOpzioni());
                    break;
                case 3:
                    domanda4.setText(domanda.getTestoDomanda());
                    ComboBox4.getItems().setAll(domanda.getOpzioni());
                    break;
                case 4:
                    domanda5.setText(domanda.getTestoDomanda());
                    ComboBox5.getItems().setAll(domanda.getOpzioni());
                    break;
            }
	        }
	    }

	    @FXML
	    void salvaPunteggio(MouseEvent event) {
	    	int punteggioLocale = 0;
	        
	        // Verifica le risposte per ogni domanda
	        for (int i = 0; i < domandeDefinizioni.size(); i++) {
	            DomandaDefinizioni domanda = domandeDefinizioni.get(i);
	            ComboBox<String> combo = getComboBoxByIndex(i);
	            String rispostaUtente = combo.getValue(); // Ottieni la risposta selezionata

	            // Se una risposta è selezionata e corretta, incrementa il punteggio
	            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
	                punteggioLocale++;
	            }
	        }
	        
	        if(utenteCorrente != null && utenteCorrente.getPg1() == 0 && punteggioLocale >= 3) {
	    		utenteCorrente.setPg1(0.33);
	    		//utenteCorrente.stampaPg1();
	    		utenteCorrente.salvaSuFile();
	        } 

	        try {
		           
				Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/DefinizioniLivelli.fxml"));

	            Stage scenaCorrente = (Stage) terminaCorreggi.getScene().getWindow();

	            Scene vecchiaScena = new Scene(scenaPrecedente);
	            scenaCorrente.setScene(vecchiaScena);
	            scenaCorrente.setFullScreen(true);
	            scenaCorrente.setFullScreenExitHint("");
	            scenaCorrente.show();
	            
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/PopUpPunteggio.fxml"));
	            Parent popUpPunteggio = loader.load();

	            PopUpPunteggioOutputController popUp = loader.getController();
	            popUp.modificaMessaggio(punteggioLocale);
	            
	            Stage popUpStage = new Stage();
	            popUpStage.setScene(new Scene(popUpPunteggio));
	            
	            popUpStage.initModality(Modality.WINDOW_MODAL); //non permette all'utente di interagire con ila finestra principale
	            popUpStage.initOwner(scenaCorrente); //mantiene il popup in primo piano e lo chiude se viene chiusa la scena genitore
	            popUpStage.show();
	            
	        } catch (NullPointerException | IOException e) {
	            System.out.println("Errore nel caricamento della schermata successiva!");
	            e.printStackTrace();
	        }
	    }
	    
	    private ComboBox<String> getComboBoxByIndex(int indice) {
	        switch (indice) {
	            case 0: 
	            	return ComboBox1;
	            case 1: 
	            	return ComboBox2;
	            case 2: 
	            	return ComboBox3;
	            case 3: 
	            	return ComboBox4;
	            case 4:
	            	return ComboBox5;
	            default: 
	            	return null;
	        }
	    }
}
	
