package com.team314.dcda.local.resources;

import java.util.Random;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team314.dcda.local.dao.UserDAO;
import com.team314.dcda.local.db.User;

@Path("/generate")
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class GenerateDataResource {

	public static final Logger LOG = LoggerFactory
			.getLogger(GenerateDataResource.class);

	@EJB
	private UserDAO userDAO;

	@Resource
	private UserTransaction utx;

	@PersistenceContext(unitName = "DCDA_Local_PU")
	protected EntityManager em;

	private static String[] email_providers = { "gmail.com", "yahoo.com",
			"live.com" };
	private static String[] roles = { "admin", "user" };

	@GET
	public void get() {
		for (int j = 0; j < 100; j++) {

			try {
				utx.begin();
				for (int i = 0; i < 10000; i++) {
					try {

						User user = new User();
						Random rand = new Random();
						user.setEmail("email" + rand.nextInt() + "@"
								+ email_providers[Math.abs(rand.nextInt() % 3)]);
						user.setFirstName("Firstname"
								+ Math.abs(rand.nextInt()));
						user.setGcmregid("GCMRegID" + Math.abs(rand.nextInt()));
						user.setGender(rand.nextInt() % 2);
						user.setLastName("Lastname" + Math.abs(rand.nextInt()));
						user.setPass("Password" + Math.abs(rand.nextInt()));
						user.setPhone(Integer.toString(Math.abs(rand.nextInt())));
						user.setRole(roles[Math.abs(rand.nextInt() % 2)]);
						this.userDAO.create(user);
						LOG.debug("Created user:" + user.getEmail());

					} catch (Exception e) {

						LOG.error("error", e);
					}
				}
				utx.commit();

			} catch (Exception e1) {
				LOG.error("error", e1);
			}
		}
	}

	enum Email {
		GOOGLE(1), YAHOO(2), LIVE(3);

		private int code;

		Email(int code) {
			this.code = code;
		}

		public String toString() {
			return this.name();
		}
	}
}
