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
	
	public class RiordinaAvanzatoController {

		 @FXML
		    private Button close, indietro, terminaCorreggi;

		    @FXML
		    private Label d1, d2, d3, d4, d5,codice1, codice2, codice3, codice4, codice5, codice6, codice7, codice8, codice9, codice10, codice11, codice12, codice13, codice14, codice15, codice16, codice17, codice18, codice19, codice20, utente; 

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
	            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/RiordinaLivelli.fxml"));

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
	            Scanner scf = new Scanner(new File("DomandeRiordinaMedio.txt"));
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
	                        String codiceLine = scf.nextLine();
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
	                    domanda = scf.nextLine(); 
	                }
	                
	                if (line.startsWith("risposta:")) {
	                    risposta = scf.nextLine(); 
	                }
	                
	                
	                if (line.equals("****")) { //il separatore '****' permette di individuare la domanda successiva
	                    if (!domanda.isEmpty() && !risposta.isEmpty()) {
	                        DomandaClassica D = new DomandaClassica(domanda,risposta);
	                        domandeClassiche.add(D);

	                        domanda = "";
	                        risposta = "";
	                    }
	                }
	            }
	            scf.close();
	            aggiornaLabel();
	        } catch (IOException e) {
	            System.out.println("Errore nella lettura del file: " + e.getMessage());
	        }
	    }
	    
	  //tramite lo switch trova il label in cui scrivere la domanda inserita precedentemente nell'arraylist
	    void aggiornaLabel() {
	        for (int i = 0; i < domandeClassiche.size(); i++) {
	            DomandaClassica domanda = domandeClassiche.get(i);

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
	            for (int j = x; j < x + 4 && j <= 20; j++) { 
	                TextField text = getText(j); 
	                risposta += text.getText(); 
	            }
	            risposte.add(risposta);
	            x += 4;  //incrementa x di 4 per il prossimo blocco
	        }
	        return risposte; 
	    }

	    
	    @FXML
	    void salvaPunteggio(MouseEvent event) {
	    	int punteggioLocale = 0;
	        ArrayList<String> risposte = risposteTotali(); 
	        for (int i = 0; i < domandeClassiche.size(); i++) {
	            DomandaClassica domanda = domandeClassiche.get(i);
	            String rispostaUtente = risposte.get(i);  

	            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
	                punteggioLocale++; //se la risposta è corretta incrementa il punteggio locale
	            }
	        }
	        
	        if(utenteCorrente != null && utenteCorrente.getPg3() == 0.66 && punteggioLocale >= 3) {
	    		utenteCorrente.setPg3(1.0); //punteggio globale incrementato solo se viene superato l'esercizio
	    		utenteCorrente.salvaSuFile();
	        } 

	        try {
		           
				Parent paginaPrecedente = FXMLLoader.load(getClass().getResource("/application/RiordinaLivelli.fxml"));

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
	            
	            popUpStage.initModality(Modality.WINDOW_MODAL); //non permette all'utente di interagire con ila finestra principale
	            popUpStage.initOwner(paginaCorrente); //mantiene il popup in primo piano e lo chiude se viene chiusa la scena genitore
	            popUpStage.show(); 
	        } catch (NullPointerException | IOException e) {
	            System.out.println("Errore nel caricamento della schermata successiva! " + e.getMessage());
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
	}
	


