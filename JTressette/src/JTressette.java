import controller.GameController;
import utils.UserProfileManager;
import view.MainMenuView;
import view.SwingGameView;
import view.SwingMainMenuView;

import javax.swing.*;
import java.io.File;

public final class JTressette {

    private JTressette() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JTressette");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);
            frame.setLocationRelativeTo(null);

            // Vista logica del menu
            MainMenuView mainMenuView = new MainMenuView();
            // Vista grafica del menu
            SwingMainMenuView mainMenuPanel = new SwingMainMenuView(mainMenuView);
            // Vista grafica del gioco
            SwingGameView gameView = new SwingGameView();

            UserProfileManager profileManager = new UserProfileManager(new File("profiles.dat"));

            // Controller
            new GameController(gameView, mainMenuView, profileManager);

            // Mostra il menu all'avvio
            frame.setContentPane(mainMenuPanel);
            frame.setVisible(true);
        });
    }
}
