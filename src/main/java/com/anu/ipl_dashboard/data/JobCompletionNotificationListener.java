package com.anu.ipl_dashboard.data;

import com.anu.ipl_dashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  private final EntityManager em;

  @Autowired
  public JobCompletionNotificationListener(EntityManager entityManager) {
    this.em = entityManager;
  }

  @Override
  @Transactional
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

//      jdbcTemplate.query("SELECT team1, team2, date FROM Match",
//        (rs, row) -> "team 1 "+
//          rs.getString(1) +"  team 2  "+
//          rs.getString(2)+ "  date  " + rs.getDate(3)
//      ).forEach(System.out::println);
      Map<String,Team> teamData = new HashMap<>();
      em.createQuery("select team1 ,count(team1) from Match group by team1", Object[].class)
              .getResultList()
              .stream()
              .map(e-> new Team((String)e[0],(long)e[1]))
              .forEach(team -> teamData.put(team.getTeamName(),team));
      em.createQuery("select team2 ,count(team2) from Match group by team2", Object[].class)
              .getResultList()
              .stream()
              .forEach(
                      e->{
                        Team team = teamData.get((String)e[0]);
                        team.setTotalMatches(team.getTotalMatches()+ (long) e[1]);
                      }
              );


      em.createQuery("select matchWinner ,count(matchWinner) from Match group by matchWinner", Object[].class)
              .getResultList()
              .stream()
              .forEach(
                e->{
                    Team team = teamData.get((String)e[0]);
                    if(team!= null)team.setTotalWins( (long)  e[1]);
                }
        );

      teamData.values().forEach(team->em.persist(team));
     // teamData.values().forEach(System.out::println);



    }
  }
}