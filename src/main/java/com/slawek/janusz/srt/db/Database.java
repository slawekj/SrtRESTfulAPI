package com.slawek.janusz.srt.db;

public interface Database {

	public String shortenLongUrl(String longUrl);

	public String lookupShortUrl(String shortUrl);

}
