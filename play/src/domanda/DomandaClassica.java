package domanda;

public class DomandaClassica extends Domanda {
	private String rispostaCorretta; 
	
	public DomandaClassica(String testoDomanda, String rispostaCorretta) {
        super(testoDomanda);
        this.rispostaCorretta = rispostaCorretta; 
    }
	
	@Override
	public boolean verificaRisposta(String rispostaUtente) {
		return rispostaUtente.equalsIgnoreCase(this.rispostaCorretta);
	}
	
	public String getRispostaCorretta() { 
		return this.rispostaCorretta; 
	}

}
