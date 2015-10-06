package com.project.graphdb.repositories;

import com.project.graphdb.entities.Player;
import com.project.graphdb.entities.Team;
import com.project.graphdb.label.ProjectLabels;
import com.project.graphdb.relationship.PlayerRelationship;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

public class PlayerRepository {

    private final ExecutionEngine engine;

    public PlayerRepository(ExecutionEngine engine) {
        this.engine = engine;
    }

    public void createTeam(Team entity, GraphDatabaseService graphDb) {
        try (Transaction tx = graphDb.beginTx()) {
            Node player = graphDb.createNode(ProjectLabels.Team);
            player.setProperty(Team.NAME, entity.getName());
            player.setProperty(Team.DESCRIPTION, entity.getDescription());
            player.setProperty(Team.FOUNDED, entity.getFounded());
            //  Node team = graphDb.getNodeById(teamId);
            //   player.createRelationshipTo(team, PlayerRelationship.COACH_OF);
            tx.success();
        }
    }

    public boolean removeTeam(long id, GraphDatabaseService graphDb) {

        try (Transaction tx = graphDb.beginTx()) {
            Map<String, Object> params = new HashMap<>();
            Node node = graphDb.getNodeById(id);
            if (node.hasRelationship()) {
                //System.out.println("Has relation");
                return false;
            }
            params.put("id", id);
            String remove = "MATCH (n:Team) WHERE ID (n) = {id} DELETE n";
            ExecutionResult result = engine.execute(remove,
                    params);
            tx.success();
        }
        return true;
    }

    public void removePlayer(Player entity, GraphDatabaseService graphDb) {
        try (Transaction tx = graphDb.beginTx()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", entity.getId());
            String remove = "MATCH (n:Player)-[r]-() WHERE ID (n) = {id} DELETE r, n";
            ExecutionResult result = engine.execute(remove,
                    params);
            tx.success();
        }
    }

    public boolean removePlayerById(Long id, GraphDatabaseService graphDb) {
        //avelacnel stugumner , ka ajdpisi nod te chka? ka relation te chka?
        try (Transaction tx = graphDb.beginTx()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
//            Node node = graphDb.getNodeById(id);
//            if (node.hasRelationship()) {
//                System.out.println("Has relation");
//                return false;
//            }
            String remove = "MATCH (n:Player)-[r]-() WHERE ID (n) = {id} DELETE r, n";
            ExecutionResult result = engine.execute(remove,
                    params);
            tx.success();
        }
        return true;
    }

    public void updateTeam(Team entity, GraphDatabaseService graphDb) {

        try (Transaction tx = graphDb.beginTx()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", entity.getId());
            params.put("name", entity.getName());
            params.put("description", entity.getDescription());
            params.put("founded", entity.getFounded());

            String query = "MATCH (n:Team) WHERE ID (n) = {id} SET  n.name={name}, n.description={description}, n.founded={founded}  RETURN n";

            ExecutionResult result = engine.execute(query,
                    params);

            tx.success();
        }
    }

