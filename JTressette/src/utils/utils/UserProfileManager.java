package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestisce il caricamento e il salvataggio dei profili utente.
 * I dati vengono memorizzati su file in formato serializzato.
 */
public class UserProfileManager {

    private final File storageFile;

    /**
     * Costruisce un nuovo gestore dei profili utente.
     * @param storageFile file di memorizzazione dei profili
     */
    public UserProfileManager(File storageFile) {
        this.storageFile = storageFile;
    }

    /**
     * Carica un profilo utente dal file di memorizzazione.
     * Se il profilo non esiste, ne crea uno nuovo.
     * @param profileName nome del profilo da caricare
     * @return profilo utente caricato o creato
     */
    public UserProfile load(String profileName) {
        Map<String, UserProfile> profiles = readAllProfiles();
        return profiles.computeIfAbsent(profileName, UserProfile::new);
    }

    /**
     * Salva un profilo utente nel file di memorizzazione.
     * @param profile profilo da salvare
     */
    public void save(UserProfile profile) {
        Map<String, UserProfile> profiles = readAllProfiles();
        profiles.put(profile.getNome(), profile);
        writeAllProfiles(profiles);
    }

    /**
     * Legge tutti i profili dal file di memorizzazione.
     * @return mappa dei profili caricati
     */
    @SuppressWarnings("unchecked")
    private Map<String, UserProfile> readAllProfiles() {
        if (!storageFile.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
            return (Map<String, UserProfile>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    /**
     * Scrive tutti i profili nel file di memorizzazione.
     * @param profiles mappa dei profili da salvare
     */
    private void writeAllProfiles(Map<String, UserProfile> profiles) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(profiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
