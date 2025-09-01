package utils;

import java.io.Serializable;

/**
 * Rappresenta il profilo di un utente.
 * Contiene informazioni su nome, avatar, statistiche e livello.
 * Fornisce metodi per aggiornare le statistiche e il livello.
 */
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String nome;
    private String avatarPath;
    private int partiteGiocate;
    private int partiteVinte;
    private int partitePerse;
    private int livello;

    /**
     * Costruisce un nuovo profilo utente con il nome specificato.
     * Le statistiche iniziano a zero. Il livello parte da 1.
     * @param nome nome del profilo
     */
    public UserProfile(String nome) {
        this.nome = nome;
        this.avatarPath = null;
        this.partiteGiocate = 0;
        this.partiteVinte = 0;
        this.partitePerse = 0;
        this.livello = 1;
    }

    /**
     * Restituisce il nome del profilo.
     * @return nome del profilo
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il percorso dell'avatar in formato PNG.
     * @param avatarPath percorso relativo alle risorse o assoluto
     */
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    /**
     * Restituisce il percorso dell'avatar in formato PNG.
     * @return percorso dell'avatar
     */
    public String getAvatarPath() {
        return avatarPath;
    }

    /**
     * Restituisce il numero di partite giocate.
     * @return numero di partite giocate
     */
    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    /**
     * Restituisce il numero di partite vinte.
     * @return numero di partite vinte
     */
    public int getPartiteVinte() {
        return partiteVinte;
    }

    /**
     * Restituisce il livello del profilo.
     * @return livello
     */
    public int getLivello() {
        return livello;
    }

    /**
     * Incrementa di uno il numero di partite giocate.
     */
    public void incrementaPartite() {
        partiteGiocate++;
        aggiornaLivello();
    }

    /**
     * Incrementa di uno il numero di partite vinte.
     */
    public void incrementaVittorie() {
        partiteVinte++;
        aggiornaLivello();
    }

    /**
     * Restituisce la percentuale di vittorie rispetto alle partite giocate.
     * @return percentuale di vittorie, oppure 0 se non sono state giocate partite
     */
    public double getPercentualeVittorie() {
        if (partiteGiocate == 0) {
            return 0.0;
        }
        return (partiteVinte * 100.0) / partiteGiocate;
    }

    /**
     * Aggiorna il livello in base alle vittorie.
     */
    public void aggiornaLivello() {
        this.livello = 1 + (partiteVinte / 5);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "nome='" + nome + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", partiteGiocate=" + partiteGiocate +
                ", partiteVinte=" + partiteVinte +
                ", partitePerse=" + partitePerse +
                ", livello=" + livello +
                '}';
    }
}
