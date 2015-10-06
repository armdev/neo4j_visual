package com.project.graphdb.entities;

import java.util.Objects;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class Player {

    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String POSITION = "position";
    private Long id;
    private String name, surname, position;
    private String teamName;
    private Long teamId;

    public Player() {
    }

    public static Player fromNode(Node node) {
        try (Transaction t = node.getGraphDatabase().beginTx()) {
            Player e = new Player(
                    node.getId(),
                    (String) node.getProperty("name"),
                    (String) node.getProperty("surname"),
                    (String) node.getProperty("position"));
            t.success();
            return e;
        }
    }

    public Player(Long id, String name, String surname, String position) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.position = position;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPosition() {
        return position;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.surname);
        hash = 29 * hash + Objects.hashCode(this.position);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", name=" + name + ", surname=" + surname + ", position=" + position + '}';
    }

}
