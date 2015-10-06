package com.project.graphdb.entities;

import java.util.Objects;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class Team {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String FOUNDED = "founded";
    private long id;
    private String name, description, founded;

    public Team() {
    }
    

    public static Team fromNode(Node node) {
        try (Transaction t = node.getGraphDatabase().beginTx()) {
            Team e = new Team(
                    node.getId(),
                    (String) node.getProperty("name"),
                    (String) node.getProperty("description"),
                    (String) node.getProperty("founded"));
            t.success();
            return e;
        }
    }

    public Team(long id, String name, String description, String founded) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.founded = founded;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFounded() {
        return founded;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Objects.hashCode(this.founded);
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
        final Team other = (Team) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.founded, other.founded)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", name=" + name + ", description=" + description + ", founded=" + founded + '}';
    }
    
    
}
