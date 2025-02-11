package sessione;

import dati.Utente;

public class SessioneGioco {

	//la SessioneGioco è unica e riferita a un giocatore alla volta 
    private static SessioneGioco instance; 
    private Utente utenteLoggato;

    private SessioneGioco() {}

    public static SessioneGioco getInstance() { //questo metodo permette di ottenere l'istanza della classe. Se non c'è un'istanza la crea
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