    public void updatePlayer(Player entity, GraphDatabaseService graphDb, long teamId) {
        //avelacnel stugumner , ka ajdpisi nod te chka? ka relation te chka?
        try (Transaction tx = graphDb.beginTx()) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", entity.getId());
            params.put("name", entity.getName());
            params.put("surname", entity.getSurname());
            params.put("position", entity.getPosition());

            String query = "MATCH (n:Player) WHERE ID (n) = {id} SET  n.name={name}, n.surname={surname}, n.position={position}  RETURN n";

            ExecutionResult result = engine.execute(query,
                    params);

            Map<String, Object> paramsSecond = new HashMap<>();
            paramsSecond.put("id", entity.getId());

            String delRel = "MATCH (a:Player) WHERE ID(a) = {id} MATCH a-[rel:PLAY_IN]-> r DELETE rel";

            ExecutionResult removeRelation = engine.execute(delRel,
                    params);
            //again do rel
            Node player = graphDb.getNodeById(entity.getId());

            Node team = graphDb.getNodeById(teamId);

            player.createRelationshipTo(team, PlayerRelationship.PLAY_IN);
            tx.success();
        }
    }

    public void createPlayerNode(Player entity, GraphDatabaseService graphDb, long teamId) {
        //avelacnel stugumner , ka ajdpisi nod te chka? ka relation te chka?
        try (Transaction tx = graphDb.beginTx()) {
            Node player = graphDb.createNode(ProjectLabels.Player);
            player.setProperty(Player.NAME, entity.getName());
            player.setProperty(Player.SURNAME, entity.getSurname());
            player.setProperty(Player.POSITION, entity.getPosition());
            Node team = graphDb.getNodeById(teamId);
            player.createRelationshipTo(team, PlayerRelationship.PLAY_IN);
            tx.success();
        }
    }

    public void createCoach(Player entity, GraphDatabaseService graphDb, long teamId) {
        try (Transaction tx = graphDb.beginTx()) {
            Node player = graphDb.createNode(ProjectLabels.Coach);
            player.setProperty(Player.NAME, entity.getName());
            player.setProperty(Player.SURNAME, entity.getSurname());
            Node team = graphDb.getNodeById(teamId);
            player.createRelationshipTo(team, PlayerRelationship.COACH_OF);
            tx.success();
        }
    }

    public List<Player> findAllPlayers() {
        ExecutionResult result = engine.execute("MATCH (e:Player) RETURN e");
        Iterator<Node> nodes = result.columnAs("e");
        return fromNodes(nodes);
    }

    //MATCH (a)-[:`PLAY_IN`]->(b) RETURN a,b LIMIT 25
    public List<Player> findAllPlayersWithTeam() {
        ExecutionResult result = engine.execute("MATCH (a)-[:`PLAY_IN`]->(b) RETURN ID(a), a.name, a.surname, a.position, ID(b), b.name");
        //System.out.println("Dump " + result.dumpToString());
        List<Player> playerList = new ArrayList<Player>();
        Player player = null;
        for (Map<String, Object> row : result) {
            player = new Player();
            for (Entry<String, Object> column : row.entrySet()) {

                if (column.getKey().equals("ID(a)")) {
                    //  System.out.println("column.getKey() " +  column.getKey());
                    if (column.getValue() != null) {
                        // System.out.println("id " +  column.getValue());
                        player.setId((long) column.getValue());
                    }
                }
                if (column.getKey().equals("a.name")) {
                    if (column.getValue() != null) {
                        player.setName((String) column.getValue());
                    }
                }
                if (column.getKey().equals("a.surname")) {
                    if (column.getValue() != null) {
                        player.setSurname((String) column.getValue());
                    }
                }
                if (column.getKey().equals("a.position")) {
                    if (column.getValue() != null) {
                        player.setPosition((String) column.getValue());
                    }
                }
                if (column.getKey().equals("ID(b)")) {
                    if (column.getValue() != null) {
                        player.setTeamId((long) column.getValue());
                    }
                }
                if (column.getKey().equals("b.name")) {
                    if (column.getValue() != null) {
                        player.setTeamName((String) column.getValue());
                    }
                }
                //  System.out.println("Save player  with id " + player.getId());

            }
            playerList.add(player);
        }
        return playerList;
    }

    public Team findTeamById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String query = "MATCH (a:Team) "
                + " WHERE ID(a) = {id} "
                + " RETURN ID(a), a.name, a.description, a.founded"; //work

        ExecutionResult result = engine.execute(query,
                params);

        Team entity = null;
        for (Map<String, Object> row : result) {
            entity = new Team();
            for (Entry<String, Object> column : row.entrySet()) {

                if (column.getKey().equals("ID(a)")) {
                    if (column.getValue() != null) {
                        entity.setId((long) column.getValue());
                    }
                }
                if (column.getKey().equals("a.name")) {
                    if (column.getValue() != null) {
                        entity.setName((String) column.getValue());
                    }
                }
                if (column.getKey().equals("a.description")) {
                    if (column.getValue() != null) {
                        entity.setDescription((String) column.getValue());
                    }
                }
                if (column.getKey().equals("a.founded")) {
                    if (column.getValue() != null) {
                        entity.setFounded((String) column.getValue());
                    }
                }

            }
        }
        return entity;
    }

    public Player findPlayerById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String query2 = "MATCH (a)"
                + " WHERE ID(a) = {id} "
                + " MATCH (a)-[:PLAY_IN]-(b:Team)"
                + " RETURN ID(a), a.name, a.surname, a.position, ID(b), b.name";//work

        String query = "MATCH (a)-[:PLAY_IN]-(b:Team) "
                + "WHERE ID(a) = {id} "
                + "RETURN ID(a), a.name, a.surname, a.position, ID(b), b.name"; //work

        ExecutionResult result = engine.execute(query2,
                params);

        Player player = null;
        for (Map<String, Object> row : result) {
            player = new Player();
            for (Entry<String, Object> column : row.entrySet()) {

                if (column.getKey().equals("ID(a)")) {
                    if (column.getValue() != null) {
                        player.setId((long) column.getValue());
                    }
                }
                if (column.getKey().equals("a.name")) {
                    if (column.getValue() != null) {
                        player.setName((String) column.getValue());
                    }
                }
                if (column.getKey().equals("a.surname")) {
                    if (column.getValue() != null) {
                        player.setSurname((String) column.getValue());
                    }
                }
                if (column.getKey().equals("a.position")) {
                    if (column.getValue() != null) {
                        player.setPosition((String) column.getValue());
                    }
                }
                if (column.getKey().equals("ID(b)")) {

                    if (column.getValue() != null) {
                        //  System.out.println("ID B " + column.getValue());
                        player.setTeamId((long) column.getValue());
                    }
                }
                if (column.getKey().equals("b.name")) {
                    if (column.getValue() != null) {
                        player.setTeamName((String) column.getValue());
                    }
                }
            }
        }
        return player;
    }

    public List<Team> findAllTeams() {
        ExecutionResult result = engine.execute("MATCH (e:Team) RETURN e");
        Iterator<Node> nodes = result.columnAs("e");
        return fromTeamNodes(nodes);
    }

    public LinkedList<Player> playIn() {
        ExecutionResult result = engine.execute("MATCH (n) -[:PLAY_IN]->( :Team ) RETURN n");
        Iterator<Node> nodes = result.columnAs("n");
        return fromNodes(nodes);
    }

    public LinkedList<Player> bySurname(String surname) {
        Map<String, Object> params = new HashMap<>();
        params.put("surname", surname);
        ExecutionResult result = engine.execute("MATCH (n:Player {surname: {surname}}) "
                + "RETURN n",
                params);
        Iterator<Node> nodes = result.columnAs("n");
        return fromNodes(nodes);
    }

    public LinkedList<Team> findTeamByName(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        ExecutionResult result = engine.execute("MATCH (n:Team {name: {name}}) "
                + "RETURN n",
                params);
        Iterator<Node> nodes = result.columnAs("n");
        return fromTeamNodes(nodes);
    }

    public LinkedList<Player> fromNodes(Iterator<Node> nodes) {
        LinkedList<Player> returnEmployees = new LinkedList<>();
        for (Node node : IteratorUtil.asIterable(nodes)) {
            returnEmployees.add(Player.fromNode(node));
        }
        return returnEmployees;
    }

    public LinkedList<Team> fromTeamNodes(Iterator<Node> nodes) {
        LinkedList<Team> teams = new LinkedList<>();
        for (Node node : IteratorUtil.asIterable(nodes)) {
            teams.add(Team.fromNode(node));
        }
        return teams;
    }
//////////////////////////////////////

    public Object shortestPath(String surname1, String surname2) {
        Map<String, Object> params = new HashMap<>();
        params.put("surname1", surname1);
        params.put("surname2", surname2);

        ExecutionResult result = engine
                .execute("MATCH (a{surname:{surname1} }), (b{surname:{surname2} }) "
                        + "RETURN allShortestPaths((a)-[*]-(b)) AS path", params);
        Iterator<Object> paths = result.columnAs("path");
        for (Object p : IteratorUtil.asIterable(paths)) {
            return p;
        }
        return null;
    }
}
