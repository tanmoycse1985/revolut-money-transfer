package com.moneytransfer.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.moneytransfer.dao.MasterDAO;
import com.moneytransfer.exception.RevolutException;
import com.moneytransfer.model.User;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
 
	private final MasterDAO masterDAO = MasterDAO.getDAOFactory(MasterDAO.H2);
    
	private static Logger log = Logger.getLogger(UserService.class);

	/**
	 * Find by userName
	 * @param userName
	 * @return
	 * @throws RevolutException
	 */
    @GET
    @Path("/{userName}")
    public User getUserByName(@PathParam("userName") String userName) throws RevolutException {
        if (log.isDebugEnabled())
            log.debug("Request Received for get User by Name " + userName);
        final User user = masterDAO.getUserDAO().getUserByName(userName);
        if (user == null) {
            throw new WebApplicationException("User Not Found", Response.Status.NOT_FOUND);
        }
        return user;
    }
    
    /**
	 * Find by all
	 * @param userName
	 * @return
	 * @throws RevolutException
	 */
    @GET
    @Path("/all")
    public List<User> getAllUsers() throws RevolutException {
        return masterDAO.getUserDAO().getAllUsers();
    }
    
    /**
     * Create User
     * @param user
     * @return
     * @throws RevolutException
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public User createUser(User user) throws RevolutException {
        if (masterDAO.getUserDAO().getUserByName(user.getUserName()) != null) {
            throw new WebApplicationException("User name already exist", Response.Status.BAD_REQUEST);
        }
        final long uId = masterDAO.getUserDAO().insertUser(user);
        return masterDAO.getUserDAO().getUserById(uId);
    }
    
    /**
     * Find by User Id
     * @param userId
     * @param user
     * @return
     * @throws RevolutException
     */
    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") long userId, User user) throws RevolutException {
        final int updateCount = masterDAO.getUserDAO().updateUser(userId, user);
        if (updateCount == 1) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    /**
     * Delete by User Id
     * @param userId
     * @return
     * @throws RevolutException
     */
    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long userId) throws RevolutException {
        int deleteCount = masterDAO.getUserDAO().deleteUser(userId);
        if (deleteCount == 1) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
