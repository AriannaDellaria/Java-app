package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import dati.Utente;
import domanda.DomandaClassica;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class DefinizioniAvanzatoController {

    @FXML
    private Label domanda1, domanda2, domanda3, domanda4, domanda5, utente, timer;
    
    @FXML
    private Button close, indietro, terminaCorreggi;
    
    @FXML
    private TextField risposta1, risposta2, risposta3, risposta4, risposta5;
    
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();

    private ArrayList<Label> domande = new ArrayList<>();
    private ArrayList<TextField> risposte = new ArrayList<>();
    private ArrayList<DomandaClassica> domandeClassiche = new ArrayList<>();
    
    private int tempoRestante = 120; //2 minuti
  
    @FXML
    public void initialize() {
        domande.add(domanda1);
        domande.add(domanda2);
        domande.add(domanda3);
        domande.add(domanda4);
        domande.add(domanda5);

        risposte.add(risposta1);
        risposte.add(risposta2);
        risposte.add(risposta3);
        risposte.add(risposta4);
        risposte.add(risposta5);
        
        letturaDaFile();  
        utente.setText(utenteCorrente.getUsername());
        avvioTimer();
        
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
    void colorChangeRed(MouseEvent event) {
    	terminaCorreggi.setStyle("-fx-background-color: #FFC8AE;-fx-border-color: #f64c4c"); 
    }
    
    @FXML
    void colorChangeBasic(MouseEvent event) {
    	terminaCorreggi.setStyle("-fx-background-color: white; -fx-border-color: #f64c4c; -fx-border-width: 2px;");
    	
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
        domandeClassiche = new ArrayList<>(); 

        try {
            Scanner scf = new Scanner(new File("DomandeDefinizioniAvanzato.txt"));
            String domanda = "";
            String risposta = "";
            
            while (scf.hasNextLine()) {
                String line = scf.nextLine().trim();
                
                if (line.startsWith("domanda:")) {
                    domanda = scf.nextLine(); 
                }
                
                if (line.startsWith("risposta:")) {
                    risposta = scf.nextLine(); 
                }
                
                if (line.equals("****")) { //"*" permette di separare la domanda successiva dalla corrente
                    if (!domanda.isEmpty() && !risposta.isEmpty()) {
                        DomandaClassica D = new DomandaClassica(domanda, risposta);
                        domandeClassiche.add(D);

                        domanda = "";
                        risposta = "";
                    }
                }
            }
            scf.close();
           aggiornaLabel();//scrive nelle label corrispondenti quello che legge da file
        } 
            catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }
    
    void aggiornaLabel() {
        for (int i = 0; i < domandeClassiche.size() && i < domande.size(); i++) {
            domande.get(i).setText(domandeClassiche.get(i).getTestoDomanda());
        }
    }


  //corregge l'esercizio ottenendo un punteggio per verificare il superamento dell'esercizio
    @FXML
    void salvaPunteggio(MouseEvent event) {
    	int punteggioLocale = 0;
        for (int i = 0; i < domandeClassiche.size(); i++) {
            DomandaClassica domanda = domandeClassiche.get(i);
            TextField text = getTextField(i);
            String rispostaUtente = text.getText().trim(); 
            
            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
                punteggioLocale++; //se la risposta è corretta incrementa il punteggio locale 
            }
        }
        
        if(utenteCorrente != null && utenteCorrente.getPg1() == 0.66 && punteggioLocale >= 3) {
    		utenteCorrente.setPg1(1.00); //punteggio globale incrementato se l'esercizio viene superato
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
            
            popUpStage.initModality(Modality.WINDOW_MODAL); 
            popUpStage.initOwner(scenaCorrente); 
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
    
    private TextField getTextField(int indice) {
        if (indice < risposte.size()) {
            return risposte.get(indice);
        }
        return null;
    }
    
}