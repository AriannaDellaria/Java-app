package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import dati.Utente;
import domanda.DomandaRiordina;
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

	public class RiordinaMedioController {

	@FXML
    private Button close;

    @FXML
    private Label codice1, codice2, codice3, codice4, codice5, codice6, codice7, codice8, codice9, codice10, codice11, codice12, codice13, codice14, codice15, codice16, codice17, codice18, codice19, codice20, utente, timer; 
   
    @FXML
    private Button indietro, terminaCorreggi;

    @FXML
    private TextField text1, text2, text3, text4, text5; 

    
    private ArrayList<DomandaRiordina> domandeRiordina;  
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato(); 
	ArrayList<ArrayList<Label>> blocchiCodici = new ArrayList<>(); 
    ArrayList<TextField> risposte = new ArrayList<>();
    
    private int tempoRestante = 120;  //2 minuti per eseguire l'esercizio
    
    
    @FXML
    public void initialize() {
    	risposte.add(text1); 
    	risposte.add(text2); 
    	risposte.add(text3); 
    	risposte.add(text4);
    	risposte.add(text5);
    	
    	ArrayList<Label> blocco1 = new ArrayList<>();
    	blocco1.add(codice1); 
    	blocco1.add(codice2); 
    	blocco1.add(codice3); 
    	blocco1.add(codice4); 
    	
    	ArrayList<Label> blocco2 = new ArrayList<>();
    	blocco2.add(codice5); 
    	blocco2.add(codice6); 
    	blocco2.add(codice7); 
    	blocco2.add(codice8); 
    	
    	ArrayList<Label> blocco3 = new ArrayList<>();
    	blocco3.add(codice9); 
    	blocco3.add(codice10); 
    	blocco3.add(codice11); 
    	blocco3.add(codice12);
    	
    	ArrayList<Label> blocco4 = new ArrayList<>();
    	blocco4.add(codice13); 
    	blocco4.add(codice14); 
    	blocco4.add(codice15); 
    	blocco4.add(codice16);
    	
    	ArrayList<Label> blocco5 = new ArrayList<>();
    	blocco5.add(codice17); 
    	blocco5.add(codice18); 
    	blocco5.add(codice19); 
    	blocco5.add(codice20);
    	
    	blocchiCodici.add(blocco1); 
    	blocchiCodici.add(blocco2); 
    	blocchiCodici.add(blocco3); 
    	blocchiCodici.add(blocco4); 
    	blocchiCodici.add(blocco5); 
    	
    	
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
    void colorChangeYellow(MouseEvent event) {
    	terminaCorreggi.setStyle("-fx-background-color: #fede77;-fx-border-color: #f9943b"); 
    }
    
    @FXML
    void colorChangeBasic(MouseEvent event) {
    	terminaCorreggi.setStyle("-fx-background-color: white; -fx-border-color: #f9943b; -fx-border-width: 2px;");
    	
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
        domandeRiordina= new ArrayList<>(); //crea una lista di domande (di tipo DomandaMultipla)
        try {
            Scanner scf = new Scanner(new File("DomandeRiordinaMedio.txt"));
            
            String codice = "";
            ArrayList<String> codici = new ArrayList<>();
            String risposta = "";

            while (scf.hasNextLine()) {
                String line = scf.nextLine().trim(); 
                	
                if(line.startsWith("codice:")) { 
                	while(scf.hasNextLine()) { 
                		String codiceLine = scf.nextLine();
	                		if(codiceLine.equals("++++")) { 
	                			break;
	                		}
	                		else if(!codiceLine.equals("////")) {
                				codice += codiceLine + "\n";
                			}
                			else { 
                				codici.add(codice); 
                				codice = ""; 
                				continue;
                			}
                	}
                }
                
                if(line.startsWith("risposta:")) {
                	risposta = scf.nextLine().trim(); 
                }
                
                if(line.startsWith("****")) { 
                	if (!codici.isEmpty()) {
                        DomandaRiordina D = new DomandaRiordina(codici, risposta);
                        domandeRiordina.add(D);

                         
                        codici = new ArrayList<>();
                        risposta = "";
                    }
                }
                
            }
            scf.close(); 
            aggiornaLabel();
        }
            
        catch (IOException e) {
            System.out.println("Errore nella lettura del file! " + e.getMessage());
        }
    }
    
   

        
    void aggiornaLabel() {
        for (int i = 0; i < domandeRiordina.size() && i < blocchiCodici.size(); i++) {
            ArrayList<Label> c = blocchiCodici.get(i);
            ArrayList<String> c1 = domandeRiordina.get(i).getCodici();
            
            for (int j = 0; j < c.size() && j < c1.size(); j++) {
                c.get(j).setText(c1.get(j));
            }
        }
    }
    
    
    @FXML
    void salvaPunteggio(MouseEvent event) {
    	int punteggioLocale = 0; 
        for (int i = 0; i < domandeRiordina.size(); i++) {
            DomandaRiordina domanda = domandeRiordina.get(i);
            TextField text = getTextField(i);
            String rispostaUtente = text.getText().trim(); 
            
            if (rispostaUtente != null && domanda.verificaRisposta(rispostaUtente)) {
                punteggioLocale++; //se la risposta è corretta incrementa il punteggio locale
            }
        }
        
        if(utenteCorrente != null && utenteCorrente.getPg3() == 0.33 && punteggioLocale >= 3) {
    		utenteCorrente.setPg3(0.66); //punteggio globale incrementato solo se viene superato l'esercizio
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
   
    private TextField getTextField(int indice) {
        if (indice < risposte.size()) {
            return risposte.get(indice);
        }
        return null;
    }
}