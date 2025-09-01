package model;

/**
 * Implementazione della strategia di calcolo punteggi per il Tressette.
 * Assegna 3 punti all'asso, 1 punto a re, cavallo, fante, due e tre, 0 alle altre carte
 * In questo modo viene semplificato il calcolo del punteggio
 */
public class TressetteScoring implements ScoringStrategy {

    @Override
    public int getCardPoints(Card card) {
        return switch (card.getValore()) {
            case ASSO -> 3;
            case RE, CAVALLO, FANTE, DUE, TRE -> 1;
            default -> 0;
        };
    }
}
