package model;

import java.util.List;

/**
 * Rappresenta un giocatore controllato dal computer.
 * Utilizza una logica automatica per determinare la carta da giocare.
 */
public class BotPlayer extends Player {

    private final ScoringStrategy scoring;

    /**
     * Costruttore del giocatore bot.
     * @param nome Nome del bot.
     * @param scoring Strategia di punteggio da utilizzare.
     */
    public BotPlayer(String nome, ScoringStrategy scoring) {
        super(nome);
        this.scoring = scoring;
    }

    /**
     * Logica automatica per selezionare la carta da giocare.
     * @param semeDominante Seme da rispettare nel turno.
     * @param carteSulTavolo Carte già giocate nel turno corrente.
     * @return Carta scelta dal bot (la rimozione dalla mano è centralizzata nel gestore partita).
     */
    @Override
    public Card giocaCarta(Card.Seme semeDominante, List<Card> carteSulTavolo) {
        List<Card> giocabili = getCarteGiocabili(semeDominante);

        int puntiSulTavolo = carteSulTavolo.stream()
                .mapToInt(scoring::getCardPoints)
                .sum();

        Card cartaVincente = carteSulTavolo.stream()
                .filter(c -> c.getSeme() == semeDominante)
                .max((a, b) -> Integer.compare(a.getRankValue(), b.getRankValue()))
                .orElse(null);

        for (Card c : giocabili) {
            boolean puoVincere = cartaVincente == null || c.getRankValue() > cartaVincente.getRankValue();
            if (c.getSeme() == semeDominante && puoVincere && puntiSulTavolo > 0.0) {
                return c;
            }
        }

        return giocabili.stream()
                .min((a, b) -> Integer.compare(scoring.getCardPoints(a), scoring.getCardPoints(b)))
                .orElse(giocabili.get(0));
    }
}
