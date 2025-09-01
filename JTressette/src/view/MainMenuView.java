package view;

/**
 * Vista del menu principale dell'applicazione.
 * Espone metodi per registrare callback ai pulsanti principali
 * e per ottenere le impostazioni selezionate dall'utente.
 */
public class MainMenuView {

    private Runnable onPlay;
    private Runnable onSettings;
    private Runnable onExit;

    /**
     * Registra la callback da eseguire quando l'utente preme il pulsante "Gioca".
     * @param onPlay azione da eseguire
     */
    public void setOnPlay(Runnable onPlay) {
        this.onPlay = onPlay;
    }

    /**
     * Registra la callback da eseguire quando l'utente preme il pulsante "Impostazioni".
     * @param onSettings azione da eseguire
     */
    public void setOnSettings(Runnable onSettings) {
        this.onSettings = onSettings;
    }

    /**
     * Registra la callback da eseguire quando l'utente preme il pulsante "Esci".
     * @param onExit azione da eseguire
     */
    public void setOnExit(Runnable onExit) {
        this.onExit = onExit;
    }

    /**
     * Metodo invocato dalla GUI quando l'utente clicca su "Gioca".
     */
    public void clickPlay() {
        if (onPlay != null) {
            onPlay.run();
        }
    }

    /**
     * Metodo invocato dalla GUI quando l'utente clicca su "Impostazioni".
     */
    public void clickSettings() {
        if (onSettings != null) {
            onSettings.run();
        }
    }

    /**
     * Metodo invocato dalla GUI quando l'utente clicca su "Esci".
     */
    public void clickExit() {
        if (onExit != null) {
            onExit.run();
        }
    }

    /**
     * Indica se la modalità di gioco selezionata è 2 contro 2.
     * @return true se è selezionata la modalità 2vs2, false altrimenti
     */
    public boolean isTwoVsTwo() {
        // Implementazione reale: legge stato GUI
        return false;
    }

    /**
     * Restituisce il nome del profilo selezionato dall'utente.
     * @return nome del profilo
     */
    public String getSelectedProfileName() {
        // Implementazione reale: legge stato GUI
        return "Default";
    }

    /**
     * Restituisce il numero di carte da distribuire a ciascun giocatore.
     * @return numero di carte per giocatore
     */
    public int getCartePerGiocatore() {
        // Implementazione reale: legge stato GUI
        return 10;
    }
}
