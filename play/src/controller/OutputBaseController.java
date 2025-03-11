package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import dati.Utente;
import domanda.DomandaVeroFalso;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class OutputBaseController {

	@FXML
    private ToggleGroup Gruppo1, Gruppo2, Gruppo3, Gruppo4, Gruppo5;

    @FXML
    private Button close, indietro, terminaCorreggi;

    @FXML
    private Label codice1, codice2, codice3, codice4, codice5, domanda1, domanda2, domanda3, domanda4, domanda5, utente, timer;

    private int tempoRestante = 120;  //2 minuti per eseguire l'esercizio
    
   
	private ArrayList<Label> domande  = new ArrayList<>();
	private ArrayList<Label> codici  = new ArrayList<>();
	private ArrayList<ToggleGroup> gruppi = new ArrayList<>(); 
	private ArrayList<DomandaVeroFalso> domandeVeroFalso;
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();

    @FXML
    public void initialize() {
        domande.add(domanda1); 
        domande.add(domanda2); 
        domande.add(domanda3); 
        domande.add(domanda4); 
        domande.add(domanda5); 
    	
        codici.add(codice1); 
        codici.add(codice2); 
        codici.add(codice3); 
        codici.add(codice4); 
        codici.add(codice5); 
        
        gruppi.add(Gruppo1); 
        gruppi.add(Gruppo2); 
        gruppi.add(Gruppo3); 
        gruppi.add(Gruppo4); 
        gruppi.add(Gruppo5); 
        
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
    void colorChangeBasic(MouseEvent event) {
    	terminaCorreggi.setStyle("-fx-background-color: white; -fx-border-color: #2379be; -fx-border-width: 2px;");
    }

    @FXML
    void colorChangeBlue(MouseEvent event) {
    	terminaCorreggi.setStyle("-fx-background-color: #ADD9F4;-fx-border-color: #2379be"); 
    }

    @FXML
    void paginaPrecedente(MouseEvent event) {
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/OutputLivelli.fxml"));

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
        domandeVeroFalso = new ArrayList<>(); 
        
        try {
            Scanner scf = new Scanner(new File("DomandeOutputBase.txt"));
            String codice = "";
            String domanda = "";
            String risposta = "";

            while (scf.hasNextLine()) {
                String line = scf.nextLine().trim();

                if (line.startsWith("codice:")) {
                	
                	codice = "";
                	while (scf.hasNextLine()) {
                        String codiceLine = scf.nextLine();
                        if (codiceLine.startsWith("////")) {
                            break;
                        }
                        codice += codiceLine + "\n"; 
                    }
                }
                
                if (line.startsWith("domanda:")) {
                    domanda = scf.nextLine(); 
                }
                
                if (line.startsWith("risposta:")) {
                    risposta = scf.nextLine(); 
                }
                
                if (line.equals("****")) { //"****" permette di separare la domanda successiva dalla corrente
                    if (!codice.isEmpty() && !domanda.isEmpty() && !risposta.isEmpty()) {
                        DomandaVeroFalso D = new DomandaVeroFalso(codice, domanda, risposta);
                        domandeVeroFalso.add(D);
                        
                        codice = "";
                        domanda = "";
                        risposta = "";
                    }
                }
            }
            scf.close();
           aggiornaLabel();//scrive nelle label corrispondenti quello che legge da file
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }
    
    //tramite lo switch trova il label in cui scrivere la domanda inserita precedentemente nell'arrayList
    void aggiornaLabel() {
    	 for (int i = 0; i < domandeVeroFalso.size(); i++) {
    	        DomandaVeroFalso domanda = domandeVeroFalso.get(i);

    	        if (i < codici.size()) {
    	            codici.get(i).setText(domanda.getCodice());
    	            domande.get(i).setText(domanda.getTestoDomanda());
    	        }
    	} 
    }

    @FXML
    void salvaPunteggio(MouseEvent event) {
    	int punteggioLocale = 0;
        for (int i = 0; i < domandeVeroFalso.size(); i++) {
            DomandaVeroFalso domanda = domandeVeroFalso.get(i);
            ToggleGroup gruppo = gruppi.get(i); 
            String rispostaUtente = getRispostaSelezionata(gruppo); 

            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
                punteggioLocale++; //se la risposta è corretta incrementa il punteggio locale 
            }
        }
        
        if(utenteCorrente != null && utenteCorrente.getPg2() == 0 && punteggioLocale >= 3) {
    		utenteCorrente.setPg2(0.33); //punteggio globale incrementato se l'esercizio viene superato
    		utenteCorrente.salvaSuFile();
        } 

        try {
	           
			Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/OutputLivelli.fxml"));

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
    
    
    private String getRispostaSelezionata(ToggleGroup gruppo) {
        Toggle selezionato = gruppo.getSelectedToggle(); //restituisce il toggle selezionato
        if (selezionato != null) {
            RadioButton rb = (RadioButton) selezionato;
            return rb.getText(); 
        }
        return null; //se non viene selezionata alcuna risposta restituisce null
    }
}