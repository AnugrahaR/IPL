package com.anu.ipl_dashboard.repository;

import com.anu.ipl_dashboard.model.Team;
import org.springframework.data.repository.CrudRepository;

    public interface TeamRepository extends CrudRepository<Team,Long> {

        public Team findAllByTeamName(String teamName);
    }