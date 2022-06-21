/*
 * AuthorizationAspect
 *
 * 0.1
 *
 * Author: J. Jansky
 */
package eu.profinit.stm.aspect;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.team.Team;
import eu.profinit.stm.model.user.User;
import eu.profinit.stm.oauth.PrincipalExtractorImpl;
import eu.profinit.stm.service.team.TeamService;
import eu.profinit.stm.service.user.UserService;
import org.aopalliance.aop.Advice;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/**
 * Team authorization aspect. For team service checks if current authenticated user is in Coach subgroup.
 * For user service controls that currently authenticated user have access only to his account information.
 * If not, throws access denied exception.
 */
@Aspect
@Component
@Profile({"authorization", "aspects", "Main"})
public class AuthorisationAspect implements Advice {

    @Autowired
    private PrincipalExtractorImpl principalExtractor;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;

    /**
     * Checks if current user is in Coach subgroup and therefore have rights to changing team properties.
     * Called before all TeamService methods except createNewTeam and getTeamById.
     *
     * @param point joint point data
     * @throws EntityNotFoundException if user or team are not found
     */
    /*
    @Before("execution(public * *..TeamService.*(..)) " +
            "&& !execution(public * *..TeamService..getTeamById(..))" +
            " && !execution(public * *..TeamService..createNewTeam(..))")
    public void CoachAuthorisation(JoinPoint point) throws EntityNotFoundException {

        Team team;
        User user;
        String userEmail = principalExtractor.getPrincipalEmail();

        team = teamService.getTeamById((Long) point.getArgs()[0]);
        user = userService.findUserByEmail(userEmail);

        if (!team.getTeamSubgroup("Coaches").isUserInList(user)) {
            throw new RuntimeException("Access denied");
        }

    }
*/
    /**
     * Checks if currently authenticated user is not try to access or change details of other users.
     * Called before all UserService methods except newUserRegistration.
     *
     * @param point joint point data
     */
/*
    @Before("execution(public * *..UserService.*(..))" +
            " && !execution(public * *..UserService.newUserRegistration(..))")
    public void UserAuthorisation(JoinPoint point) {
        User user = null;
        String userEmail = principalExtractor.getPrincipalEmail();
        if (!userEmail.equals(point.getArgs()[0])) {
            throw new RuntimeException("Access denied");
        }
    }

 */
}
