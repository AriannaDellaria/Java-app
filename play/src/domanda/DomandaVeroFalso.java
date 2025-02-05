package domanda;

public class DomandaVeroFalso extends DomandaConCodice {
	private String rispostaCorretta;

	public DomandaVeroFalso(String codice, String testoDomanda, String rispostaCorretta ) {
		super(codice, testoDomanda);
		this.rispostaCorretta = rispostaCorretta;
	}

	@Override
	public boolean verificaRisposta(String rispostaUtente) {
		return rispostaUtente.equals(this.rispostaCorretta);
	}
	
	public String getRispostaCorretta() {
		return this.rispostaCorretta;
	}

}