package sessione;

import dati.Utente;

public class SessioneGioco {

    private static SessioneGioco instance; // Unica istanza della classe
    private Utente utenteLoggato; // Utente loggato

    // Costruttore privato per impedire istanze multiple
    private SessioneGioco() {}

    // Metodo statico per ottenere l'istanza unica
    public static SessioneGioco getInstance() {
        if (instance == null) {
            instance = new SessioneGioco();
        }
        return instance;
    }

    // Getter e setter per l'utente loggato
    public void setUtenteLoggato(Utente utente) {
        this.utenteLoggato = utente;
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }
}
