package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import dati.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sessione.SessioneGioco;

public class ScriviRecensioneController {

    @FXML
    private Button indietro, invio, close;

    @FXML
    private TextArea recensione;

    @FXML
    private ComboBox<String> stelline;

    @FXML
    private Label utente, erroreStelline;
    
    SessioneGioco sessioneGioco = SessioneGioco.getInstance();
    Utente utenteCorrente = sessioneGioco.getUtenteLoggato();
    
    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow();  
        stage.close(); 
    }
 
    @FXML
    void colorChangeGreen(MouseEvent event) {
    	invio.setStyle("-fx-background-color: #A2D8A3;-fx-border-color: #2AAA4A"); 
    }
    
    @FXML
    void colorChangeBasic(MouseEvent event) {
    	invio.setStyle("-fx-background-color: white; -fx-border-color: #2AAA4A; -fx-border-width: 2px;");  	
    }

    @FXML
    void paginaPrecedente(MouseEvent event) {
    	try {
            Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));

            Stage scenaCorrente = (Stage) indietro.getScene().getWindow();

            Scene vecchiaScena = new Scene(scenaPrecedente);
            scenaCorrente.setScene(vecchiaScena);
            scenaCorrente.show();
        } catch (IOException e) {
            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
        }
    }
    
    @FXML
    private void initialize() {
    	utente.setText(utenteCorrente.getUsername() + ", cosa ne pensi dell'applicazione?");
    	stelline.getItems().addAll("★★★★★", "★★★★", "★★★", "★★", "★"); //getItems -> metodo per inserire gli elementi nel menù a tendina 
    }
    
    //salva la recensione dell'utente nel file di testo
    //la recensione viene inserita solo se l'utente compila il campo relativo alle stelline 
    @FXML
    void inviaRecensione(MouseEvent event) {
    	File file = new File("tutteLeRecensioni.txt");
    	if(stelline.getValue() != null) { 
    		 try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) { 
                 writer.println();
                 writer.println("utente:");
                 writer.write(utenteCorrente.getUsername());
                 writer.println("\n" + "stelline:"); 
                 writer.write(stelline.getValue());
                 writer.println("\n" + "commento:"); 
                 writer.write(recensione.getText());
                 writer.println("\n"  + "////");
                 writer.print("****");
      	     } catch(IOException e) {
             		System.out.println("Errore nel salvataggio " + e.getMessage());
             }
    		 //una volta salvata la recensione l'utente va alla home 
    		 try {
    			 Parent scenaPrecedente = FXMLLoader.load(getClass().getResource("/application/Home.fxml"));

    			 Stage scenaCorrente = (Stage) invio.getScene().getWindow();

    			 Scene vecchiaScena = new Scene(scenaPrecedente);
    			 scenaCorrente.setScene(vecchiaScena);
    			 scenaCorrente.show();
    		 } catch (IOException e) {
	            System.out.println("Errore nel caricamento della schermata precedente! " + e.getMessage());
    		 }
    	} else { 
    		 erroreStelline.setVisible(true); 
    	}
    }
}
