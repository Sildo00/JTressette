package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta un mazzo di 40 carte italiane per il gioco del Tressette.
 * Permette di creare il mazzo completo, mescolarlo, pescare singole carte o più carte,
 * e verificare lo stato delle carte rimanenti.
 */
public class Deck {

    private final List<Card> cards = new ArrayList<>();

    /**
     * Costruttore del mazzo.
     * Inizializza il mazzo completo richiamando il metodo {@link #reset()}.
     */
    public Deck() {
        reset();
    }

    /**
     * Ricrea il mazzo completo da 40 carte, combinando tutti i semi e tutti i valori.
     * Cancella eventuali carte presenti e ricostruisce il mazzo.
     */
    public final void reset() {
        cards.clear();
        for (Card.Seme seme : Card.Seme.values()) {
            for (Card.Valore valore : Card.Valore.values()) {
                cards.add(new Card(seme, valore));
            }
        }
    }

    /**
     * Mescola casualmente le carte presenti nel mazzo.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Pesca la prima carta disponibile dal mazzo.
     * @return La carta pescata.
     * @throws IllegalStateException se il mazzo è vuoto.
     */
    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Il mazzo è vuoto");
        }
        return cards.remove(0);
    }

    /**
     * Pesca un numero specificato di carte dal mazzo.
     * @param n Numero di carte da pescare.
     * @return Lista contenente le carte pescate nell'ordine di estrazione.
     * @throws IllegalArgumentException se il numero richiesto è negativo o superiore alle carte disponibili.
     */
    public List<Card> draw(int n) {
        if (n < 0 || n > cards.size()) {
            throw new IllegalArgumentException("Numero di carte da pescare non valido");
        }
        List<Card> picked = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            picked.add(draw());
        }
        return picked;
    }

    /**
     * Restituisce il numero di carte rimanenti nel mazzo.
     * @return Numero di carte presenti.
     */
    public int size() {
        return cards.size();
    }

    /**
     * Indica se il mazzo è vuoto.
     * @return true se non ci sono carte, false altrimenti.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}