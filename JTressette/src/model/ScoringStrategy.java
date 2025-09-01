package model;

/**
 * Strategia per il calcolo del valore in punti di una carta.
 * Permette di sostituire/estendere facilmente il sistema di punteggio.
 */
public interface ScoringStrategy {
    int getCardPoints(Card card);
}
