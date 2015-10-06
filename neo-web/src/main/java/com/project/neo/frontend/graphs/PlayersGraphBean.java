package com.project.neo.frontend.graphs;

import com.project.graphdb.entities.Player;
import com.project.graphdb.relationship.PlayerRelationship;
import com.project.graphdb.repositories.PlayerRepository;
import com.project.neo.ServiceManagerBean;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.primefaces.model.graphvis.GraphvisEdge;
import org.primefaces.model.graphvis.GraphvisModel;
import org.primefaces.model.graphvis.GraphvisNode;
import org.primefaces.model.graphvis.GraphvisNode.NODE_SHAPE;
import org.primefaces.model.graphvis.impl.GraphvisModelImpl;

@ManagedBean(name = "playersGraphBean")
@SessionScoped
public class PlayersGraphBean implements Serializable {

    @ManagedProperty("#{serviceManagerBean}")
    private ServiceManagerBean serviceManagerBean;
    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    private FacesContext context = null;
    private ExternalContext externalContext = null;
    protected GraphvisModel graphModel;
    protected List<GraphvisNode.NODE_SHAPE> nodeShapes = Arrays.asList(GraphvisNode.NODE_SHAPE.values());
    private PlayerRepository repository;

    public PlayersGraphBean() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        ExecutionEngine engine = new ExecutionEngine(serviceManagerBean.getDatabaseService());
        repository = new PlayerRepository(engine);
        try {
            if (graphModel == null) {
                graphModel = new GraphvisModelImpl();

                List<Player> playerList = repository.findAllPlayersWithTeam();
                System.out.println("Size " + playerList.size());

                int maxWeight = 3;

                GraphvisEdge edge = null;

                for (Player player : playerList) {
                    graphModel.addNode(player.getId().toString(), player.getName() + " " + player.getSurname());//set player node
                    graphModel.addNode(player.getTeamId().toString(), player.getTeamName());//set team node
                    GraphvisNode playerNode = graphModel.getNode(player.getId().toString());
                    GraphvisNode teamNode = graphModel.getNode(player.getTeamId().toString());
                    edge = graphModel.addEdge(PlayerRelationship.PLAY_IN.name(), playerNode, teamNode);
                    edge.setWidth((int) (Math.round(Math.random() * maxWeight) + 1));
                    //if coach set1 if player set 2
                    edge.setLabel("" + player.getPosition());
                    edge.setShape(GraphvisEdge.ARROW_SHAPE.CIRCLE);
                    edge.setDirected(true);
                }

                graphModel.setLayout("ForceDirected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GraphvisModel getGraphModel() {

        return graphModel;
    }

    public void setGraphModel(GraphvisModel graphModel) {
        this.graphModel = graphModel;
    }

    public ServiceManagerBean getServiceManagerBean() {
        return serviceManagerBean;
    }

    public void setServiceManagerBean(ServiceManagerBean serviceManagerBean) {
        this.serviceManagerBean = serviceManagerBean;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public PlayerRepository getRepository() {
        return repository;
    }

    public void setRepository(PlayerRepository repository) {
        this.repository = repository;
    }

    public List<NODE_SHAPE> getNodeShapes() {
        return nodeShapes;
    }

    public void setNodeShapes(List<NODE_SHAPE> nodeShapes) {
        this.nodeShapes = nodeShapes;
    }

}
