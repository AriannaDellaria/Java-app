package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import dati.Utente;
import domanda.DomandaClassica;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class DefinizioniAvanzatoController {

    @FXML
    private Label domanda1, domanda2, domanda3, domanda4, domanda5, utente;
    
    @FXML
    private Button close, indietro, terminaCorreggi;
    
    @FXML
    private TextField risposta1, risposta2, risposta3, risposta4, risposta5;
    
    private ArrayList<DomandaClassica> domandeClassiche;
    
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
                
                if (line.equals("****")) { //"****" permette di separare la domanda successiva dalla corrente
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
    
    //tramite lo switch trova il label in cui scrivere la domanda inserita precedentemente nell'arrayList
    void aggiornaLabel() {
        for (int i = 0; i < domandeClassiche.size(); i++) {
            DomandaClassica domanda = domandeClassiche.get(i);

            switch (i) {
            case 0:
                domanda1.setText(domanda.getTestoDomanda()); 
                break;
            case 1:
                domanda2.setText(domanda.getTestoDomanda());
                break;
            case 2:
                domanda3.setText(domanda.getTestoDomanda());
                break;
            case 3:
                domanda4.setText(domanda.getTestoDomanda());
                break;
            case 4:
                domanda5.setText(domanda.getTestoDomanda());
                break;
            }
        }
    }

  //corregge l'esercizio ottenendo un punteggio per verificare il superamento dell'esercizio
    @FXML
    void salvaPunteggio(MouseEvent event) {
    	int punteggioLocale = 0;
        for (int i = 0; i < domandeClassiche.size(); i++) {
            DomandaClassica domanda = domandeClassiche.get(i);
            TextField text = getTextField(i);
            String rispostaUtente = text.getText(); 
            
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
        switch (indice) {
            case 0: 
            	return risposta1;
            case 1: 
            	return risposta2;
            case 2: 
            	return risposta3;
            case 3: 
            	return risposta4;
            case 4:
            	return risposta5;
            default: 
            	return null;
        }
    }
    
}




