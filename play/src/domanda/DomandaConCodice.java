package domanda;

public abstract class DomandaConCodice extends Domanda {
    private String codice;

    public DomandaConCodice(String codice, String testoDomanda) {
        super(testoDomanda);
        this.codice = codice;
    }

	public String getCodice() {
		return codice;
	}
}