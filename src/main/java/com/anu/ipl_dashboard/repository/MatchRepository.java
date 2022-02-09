package com.anu.ipl_dashboard.repository;

import com.anu.ipl_dashboard.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface MatchRepository extends CrudRepository<Match,Long> {

    List<Match> findByTeam1OrTeam2OrderByDateDesc(String team1,  String team2, Pageable pageable);




     default List<Match> getLatestMatchesByTeam(String teamName, int count){
         return findByTeam1OrTeam2OrderByDateDesc(teamName,teamName,PageRequest.of(0,4));

     }

}
