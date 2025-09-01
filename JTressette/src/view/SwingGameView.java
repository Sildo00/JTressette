package view;

import controller.GameController;
import model.Card;
import model.HumanPlayer;
import model.Player;
import model.Team;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementazione Swing della GameView.
 * Mostra area tavolo, mano del giocatore umano, punteggi e log eventi.
 */
public class SwingGameView extends JFrame implements GameView {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GameController controller;

    private final JPanel tavoloPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 16));
    private final JPanel manoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
    private final JPanel punteggiPanel = new JPanel(new GridLayout(0, 1, 4, 4));
    private final JTextArea logArea = new JTextArea(8, 60);

    private final Map<Player, JLabel> lastPlayedByPlayer = new ConcurrentHashMap<>();

    /**
     * Costruisce la finestra principale di gioco.
     */
    public SwingGameView() {
        super("JTressette");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JScrollPane logScroll = new JScrollPane(logArea);
        logArea.setEditable(false);

        JPanel top = new JPanel(new BorderLayout());
        top.add(punteggiPanel, BorderLayout.WEST);
        top.add(tavoloPanel, BorderLayout.CENTER);

        add(top, BorderLayout.CENTER);
        add(manoPanel, BorderLayout.SOUTH);
        add(logScroll, BorderLayout.EAST);

        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    @Override
    public void impostaController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void mostraImpostazioni() {
        JOptionPane.showMessageDialog(this, "Impostazioni (da implementare)", "Impostazioni", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void abilitaSelezioneCarte(HumanPlayer giocatore) {
        manoPanel.removeAll();
        List<Card> mano = giocatore.getMano();
        if (mano != null) {
            for (Card c : mano) {
                JButton btn = new JButton(toLabel(c));
                btn.addActionListener(e -> controller.cartaSelezionataDalGiocatore(c));
                manoPanel.add(btn);
            }
        }
        manoPanel.revalidate();
        manoPanel.repaint();
        log("Seleziona una carta da giocare.");
    }

    @Override
    public void mostraCartaGiocata(Player player, Card card) {
        JLabel lbl = new JLabel(toLabel(card));
        lbl.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        tavoloPanel.add(lbl);
        tavoloPanel.revalidate();
        tavoloPanel.repaint();
        lastPlayedByPlayer.put(player, lbl);
        log(player.getNome() + " gioca " + card);
    }

    @Override
    public void mostraCartaPescata(Player player, Card card, boolean revealTemporaneo) {
        String info = player.getNome() + " pesca " + (revealTemporaneo ? card.toString() : "una carta");
        log(info);
        // Facoltativo: highlight temporaneo
        if (revealTemporaneo) {
            flashMessage(info);
        }
    }

    @Override
    public void mostraFinePresa(Player winner, int points) {
        log("Presa a " + winner.getNome() + " (" + points + " punti).");
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
        scores.forEach((t, s) -> punteggiPanel.add(new JLabel(t.getName() + ": " + s)));
        punteggiPanel.revalidate();
        punteggiPanel.repaint();
    }

    @Override
    public void mostraFineRound() {
        log("Fine round.");
        flashMessage("Fine round");
    }

    @Override
    public void mostraFinePartitaGiocatore(Player winner) {
        String msg = winner == null ? "Parità! Spareggio necessario." : "Vince " + winner.getNome();
        JOptionPane.showMessageDialog(this, msg, "Fine partita 1vs1", JOptionPane.INFORMATION_MESSAGE);
        log("Fine partita 1vs1: " + msg);
    }

    @Override
    public void mostraFinePartitaSquadra(Team winner) {
        String msg = winner == null ? "Parità! Spareggio necessario." : "Vince " + winner.getName();
        JOptionPane.showMessageDialog(this, msg, "Fine partita 2vs2", JOptionPane.INFORMATION_MESSAGE);
        log("Fine partita 2vs2: " + msg);
    }

    private void log(String s) {
        logArea.append(s + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void flashMessage(String text) {
        JDialog d = new JDialog(this, "Info", false);
        d.add(new JLabel(text, SwingConstants.CENTER));
        d.setSize(300, 120);
        d.setLocationRelativeTo(this);
        Timer t = new Timer(1400, e -> d.dispose());
        t.setRepeats(false);
        t.start();
        d.setVisible(true);
    }

    private static String toLabel(Card c) {
        return c.getValore() + " di " + c.getSeme();
    }
}
