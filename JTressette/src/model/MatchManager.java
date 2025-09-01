package model;

import utils.MatchObservable;
import utils.MatchObserver;

import java.util.*;

/**
 * Gestisce una partita di Tressette in modalità 1vs1 o 2vs2.
 * Coordina prese, punteggi, distribuzione iniziale, pescata 1vs1, condizioni di vittoria e spareggio.
 */
public class MatchManager implements MatchObservable {

    private final List<Player> players;
    private final List<Team> teams;
    private final boolean twoVsTwo;

    private final Map<Player, Integer> punteggiGiocatore = new LinkedHashMap<>();
    private final Map<Team, Integer> punteggiSquadra = new LinkedHashMap<>();

    private final List<MatchObserver> observers = new ArrayList<>();
    private final RoundManager roundManager = new RoundManager();
    private final TurnManager turnManager;
    private final ScoringStrategy scoring;
	
	private final Map<Player, Integer> puntiRoundGiocatore = new LinkedHashMap<>();
    private final Map<Team, Integer> puntiRoundSquadra = new LinkedHashMap<>();

    private Deck deck;
    private Player ultimoVincitorePresa;
	
    private boolean matchTerminato = false;
    private final int cartePerGiocatore = 10;

    /**
     * Costruttore per la modalità 1vs1.
     * @param players Lista dei due giocatori.
     * @param startingPlayer Giocatore di mano all'inizio della partita.
     * @param scoring Strategia di calcolo punti.
     */
    public MatchManager(List<Player> players, Player startingPlayer, ScoringStrategy scoring) {
        if (players == null || players.size() != 2) {
            throw new IllegalArgumentException("In 1vs1 devono esserci esattamente 2 giocatori");
        }
        this.players = List.copyOf(players);
        this.teams = null;
        this.twoVsTwo = false;
        this.scoring = Objects.requireNonNull(scoring, "Strategia di punteggio nulla");
        players.forEach(p -> {
            punteggiGiocatore.put(p, 0);
            puntiRoundGiocatore.put(p, 0);
        });  
        this.turnManager = new TurnManager(this.players, startingPlayer);
    }

    /**
     * Costruttore per la modalità 2vs2.
     * @param players Lista dei quattro giocatori.
     * @param startingPlayer Giocatore di mano all'inizio della partita.
     * @param scoring Strategia di calcolo punti.
     * @param teams Lista delle due squadre.
     */
    public MatchManager(List<Player> players, Player startingPlayer, ScoringStrategy scoring, List<Team> teams) {
        if (players == null || players.size() != 4) {
            throw new IllegalArgumentException("In 2vs2 devono esserci esattamente 4 giocatori");
        }
        if (teams == null || teams.size() != 2) {
            throw new IllegalArgumentException("Devono esserci esattamente due squadre");
        }
        this.players = List.copyOf(players);
        this.teams = List.copyOf(teams);
        this.twoVsTwo = true;
        this.scoring = Objects.requireNonNull(scoring, "Strategia di punteggio nulla");
        teams.forEach(t -> {
            punteggiSquadra.put(t, 0);
            puntiRoundSquadra.put(t, 0);
        });  
        this.turnManager = new TurnManager(this.players, startingPlayer);
    }

    /**
     * Inizializza il mazzo da riutilizzare per tutta la partita.
     * @param deck mazzo da usare e rimescolare a ogni round
     */
    public void enableDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Avvia un nuovo round distribuendo le carte iniziali e notificando il primo turno.
     */
    public void startNewRound() {
        ultimoVincitorePresa = null;
        roundManager.reset();
		if (deck == null) {
            deck = new Deck();
        } else {
            deck.reset();
        }
        deck.shuffle();
        players.forEach(p -> p.setMano(new ArrayList<>()));
        for (int i = 0; i < cartePerGiocatore; i++) {
            for (Player p : players) {
                p.aggiungiCarta(deck.draw());
            }
        }
        notifyTurnStart(turnManager.getCurrentPlayer());
    }

    /**
     * Gioca una carta per il giocatore di turno.
     * @param player Giocatore che gioca la carta.
     * @param card Carta giocata.
     */
    public void playCard(Player player, Card card) {
        if (matchTerminato) return;
        if (!Objects.equals(player, turnManager.getCurrentPlayer())) {
            throw new IllegalStateException("Non è il turno di " + player.getNome());
        }
        if (!player.haCarta(card)) {
            throw new IllegalStateException(player.getNome() + " non ha in mano: " + card);
        }
        if (roundManager.getSemeDominante() != null && !player.getCarteGiocabili(roundManager.getSemeDominante()).contains(card)) {
            throw new IllegalStateException("La carta " + card + " non è valida in questo turno");
        }
        player.rimuoviCarta(card);
        roundManager.aggiungiGiocata(player, card);
        notifyCardPlayed(player, card);
        if (roundManager.getGiocate().size() == players.size()) {
            chiudiPresa();
        } else {
            turnManager.advanceToNextPlayer();
            notifyTurnStart(turnManager.getCurrentPlayer());
        }
    }

