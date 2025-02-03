package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PopUpPunteggioOutputController {

    @FXML
    private Button close;
    
    @FXML
    private Label fraseEsito;

    @FXML
    private Label frasePunteggio;
    
    @FXML
    private BorderPane sfondo;

    @FXML
    void closeButton(MouseEvent event) {
    	Stage stage = (Stage) close.getScene().getWindow(); 
        stage.close(); 
    }

    @FXML
    void colorChangeBasic(MouseEvent event) {
    	close.setStyle("");
    }

    @FXML
    void colorChangeRed(MouseEvent event) {
    	close.setStyle("-fx-background-color: red;");
    }
    
    void modificaMessaggio(int punteggioLocale) {
    	frasePunteggio.setText("Hai ottenuto un punteggio di: " + punteggioLocale + "/5");
    	if(punteggioLocale >= 3) {
    		sfondo.setStyle("-fx-background-color: #e7fae0;");
    		fraseEsito.setTextFill(javafx.scene.paint.Color.web("#399c51"));
    		fraseEsito.setText("Congratulazioni!\n Esercizio superato con successo.");
    		frasePunteggio.setTextFill(javafx.scene.paint.Color.web("#399c51"));
    		
    	}else {
    		sfondo.setStyle("-fx-background-color: #ffcccc");
    		fraseEsito.setTextFill(javafx.scene.paint.Color.web("red"));
    		fraseEsito.setText("Mi dispiace!\n Esercizio non superato.");
    		frasePunteggio.setTextFill(javafx.scene.paint.Color.web("red"));
    	}
    }

}