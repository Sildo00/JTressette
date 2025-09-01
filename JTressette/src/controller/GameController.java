package controller;

import model.*;
import utils.MatchObserver;
import utils.UserProfile;
import utils.UserProfileManager;
import view.GameView;
import view.MainMenuView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller principale che coordina il flusso di gioco tra Model e View.
 * Si registra come osservatore del MatchManager e gestisce:
 * - Avvio di nuove partite
 * - Gestione dei turni di gioco (bot e umano)
 * - Aggiornamento della vista in base agli eventi di gioco
 * - Salvataggio e aggiornamento delle statistiche del profilo utente
 */
public class GameController implements MatchObserver {

    private final GameView gameView;
    private final MainMenuView mainMenuView;
    private final UserProfileManager profileManager;
    private MatchManager matchManager;
    private UserProfile currentProfile;

    /**
     * Costruisce un nuovo GameController.
     * @param gameView Vista di gioco
     * @param mainMenuView Vista del menu principale
     * @param profileManager Gestore dei profili utente
     * @throws NullPointerException se uno dei parametri è null
     */
    public GameController(GameView gameView, MainMenuView mainMenuView, UserProfileManager profileManager) {
        this.gameView = Objects.requireNonNull(gameView);
        this.mainMenuView = Objects.requireNonNull(mainMenuView);
        this.profileManager = Objects.requireNonNull(profileManager);
        configuraMenuPrincipale();
    }

    /**
     * Configura le azioni dei pulsanti del menu principale
     * registrando le callback fornite dal controller.
     */
    private void configuraMenuPrincipale() {
        mainMenuView.setOnPlay(this::avviaNuovaPartita);
        mainMenuView.setOnSettings(gameView::mostraImpostazioni);
        mainMenuView.setOnExit(() -> System.exit(0));
    }

    /**
     * Avvia una nuova partita in base alle impostazioni selezionate nella vista.
     * Carica il profilo utente, crea i giocatori e le squadre (se modalità 2vs2),
     * istanzia il MatchManager con TressetteScoring e avvia il primo round.
     */
    private void avviaNuovaPartita() {
        currentProfile = profileManager.load(mainMenuView.getSelectedProfileName());

        ScoringStrategy scoring = new TressetteScoring();
        List<Player> players;
        Player startingPlayer;

        if (mainMenuView.isTwoVsTwo()) {
            players = creaGiocatori2vs2(scoring);
            startingPlayer = players.get(0);
            List<Team> teams = creaSquadre(players);
            matchManager = new MatchManager(players, startingPlayer, scoring, teams);
        } else {
            players = creaGiocatori1vs1(scoring);
            startingPlayer = players.get(0);
            matchManager = new MatchManager(players, startingPlayer, scoring);
            matchManager.enableDeck(new Deck());
        }

        matchManager.addObserver(this);
        gameView.impostaController(this);
        matchManager.startNewRound();
    }

    /**
     * Crea due giocatori (umano e bot) per la modalità 1vs1.
     * @param scoring Strategia di punteggio da assegnare al bot
     * @return Lista contenente i due giocatori
     */
    private List<Player> creaGiocatori1vs1(ScoringStrategy scoring) {
        Player umano = new HumanPlayer(currentProfile.getNome());
        Player bot = new BotPlayer("Bot", scoring);
        return List.of(umano, bot);
    }

    /**
     * Crea quattro giocatori (umano e tre bot) per la modalità 2vs2.
     * @param scoring Strategia di punteggio da assegnare ai bot
     * @return Lista contenente i quattro giocatori
     */
    private List<Player> creaGiocatori2vs2(ScoringStrategy scoring) {
        Player umano = new HumanPlayer(currentProfile.getNome());
        Player botAlleato = new BotPlayer("Bot Alleato", scoring);
        Player botAvversario1 = new BotPlayer("Bot Avv1", scoring);
        Player botAvversario2 = new BotPlayer("Bot Avv2", scoring);
        return List.of(umano, botAvversario1, botAlleato, botAvversario2);
    }

    /**
     * Crea due squadre per la modalità 2vs2.
     * @param players Lista dei giocatori
     * @return Lista contenente le due squadre
     */
    private List<Team> creaSquadre(List<Player> players) {
        Team squadra1 = new Team("Squadra 1", List.of(players.get(0), players.get(2)));
        Team squadra2 = new Team("Squadra 2", List.of(players.get(1), players.get(3)));
        return List.of(squadra1, squadra2);
    }

    /**
     * Gestisce la selezione di una carta da parte del giocatore umano.
     * @param carta Carta selezionata
     * @throws IllegalStateException se non esiste un giocatore umano nella partita
     */
    public void cartaSelezionataDalGiocatore(Card carta) {
        Player umano = matchManager.getPunteggiGiocatore().keySet().stream()
                .filter(p -> p instanceof HumanPlayer)
                .findFirst()
                .orElseThrow();
        matchManager.playCard(umano, carta);
    }

    @Override
    public void onTurnStart(Player currentPlayer) {
        if (currentPlayer instanceof BotPlayer bot) {
            Card scelta = bot.giocaCarta(
                    matchManager.getRoundManager().getSemeDominante(),
                    matchManager.getRoundManager().getGiocate().stream().map(RoundManager.Giocata::carta).toList()
            );
            matchManager.playCard(bot, scelta);
        } else {
            gameView.abilitaSelezioneCarte((HumanPlayer) currentPlayer);
        }
    }

    @Override
    public void onCardPlayed(Player player, Card card) {
        gameView.mostraCartaGiocata(player, card);
    }

    @Override
    public void onCardDrawn(Player player, Card card, boolean revealTemporaneo) {
        gameView.mostraCartaPescata(player, card, revealTemporaneo);
    }

    @Override
    public void onTrickEnd(Player winner, int points) {
        gameView.mostraFinePresa(winner, points);
    }

    @Override
    public void onScoreUpdateGiocatori(Map<Player, Integer> scores) {
        gameView.aggiornaPunteggiGiocatori(scores);
    }

    @Override
    public void onScoreUpdateSquadre(Map<Team, Integer> scores) {
        gameView.aggiornaPunteggiSquadre(scores);
    }

    @Override
    public void onRoundEnd() {
        gameView.mostraFineRound();
    }

    @Override
    public void onMatchEndGiocatore(Player winnerOrNullOnTie) {
        aggiornaStatisticheProfilo(winnerOrNullOnTie != null && winnerOrNullOnTie instanceof HumanPlayer);
        gameView.mostraFinePartitaGiocatore(winnerOrNullOnTie);
    }

    @Override
    public void onMatchEndSquadra(Team winnerOrNullOnTie) {
        boolean vittoria = winnerOrNullOnTie != null &&
                winnerOrNullOnTie.getMembers().stream().anyMatch(p -> p instanceof HumanPlayer);
        aggiornaStatisticheProfilo(vittoria);
        gameView.mostraFinePartitaSquadra(winnerOrNullOnTie);
    }

    /**
     * Aggiorna le statistiche del profilo utente corrente e salva i dati.
     * @param vittoria true se il giocatore umano ha vinto, false altrimenti
     */
    private void aggiornaStatisticheProfilo(boolean vittoria) {
        currentProfile.incrementaPartite();
        if (vittoria) {
            currentProfile.incrementaVittorie();
        }
        profileManager.save(currentProfile);
    }
}