    /**
     * Chiude la presa corrente, assegna i punti e determina il prossimo di mano.
     * Integra la pescata in 1vs1 a fine presa.
     */
    private void chiudiPresa() {
        Optional<Player> vincitoreOpt = roundManager.determinaVincitore();
        int puntiPresa = roundManager.calcolaPuntiPresa(scoring);
        vincitoreOpt.ifPresent(vincitore -> {
            ultimoVincitorePresa = vincitore;
            if (twoVsTwo) {
                Team squadra = trovaSquadraDi(vincitore);
                puntiRoundSquadra.put(squadra, puntiRoundSquadra.get(squadra) + puntiPresa);
            } else {
                puntiRoundGiocatore.put(vincitore, puntiRoundGiocatore.get(vincitore) + puntiPresa);
            }
            notifyTrickEnd(vincitore, puntiPresa);
            notifyScoreUpdate();
            turnManager.setCurrentPlayer(vincitore);
            if (!twoVsTwo && deck != null && !deck.isEmpty()) {
                eseguiPescata1vs1(vincitore);
            }
        });
        roundManager.reset();
        Player prossimo = turnManager.getCurrentPlayer();
        if (twoVsTwo) {
            if (players.stream().allMatch(Player::manoVuota)) {
                chiudiRound();
            } else {
                notifyTurnStart(prossimo);
            }
        } else {
            boolean mazzoVuoto = deck == null || deck.isEmpty();
            boolean maniVuote = players.stream().allMatch(Player::manoVuota);
            if (mazzoVuoto && maniVuote) {
                chiudiRound();
            } else {
                notifyTurnStart(prossimo);
            }
        }
    }

    /**
     * Esegue la pescata 1vs1 dopo una presa, partendo dal vincitore.
     * La carta del bot va rivelata temporaneamente alla GUI.
     * @param vincitore Vincitore della presa.
     */
    private void eseguiPescata1vs1(Player vincitore) {
        if (deck == null || deck.isEmpty()) return;
        Player avversario = players.get(0).equals(vincitore) ? players.get(1) : players.get(0);
        Player[] ordine = new Player[] { vincitore, avversario };
        for (Player p : ordine) {
            if (deck.isEmpty()) break;
            Card pescata = deck.draw();
            p.aggiungiCarta(pescata);
            boolean revealTemporaneo = (p instanceof BotPlayer);
            notifyCardDrawn(p, pescata, revealTemporaneo);
        }
    }

    /**
     * Chiude il round, calcola i punti ufficiali e aggiorna i punteggi totali.
     * Gestisce la condizione di spareggio e la fine della partita.
     */
    private void chiudiRound() {
    notifyRoundEnd();

    if (twoVsTwo) {
        for (Team t : teams) {
			int puntiUfficiali = puntiRoundSquadra.get(t) / 3;
			if ( t == trovaSquadraDi(ultimoVincitorePresa)) puntiUfficiali += 1;
            punteggiSquadra.put(t, punteggiSquadra.get(t) + puntiUfficiali);
            puntiRoundSquadra.put(t, 0);
        }
    } else {
        for (Player p : players) {
            int puntiUfficiali = puntiRoundGiocatore.get(p) / 3;
			if ( p == ultimoVincitorePresa) puntiUfficiali += 1;
            punteggiGiocatore.put(p, punteggiGiocatore.get(p) + puntiUfficiali);
            puntiRoundGiocatore.put(p, 0);
        }
    }

    if (haRaggiuntoSoglia31()) {
        if (twoVsTwo) {
            Team vincitore = trovaVincitoreSquadra();
            if (vincitore != null) {
                matchTerminato = true;
                notifyMatchEndSquadra(vincitore);
            }
        } else {
            Player vincitore = trovaVincitore();
            if (vincitore != null) {
                matchTerminato = true;
                notifyMatchEndGiocatore(vincitore);
            	}
            }
        }
    }

    /**
     * Verifica se una parte (giocatore o squadra) ha raggiunto la soglia di 31 punti.
     * @return True se la soglia è stata raggiunta.
     */
    private boolean haRaggiuntoSoglia31() {
        if (twoVsTwo) {
            return punteggiSquadra.values().stream().anyMatch(p -> p >= 31);
        } else {
            return punteggiGiocatore.values().stream().anyMatch(p -> p >= 31);
        }
    }

    /**
     * Restituisce il vincitore in modalità 1vs1 o null in caso di pareggio sul punteggio massimo.
     * @return Giocatore vincitore o null.
     */
    private Player trovaVincitore() {
        int max = punteggiGiocatore.values().stream().max().orElse(0);
        List<Player> top = punteggiGiocatore.entrySet().stream()
                .filter(e -> e.getValue().intValue() == max)
                .map(Map.Entry::getKey)
                .toList();
        return top.size() == 1 ? top.get(0) : null;
    }

