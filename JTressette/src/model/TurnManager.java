package model;

import java.util.List;

/**
 * Gestisce l'ordine di gioco e il giocatore di turno.
 * Permette di avanzare al giocatore successivo o impostare manualmente il turno.
 */
public class TurnManager {

    private final List<Player> players;
    private int currentIndex;

    /**
     * Costruttore del gestore dei turni.
     * @param players Lista dei giocatori.
     * @param startingPlayer Giocatore che inizia la partita.
     * @throws IllegalArgumentException se la lista è vuota o il giocatore iniziale non è presente.
     */
    public TurnManager(List<Player> players, Player startingPlayer) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("La lista dei giocatori non può essere vuota");
        }
        this.players = List.copyOf(players);
        int index = players.indexOf(startingPlayer);
        if (index == -1) {
            throw new IllegalArgumentException("Il giocatore iniziale non è presente nella lista");
        }
        this.currentIndex = index;
    }

    /**
     * Restituisce il giocatore di turno.
     * @return Giocatore corrente.
     */
    public Player getCurrentPlayer() {
        return players.get(currentIndex);
    }

    /**
     * Avanza al giocatore successivo in ordine ciclico.
     */
    public void advanceToNextPlayer() {
        currentIndex = (currentIndex + 1) % players.size();
    }

    /**
     * Imposta manualmente il giocatore di turno.
     * @param player Giocatore da impostare come corrente.
     * @throws IllegalArgumentException se il giocatore non è presente nella lista.
     */
    public void setCurrentPlayer(Player player) {
        int index = players.indexOf(player);
        if (index == -1) {
            throw new IllegalArgumentException("Il giocatore non è presente nella lista");
        }
        this.currentIndex = index;
    }
}
