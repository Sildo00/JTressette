package view;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello del menu principale con titolo e pulsanti Gioca, Impostazioni, Chiudi.
 * Delega le azioni alla vista logica MainMenuView.
 */
public class SwingMainMenuView extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Costruisce il pannello del menu con layout e listener collegati alla vista logica.
     * @param mainMenuView vista logica del menu
     */
    public SwingMainMenuView(MainMenuView mainMenuView) {
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 102, 0));

        JLabel titolo = new JLabel("Tressette");
        titolo.setFont(new Font("Serif", Font.BOLD, 48));
        titolo.setForeground(Color.WHITE);

        JButton gioca = new JButton("Gioca");
        gioca.addActionListener(e -> mainMenuView.clickPlay());

        JButton impostazioni = new JButton("Impostazioni");
        impostazioni.addActionListener(e -> mainMenuView.clickSettings());

        JButton esci = new JButton("Chiudi");
        esci.addActionListener(e -> mainMenuView.clickExit());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titolo, gbc);
        gbc.gridy++;
        add(gioca, gbc);
        gbc.gridy++;
        add(impostazioni, gbc);
        gbc.gridy++;
        add(esci, gbc);
    }
}
