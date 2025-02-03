package domanda;

import java.util.ArrayList;

public class DomandaDefinizioni extends Domanda {
	private  ArrayList<String> opzioni;
	private String rispostaCorretta;

	public DomandaDefinizioni(String testoDomanda,  ArrayList<String> opzioni, String rispostaCorretta) {
		super(testoDomanda);
		this.opzioni = opzioni;
		this.rispostaCorretta = rispostaCorretta;
	}

	@Override
	public boolean verificaRisposta(String rispostaUtente) {
		return rispostaUtente.equals(this.rispostaCorretta);
	}

	public  ArrayList<String> getOpzioni() {
		return opzioni;
	}

	public String getRispostaCorretta() {
		return rispostaCorretta;
	}
    
}

