package domanda;

import java.util.ArrayList;

public class DomandaMultipla extends DomandaConCodice {
	
	private  ArrayList<String>  opzioni;
	private String rispostaCorretta;

	public DomandaMultipla(String codice, String testoDomanda,  ArrayList<String> opzioni, String rispostaCorretta) {
		super(codice, testoDomanda);
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