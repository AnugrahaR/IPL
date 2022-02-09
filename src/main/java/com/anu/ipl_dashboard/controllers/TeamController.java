package com.anu.ipl_dashboard.controllers;

import com.anu.ipl_dashboard.model.Match;
import com.anu.ipl_dashboard.model.Team;
import com.anu.ipl_dashboard.repository.MatchRepository;
import com.anu.ipl_dashboard.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    MatchRepository matchRepository;

    @GetMapping("/team/{teamName}")
    public Team getTeamdetails(@PathVariable String teamName){
        Team team = teamRepository.findAllByTeamName(teamName);
      team.setLatestMatches(matchRepository.getLatestMatchesByTeam(teamName,4));

        return team;
    }

    @GetMapping("/latestMatches/{teamName}")
    public List<Match> getlatestMatchesdetails(@PathVariable String teamName){


        List<Match> matches = matchRepository.getLatestMatchesByTeam(teamName,4);
        return matches;
    }
}
