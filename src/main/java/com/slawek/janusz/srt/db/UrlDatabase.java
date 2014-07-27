package com.slawek.janusz.srt.db;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class UrlDatabase implements Database {
	private static final Logger LOG = Logger.getLogger(UrlDatabase.class);
	private String prefix;
	private char base[];

	private Map<String, String> l2s;
	private Map<String, String> s2l;
	private int counter;

	public UrlDatabase(String prefix, String alphabet) {
		this.prefix = prefix;
		if (this.prefix == null || !isValidURL(prefix)) {
			throw new RuntimeException("prefix not valid");
		}

		base = removeDuplicates(alphabet).toCharArray();
		if (base == null || base.length <= 1) {
			throw new RuntimeException("alphabet not valid");
		}

		l2s = new HashMap<String, String>();
		s2l = new HashMap<String, String>();
		counter = 0;
	}

	public synchronized String shortenLongUrl(String longUrl) {
		String shortUrl = null;
		if (isValidURL(longUrl)) {
			if (l2s.containsKey(longUrl)) {
				shortUrl = l2s.get(longUrl);
			} else {
				shortUrl = num2str(counter++);
				l2s.put(longUrl, shortUrl);
				s2l.put(shortUrl, longUrl);
				LOG.debug("Mapping between " + shortUrl + " and " + longUrl
						+ " added. Counter now " + counter + ".");
			}
		}
		return shortUrl;
	}

	public synchronized String lookupShortUrl(String shortUrl) {
		String longUrl = null;
		if (isValidURL(shortUrl)) {
			if (s2l.containsKey(shortUrl)) {
				longUrl = s2l.get(shortUrl);
			}
		}
		return longUrl;
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

	private String num2str(int n) {
		StringBuffer result = new StringBuffer();
		do {
			result.append(base[n % base.length]);
			n /= base.length;
		} while (n > 0);
		return prefix + result.reverse().toString();
	}

	private String removeDuplicates(String s) {
		char[] chars = s.toCharArray();
		Set<Character> charSet = new HashSet<Character>();
		for (char c : chars) {
			charSet.add(c);
		}

		StringBuilder sb = new StringBuilder();
		for (Character character : charSet) {
			sb.append(character);
		}
		return sb.toString();
	}
}
