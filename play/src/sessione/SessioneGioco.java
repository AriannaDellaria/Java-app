package sessione;

import dati.Utente;

public class SessioneGioco {

	//la SessioneGioco è unica e riferita a un giocatore alla volta 
    private static SessioneGioco instance; 
    private Utente utenteLoggato;

    //il costruttore è privato per garantire che la classe abbia una sola istanza e impedire la creazione dell'oggetto da altre classi
    private SessioneGioco() {}
    
    //metodo che permette di ottenere l'istanza della classe. Se non c'è un'istanza la crea
    public static SessioneGioco getInstance() { 
        if (instance == null) {
            instance = new SessioneGioco();
        }
        return instance;
    }

    public void setUtenteLoggato(Utente utente) {
        this.utenteLoggato = utente;
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }
}
