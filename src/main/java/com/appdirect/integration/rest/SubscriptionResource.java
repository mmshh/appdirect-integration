package com.appdirect.integration.rest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.appdirect.integration.dto.Event;
import com.appdirect.integration.dto.Payload;
import com.appdirect.integration.dto.Result;
import com.appdirect.integration.entity.CustomerOrder;
import com.appdirect.integration.util.EventProcessor;

@Path("/event/")
public class SubscriptionResource {

	private static final Logger log = Logger.getLogger( SubscriptionResource.class.getName() );

	@GET
	@Produces("application/xml")
	@Path("/create")
	public Response createOrder(@QueryParam("url") String encodedUrl) {
		String url;
		Result response = null;
		Event event = null;
		try {
			//decode url
			url = URLDecoder.decode(encodedUrl, "UTF-8");

			//get the data
			StringBuffer xmlContent;
			xmlContent = EventProcessor.fetchOrder(url);

			//unmarshal to event object
			JAXBContext jc = JAXBContext.newInstance(Event.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			event = (Event) unmarshaller.unmarshal( new StreamSource( new StringReader( xmlContent.toString() ) ) );
		}catch (UnsupportedEncodingException e) {
			response = new Result(false, "500", "Unable to decode event url. ");
		}catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException | IOException e) {
			response = new Result(false, "401", "Authorization Failed. ");
		}catch (JAXBException e) {
			log.severe("Unmarshalling of XML data failed. ");
			response = new Result(false, "500", "Unmarshalling of XML data failed.");
		}

		if (event!=null){
			Payload payload = event.getPayload();

			CustomerOrder customerOrder = new CustomerOrder(payload.getCompany().getUuid(),event.getCreator().getFirstName(), 
					payload.getOrder().getEditionCode());

			EventProcessor.saveOrder(customerOrder);
			response = new Result(true, "200", "Order succesfully created. ");

		}
		String xmlResponse = generateResponse(response);

		return Response.ok(xmlResponse, MediaType.APPLICATION_XML).build();
	}

	@GET
	@Produces("text/plain")
	@Path("/customers")
	public String customerList(){
		List<CustomerOrder> orderList = EventProcessor.listOrders();

		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < orderList.size(); i++){
			sb.append("Customer #"+ (i+1) + "\n");
			sb.append(orderList.get(i).toString());
			sb.append("\n\n");
		}
		return sb.toString();
	}
	public static String generateResponse(Result result) {
		final StringWriter sw = new StringWriter();
		try {
		final Marshaller marshaller = JAXBContext.newInstance(Result.class)
				.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		marshaller.marshal(result, sw);
		}catch (JAXBException e) {
			log.warning("Unable to convert response to xml. ");
			return null;
		}
		return sw.toString();
	}
}
