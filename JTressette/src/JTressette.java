import controller.GameController;
import utils.UserProfileManager;
import view.MainMenuView;
import view.SwingGameView;

import javax.swing.SwingUtilities;
import java.io.File;

/**
 * Entry point dell'applicazione JTressette.
 * Crea View e servizi, li collega al GameController e avvia l'EDT Swing.
 */
public final class JTressette {

    private JTressette() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingGameView gameView = new SwingGameView();
            MainMenuView mainMenuView = new MainMenuView();
            UserProfileManager profileManager = new UserProfileManager(new File("profiles.dat"));

            new GameController(gameView, mainMenuView, profileManager);

            // La GameView è una JFrame: la rendiamo visibile subito.
            gameView.setVisible(true);
        });
    }
}
