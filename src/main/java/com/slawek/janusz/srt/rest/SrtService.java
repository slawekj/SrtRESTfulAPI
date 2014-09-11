package com.slawek.janusz.srt.rest;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.slawek.janusz.srt.db.Database;

/**
 * This class is responsible for processing requests from users. It receives GET
 * HTTP requests to shorten URLs or lookup URLs.
 * 
 * @author janusz
 * 
 */
@Component
@Path("/")
public class SrtService {

	/**
	 * Inversion of control, by Spring framework, is used to inject this field.
	 * This is a database that will store all the mappings between short and
	 * long URLs.
	 */
	@Autowired
	Database db;

	/**
	 * This method is responsible for shortening requests.
	 * 
	 * @param url Long url to be shortened
	 * @return HTTP response with shortened url
	 */
	@GET
	@Path("/s")
	public Response shorten(@QueryParam("url") String url) {
		String shortUrl;

		if (isValidURL(url)) {
			shortUrl = db.map(url);
		} else {
			shortUrl = "url not valid";
		}

		return Response.status(200).entity(shortUrl).build();
	}

	/**
	 * This method is responsible for lookup requests.
	 * 
	 * @param url Short url to be looked-up
	 * @return HTTP response with long url
	 */
	@GET
	@Path("/l")
	public Response lookup(@QueryParam("url") String url) {
		String response;
		String longUrl;

		if (isValidURL(url)) {
			longUrl = db.lookup(url);
			response = longUrl == null ? "url not found" : longUrl;
		} else {
			response = "url not valid";
		}

		return Response.status(200).entity(response).build();
	}

	/**
	 * This is a helper method that checks if input is a valid URL.
	 * 
	 * @param url Url to be validated
	 * @return true if it is a valid url, false otherwise
	 */
	private boolean isValidURL(String url) {
		boolean valid = true;
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			valid = false;
		}
		return valid;
	}
}