    /**
     * Restituisce il vincitore in modalità 2vs2 o null in caso di pareggio sul punteggio massimo.
     * @return Squadra vincitrice o null.
     */
    private Team trovaVincitoreSquadra() {
        int max = punteggiSquadra.values().stream().max().orElse(0);
        List<Team> top = punteggiSquadra.entrySet().stream()
                .filter(e -> e.getValue().intValue() == max)
                .map(Map.Entry::getKey)
                .toList();
        return top.size() == 1 ? top.get(0) : null;
    }

    /**
     * Trova la squadra a cui appartiene un giocatore.
     * @param player Giocatore da cercare.
     * @return Squadra del giocatore.
     */
    private Team trovaSquadraDi(Player player) {
        return teams.stream()
                .filter(t -> t.getMembers().contains(player))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Il giocatore non appartiene a nessuna squadra"));
    }

    /**
     * Registra un osservatore per ricevere notifiche sugli eventi della partita.
     * @param observer Osservatore da aggiungere.
     */
    @Override
    public void addObserver(MatchObserver observer) {
        observers.add(observer);
    }

    /**
     * Rimuove un osservatore registrato.
     * @param observer Osservatore da rimuovere.
     */
    @Override
    public void removeObserver(MatchObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica l'inizio del turno al giocatore corrente.
     * @param currentPlayer Giocatore di turno.
     */
    private void notifyTurnStart(Player currentPlayer) {
        observers.forEach(o -> o.onTurnStart(currentPlayer));
    }

    /**
     * Notifica che un giocatore ha giocato una carta.
     * @param player Giocatore che ha giocato.
     * @param card Carta giocata.
     */
    private void notifyCardPlayed(Player player, Card card) {
        observers.forEach(o -> o.onCardPlayed(player, card));
    }

    /**
     * Notifica che un giocatore ha pescato una carta.
     * @param player Giocatore che ha pescato.
     * @param card Carta pescata.
     * @param revealTemporaneo Indica se la carta va rivelata in GUI.
     */
    private void notifyCardDrawn(Player player, Card card, boolean revealTemporaneo) {
        observers.forEach(o -> o.onCardDrawn(player, card, revealTemporaneo));
    }

    /**
     * Notifica la fine di una presa.
     * @param winner Giocatore vincitore della presa.
     * @param punti Punti ottenuti nella presa.
     */
    private void notifyTrickEnd(Player winner, int punti) {
        observers.forEach(o -> o.onTrickEnd(winner, punti));
    }

    /**
     * Notifica l'aggiornamento dei punteggi.
     */
    private void notifyScoreUpdate() {
        if (twoVsTwo) {
            observers.forEach(o -> o.onScoreUpdateSquadre(Map.copyOf(punteggiSquadra)));
        } else {
            observers.forEach(o -> o.onScoreUpdateGiocatori(Map.copyOf(punteggiGiocatore)));
        }
    }

    /**
     * Notifica la fine del round.
     */
    private void notifyRoundEnd() {
        observers.forEach(MatchObserver::onRoundEnd);
    }

    /**
     * Notifica la fine della partita (1vs1).
     * @param winner Vincitore o null in caso di pareggio.
     */
    private void notifyMatchEndGiocatore(Player winner) {
        observers.forEach(o -> o.onMatchEndGiocatore(winner));
    }

    /**
     * Notifica la fine della partita (2vs2).
     * @param winner Vincitore o null in caso di pareggio.
     */
    private void notifyMatchEndSquadra(Team winner) {
        observers.forEach(o -> o.onMatchEndSquadra(winner));
    }

    /**
     * Indica se la modalità di gioco è 2vs2.
     * @return True se la modalità è 2vs2.
     */
    public boolean isTwoVsTwo() {
        return twoVsTwo;
    }

    /**
     * Restituisce i punteggi dei giocatori in modalità 1vs1.
     * @return Mappa dei punteggi per giocatore.
     */
    public Map<Player, Integer> getPunteggiGiocatore() {
        return Collections.unmodifiableMap(punteggiGiocatore);
    }

    /**
     * Restituisce i punteggi delle squadre in modalità 2vs2.
     * @return Mappa dei punteggi per squadra.
     */
    public Map<Team, Integer> getPunteggiSquadra() {
        return Collections.unmodifiableMap(punteggiSquadra);
    }

    /**
     * Indica se la partita è terminata.
     * @return True se la partita è terminata.
     */
    public boolean isMatchTerminato() {
        return matchTerminato;
    }

    /**
     * Restituisce il gestore della presa corrente.
     * @return RoundManager corrente.
     */
    public RoundManager getRoundManager() {
        return roundManager;
    }
}
