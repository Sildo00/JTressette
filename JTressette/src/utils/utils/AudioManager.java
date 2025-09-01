package utils;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Gestisce la riproduzione di effetti sonori.
 * Supporta sia risorse sul classpath sia file su disco.
 */
public class AudioManager {

    private static AudioManager instance;

    /**
     * Restituisce l'istanza singleton.
     * @return istanza di AudioManager
     */
    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Costruttore privato per pattern Singleton.
     */
    private AudioManager() { }

    /**
     * Riproduce un audio da resources (classpath).
     * @param resourcePath percorso della risorsa audio nel classpath
     */
    public void playResource(String resourcePath) {
        try (InputStream in = new BufferedInputStream(getClass().getResourceAsStream(resourcePath))) {
            try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(in)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Riproduce un audio da file system.
     * @param filename percorso del file audio
     */
    public void playFile(String filename) {
        try (InputStream in = new BufferedInputStream(new FileInputStream(filename));
             AudioInputStream audioIn = AudioSystem.getAudioInputStream(in)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}
