package model;

import java.util.List;

/**
 * Rappresenta un generico giocatore di una partita.
 * Può essere umano o controllato dal computer.
 * Contiene attributi e comportamenti comuni a tutti i giocatori.
 */
public abstract class Player {

    protected String nome;
    protected List<Card> mano;
    private int punteggio;

    /**
     * Costruttore del giocatore.
     * @param nome Nome del giocatore.
     */
    public Player(String nome) {
        this.nome = nome;
        this.punteggio = 0;
    }

    /**
     * Restituisce il nome del giocatore.
     * @return Nome del giocatore.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce la mano del giocatore.
     * @return Lista di carte in mano.
     */
    public List<Card> getMano() {
        return mano;
    }

    /**
     * Imposta la mano del giocatore.
     * @param mano Lista di carte da assegnare.
     */
    public void setMano(List<Card> mano) {
        this.mano = mano;
    }

    /**
     * Restituisce il punteggio del giocatore.
     * @return Punteggio attuale.
     */
    public int getPunteggio() {
        return punteggio;
    }

    /**
     * Aggiunge punti al punteggio del giocatore.
     * @param punti Punti da aggiungere.
     */
    public void aggiungiPunti(int punti) {
        this.punteggio += punti;
    }

    /**
     * Reimposta il punteggio del giocatore a zero.
     */
    public void resetPunteggio() {
        this.punteggio = 0;
    }

    /**
     * Verifica se il giocatore ha in mano una determinata carta.
     * @param carta Carta da verificare.
     * @return true se la carta è presente, false altrimenti.
     */
    public boolean haCarta(Card carta) {
        return mano != null && mano.contains(carta);
    }

    /**
     * Aggiunge una carta alla mano del giocatore.
     * @param carta Carta da aggiungere.
     */
    public void aggiungiCarta(Card carta) {
        if (mano == null) {
            throw new IllegalStateException("La mano non è stata inizializzata");
        }
        mano.add(carta);
    }

    /**
     * Rimuove una carta dalla mano del giocatore.
     * @param carta Carta da rimuovere.
     */
    public void rimuoviCarta(Card carta) {
        if (mano == null || !mano.remove(carta)) {
            throw new IllegalStateException(nome + " non ha in mano: " + carta);
        }
    }

    /**
     * Restituisce la lista di carte giocabili in base al seme dominante.
     * @param semeDominante Seme da rispettare nel turno.
     * @return Lista di carte giocabili, oppure l'intera mano se non ha carte del seme.
     */
    public List<Card> getCarteGiocabili(Card.Seme semeDominante) {
        if (mano == null || mano.isEmpty()) {
            return List.of();
        }
        List<Card> giocabili = mano.stream()
                .filter(c -> c.getSeme() == semeDominante)
                .toList();
        return giocabili.isEmpty() ? mano : giocabili;
    }

    /**
     * Indica se il giocatore non ha più carte in mano.
     * @return true se la mano è vuota, false altrimenti.
     */
    public boolean manoVuota() {
        return mano == null || mano.isEmpty();
    }

    /**
     * Metodo astratto per giocare una carta in base alla logica del giocatore.
     * La rimozione della carta dalla mano è responsabilità del gestore della partita.
     * @param semeDominante Seme da rispettare nel turno.
     * @param carteSulTavolo Carte già giocate nel turno corrente.
     * @return Carta scelta per la giocata.
     */
    public abstract Card giocaCarta(Card.Seme semeDominante, List<Card> carteSulTavolo);
}
