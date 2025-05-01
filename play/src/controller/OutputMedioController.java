package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import dati.Utente;
import domanda.DomandaMultipla;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class OutputMedioController {

	@FXML
    private ToggleGroup Gruppo1, Gruppo2, Gruppo3, Gruppo4, Gruppo5;

    @FXML
    private Button close, indietro, terminaCorreggi;

    @FXML
    private Label codice1, codice2, codice3, codice4, codice5, domanda1, domanda2, domanda3, domanda4, domanda5, utente, timer;

    @FXML
    private RadioButton opzione1_1, opzione1_2, opzione1_3, opzione1_4;
    @FXML
    private RadioButton opzione2_1, opzione2_2, opzione2_3, opzione2_4;
    @FXML
    private RadioButton opzione3_1, opzione3_2, opzione3_3, opzione3_4;
    @FXML
    private RadioButton opzione4_1, opzione4_2, opzione4_3, opzione4_4;
    @FXML
    private RadioButton opzione5_1, opzione5_2, opzione5_3, opzione5_4;
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
    
    private ArrayList<DomandaMultipla> domandeMultiple;
    private ArrayList<Label> codici = new ArrayList<>();
    private ArrayList<Label> domande = new ArrayList<>();
    private ArrayList<ToggleGroup> gruppiToggle = new ArrayList<>();
    private ArrayList<ArrayList<RadioButton>> opzioni = new ArrayList<>();
    
    private int tempoRestante = 300;
    private boolean timerAttivo = true;
    
    @FXML
    void closeButton(MouseEvent event) {
    	timerAttivo = false;
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
    	timerAttivo = false;
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
    
    //aggiunge all'arrayList i codici, le domande e le opzioni (esiste un arrayList per ogni gruppo di radioButton) 
	@FXML
    public void initialize() {

        codici.add(codice1);     
        codici.add(codice2);     
        codici.add(codice3);     
        codici.add(codice4);     
        codici.add(codice5);     
        
        domande.add(domanda1);     
        domande.add(domanda2);     
        domande.add(domanda3);     
        domande.add(domanda4);     
        domande.add(domanda5);     
        
        gruppiToggle.add(Gruppo1);     
        gruppiToggle.add(Gruppo2);     
        gruppiToggle.add(Gruppo3);     
        gruppiToggle.add(Gruppo4);     
        gruppiToggle.add(Gruppo5);
     
        ArrayList<RadioButton> gruppo1 = new ArrayList<>();
        gruppo1.add(opzione1_1);
        gruppo1.add(opzione1_2);
        gruppo1.add(opzione1_3);
        gruppo1.add(opzione1_4);
        
        ArrayList<RadioButton> gruppo2 = new ArrayList<>();
        gruppo2.add(opzione2_1);
        gruppo2.add(opzione2_2);
        gruppo2.add(opzione2_3);
        gruppo2.add(opzione2_4);
     
        ArrayList<RadioButton> gruppo3 = new ArrayList<>();
        gruppo3.add(opzione3_1);
        gruppo3.add(opzione3_2);
        gruppo3.add(opzione3_3);
        gruppo3.add(opzione3_4);
     
        ArrayList<RadioButton> gruppo4 = new ArrayList<>();
        gruppo4.add(opzione4_1);
        gruppo4.add(opzione4_2);
        gruppo4.add(opzione4_3);
        gruppo4.add(opzione4_4);
     
        ArrayList<RadioButton> gruppo5 = new ArrayList<>();
        gruppo5.add(opzione5_1);
        gruppo5.add(opzione5_2);
        gruppo5.add(opzione5_3);
        gruppo5.add(opzione5_4);
     
        // Aggiungi tutto alla lista principale
        opzioni.add(gruppo1);
        opzioni.add(gruppo2);
        opzioni.add(gruppo3);
        opzioni.add(gruppo4);
        opzioni.add(gruppo5);
        
        letturaDaFile();
        utente.setText(utenteCorrente.getUsername());
        avvioTimer();
    }
	
	private void avvioTimer() {
	    Thread t = new Thread(() -> { //viene creato un thread secondario, parallelo a quello principale (interfaccia grafica), che permette di gestire il timer 
	    	while (tempoRestante > 0 && timerAttivo) {
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
	        if (tempoRestante == 0 && timerAttivo) {
	            Platform.runLater(() -> {
	            	salvaPunteggio(null);//il timer è scaduto ma il bottone non è stato cliccato -> effettua comunque la correzione degli esercizi fatti fino a quel momento e salva il punteggio 
	            });
	        }
	    });
	    t.setDaemon(true);//consente all'utente di finire l'esercizio anche prima dello scadere del timer
	    t.start();//consente di avviare il thread secondario
	}
  
    //legge il file e recupera le varie parti dell'esercizio
    //la struttura del file stesso consente la divisione delle parti 
    void letturaDaFile() {
        domandeMultiple = new ArrayList<>(); //crea una lista di domande (di tipo DomandaMultipla)
        try {
            Scanner scf = new Scanner(new File("DomandeOutputMedio.txt"));
            
            String codice = "";
            String domanda = "";
            ArrayList<String> opzioni = new ArrayList<>();
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
                if (line.startsWith("opzioni:")) {
                	opzioni.clear();
                	while (scf.hasNextLine()) {
                        String opzioniLine = scf.nextLine();
                        if (opzioniLine.startsWith("++++")) {
                            break;
                        }
                        opzioni.add(opzioniLine);
                    }
                }
                if (line.startsWith("risposta:")) {
                    risposta = scf.nextLine(); 
                } 
                if (line.equals("****")) { //il separatore '****' permette di individuare la domanda successiva
                    if (!codice.isEmpty() && !domanda.isEmpty() && !opzioni.isEmpty()) {
                        DomandaMultipla D = new DomandaMultipla(codice, domanda, opzioni, risposta);
                        domandeMultiple.add(D);

                        codice = "";
                        domanda = "";
                        opzioni = new ArrayList<>();
                        risposta = "";
                    }
                }
            }
            scf.close();            
           aggiornaLabel();//scrive nelle label corrispondenti quello che legge nel file
           aggiornaRadioButton(); //scrive le varie opzioni dei button corrispondenti  
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file! " + e.getMessage());
        }
    }
    
    //scrive nelle label i vari codici e le varie domande recuperate dall'arrayList 
    void aggiornaLabel() {
        for (int i = 0; i < domandeMultiple.size() && i < codici.size(); i++) {
            DomandaMultipla domanda = domandeMultiple.get(i);
            codici.get(i).setText(domanda.getCodice());
            domande.get(i).setText(domanda.getTestoDomanda());
        }
    }
    
    //inserisce nei RadioButton dell'interfaccia grafica le opzioni di risposta lette dal file
    void aggiornaRadioButton() {
        for (int i = 0; i < domandeMultiple.size() && i < opzioni.size(); i++) {
            ArrayList<RadioButton> opzioniBottoni = opzioni.get(i);
            ArrayList<String> opzioniTesto = domandeMultiple.get(i).getOpzioni();
            
            for (int j = 0; j < opzioniBottoni.size() && j < opzioniTesto.size(); j++) {
                opzioniBottoni.get(j).setText(opzioniTesto.get(j));
            }
        }
    }

    //se è stata effettuata la correzione, l'utente torna alla pagina dei livelli e visualizza un popUp relativo al punteggio ottenuto 
    @FXML
    void salvaPunteggio(MouseEvent event) {
    	timerAttivo = false;
    	int punteggioLocale = 0;
        for (int i = 0; i < domandeMultiple.size(); i++) {
            DomandaMultipla domanda = domandeMultiple.get(i);
            ToggleGroup gruppo = getGruppoDaIndice(i); 
            String rispostaUtente = getRispostaSelezionata(gruppo); 

            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
                punteggioLocale++; //se la risposta è corretta incrementa il punteggio locale
            }
        }
        
        if(utenteCorrente != null && utenteCorrente.getPg2() == 0.33 && punteggioLocale >= 3) {
    		utenteCorrente.setPg2(0.66); //punteggio globale incrementato solo se viene superato l'esercizio
    		utenteCorrente.salvaSuFile();
        } 

        //se è stata effettuata la correzione, l'utente torna alla pagina dei livelli e visualizza un popUp relativo al punteggio ottenuto 
        try {      
			Parent paginaPrecedente = FXMLLoader.load(getClass().getResource("/application/OutputLivelli.fxml"));

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
    
    private ToggleGroup getGruppoDaIndice(int indice) {
        return (indice >= 0 && indice < gruppiToggle.size()) ? gruppiToggle.get(indice) : null;
    }

    private String getRispostaSelezionata(ToggleGroup gruppo) {
        Toggle selezionato = gruppo.getSelectedToggle();//restituisce il toggle selezionato
        if (selezionato != null) {
            RadioButton rb = (RadioButton) selezionato;
            return rb.getText(); 
        }
        return null; //se non viene selezionata alcuna risposta restituisce null
    }
}
