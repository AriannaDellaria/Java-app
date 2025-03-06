package dati;

public class Recensione {

	private String utente; 
	private String stelline; 
	private String commento;
	
	public Recensione(String utente, String stelline, String commento) { 
		this.utente = utente; 
		this.stelline = stelline;
		this.commento = commento; 
	}
	
	public String getUtente() {
		return utente;
	}
	public String getStelline() {
		return stelline;
	}
	public String getCommento() {
		return commento;
	}
	
}
