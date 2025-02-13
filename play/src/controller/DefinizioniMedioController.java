package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;
	
	public class DefinizioniMedioController {

	    @FXML
	    private ComboBox<String> ComboBox1, ComboBox2, ComboBox3, ComboBox4, ComboBox5;

	    @FXML
	    private Label domanda1, domanda2, domanda3, domanda4, domanda5, utente, timer;

	    @FXML
	    private Button close, indietro, terminaCorreggi;
	    
	    @FXML
	    private ImageView immagine;
	    
	    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
	    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
	   
	    private ArrayList<DomandaDefinizioni> domandeDefinizioni;
	    private ArrayList<Label> domande = new ArrayList<>();
	    private ArrayList<ComboBox<String>> opzioni = new ArrayList<>();
	   
	    private int tempoRestante = 120; //2 minuti

	    //carica immagine dell'orologio per il timer
		private final Image orologio = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/immagini/orologio.png")));
		
	    @FXML
	    public void initialize() {
	        domande.add(domanda1);
	        domande.add(domanda2);
	        domande.add(domanda3);
	        domande.add(domanda4);
	        domande.add(domanda5);

	        opzioni.add(ComboBox1);
	        opzioni.add(ComboBox2);
	        opzioni.add(ComboBox3);
	        opzioni.add(ComboBox4);
	        opzioni.add(ComboBox5);

	        letturaDaFile();  
	        utente.setText(utenteCorrente.getUsername());
	        avvioTimer();
	        
	        immagine.setImage(orologio); 
	        immagine.setFitWidth(20); //larghezza
	        immagine.setFitHeight(20); //altezza
	    }
	    
	    
	    private void avvioTimer() {
		    Thread t = new Thread(() -> { //viene creato un thread secondario, parallelo a quello principale (interfaccia grafica), che permette di gestire il timer 
		    	while (tempoRestante > 0 ) {
		            try {
		                Thread.sleep(1000); //fa scorrere il timer di secondo in secondo (1000 millisecondi) 
		                tempoRestante--; //timer decrescente
		                Platform.runLater(() -> { //platform -> permette al thread secondario di interagire con quello principale (modifica l'interfaccia e mostra  lo scorrere del timer) 
		                    int minuti = tempoRestante / 60;
		                    int secondi = tempoRestante % 60;
		                    timer.setText(String.format("%02d:%02d", minuti, secondi)); //viene aggiornata la label
		                });
		            } catch (InterruptedException e) {
		            	System.out.println("Attenzione! L'operazione si è interrotta nel thread! " +e.getMessage()); 
		            }
		        }
		        if (tempoRestante == 0) {
		            Platform.runLater(() -> {
		            	salvaPunteggio(null);//il timer è scaduto ma il bottone non è stato cliccato -> effettua comunque la correzione degli esercizi fatti fino a quel momento e salva il punteggio 
		            });
		        }
		    });
		    t.setDaemon(true);//consente all'utente di finire l'esercizio anche prima dello scadere del timer
		    t.start();//consente di avviare il thread secondario
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
	            Scanner scf = new Scanner(new File("DomandeDefinizioniMedio.txt"));
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
	                
	                
	                if (line.equals("****")) { //"*" permette di separare la domanda successiva dalla corrente
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
	    
	    //recupera dall'arrayList le varie domande e le varie opzioni
	    void aggiornaLabel() {
	        for (int i = 0; i < domandeDefinizioni.size() && i < domande.size(); i++) {
	            domande.get(i).setText(domandeDefinizioni.get(i).getTestoDomanda());
	            opzioni.get(i).getItems().setAll(domandeDefinizioni.get(i).getOpzioni());
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
	        return (indice >= 0 && indice < opzioni.size()) ? opzioni.get(indice) : null;
	    }
}


	

/*
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

public class DefinizioniMedioController {
    @FXML
    private Button close, indietro, terminaCorreggi;

    @FXML
    private ComboBox<String> ComboBox1, ComboBox2, ComboBox3, ComboBox4, ComboBox5;
 
    @FXML
    private Label domanda1, domanda2, domanda3, domanda4, domanda5, utente;
    
    private ArrayList<DomandaDefinizioni> domandeDefinizioni;
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();

    
    @FXML
    public void initialize() {
            letturaDaFile();  
            utente.setText(utenteCorrente.getUsername()); 
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
            System.out.println("Errore nel caricamento della schermata precedente!");
        }
    }
    
  
    //legge il file e recupera le varie parti dell'esercizio
    //la struttura del file stesso consente la divisione delle parti 
    void letturaDaFile() {
        domandeDefinizioni = new ArrayList<>();

        try {
            Scanner scf = new Scanner(new File("DomandeDefinizioniMedio.txt"));
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
                
                
                if (line.equals("****")) {  //"****" permette di separare la domanda successiva dalla corrente
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
           aggiornaLabel();  //scrive nelle label corrispondenti quello che legge da file
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
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
        
        if(utenteCorrente != null && utenteCorrente.getPg1() == 0.33 && punteggioLocale >= 3) {
    		utenteCorrente.setPg1(0.66); //punteggio globale incrementato se l'esercizio viene superato
    		utenteCorrente.salvaSuFile();
        } 

        try {
	           
			Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/DefinizioniLivelli.fxml"));

            Stage scenaCorrente = (Stage) terminaCorreggi.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/PopUpPunteggio.fxml"));
            Parent popUpPunteggio = loader.load();

            PopUpPunteggioController popUp = loader.getController();
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
*/


