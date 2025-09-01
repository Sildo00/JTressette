package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Dialog per la scelta della modalità di gioco.
 */
public class GameModeDialog extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Mode { ONE_VS_ONE, TWO_VS_TWO }

    private Mode selectedMode = Mode.ONE_VS_ONE;

    public GameModeDialog(JFrame owner, Consumer<Mode> onContinue) {
        super(owner, "Gioca", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(owner);

        JLabel title = new JLabel("Quanti giocatori?", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel modePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JToggleButton oneVsOne = new JToggleButton("1 vs 1");
        JToggleButton twoVsTwo = new JToggleButton("2 vs 2");

        ButtonGroup group = new ButtonGroup();
        group.add(oneVsOne);
        group.add(twoVsTwo);

        oneVsOne.setSelected(true);

        oneVsOne.addActionListener(e -> selectedMode = Mode.ONE_VS_ONE);
        twoVsTwo.addActionListener(e -> selectedMode = Mode.TWO_VS_TWO);

        modePanel.add(oneVsOne);
        modePanel.add(twoVsTwo);

        add(modePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Chiudi");
        closeButton.addActionListener(e -> dispose());

        JButton continueButton = new JButton("Continua");
        continueButton.setBackground(Color.GREEN);
        continueButton.setForeground(Color.WHITE);
        continueButton.addActionListener(e -> {
            if (onContinue != null) onContinue.accept(selectedMode);
            dispose();
        });

        bottomPanel.add(closeButton);
        bottomPanel.add(continueButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}
