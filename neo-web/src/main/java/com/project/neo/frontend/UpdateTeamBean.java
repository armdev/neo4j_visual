package com.project.neo.frontend;

import com.project.graphdb.entities.Player;
import com.project.graphdb.entities.Team;
import com.project.graphdb.repositories.PlayerRepository;
import com.project.neo.ServiceManagerBean;
import com.project.utils.ParamUtil;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.neo4j.cypher.javacompat.ExecutionEngine;

@ManagedBean(name = "updateTeamBean")
@ViewScoped
public class UpdateTeamBean implements Serializable {

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    @ManagedProperty("#{serviceManagerBean}")
    private ServiceManagerBean serviceManagerBean;
    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    private PlayerRepository repository;
    private Team team;
    private Long teamId;

    public UpdateTeamBean() {
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        team = new Team();
        ExecutionEngine engine = new ExecutionEngine(serviceManagerBean.getDatabaseService());
        repository = new PlayerRepository(engine);
        teamId = ParamUtil.longValue(this.getRequestParameter("id"));
        if (teamId != null) {
            team = repository.findTeamById(teamId);
        }

    }

    public String updateTeam() {
        repository.updateTeam(team, serviceManagerBean.getDatabaseService());
        return "teams";
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}
