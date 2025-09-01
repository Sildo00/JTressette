package model;

import java.util.List;

/**
 * Rappresenta il giocatore umano che seleziona manualmente la carta da giocare.
 * La selezione avviene tramite interfaccia utente e viene validata prima della giocata.
 */
public class HumanPlayer extends Player {

    private Card cartaSelezionata;

    /**
     * Costruttore del giocatore umano.
     * @param nome Nome scelto dall'utente.
     */
    public HumanPlayer(String nome) {
        super(nome);
    }

    /**
     * Imposta la carta selezionata dall'utente.
     * La view deve garantire che la carta sia valida prima di chiamare questo metodo.
     * @param carta Carta selezionata.
     * @param semeDominante Seme da rispettare nel turno.
     */
    public void setCartaSelezionata(Card carta, Card.Seme semeDominante) {
        if (getCarteGiocabili(semeDominante).contains(carta)) {
            this.cartaSelezionata = carta;
        } else {
            throw new IllegalArgumentException("Carta non valida per questo turno");
        }
    }

    /**
     * Restituisce la carta attualmente selezionata dall'utente.
     * @return Carta selezionata.
     */
    public Card getCartaSelezionata() {
        return cartaSelezionata;
    }

    /**
     * Seleziona la carta da giocare (senza rimuoverla dalla mano).
     * @param semeDominante Seme da rispettare nel turno.
     * @param carteSulTavolo Carte già giocate nel turno corrente.
     * @return Carta selezionata, oppure null se nessuna selezione è presente.
     */
    @Override
    public Card giocaCarta(Card.Seme semeDominante, List<Card> carteSulTavolo) {
        return cartaSelezionata;
    }

    /**
     * Resetta la carta selezionata (da chiamare dopo che il gestore della partita ha registrato la giocata).
     */
    public void resetSelezione() {
        this.cartaSelezionata = null;
    }
}
