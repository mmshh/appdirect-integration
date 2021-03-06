package com.appdirect.integration.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.appdirect.integration.entity.CustomerOrder;
import com.googlecode.objectify.ObjectifyService;




public class EventProcessor {

	private static final Logger log = Logger.getLogger( EventProcessor.class.getName() );
	
	public static StringBuffer fetchOrder(String eventUrl) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{

		PropertyReader propReader = new PropertyReader();
		String key, secret;
		try{
			key = propReader.getPropertyValue("consumer.key");
			secret = propReader.getPropertyValue("consumer.secret");
		} catch(IOException e){
			log.severe("Unable to read oauth properties from properties file.");
			throw e;
		}
		//Sign oauth
		OAuthConsumer consumer = new DefaultOAuthConsumer(key, secret);
		URL url = new URL(eventUrl);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		consumer.sign(request);

		return getOrderContent(request);

	}

	private static StringBuffer getOrderContent(HttpURLConnection request) throws IOException{
		//read in the data after auth
		BufferedReader in = new BufferedReader(
				new InputStreamReader(request.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response;
	}

	public static void saveOrder(CustomerOrder customerOrder) {
		
		ObjectifyService.register(CustomerOrder.class);
		ObjectifyService.ofy().save().entity(customerOrder).now();

	}

	public static List<CustomerOrder> listOrders(){
		
		ObjectifyService.register(CustomerOrder.class);
		List<CustomerOrder> orderlist = ObjectifyService.ofy().load().type(CustomerOrder.class).list();
		
		return orderlist;
	}
}
