package com.appdirect.integration.rest;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.sun.jersey.api.core.HttpContext;

@Path("/event/")
public class SubscriptionResource {

	@GET
	@Produces("text/plain")
	@Path("/test")
	public String testing(@Context HttpContext context){
		return context.getRequest().getRequestUri().toASCIIString();
	}
}
