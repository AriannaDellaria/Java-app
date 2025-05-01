package domanda;

public abstract class Domanda {
	private String testoDomanda; 
		
	public Domanda(String testoDomanda) { 
		this.testoDomanda = testoDomanda; 
	}
	
	public String getTestoDomanda() { 
		return this.testoDomanda; 
	}
	
    public abstract boolean verificaRisposta(String rispostaUtente);
}