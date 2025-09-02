package view;

import model.Card;
import model.Player;
import model.Team;

import java.util.Map;

/**
 * Interfaccia della vista di gioco.
 * Definisce i metodi che il controller può invocare per aggiornare la UI.
 */
public interface GameView {

    /**
     * Collega il controller alla vista.
     */
    void impostaController(controller.GameController controller);

    /**
     * Mostra la carta giocata da un giocatore.
     */
    void mostraCartaGiocata(Player player, Card card);

    /**
     * Mostra la carta pescata da un giocatore.
     */
    void mostraCartaPescata(Player player, Card card, boolean revealTemporaneo);

    /**
     * Mostra la fine di una presa.
     */
    void mostraFinePresa(Player winner, int points);

    /**
     * Aggiorna i punteggi in modalità 1vs1.
     */
    void aggiornaPunteggiGiocatori(Map<Player, Integer> scores);

    /**
     * Aggiorna i punteggi in modalità 2vs2.
     */
    void aggiornaPunteggiSquadre(Map<Team, Integer> scores);

    /**
     * Mostra la fine di un round.
     */
    void mostraFineRound();

    /**
     * Mostra la fine della partita in modalità 1vs1.
     */
    void mostraFinePartitaGiocatore(Player winnerOrNullOnTie);

    /**
     * Mostra la fine della partita in modalità 2vs2.
     */
    void mostraFinePartitaSquadra(Team winnerOrNullOnTie);

    /**
     * Abilita la selezione delle carte per il giocatore umano.
     */
    void abilitaSelezioneCarte(Player humanPlayer);

    /**
     * Mostra la schermata impostazioni.
     */
    void mostraImpostazioni();

    /**
     * Imposta le posizioni dei giocatori sul tavolo.
     */
    void impostaPosizioniGiocatori(Map<Player, Position> mappaPosizioni);
}
