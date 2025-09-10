package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import dati.Recensione;
import dati.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class RecensioniController {

    @FXML
    private Button close;

    @FXML
    private Button indietro;

    @FXML
    private Label successivo;

    @FXML
    private VBox vBoxEsterno;
    
    private ArrayList<Recensione> recensioni  = new ArrayList<>();

    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
    
    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow();  
        stage.close(); 
    }

    //la pagina recensioni può essere aperta sia da Home che da Gioca
    //se l'utente si trova in Home, vuol dire che ha già eseguito il login e esiste un utenteCorrente 
    //se l'utente si trova in Gioca, vuol dire che non esiste un utenteCorrente
    //in base a questo, cambia la paginaPrecedente
    @FXML
    void paginaPrecedente(MouseEvent event) {
    	String nomeFile = "";
    	if(utenteCorrente != null) {
    		nomeFile = "/application/Home.fxml";
    	} else { 
    		nomeFile = "/application/Gioca.fxml";
    	}
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource(nomeFile));

            Stage scenaCorrente = (Stage) indietro.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();
        } catch (NullPointerException | IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente! " +e.getMessage());
        }
    }

    //se esiste l'utenteCorrente viene data la possibilità di scrivere una recensione, altrimenti rimanda al login 
    //viene settata una label con un testo diverso a seconda della situazione 
    @FXML
    private void initialize() {
    	if(utenteCorrente != null) {
    		 successivo.setText("Scrivi la tua opinione");
    	} else { 
    		successivo.setText("Mettiti in gioco");
    	}
    	letturaDaFile(); 
    }
    
    //viene utilizzato lo stesso meccanismo di paginaPrecedente, ma cambia la paginaSuccessiva
    @FXML
    void finestraSuccessiva(MouseEvent event) {
    	String nomeFile = "";
    	if(utenteCorrente != null) {
    		nomeFile = "/application/ScriviRecensione.fxml";
    	} else { 
    		nomeFile = "/application/Login.fxml";
    	}
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource(nomeFile));

            Stage scenaCorrente = (Stage) successivo.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();
	        } catch (IOException e) {
	            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
	        }
    }

    //metodo che legge il file delle recensioni, creando un oggetto per ogni recensione lasciata dall'utente 
    void letturaDaFile() {
        recensioni = new ArrayList<>(); 
        try {
            Scanner scf = new Scanner(new File("tutteLeRecensioni.txt"));
            String commento = "";
            String utente = "";
            String stelline = "";
            
            while (scf.hasNextLine()) {
                String line = scf.nextLine().trim();

                if (line.startsWith("commento:")) {
                	commento = "";
                	while (scf.hasNextLine()) {
                        String commentoLine = scf.nextLine();
                        if (commentoLine.startsWith("////")) {
                            break;
                        }
                        commento += commentoLine + "\n"; 
                    }
                }
               
                if (line.startsWith("utente:")) {
                    utente = scf.nextLine(); 
                }
                
                if (line.startsWith("stelline:")) {
                    stelline = scf.nextLine();
                }
                
                if (line.equals("****")) { 
                    if (!commento.isEmpty() && !utente.isEmpty() && !stelline.isEmpty()) {
                        Recensione R = new Recensione(utente, stelline, commento); //creazione oggetto
                        recensioni.add(R); //viene aggiunto l'oggetto all'arrayList delle recensioni 
                        commento = "";
                        utente = "";
                        stelline = "";
                    }
                }
            }
          scf.close();
          aggiungiRecensione();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
	    }
    }
    
	//recupera una recensione dell'array e la aggiunge in maniera dinamica all'interfaccia grafica 
    //il metodo crea un vBox che contiene le varie parti di una recensione (nomeUtente, stelline e commento) 
    //i vBox delle singole recensioni vengono inserite all'interno di un altro vBox
    private void aggiungiRecensione() {
    	   for (Recensione r : recensioni) {
    	        VBox vBoxInterno = new VBox();
    	        vBoxInterno.setStyle("-fx-border-color: #77358f; -fx-padding: 10; -fx-background-color: white;");
    	        vBoxInterno.setMinWidth(900);   

    	        Label utenteLabel = new Label(r.getUtente());
    	        utenteLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    	        Label stelleLabel = new Label(r.getStelline());
    	        stelleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #FFD13B;");

    	        Label commentoLabel = new Label(r.getCommento());
    	        commentoLabel.setWrapText(true);
    	        commentoLabel.setStyle("-fx-font-size: 14px;");

    	        vBoxInterno.getChildren().addAll(utenteLabel, stelleLabel, commentoLabel);
	    	    vBoxEsterno.getChildren().add(vBoxInterno);
	    	}
	 }
}


