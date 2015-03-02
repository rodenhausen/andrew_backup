package edu.arizona.biosemantics.oto.oto.rest;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.arizona.biosemantics.oto.common.model.GlossaryDictionaryEntry;
import edu.arizona.biosemantics.oto.oto.beans.User;
import edu.arizona.biosemantics.oto.oto.db.UserDataAccess;
import edu.arizona.biosemantics.oto.oto.rest.db.GlossaryDictionaryDAO;

@Path("/termCategories")
public class GlossaryDictionaryResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;		
	
	private Logger logger;
	
	public GlossaryDictionaryResource() {
		logger =  LoggerFactory.getLogger(this.getClass());
	}
	
	@Path("/{glossaryType}/{term}/{category}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public GlossaryDictionaryEntry insertAndGetGlossaryDictionaryEntry(@PathParam("glossaryType") String glossaryType, @PathParam("term") String term, @PathParam("category") String category,
			GlossaryDictionaryEntryData data) {
		GlossaryDictionaryEntry result = null;
		
		//verify login credentials
		User user = new User();
		user.setUserEmail(data.getLoginData().getUserEmail());
		user.setPassword(data.getLoginData().getUserPassword());
		
		try {
    		UserDataAccess uaccess = new UserDataAccess();
        	if(uaccess.validateUser(user)) {
        		
        		try {
        			result = GlossaryDictionaryDAO.getInstance().insertAndGetGlossaryDictionaryEntry(glossaryType, term, category, data.getTermDescription());
        		} catch (Exception e) {
        			logger.error("Exception " + e.toString());
        			e.printStackTrace();
        		}
        		return result;
        		
        	} else { // invalid login 
        		return null;
        	}
    	} catch (Exception exe) {
    		logger.error("Exception " + exe.toString());
    		exe.printStackTrace();
    		return null;
    	}
	}
	
	@Path("/{glossaryType}/{term}/{category}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public GlossaryDictionaryEntry getGlossaryDictionaryEntry(@PathParam("glossaryType") String glossaryType, @PathParam("term") String term, @PathParam("category") String category) {
		GlossaryDictionaryEntry result = null;
		try {
			result = GlossaryDictionaryDAO.getInstance().getGlossaryDictionaryEntry(glossaryType, term, category);
		} catch (Exception e) {
			logger.error("Exception " + e.toString());
			e.printStackTrace();
		}
		return result;
	}
	
	@Path("/{glossaryType}/{term}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<GlossaryDictionaryEntry> getGlossaryDictionaryEntries(@PathParam("glossaryType") String glossaryType, @PathParam("term") String term) {
		List<GlossaryDictionaryEntry> result = new LinkedList<GlossaryDictionaryEntry>();
		try {
			result = GlossaryDictionaryDAO.getInstance().getGlossaryDictionaryEntries(glossaryType, term);
		} catch (Exception e) {
			logger.error("Exception " + e.toString());
			e.printStackTrace();
		}
		return result;
	}
	
}
