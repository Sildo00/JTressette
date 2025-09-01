package view;

import controller.GameController;
import model.Card;
import model.HumanPlayer;
import model.Player;
import model.Team;

import java.util.Map;

/**
 * Definisce le operazioni che la vista deve esporre per interagire col controller.
 * Gestisce aggiornamenti grafici, input dell'utente e feedback sonori/visivi.
 */
public interface GameView {

    /**
     * Collega il controller alla vista.
     * @param controller controller principale
     */
    void impostaController(GameController controller);

    /**
     * Mostra o gestisce la schermata impostazioni.
     */
    void mostraImpostazioni();

    /**
     * Abilita la selezione delle carte per il giocatore umano di turno.
     * @param giocatore giocatore umano corrente
     */
    void abilitaSelezioneCarte(HumanPlayer giocatore);

    /**
     * Visualizza una carta giocata sul tavolo.
     * @param player autore della giocata
     * @param card carta giocata
     */
    void mostraCartaGiocata(Player player, Card card);

    /**
     * Visualizza una carta pescata. In 1vs1 la carta del bot va sempre rivelata.
     * @param player autore della pescata
     * @param card carta pescata
     * @param revealTemporaneo se la carta va rivelata temporaneamente
     */
    void mostraCartaPescata(Player player, Card card, boolean revealTemporaneo);

    /**
     * Mostra la fine di una presa con vincitore e punti ottenuti.
     * @param winner vincitore della presa
     * @param points punti raccolti nella presa
     */
    void mostraFinePresa(Player winner, int points);

    /**
     * Aggiorna i punteggi in modalità 1vs1.
     * @param scores mappa giocatore → punteggio
     */
    void aggiornaPunteggiGiocatori(Map<Player, Integer> scores);

    /**
     * Aggiorna i punteggi in modalità 2vs2.
     * @param scores mappa squadra → punteggio
     */
    void aggiornaPunteggiSquadre(Map<Team, Integer> scores);

    /**
     * Mostra la fine del round.
     */
    void mostraFineRound();

    /**
     * Mostra la fine partita in modalità 1vs1.
     * @param winner vincitore o null in caso di pareggio
     */
    void mostraFinePartitaGiocatore(Player winner);

    /**
     * Mostra la fine partita in modalità 2vs2.
     * @param winner vincitore o null in caso di pareggio
     */
    void mostraFinePartitaSquadra(Team winner);
}
