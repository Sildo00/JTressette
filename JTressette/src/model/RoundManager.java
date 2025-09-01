package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gestisce una singola presa all'interno di un round.
 * Tiene traccia delle carte giocate, del seme dominante e determina il vincitore della presa.
 */
public class RoundManager {

    private final List<Giocata> giocate = new ArrayList<>();
    private Card.Seme semeDominante;

    /**
     * Aggiunge una giocata alla presa corrente.
     * Se è la prima giocata, imposta il seme dominante.
     * @param player Giocatore che ha effettuato la giocata.
     * @param card Carta giocata.
     */
    public void aggiungiGiocata(Player player, Card card) {
        if (giocate.isEmpty()) {
            semeDominante = card.getSeme();
        }
        giocate.add(new Giocata(player, card));
    }

    /**
     * Restituisce il seme dominante della presa.
     * @return Seme dominante.
     */
    public Card.Seme getSemeDominante() {
        return semeDominante;
    }

    /**
     * Restituisce la lista delle giocate effettuate nella presa.
     * @return Lista di giocate.
     */
    public List<Giocata> getGiocate() {
        return new ArrayList<>(giocate);
    }

    /**
     * Determina il vincitore della presa in base al seme dominante e ai valori delle carte.
     * @return Giocatore vincitore, se presente.
     */
    public Optional<Player> determinaVincitore() {
        return giocate.stream()
                .filter(p -> p.carta().getSeme() == semeDominante)
                .max((p1, p2) -> Integer.compare(p1.carta().getRankValue(), p2.carta().getRankValue()))
                .map(Giocata::giocatore);
    }

    /**
     * Calcola i punti della presa in base alla strategia di punteggio.
     * @param scoring Strategia di calcolo punti.
     * @return Punti totali della presa.
     */
    public int calcolaPuntiPresa(ScoringStrategy scoring) {
        return giocate.stream()
                .mapToInt(p -> scoring.getCardPoints(p.carta()))
                .sum();
    }

    /**
     * Reimposta lo stato della presa per iniziare una nuova.
     */
    public void reset() {
        giocate.clear();
        semeDominante = null;
    }

    /**
     * Record che rappresenta una giocata composta da un giocatore e una carta.
     * @param giocatore Giocatore che ha effettuato la giocata.
     * @param carta Carta giocata.
     */
    public record Giocata(Player giocatore, Card carta) { }
}
