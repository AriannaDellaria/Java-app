	package controller;
	
	import java.io.File;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Scanner;
	import dati.Utente;
	import domanda.DomandaDefinizioni;
import domanda.DomandaRiordina;
import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.ComboBox;
	import javafx.scene.control.Label;
	import javafx.scene.control.TextField;
	import javafx.scene.input.MouseEvent;
	import javafx.stage.Modality;
	import javafx.stage.Stage;
	import sessione.SessioneGioco;
	
	public class RiordinaBaseController {

		 @FXML
		    private Button close;

		    @FXML
		    private Label codice1, codice2, codice3, codice4, codice5, codice6, codice7, codice8, codice9, codice10, codice11, codice12, codice13, codice14, codice15, codice16, codice17, codice18, codice19, codice20; 
		   
		    @FXML
		    private Button indietro;

		    @FXML
		    private Button terminaCorreggi;

		    @FXML
		    private TextField text1,text11,text111,text1111; 

		    @FXML
		    private TextField text2,text22,text222,text2222; 

		    @FXML
		    private TextField text3,text33,text333,text3333; 

		    @FXML
		    private TextField text4,text44,text444,text4444; 

		    @FXML
		    private TextField text5,text55,text555,text5555;
		    
		    @FXML
		    private Label d1,d2,d3,d4,d5;
		    
	    private ArrayList<DomandaRiordina> domandeRiordina;  
	    
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
	            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/RiordinaLivelli.fxml"));

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
	            letturaDaFile();  // Solo se le etichette sono correttamente inizializzate
	    }
	    
	    void letturaDaFile() {
	        domandeRiordina = new ArrayList<>(); // Inizializza la lista

	        try {
	            Scanner scf = new Scanner(new File("DomandeRiordinaBase.txt"));
	            String domanda = "";
	            String risposta = ""; 
	            String codice = ""; 
	            while (scf.hasNextLine()) {
	                String line = scf.nextLine().trim();

	                for(int i = 1; i < 21; i++) { 
	                	String x = "codice" + i + ":"; 
	                	Label label = getLabelByNumber(i); 
	                	
	                	if (line.startsWith(x)) {
	                	codice = "" ; 
	                	
	                    while (scf.hasNextLine()) {
	                        String codiceLine = scf.nextLine().trim();
	                        if (codiceLine.equals("////")) {
	                            break; 
	                        }
	                        codice += codiceLine + "\n"; 
	                        if (label != null) {
	                            label.setText(codice);
	                        } else {
	                            System.out.println("Errore: label non trovata per codice " + i);
	                        }
	                        }
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
	                    if (!domanda.isEmpty() && !risposta.isEmpty()) {
	                        DomandaRiordina D = new DomandaRiordina(domanda,risposta);
	                        domandeRiordina.add(D);

	                        // Reset delle variabili per la prossima domanda
	                        domanda = "";
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
	    
	    private Label getLabelByNumber(int i) {
	        switch (i) {
	            case 1: return codice1;
	            case 2: return codice2;
	            case 3: return codice3;
	            case 4: return codice4;
	            case 5: return codice5;
	            case 6: return codice6;
	            case 7: return codice7;
	            case 8: return codice8;
	            case 9: return codice9;
	            case 10: return codice10;
	            case 11: return codice11;
	            case 12: return codice12;
	            case 13: return codice13;
	            case 14: return codice14;
	            case 15: return codice15;
	            case 16: return codice16;
	            case 17: return codice17;
	            case 18: return codice18;
	            case 19: return codice19;
	            case 20: return codice20;
	            default: return null;
	        }
	    }
	    
	    void aggiornaLabel() {
	        for (int i = 0; i < domandeRiordina.size(); i++) {
	            DomandaRiordina domanda = domandeRiordina.get(i);

	            // Usa un if o switch per aggiornare le label
	            switch (i) {
                case 0:
                    d1.setText(domanda.getTestoDomanda());
                    break;
                case 1:
                    d2.setText(domanda.getTestoDomanda());
                    break;
                case 2:
                    d3.setText(domanda.getTestoDomanda());
                    break;
                case 3:
                    d4.setText(domanda.getTestoDomanda());
                    break;
                case 4:
                    d5.setText(domanda.getTestoDomanda());
                    break;
            }
	        }
	    }
	    
	    
	    ArrayList<String> risposteTotali() { 
	        ArrayList<String> risposte = new ArrayList<>(); 
	        int x = 1; 
	        for (int i = 0; i < 5; i++) { 
	            String risposta = ""; 
	            // Modifica da x + 5 a x + 4
	            for (int j = x; j < x + 4 && j <= 20; j++) { 
	                TextField text = getText(j); 
	                risposta += text.getText(); 
	            }
	            risposte.add(risposta);
	            x += 4;  // Incrementa x di 4 per il prossimo blocco
	        }
	        return risposte; 
	    }

	    
	    private TextField getText(int i) {
	        switch (i) {
	            case 1: return text1 ;
	            case 2: return text11;
	            case 3: return text111;
	            case 4: return text1111;
	            case 5: return text2;
	            case 6: return text22;
	            case 7: return text222;
	            case 8: return text2222;
	            case 9: return text3;
	            case 10: return text33;
	            case 11: return text333;
	            case 12: return text3333;
	            case 13: return text4;
	            case 14: return text44;
	            case 15: return text444;
	            case 16: return text4444;
	            case 17: return text5;
	            case 18: return text55;
	            case 19: return text555;
	            case 20: return text5555;
	            default: return null;
	        }
	    }
	    
	    @FXML
	    void salvaPunteggio(MouseEvent event) {
	    	int punteggioLocale = 0;
	        ArrayList<String> risposte = risposteTotali(); 
	  
	        // Verifica le risposte per ogni domanda
	        for (int i = 0; i < domandeRiordina.size(); i++) {
	            DomandaRiordina domanda = domandeRiordina.get(i);
	            String rispostaUtente = risposte.get(i);  // Ottieni la risposta selezionata

	            // Se una risposta è selezionata e corretta, incrementa il punteggio
	            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
	                punteggioLocale++;
	            }
	        }
	        
	        if(utenteCorrente != null && utenteCorrente.getPg3() == 0 && punteggioLocale >= 3) {
	    		utenteCorrente.setPg3(0.33);
	    		utenteCorrente.salvaSuFile();
	        } 

	        try {
		           
				Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/RiordinaLivelli.fxml"));

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
	}
	


