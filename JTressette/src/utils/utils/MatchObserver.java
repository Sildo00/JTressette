package utils;

import model.Card;
import model.Player;
import model.Team;

import java.util.Map;

/**
 * Interfaccia che la View o il Controller implementano per ricevere notifiche dal Model sugli eventi principali della partita.
 */
public interface MatchObserver {

    void onTurnStart(Player currentPlayer);

    void onCardPlayed(Player player, Card card);

    void onCardDrawn(Player player, Card card, boolean revealTemporaneo);

    void onTrickEnd(Player winner, int points);

    void onScoreUpdateGiocatori(Map<Player, Integer> scores);

    void onScoreUpdateSquadre(Map<Team, Integer> scores);

    void onRoundEnd();

    void onMatchEndGiocatore(Player winnerOrNullOnTie);

    void onMatchEndSquadra(Team winnerOrNullOnTie);
}
