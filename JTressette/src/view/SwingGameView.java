package view;

import controller.GameController;
import model.Card;
import model.HumanPlayer;
import model.Player;
import model.Team;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementazione Swing della GameView.
 * Gestisce la visualizzazione del tavolo, delle mani, degli avversari e dei punteggi.
 */
public class SwingGameView extends JFrame implements GameView {

    private static final long serialVersionUID = 1L;

    private GameController controller;
    private final JPanel tavoloPanel;
    private final JPanel manoGiocatorePanel;
    private final JPanel avversarioNordPanel;
    private final JPanel avversarioOvestPanel;
    private final JPanel avversarioEstPanel;
    private final JPanel punteggiPanel;
    private final JButton exitButton;

    private final Map<Player, Position> posizioniGiocatori = new HashMap<>();

    public SwingGameView() {
        setTitle("JTressette");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tavoloPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        tavoloPanel.setBackground(new Color(0, 102, 0));

        manoGiocatorePanel = new JPanel(new FlowLayout());
        manoGiocatorePanel.setOpaque(false);

        avversarioNordPanel = new JPanel(new FlowLayout());
        avversarioNordPanel.setOpaque(false);

        avversarioOvestPanel = new JPanel(new GridLayout(3, 1));
        avversarioOvestPanel.setOpaque(false);

        avversarioEstPanel = new JPanel(new GridLayout(3, 1));
        avversarioEstPanel.setOpaque(false);

        punteggiPanel = new JPanel();
        punteggiPanel.setOpaque(false);
        punteggiPanel.setLayout(new BoxLayout(punteggiPanel, BoxLayout.Y_AXIS));

        exitButton = new JButton("Esci");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(avversarioNordPanel, BorderLayout.CENTER);
        topPanel.add(exitButton, BorderLayout.EAST);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setOpaque(false);
        westPanel.add(avversarioOvestPanel, BorderLayout.CENTER);
        westPanel.add(punteggiPanel, BorderLayout.SOUTH);

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setOpaque(false);
        eastPanel.add(avversarioEstPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(tavoloPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(manoGiocatorePanel, BorderLayout.SOUTH);
    }
    
    /**
     * Mostra la schermata di gioco.
     */
    public void mostraGioco() {
        setVisible(true);
    }

    /**
     * Torna al menu principale.
     */
    public void mostraMenuPrincipale() {
        setVisible(false);
    }


    @Override
    public void impostaController(GameController controller) {
        this.controller = controller;
        exitButton.addActionListener(e -> {
            int scelta = JOptionPane.showConfirmDialog(
                    this,
                    "Sei sicuro di uscire dalla partita?",
                    "Conferma uscita",
                    JOptionPane.YES_NO_OPTION
            );
            if (scelta == JOptionPane.YES_OPTION) {
                controller.handleExitPartita();
            }
        });
    }

    @Override
    public void impostaPosizioniGiocatori(Map<Player, Position> mappaPosizioni) {
        posizioniGiocatori.clear();
        posizioniGiocatori.putAll(mappaPosizioni);
    }

    @Override
    public void abilitaSelezioneCarte(Player humanPlayer) {
        if (!(humanPlayer instanceof HumanPlayer umano)) {
            return;
        }
        manoGiocatorePanel.removeAll();
        for (Card c : umano.getMano()) {
            JButton btn = new JButton(new ImageIcon(getClass().getResource(
                    "/carte/" + c.getSeme().name().toLowerCase() + "_" + c.getValore().name().toLowerCase() + ".png"
            )));
            btn.addActionListener(e -> controller.cartaSelezionataDalGiocatore(c));
            manoGiocatorePanel.add(btn);
        }
        manoGiocatorePanel.revalidate();
        manoGiocatorePanel.repaint();
    }

    @Override
    public void mostraCartaGiocata(Player player, Card card) {
        JLabel lbl = new JLabel(new ImageIcon(getClass().getResource(
                "/carte/" + card.getSeme().name().toLowerCase() + "_" + card.getValore().name().toLowerCase() + ".png"
        )));
        Position pos = posizioniGiocatori.get(player);
        if (pos != null) {
            switch (pos) {
                case SOUTH -> manoGiocatorePanel.add(lbl);
                case NORTH -> avversarioNordPanel.add(lbl);
                case WEST -> avversarioOvestPanel.add(lbl);
                case EAST -> avversarioEstPanel.add(lbl);
            }
        } else {
            tavoloPanel.add(lbl);
        }
        revalidate();
        repaint();
    }

    @Override
    public void mostraCartaPescata(Player player, Card card, boolean revealTemporaneo) {
        if (player instanceof HumanPlayer umano) {
            abilitaSelezioneCarte(umano);
        }
    }

    @Override
    public void mostraFinePresa(Player winner, int points) {
        JOptionPane.showMessageDialog(this, winner.getNome() + " prende. +" + points + " punti");
        tavoloPanel.removeAll();
        tavoloPanel.revalidate();
        tavoloPanel.repaint();
    }

    @Override
    public void aggiornaPunteggiGiocatori(Map<Player, Integer> scores) {
        punteggiPanel.removeAll();
        scores.forEach((p, s) -> punteggiPanel.add(new JLabel(p.getNome() + ": " + s)));
        punteggiPanel.revalidate();
        punteggiPanel.repaint();
    }

    @Override
    public void aggiornaPunteggiSquadre(Map<Team, Integer> scores) {
        punteggiPanel.removeAll();
        scores.forEach((t, s) -> punteggiPanel.add(new JLabel(t.getNome() + ": " + s)));
        punteggiPanel.revalidate();
        punteggiPanel.repaint();
    }

    @Override
    public void mostraFineRound() {
        JOptionPane.showMessageDialog(this, "Fine round");
    }

    @Override
    public void mostraFinePartitaGiocatore(Player winnerOrNullOnTie) {
        String msg = winnerOrNullOnTie == null ? "Pareggio" : "Vince " + winnerOrNullOnTie.getNome();
        JOptionPane.showMessageDialog(this, msg);
    }

    @Override
    public void mostraFinePartitaSquadra(Team winnerOrNullOnTie) {
        String msg = winnerOrNullOnTie == null ? "Pareggio" : "Vince " + winnerOrNullOnTie.getNome();
        JOptionPane.showMessageDialog(this, msg);
    }

    @Override
    public void mostraImpostazioni() {
        // Apertura finestra impostazioni gestita dal controller
    }
}
