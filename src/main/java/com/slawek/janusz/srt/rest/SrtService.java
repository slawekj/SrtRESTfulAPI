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

@Component
@Path("/")
public class SrtService {

	@Autowired
	Database db;

	@GET
	@Path("/s")
	public Response shorten(@QueryParam("url") String url) {
		String shortUrl;

		if (isValidURL(url)) {
			shortUrl = db.shortenLongUrl(url);
		} else {
			shortUrl = "url not valid";
		}

		return Response.status(200).entity(shortUrl).build();
	}

	@GET
	@Path("/l")
	public Response lookup(@QueryParam("url") String url) {
		String response = null;
		String longUrl = null;

		if (isValidURL(url)) {
			longUrl = db.lookupShortUrl(url);
			response = longUrl == null ? "url not found" : longUrl;
		} else {
			response = "url not valid";
		}

		return Response.status(200).entity(response).build();
	}

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
