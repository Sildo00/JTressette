package model;

/**
 * Rappresenta una singola carta da gioco nel mazzo di Tressette.
 * Ogni carta ha un seme e un valore.
 */
public class Card {

    /**
     * Enumerazione dei semi disponibili nel mazzo.
     */
    public enum Seme {
        DENARI, COPPE, SPADE, BASTONI
    }

    /**
     * Enumerazione dei valori disponibili nel mazzo.
     * L'ordine riflette la gerarchia di presa nel gioco.
     */
    public enum Valore {
        DUE, QUATTRO, CINQUE, SEI, SETTE, FANTE, CAVALLO, RE, TRE, ASSO
    }

    private final Seme seme;
    private final Valore valore;

    /**
     * Costruttore della carta.
     * @param seme Seme della carta.
     * @param valore Valore della carta.
     */
    public Card(Seme seme, Valore valore) {
        this.seme = seme;
        this.valore = valore;
    }

    /**
     * Restituisce il seme della carta.
     * @return Seme.
     */
    public Seme getSeme() {
        return seme;
    }

    /**
     * Restituisce il valore della carta.
     * @return Valore.
     */
    public Valore getValore() {
        return valore;
    }

    /**
     * Restituisce il valore gerarchico della carta per determinare la presa.
     * Maggiore è il valore, più alta è la carta.
     * @return Valore numerico per confronto.
     */
    public int getRankValue() {
        return switch (valore) {
            case ASSO -> 10;
            case TRE -> 9;
            case RE -> 8;
            case CAVALLO -> 7;
            case FANTE -> 6;
            case SETTE -> 5;
            case SEI -> 4;
            case CINQUE -> 3;
            case QUATTRO -> 2;
            case DUE -> 1;
        };
    }

    /**
     * Rappresentazione testuale della carta.
     * @return Stringa descrittiva.
     */
    @Override
    public String toString() {
        return valore + " di " + seme;
    }

    /**
     * Confronta due carte in base a seme e valore.
     * @param obj Oggetto da confrontare.
     * @return true se seme e valore coincidono, false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card other)) return false;
        return this.seme == other.seme && this.valore == other.valore;
    }

    /**
     * Hash code coerente con equals basato su seme e valore.
     * @return Valore di hash.
     */
    @Override
    public int hashCode() {
        int result = seme != null ? seme.hashCode() : 0;
        result = 31 * result + (valore != null ? valore.hashCode() : 0);
        return result;
    }
}
