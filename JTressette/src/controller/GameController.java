package controller;

import model.*;
import utils.AudioManager;
import utils.MatchObserver;
import utils.UserProfile;
import utils.UserProfileManager;
import view.GameView;
import view.MainMenuView;
import view.Position;
import view.SwingGameView;

import javax.swing.*;
import java.util.*;

/**
 * Controller principale che gestisce il flusso tra menu e partita.
 * Coordina creazione giocatori, posizionamento, avvio e gestione eventi di gioco.
 */
public class GameController implements MatchObserver {

    private final GameView gameView;
    private final MainMenuView mainMenuView;
    private final UserProfileManager profileManager;

    private MatchManager matchManager;
    private UserProfile currentProfile;

    /**
     * Inizializza il controller e collega le viste.
     */
    public GameController(GameView gameView, MainMenuView mainMenuView, UserProfileManager profileManager) {
        this.gameView = Objects.requireNonNull(gameView);
        this.mainMenuView = Objects.requireNonNull(mainMenuView);
        this.profileManager = Objects.requireNonNull(profileManager);
        this.gameView.impostaController(this);
        configuraMenuPrincipale();
    }

    /**
     * Registra le azioni del menu principale.
     */
    private void configuraMenuPrincipale() {
        mainMenuView.setOnPlay(this::avviaNuovaPartita);
        mainMenuView.setOnSettings(this::mostraImpostazioni);
        mainMenuView.setOnExit(() -> System.exit(0));
    }

    /**
     * Avvia una nuova partita e imposta posizioni e squadre.
     */
    private void avviaNuovaPartita() {
        currentProfile = profileManager.load(mainMenuView.getSelectedProfileName());

        ScoringStrategy scoring = new TressetteScoring();
        List<Player> players;
        Player startingPlayer;
        List<Team> teams = null;

        if (mainMenuView.isTwoVsTwo()) {
            players = creaGiocatori2vs2(scoring);
            startingPlayer = scegliGiocatoreCasuale(players);
            teams = creaSquadre2vs2(players);
        } else {
            players = creaGiocatori1vs1(scoring);
            startingPlayer = scegliGiocatoreCasuale(players);
        }

        if (teams != null) {
            matchManager = new MatchManager(players, startingPlayer, scoring, teams);
        } else {
            matchManager = new MatchManager(players, startingPlayer, scoring);
            matchManager.enableDeck(new Deck());
        }

        matchManager.addObserver(this);
        gameView.impostaPosizioniGiocatori(calcolaPosizioni(players, mainMenuView.isTwoVsTwo()));

        AudioManager.getInstance().playResource("/audio/start.wav");

        if (gameView instanceof SwingGameView sgv) {
            SwingUtilities.invokeLater(sgv::mostraGioco);
        }

        matchManager.startNewRound();
    }

    /**
     * Mostra la schermata impostazioni.
     */
    private void mostraImpostazioni() {
        gameView.mostraImpostazioni();
    }

    /**
     * Crea i giocatori per la modalità 1vs1.
     */
    private List<Player> creaGiocatori1vs1(ScoringStrategy scoring) {
        Player umano = new HumanPlayer(currentProfile.getNome());
        Player bot = new BotPlayer("Bot", scoring);
        return List.of(umano, bot);
    }

    /**
     * Crea i giocatori per la modalità 2vs2.
     */
    private List<Player> creaGiocatori2vs2(ScoringStrategy scoring) {
        Player umano = new HumanPlayer(currentProfile.getNome());
        Player bot1 = new BotPlayer("Bot 1", scoring);
        Player bot2 = new BotPlayer("Bot 2", scoring);
        Player bot3 = new BotPlayer("Bot 3", scoring);
        return List.of(umano, bot1, bot2, bot3);
    }

    /**
     * Crea le squadre per la modalità 2vs2 con umano e bot a NORTH nella stessa squadra.
     */
    private List<Team> creaSquadre2vs2(List<Player> players) {
        Map<Player, Position> posizioni = calcolaPosizioni(players, true);
        Player umano = players.stream().filter(p -> p instanceof HumanPlayer).findFirst().orElseThrow();
        Player alleato = posizioni.entrySet().stream()
                .filter(e -> e.getValue() == Position.NORTH)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
        List<Player> squadraUmano = List.of(umano, alleato);
        List<Player> squadraAvversaria = new ArrayList<>(players);
        squadraAvversaria.removeAll(squadraUmano);
        return List.of(new Team("Squadra 1", squadraUmano), new Team("Squadra 2", squadraAvversaria));
    }

