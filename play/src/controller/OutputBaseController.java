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
    private ToggleGroup Gruppo1;

    @FXML
    private ToggleGroup Gruppo2;

    @FXML
    private ToggleGroup Gruppo3;

    @FXML
    private ToggleGroup Gruppo4;

    @FXML
    private ToggleGroup Gruppo5;

    @FXML
    private Button close;

    @FXML
    private Label codice1;

    @FXML
    private Label codice2;

    @FXML
    private Label codice3;

    @FXML
    private Label codice4;

    @FXML
    private Label codice5;

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
    
    private ArrayList<DomandaVeroFalso> domandeVeroFalso;
    
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
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/PrevediOutputLivelli.fxml"));

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
        letturaDaFile(); 
    }
  
    void letturaDaFile() {
        domandeVeroFalso = new ArrayList<>(); // Inizializza la lista

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
                    domanda = scf.nextLine(); // Prendi la riga successiva per la domanda
                }
                
                
                if (line.startsWith("risposta:")) {
                    risposta = scf.nextLine(); // Prendi la riga successiva per la risposta
                }
                
                if (line.equals("****")) {
                    // Quando trovi il separatore, crea una nuova domanda
                    if (!codice.isEmpty() && !domanda.isEmpty() && !risposta.isEmpty()) {
                        DomandaVeroFalso D = new DomandaVeroFalso(codice, domanda, risposta);
                        domandeVeroFalso.add(D);

                        // Reset delle variabili per la prossima domanda
                        codice = "";
                        domanda = "";
                        risposta = "";
                    }
                }
            }
            scf.close();

            // Aggiorna le label con i dati delle domande
           aggiornaLabel();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }
    
    void aggiornaLabel() {
        for (int i = 0; i < domandeVeroFalso.size(); i++) {
            DomandaVeroFalso domanda = domandeVeroFalso.get(i);

            // Usa un if o switch per aggiornare le label
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
        
        // Verifica le risposte per ogni domanda
        for (int i = 0; i < domandeVeroFalso.size(); i++) {
            DomandaVeroFalso domanda = domandeVeroFalso.get(i);
            ToggleGroup gruppo = getGruppoDaIndice(i); // Ottieni il ToggleGroup associato alla domanda
            String rispostaUtente = getRispostaSelezionata(gruppo); // Ottieni la risposta selezionata

            // Se una risposta è selezionata e corretta, incrementa il punteggio
            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
                punteggioLocale++;
            }
        }
        
        if(utenteCorrente != null && utenteCorrente.getPg2() == 0 && punteggioLocale >= 3) {
    		utenteCorrente.setPg2(0.33);
    		utenteCorrente.salvaSuFile();
        } 

        try {
	           
			Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/PrevediOutputLivelli.fxml"));

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
    
    private ToggleGroup getGruppoDaIndice(int indice) {
        // Restituisce il ToggleGroup in base all'indice
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
        // Restituisce la risposta selezionata (Vero o Falso) da un ToggleGroup
        Toggle selezionato = gruppo.getSelectedToggle();
        if (selezionato != null) {
            RadioButton rb = (RadioButton) selezionato;
            return rb.getText(); // Vero o Falso
        }
        return null; // Se non è selezionata nessuna risposta
    }
}