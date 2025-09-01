package view;

import java.util.List;

/**
 * Vista per la configurazione iniziale di una partita.
 * Permette all'utente di selezionare le impostazioni di gioco
 * come numero di giocatori, modalità, profilo e altre opzioni.
 */
public class GameSetupView {

    private Runnable onConfirm;
    private Runnable onCancel;

    private String selectedProfileName;
    private boolean twoVsTwo;
    private int cartePerGiocatore;
    private List<String> availableProfiles;

    /**
     * Registra la callback da eseguire quando l'utente conferma la configurazione.
     * @param onConfirm azione da eseguire
     */
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }

    /**
     * Registra la callback da eseguire quando l'utente annulla la configurazione.
     * @param onCancel azione da eseguire
     */
    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

    /**
     * Metodo invocato dalla GUI quando l'utente conferma la configurazione.
     */
    public void clickConfirm() {
        if (onConfirm != null) {
            onConfirm.run();
        }
    }

    /**
     * Metodo invocato dalla GUI quando l'utente annulla la configurazione.
     */
    public void clickCancel() {
        if (onCancel != null) {
            onCancel.run();
        }
    }

    /**
     * Imposta il nome del profilo selezionato.
     * @param profileName nome del profilo
     */
    public void setSelectedProfileName(String profileName) {
        this.selectedProfileName = profileName;
    }

    /**
     * Restituisce il nome del profilo selezionato.
     * @return nome del profilo
     */
    public String getSelectedProfileName() {
        return selectedProfileName;
    }

    /**
     * Imposta se la modalità selezionata è 2 contro 2.
     * @param twoVsTwo true se modalità 2vs2, false altrimenti
     */
    public void setTwoVsTwo(boolean twoVsTwo) {
        this.twoVsTwo = twoVsTwo;
    }

    /**
     * Indica se la modalità selezionata è 2 contro 2.
     * @return true se modalità 2vs2, false altrimenti
     */
    public boolean isTwoVsTwo() {
        return twoVsTwo;
    }

    /**
     * Imposta il numero di carte da distribuire a ciascun giocatore.
     * @param carte numero di carte per giocatore
     */
    public void setCartePerGiocatore(int carte) {
        this.cartePerGiocatore = carte;
    }

    /**
     * Restituisce il numero di carte da distribuire a ciascun giocatore.
     * @return numero di carte per giocatore
     */
    public int getCartePerGiocatore() {
        return cartePerGiocatore;
    }

    /**
     * Imposta la lista dei profili disponibili.
     * @param profiles lista dei nomi dei profili
     */
    public void setAvailableProfiles(List<String> profiles) {
        this.availableProfiles = profiles;
    }

    /**
     * Restituisce la lista dei profili disponibili.
     * @return lista dei nomi dei profili
     */
    public List<String> getAvailableProfiles() {
        return availableProfiles;
    }
}
