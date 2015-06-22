package com.appdirect.integration.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;




public class Authorization {
	
	private static final Logger log = Logger.getLogger( Authorization.class.getName() );
	
	public static String sign(String eventUrl, String token) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{

		PropertyReader propReader = new PropertyReader();
		String key, secret;
		try{
			key = propReader.getPropertyValue("consumer.key");
			secret = propReader.getPropertyValue("consumer.secret");
		} catch(IOException e){
			log.severe("Unable to read oauth properties from properties file.");
			throw e;
		}
		
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(key, secret);
		HttpGet request = new HttpGet(eventUrl);
		
		consumer.sign(request);
		
		HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
        
		//return request.getHeaderField("Authorization");
        return response.toString();
	}
}
