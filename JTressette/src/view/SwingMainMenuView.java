package view;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello principale del menu di Tressette.
 */
public class SwingMainMenuView extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Runnable onPlay;
    private Runnable onSettings;
    private Runnable onExit;

    public SwingMainMenuView() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("Tressette", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

        JButton playButton = new JButton("Gioca");
        playButton.addActionListener(e -> {
            if (onPlay != null) onPlay.run();
        });

        JButton settingsButton = new JButton("Impostazioni");
        settingsButton.addActionListener(e -> {
            if (onSettings != null) onSettings.run();
        });

        JButton exitButton = new JButton("Esci");
        exitButton.addActionListener(e -> {
            if (onExit != null) onExit.run();
        });

        buttonPanel.add(playButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public void setOnPlay(Runnable onPlay) {
        this.onPlay = onPlay;
    }

    public void setOnSettings(Runnable onSettings) {
        this.onSettings = onSettings;
    }

    public void setOnExit(Runnable onExit) {
        this.onExit = onExit;
    }
}
