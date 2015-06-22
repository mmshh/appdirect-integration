package com.appdirect.integration.rest;

import java.io.IOException;
import java.net.URLDecoder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.appdirect.integration.util.Authorization;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

@Path("/event/")
public class SubscriptionResource {

	@GET
	@Produces("application/xml")
	@Path("/test")
	public String testing(@Context UriInfo uriInfo, @QueryParam("url") String encodedUrl, @QueryParam("token") String token) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException{
		
		String url = URLDecoder.decode(encodedUrl, "UTF-8");
		String content = Authorization.sign(url, token);
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<result>\n" +
                "    <success>false</success>\n" +
                "    <errorCode>USER_ALREADY_EXISTS</errorCode>\n" +
                "    <message>"+token+"</message>\n" +
                "</result>";
	}
	
}
