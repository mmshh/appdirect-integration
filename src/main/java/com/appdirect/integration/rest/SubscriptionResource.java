package com.appdirect.integration.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/event/")
public class SubscriptionResource {

	@GET
	@Produces("text/plain")
	@Path("/test")
	public String testing(){
		return "Good call";
	}
}
