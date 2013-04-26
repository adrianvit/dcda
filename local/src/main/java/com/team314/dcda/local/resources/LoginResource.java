package com.team314.dcda.local.resources;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.team314.dcda.local.dao.LoginDAO;
import com.team314.dcda.local.db.LoggedUser;

@Stateless
@Path("/login")
public class LoginResource {

	@EJB
    private LoginDAO loginDAO;

    @POST
    @Produces({MediaType.APPLICATION_JSON })
    public Response login(@QueryParam("email") String email, @QueryParam("password") String password) {
        if (email != null && password != null) {
        	
        	LoggedUser loggedUser = loginDAO.login(email, password);
            if (loggedUser == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            } else {
                return Response.ok().entity(loggedUser).build();
            }

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
