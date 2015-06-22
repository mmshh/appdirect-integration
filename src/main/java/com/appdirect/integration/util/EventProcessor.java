package com.appdirect.integration.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;




public class EventProcessor {
	
	private static final Logger log = Logger.getLogger( EventProcessor.class.getName() );
	
	public static String fetchOrder(String eventUrl) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{

		PropertyReader propReader = new PropertyReader();
		String key, secret;
		try{
			key = propReader.getPropertyValue("consumer.key");
			secret = propReader.getPropertyValue("consumer.secret");
		} catch(IOException e){
			log.severe("Unable to read oauth properties from properties file.");
			throw e;
		}
		
		OAuthConsumer consumer = new DefaultOAuthConsumer(key, secret);
		URL url = new URL(eventUrl);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		consumer.sign(request);
		
		String orderContent = getOrderContent(request);
		
		return orderContent;
		
	}
	
	private static String getOrderContent(HttpURLConnection request) throws IOException{
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(request.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
        return response.toString();
	}
}