    /**
     * Seleziona casualmente il primo giocatore di mano.
     */
    private Player scegliGiocatoreCasuale(List<Player> players) {
        return players.get(new Random().nextInt(players.size()));
    }

    /**
     * Calcola le posizioni dei giocatori.
     */
    private Map<Player, Position> calcolaPosizioni(List<Player> players, boolean isTwoVsTwo) {
        Map<Player, Position> posizioni = new HashMap<>();
        Player umano = players.stream().filter(p -> p instanceof HumanPlayer).findFirst().orElseThrow();
        posizioni.put(umano, Position.SOUTH);

        List<Player> altri = new ArrayList<>(players);
        altri.remove(umano);

        if (isTwoVsTwo) {
            Position[] ordine = {Position.WEST, Position.NORTH, Position.EAST};
            for (int i = 0; i < altri.size(); i++) {
                posizioni.put(altri.get(i), ordine[i]);
            }
        } else {
            posizioni.put(altri.get(0), Position.NORTH);
        }
        return posizioni;
    }

    /**
     * Gestisce la carta selezionata dall'umano.
     */
    public void cartaSelezionataDalGiocatore(Card carta) {
        Player umano = matchManager.getPunteggiGiocatore().keySet().stream()
                .filter(p -> p instanceof HumanPlayer)
                .findFirst()
                .orElseThrow();
        matchManager.playCard(umano, carta);
    }

    /**
     * Gestisce l'uscita dalla partita.
     */
    public void handleExitPartita() {
        if (currentProfile != null) {
            currentProfile.incrementaPartite();
            profileManager.save(currentProfile);
        }
        AudioManager.getInstance().playResource("/audio/exit.wav");
        if (gameView instanceof SwingGameView sgv) {
            SwingUtilities.invokeLater(sgv::mostraMenuPrincipale);
        }
        matchManager = null;
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
            gameView.abilitaSelezioneCarte(currentPlayer);
        }
    }

    @Override
    public void onCardPlayed(Player player, Card card) {
        AudioManager.getInstance().playResource("/audio/card_play.wav");
        gameView.mostraCartaGiocata(player, card);
    }

    @Override
    public void onCardDrawn(Player player, Card card, boolean revealTemporaneo) {
        AudioManager.getInstance().playResource("/audio/card_draw.wav");
        gameView.mostraCartaPescata(player, card, revealTemporaneo);
    }

    @Override
    public void onTrickEnd(Player winner, int points) {
        AudioManager.getInstance().playResource("/audio/trick_win.wav");
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
        AudioManager.getInstance().playResource("/audio/round_end.wav");
        gameView.mostraFineRound();
    }

    @Override
    public void onMatchEndGiocatore(Player winnerOrNullOnTie) {
        boolean vittoria = winnerOrNullOnTie != null && winnerOrNullOnTie instanceof HumanPlayer;
        aggiornaStatisticheProfilo(vittoria);
        AudioManager.getInstance().playResource("/audio/game_end.wav");
        gameView.mostraFinePartitaGiocatore(winnerOrNullOnTie);
    }

    @Override
    public void onMatchEndSquadra(Team winnerOrNullOnTie) {
        boolean vittoria = winnerOrNullOnTie != null &&
                winnerOrNullOnTie.getMembers().stream().anyMatch(p -> p instanceof HumanPlayer);
        aggiornaStatisticheProfilo(vittoria);
        AudioManager.getInstance().playResource("/audio/game_end.wav");
        gameView.mostraFinePartitaSquadra(winnerOrNullOnTie);
    }

    /**
     * Aggiorna le statistiche del profilo in base all'esito della partita.
     */
    private void aggiornaStatisticheProfilo(boolean vittoria) {
        if (currentProfile == null) {
            return;
        }
        currentProfile.incrementaPartite();
        if (vittoria) {
            currentProfile.incrementaVittorie();
        }
        profileManager.save(currentProfile);
    }
}