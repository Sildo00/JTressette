package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta una squadra (usata nel 2vs2).
 * Contiene un nome e l'elenco dei membri.
 */
public class Team {
    private final String nome;
    private final List<Player> members;

    /**
     * Crea una squadra.
     * @param nome Nome della squadra (solo descrittivo).
     * @param members Lista dei membri. Nel 2vs2 ci si aspetta esattamente 2 giocatori.
     */
    public Team(String nome, List<Player> members) {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Una squadra deve avere almeno un membro");
        }
        this.nome = nome != null ? nome : "Team";
        this.members = new ArrayList<>(members);
    }

    public String getNome() {
        return nome;
    }

    /**
     * Restituisce una copia della lista membri per garantire l'immutabilità esterna.
     */
    public List<Player> getMembers() {
        return new ArrayList<>(members);
    }

    @Override
    public String toString() {
        return "Team{" + nome + ", membri=" + members.stream().map(Player::getNome).toList() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(members, team.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(members);
    }
}
