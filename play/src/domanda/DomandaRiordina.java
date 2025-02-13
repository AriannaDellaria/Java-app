package domanda;

import java.util.ArrayList;

public class DomandaRiordina {
	
	private  ArrayList<String>  codici;
	private String rispostaCorretta;
	
	public DomandaRiordina(ArrayList<String> codici, String rispostaCorretta) {
		this.codici = codici; 
		this.rispostaCorretta = rispostaCorretta;
	}

	public boolean verificaRisposta(String rispostaUtente) {
		return rispostaUtente.equals(this.rispostaCorretta);
	}

	public  ArrayList<String> getCodici() {
		return codici;
	}

	public String getRispostaCorretta() {
		return rispostaCorretta;
	}
}
