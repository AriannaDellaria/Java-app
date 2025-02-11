package controller;

	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Scanner;
	import dati.Utente;
	import domanda.DomandaDefinizioni;
import javafx.application.Platform;
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
	    private ComboBox<String> ComboBox1, ComboBox2, ComboBox3, ComboBox4, ComboBox5;

	    @FXML
	    private Label domanda1, domanda2, domanda3, domanda4, domanda5, utente, timerLabel;

	    @FXML
	    private Button close, indietro, terminaCorreggi;

	    private ArrayList<DomandaDefinizioni> domandeDefinizioni;
	     
	    //
	    private int tempoRimanente = 60; // 10 minuti (600 secondi)
	    private boolean timerAttivo = false;
	    // 
	    
	    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
	    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();

	    @FXML
	    public void initialize() {
	            letturaDaFile();  
	            utente.setText(utenteCorrente.getUsername()); 
	            //
	            startTimer();  // Avvia il timer all'inizio
	    }
	    
	    // *** Metodo per avviare il timer (Countdown) ***
	    private void startTimer() {
	        timerAttivo = true;
	        Thread timerThread = new Thread(() -> {
	            while (tempoRimanente > 0 && timerAttivo) {
	                try {
	                    Thread.sleep(1000); // Una pausa di 1 secondo tra ogni iterazione
	                    tempoRimanente--; // Decrementa il tempo rimanente
	                    Platform.runLater(() -> {
	                        int minuti = tempoRimanente / 60;
	                        int secondi = tempoRimanente % 60;
	                        timerLabel.setText(String.format("%02d:%02d", minuti, secondi)); // Aggiorna la label del timer
	                    });
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (tempoRimanente == 0) {
	                Platform.runLater(() -> {
	                    terminaEsercizio(); // Quando il tempo scade, ferma l'esercizio
	                });
	            }
	        });
	        timerThread.setDaemon(true); // Assicurati che il thread si chiuda quando l'applicazione termina
	        timerThread.start();
	    }

	    // *** Metodo che termina l'esercizio quando il timer finisce ***
	    private void terminaEsercizio() {
	        salvaPunteggio(null); // Una volta finito il timer, ferma l'esercizio e calcola il punteggio
	    }
	    
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
	            scenaCorrente.show();
	        } catch (NullPointerException | IOException e) {
	            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
	        }
	    }
	  
	    //legge il file e recupera le varie parti dell'esercizio
	    //la struttura del file stesso consente la divisione delle parti 
	    void letturaDaFile() {
	        domandeDefinizioni = new ArrayList<>(); 

	        try {
	            Scanner scf = new Scanner(new File("DomandeDefinizioniBase.txt"));
	            String domanda = "";
	            String risposta = "";
	            ArrayList<String> opzioni = new ArrayList<>();
	            
	            while (scf.hasNextLine()) {
	                String line = scf.nextLine().trim();

	                if (line.startsWith("opzioni:")) {
	                    opzioni.clear(); 
	                    while (scf.hasNextLine()) {
	                        line = scf.nextLine().trim();
	                        if (line.equals("////")) {
	                            break; 
	                        }
	                        opzioni.add(line);
	                    }
	                }
	                
	                if (line.startsWith("domanda:")) {
	                    domanda = scf.nextLine(); 
	                }
	                
	                if (line.startsWith("risposta:")) {
	                    risposta = scf.nextLine(); 
	                }
	                
	                
	                if (line.equals("****")) { //"****" permette di separare la domanda successiva dalla corrente
	                    if (!domanda.isEmpty() && !risposta.isEmpty()) {
	                        DomandaDefinizioni D = new DomandaDefinizioni(domanda, opzioni, risposta);
	                        domandeDefinizioni.add(D);

	                        domanda = "";
	                        opzioni = new ArrayList<>();
	                        risposta = "";
	                    }
	                }
	            }
	            scf.close();
	           aggiornaLabel(); //scrive nelle label corrispondenti quello che legge da file
	        } catch (IOException e) {
	            System.out.println("Errore nella lettura del file! " + e.getMessage());
	        }
	    }
	    
	    //tramite lo switch trova il label in cui scrivere la domanda inserita precedentemente nell'arrayList
	    void aggiornaLabel() {
	        for (int i = 0; i < domandeDefinizioni.size(); i++) {
	            DomandaDefinizioni domanda = domandeDefinizioni.get(i);

	            switch (i) {
                case 0:
                    domanda1.setText(domanda.getTestoDomanda());
                    ComboBox1.getItems().setAll(domanda.getOpzioni());
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
	    
	   //corregge l'esercizio ottenendo un punteggio per verificare il superamento dell'esercizio
	    @FXML
	    void salvaPunteggio(MouseEvent event) {
	    	int punteggioLocale = 0;

	        for (int i = 0; i < domandeDefinizioni.size(); i++) {
	            DomandaDefinizioni domanda = domandeDefinizioni.get(i);
	            ComboBox<String> combo = getCombo(i); 
	            String rispostaUtente = combo.getValue(); 
	            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
	                punteggioLocale++; //se la risposta è corretta incrementa il punteggio locale 
	            }
	        }
	        
	        if(utenteCorrente != null && utenteCorrente.getPg1() == 0 && punteggioLocale >= 3) {
	    		utenteCorrente.setPg1(0.33); //punteggio globale incrementato se l'esercizio viene superato
	    		utenteCorrente.salvaSuFile();
	        } 

	        try {
		           
				Parent paginaPrecedente = FXMLLoader.load(getClass().getResource("/application/DefinizioniLivelli.fxml"));

	            Stage paginaCorrente = (Stage) terminaCorreggi.getScene().getWindow();

	            Scene vecchiaScena = new Scene(paginaPrecedente);
	            paginaCorrente.setScene(vecchiaScena);
	            paginaCorrente.show();
	            
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/PopUpPunteggio.fxml"));
	            Parent popUpPunteggio = loader.load();

	            PopUpPunteggioController popUp = loader.getController();
	            popUp.modificaMessaggio(punteggioLocale);
	            
	            Stage popUpStage = new Stage();
	            popUpStage.setScene(new Scene(popUpPunteggio));
	            popUpStage.initModality(Modality.WINDOW_MODAL); 
	            popUpStage.initOwner(paginaCorrente); 
	            popUpStage.show();
	        } catch (NullPointerException | IOException e) {
	            System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
	        }
	    }
	    
	    @FXML
	    void popUpUtente() {
	    	    try {
	    	        Stage paginaPrincipale = (Stage) utente.getScene().getWindow();

	    	        Parent popUp = FXMLLoader.load(getClass().getResource("/application/PopUpUtente.fxml"));
	    	        
	    	        Stage popUpStage = new Stage();
	    	        popUpStage.setScene(new Scene(popUp));
	    	        popUpStage.initModality(Modality.WINDOW_MODAL); 
	    	        popUpStage.initOwner(paginaPrincipale);
	    	        popUpStage.show(); 
	    	    } catch (NullPointerException | IOException e) {
	    	        System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
	    	    }   
		}
	    
	    
	    private ComboBox<String> getCombo(int indice) {
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
	
