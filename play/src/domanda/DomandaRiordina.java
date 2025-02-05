package domanda;

import java.util.ArrayList;

public class DomandaRiordina extends Domanda {
	private String rispostaCorretta; 
	
	public DomandaRiordina(String testoDomanda, String rispostaCorretta) {
        super(testoDomanda);
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
