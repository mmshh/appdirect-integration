package com.appdirect.integration.rest;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.appdirect.integration.dto.Event;
import com.appdirect.integration.entity.CustomerOrder;
import com.appdirect.integration.util.EMF;
import com.appdirect.integration.util.EventProcessor;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

@Path("/event/")
public class SubscriptionResource {
	
	@GET
	@Produces("application/xml")
	@Path("/test")
	public String testing(@QueryParam("url") String encodedUrl) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, JAXBException{

		//decode url
		String url = URLDecoder.decode(encodedUrl, "UTF-8");
		//get the data
		StringBuffer xmlContent = EventProcessor.fetchOrder(url);

		//unmarshal to event object
		JAXBContext jc = JAXBContext.newInstance(Event.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Event event = (Event) unmarshaller.unmarshal( new StreamSource( new StringReader( xmlContent.toString() ) ) );
		
		CustomerOrder customerOrder = new CustomerOrder(event.getCreator().getFirstName(), 
				event.getPayload().getOrder().getEditionCode());
		
		EventProcessor.persist(customerOrder);

		return generateResponse(true, "200", "ITS OKAY");
	}

	public static String generateResponse(boolean success, String errorCode, String message){
		String response = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
				"<result>\n" +
				"    <success>" + success + "</success>\n" +
				"    <errorCode>"+ errorCode +"</errorCode>\n" +
				"    <message>"+ message +"</message>\n" +
				"</result>";

		return response;
	}
}
