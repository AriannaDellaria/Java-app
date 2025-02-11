package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import dati.Utente;
import domanda.DomandaMultipla;
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
    private Label codice1, codice2, codice3, codice4, codice5, domanda1, domanda2, domanda3, domanda4, domanda5, utente;

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
    
    private ArrayList<DomandaMultipla> domandeMultiple;
    
    private ArrayList<Label> codiciLabel = new ArrayList<>();
    private ArrayList<Label> domandeLabel = new ArrayList<>();
    private ArrayList<ToggleGroup> gruppiToggle = new ArrayList<>();
    private ArrayList<ArrayList<RadioButton>> radioButtons = new ArrayList<>();
 
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();

    @FXML
    public void initialize() {
        // Inizializzazione della lista di ToggleGroup
        gruppiToggle = new ArrayList<>(Arrays.asList(Gruppo1, Gruppo2, Gruppo3, Gruppo4, Gruppo5));

        // Inizializzazione della lista di Label per i codici
        codiciLabel = new ArrayList<>(Arrays.asList(codice1, codice2, codice3, codice4, codice5));

        // Inizializzazione della lista di Label per le domande
        domandeLabel = new ArrayList<>(Arrays.asList(domanda1, domanda2, domanda3, domanda4, domanda5));

        // Inizializzazione della lista di RadioButton per ciascun gruppo
        radioButtons = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(opzione1_1, opzione1_2, opzione1_3, opzione1_4)),
            new ArrayList<>(Arrays.asList(opzione2_1, opzione2_2, opzione2_3, opzione2_4)),
            new ArrayList<>(Arrays.asList(opzione3_1, opzione3_2, opzione3_3, opzione3_4)),
            new ArrayList<>(Arrays.asList(opzione4_1, opzione4_2, opzione4_3, opzione4_4)),
            new ArrayList<>(Arrays.asList(opzione5_1, opzione5_2, opzione5_3, opzione5_4))
        ));

        // Esegui la lettura del file
        letturaDaFile();

        // Imposta il nome dell'utente
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
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/OutputLivelli.fxml"));

            Stage scenaCorrente = (Stage) indietro.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();  
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
        }
    }
    
    @FXML
    void popUpUtente() {
    	    try {
    	        Stage stagePrincipale = (Stage) utente.getScene().getWindow();

    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/PopUpUtente.fxml"));
    	        Parent popUp = loader.load();
    	        
    	        Stage popUpStage = new Stage();
    	        popUpStage.setScene(new Scene(popUp));
    	        popUpStage.initModality(Modality.WINDOW_MODAL); 
    	        popUpStage.initOwner(stagePrincipale); 
    	        popUpStage.show();
    	    } catch (NullPointerException | IOException e) {
    	        System.out.println("Errore nel caricamento della schermata successiva!");
    	    }   
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
    
    void aggiornaLabel() {
        for (int i = 0; i < domandeMultiple.size() && i < codiciLabel.size(); i++) {
            DomandaMultipla domanda = domandeMultiple.get(i);
            codiciLabel.get(i).setText(domanda.getCodice());
            domandeLabel.get(i).setText(domanda.getTestoDomanda());
        }
    }
    
    void aggiornaRadioButton() {
        for (int i = 0; i < domandeMultiple.size() && i < radioButtons.size(); i++) {
            ArrayList<RadioButton> opzioniBottoni = radioButtons.get(i);
            ArrayList<String> opzioniTesto = domandeMultiple.get(i).getOpzioni();
            
            for (int j = 0; j < opzioniBottoni.size() && j < opzioniTesto.size(); j++) {
                opzioniBottoni.get(j).setText(opzioniTesto.get(j));
            }
        }
    }

    //corregge l'esercizio ottenendo un punteggio per verificare il superamento dell'esercizio
    @FXML
    void salvaPunteggio(MouseEvent event) {
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