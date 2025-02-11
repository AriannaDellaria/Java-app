package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dati.Utente;
import domanda.DomandaVeroFalso;
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

public class OutputBaseController {

	@FXML
    private ToggleGroup Gruppo1, Gruppo2, Gruppo3, Gruppo4, Gruppo5;

    @FXML
    private Button close, indietro, terminaCorreggi;

    @FXML
    private Label codice1, codice2, codice3, codice4, codice5, domanda1, domanda2, domanda3, domanda4, domanda5, utente;

    private ArrayList<DomandaVeroFalso> domandeVeroFalso;
    
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

            switch (i) {
                case 0:
                    codice1.setText(domanda.getCodice());
                    domanda1.setText(domanda.getTestoDomanda());
                    break;
                case 1:
                    codice2.setText(domanda.getCodice());
                    domanda2.setText(domanda.getTestoDomanda());
                    break;
                case 2:
                    codice3.setText(domanda.getCodice());
                    domanda3.setText(domanda.getTestoDomanda());
                    break;
                case 3:
                    codice4.setText(domanda.getCodice());
                    domanda4.setText(domanda.getTestoDomanda());
                    break;
                case 4:
                    codice5.setText(domanda.getCodice());
                    domanda5.setText(domanda.getTestoDomanda());
                    break;
            }
        }
    }

    @FXML
    void salvaPunteggio(MouseEvent event) {
    	int punteggioLocale = 0;
        for (int i = 0; i < domandeVeroFalso.size(); i++) {
            DomandaVeroFalso domanda = domandeVeroFalso.get(i);
            ToggleGroup gruppo = getGruppoDaIndice(i); 
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
    
    private ToggleGroup getGruppoDaIndice(int indice) {
        switch (indice) {
            case 0:
                return Gruppo1;
            case 1:
                return Gruppo2;
            case 2:
                return Gruppo3;
            case 3:
                return Gruppo4;
            case 4:
                return Gruppo5;
            default:
                return null;
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