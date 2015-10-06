package com.project.neo.frontend;

import com.project.graphdb.entities.Player;
import com.project.graphdb.entities.Team;
import com.project.graphdb.repositories.PlayerRepository;
import com.project.neo.ServiceManagerBean;
import com.project.neo.enums.Positions;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;

@ManagedBean(name = "makeGraphBean")
@ViewScoped
public class MakeGraphBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    @ManagedProperty("#{serviceManagerBean}")
    private ServiceManagerBean serviceManagerBean;
    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    private PlayerRepository repository;
    private Player player;
    private Long teamId;
    private Team team;

    public MakeGraphBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        player = new Player();
        team = new Team();
        ExecutionEngine engine = new ExecutionEngine(serviceManagerBean.getDatabaseService());
        repository = new PlayerRepository(engine);

    }

    public String remove(Long id) {
        boolean check =  repository.removePlayerById(id, serviceManagerBean.getDatabaseService());
//        if(!check){
//             facesError(bundle.getString("errorDeleteNode"));
//        }
        return null;
    }

    public String removeTeam(long id) {
      //  System.out.println("team id " + id);
        boolean check = repository.removeTeam(id, serviceManagerBean.getDatabaseService());
        if(!check){
             facesError(bundle.getString("errorDeleteNode"));
        }
        return null;
    }

    public String savePlayer() {
        repository.createPlayerNode(player, serviceManagerBean.getDatabaseService(), teamId);
        return "players";
    }

    public String saveTeam() {
        repository.createTeam(team, serviceManagerBean.getDatabaseService());
        return "teams";
    }

   public SelectItem[] getPostionList() {
        SelectItem[] items = new SelectItem[Positions.values().length];
        int i = 0;
        for (Positions g : Positions.values()) {
            items[i++] = new SelectItem(g, g.name());
        }
        return items;
    }
    
    public List<Team> getAllTeams() {
        return repository.findAllTeams();
    }

    public List<Player> getAllConnections() {
        return repository.findAllPlayersWithTeam();
    }

    public List<Player> getAllPlayers() {
        return repository.findAllPlayers();
    }

    public void setServiceManagerBean(ServiceManagerBean serviceManagerBean) {
        this.serviceManagerBean = serviceManagerBean;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    private String getRequestParameter(String paramName) {
        String returnValue = null;
        if (externalContext.getRequestParameterMap().containsKey(paramName)) {
            returnValue = (externalContext.getRequestParameterMap().get(paramName));
        }
        return returnValue;
    }
    private void facesError(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public Long getTeamId() {
        return teamId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
