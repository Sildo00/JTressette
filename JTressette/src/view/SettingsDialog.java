package view;

import utils.UserProfile;
import utils.UserProfileManager;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog per la gestione delle impostazioni di gioco e del profilo utente.
 */
public class SettingsDialog extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final UserProfileManager profileManager;
    private UserProfile currentProfile;

    private JTextField nicknameField;
    private JLabel statsLabel;
    private JSlider musicSlider;
    private JCheckBox sfxCheck;

    /**
     * Crea la finestra impostazioni.
     *
     * @param owner finestra principale
     * @param profileManager gestore dei profili utente
     * @param currentProfile profilo attualmente in uso
     */
    public SettingsDialog(JFrame owner, UserProfileManager profileManager, UserProfile currentProfile) {
        super(owner, "Impostazioni", true);
        this.profileManager = profileManager;
        this.currentProfile = currentProfile;

        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(owner);

        JTabbedPane tabs = new JTabbedPane();

        // Tab profilo
        JPanel profilePanel = new JPanel();
        profilePanel.add(new JLabel("Nickname:"));
        nicknameField = new JTextField(currentProfile.getNome(), 15);
        profilePanel.add(nicknameField);
        JButton loadSaveButton = new JButton("Carica/Salva");
        loadSaveButton.addActionListener(e -> loadOrSaveProfile());
        profilePanel.add(loadSaveButton);

        // Tab avatar
        JPanel avatarPanel = new JPanel();
        avatarPanel.add(new JLabel("Avatar utente e bot (da implementare)"));

        // Tab audio
        JPanel audioPanel = new JPanel();
        audioPanel.add(new JLabel("Volume musica:"));
        musicSlider = new JSlider(0, 100, 50);
        audioPanel.add(musicSlider);
        sfxCheck = new JCheckBox("Effetti sonori attivi", true);
        audioPanel.add(sfxCheck);

        // Tab statistiche
        JPanel statsPanel = new JPanel();
        statsLabel = new JLabel(getStatsText(currentProfile));
        statsPanel.add(statsLabel);

        tabs.addTab("Profilo", profilePanel);
        tabs.addTab("Avatar", avatarPanel);
        tabs.addTab("Audio", audioPanel);
        tabs.addTab("Statistiche", statsPanel);

        add(tabs, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Chiudi");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Carica un profilo esistente o ne crea uno nuovo se non trovato.
     */
    private void loadOrSaveProfile() {
        String nickname = nicknameField.getText().trim();
        if (nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci un nickname valido.");
            return;
        }

        UserProfile loaded = profileManager.load(nickname);
        if (loaded == null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Profilo non trovato. Crearlo?",
                    "Nuovo profilo",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                loaded = new UserProfile(nickname);
                profileManager.save(loaded);
                JOptionPane.showMessageDialog(this, "Nuovo profilo creato.");
            } else {
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Profilo caricato.");
        }

        currentProfile = loaded;
        statsLabel.setText(getStatsText(currentProfile));
    }

    /**
     * Restituisce il testo delle statistiche del profilo.
     *
     * @param profile profilo utente
     * @return testo formattato
     */
    private String getStatsText(UserProfile profile) {
        return "<html>Partite giocate: " + profile.getPartiteGiocate() +
                "<br>Partite vinte: " + profile.getPartiteVinte() + "</html>";
    }
}
