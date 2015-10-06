package com.project.neo.frontend;

import com.project.graphdb.entities.Player;
import com.project.graphdb.entities.Team;
import com.project.graphdb.repositories.PlayerRepository;
import com.project.neo.ServiceManagerBean;
import com.project.neo.enums.Positions;
import com.project.utils.ParamUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;

@ManagedBean(name = "updatePlayerBean")
@ViewScoped
public class UpdatePlayerBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    @ManagedProperty("#{serviceManagerBean}")
    private ServiceManagerBean serviceManagerBean;
    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    private PlayerRepository repository;
    private Player player;
    private Long teamId;
    private Long playerId;

    public UpdatePlayerBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        player = new Player();
        ExecutionEngine engine = new ExecutionEngine(serviceManagerBean.getDatabaseService());
        repository = new PlayerRepository(engine);
        playerId = ParamUtil.longValue(this.getRequestParameter("id"));
        if (playerId != null) {
            player = repository.findPlayerById(playerId);
        }

    }

    public SelectItem[] getPostionList() {
        SelectItem[] items = new SelectItem[Positions.values().length];
        int i = 0;
        for (Positions g : Positions.values()) {
            items[i++] = new SelectItem(g, g.name());
        }
        return items;
    }

    //old
    public String remove() {
        repository.removePlayer(player, serviceManagerBean.getDatabaseService());
        return "players";
    }

    public String updatePlayer() {
        repository.updatePlayer(player, serviceManagerBean.getDatabaseService(), player.getTeamId());
        return "players";
    }

    public void setServiceManagerBean(ServiceManagerBean serviceManagerBean) {
        this.serviceManagerBean = serviceManagerBean;
    }

    public List<Team> getAllTeams() {
        return repository.findAllTeams();
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
