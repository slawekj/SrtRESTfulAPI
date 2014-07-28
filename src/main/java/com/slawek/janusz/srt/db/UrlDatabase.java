package com.slawek.janusz.srt.db;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * This class implements Database for URLs. It maps long URLs to short URLs and
 * retrieves long URLs from short ones. It has additional private helper methods
 * to validate URLs, remove duplicates from an alphabet, convert number to a
 * representation in the alphabet. This Database is thread-safe, e.g. multiple
 * threads are allowed to invoke public methods.
 * 
 * @author janusz
 * 
 */
public class UrlDatabase implements Database {
	private static final Logger LOG = Logger.getLogger(UrlDatabase.class);
	private String prefix;
	private char base[];

	private Map<String, String> l2s;
	private Map<String, String> s2l;
	private int counter;

	/**
	 * This is a sole constructor for the UrlDatabase. It creates an object with
	 * custom prefix and alphabet.
	 * 
	 * @param prefix
	 *            It is a URL prefix of the service, all URLs are shortened to
	 *            this domain.
	 * @param alphabet
	 *            It is an alphabet of short URLs, i.e. characters from the
	 *            alphabet are used to construct an output URL, except from
	 *            prefix. Duplicate characters are removed from alphabet. Also,
	 *            characters from alphabet are shuffled.
	 */
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

	/**
	 * This method maps a long URL to a short URL and returns a short URL. It
	 * works in constant time in function of the number of existing mappings in
	 * the database.
	 */
	public synchronized String map(String longUrl) {
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

	/**
	 * This method returns a reverse mapping, i.e. what initial long URL was
	 * used to construct a short URL. It works in constant time in function of
	 * the number of existing mappings in the database.
	 */
	public synchronized String lookup(String shortUrl) {
		String longUrl = null;
		if (isValidURL(shortUrl)) {
			if (s2l.containsKey(shortUrl)) {
				longUrl = s2l.get(shortUrl);
			}
		}
		return longUrl;
	}

	/**
	 * This is a helper function. It checks if input is a valid URL.
	 * 
	 * @param url
	 * @return
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

	/**
	 * This function converts a number to a string representation. For example
	 * in alphabet {k,l,m} consecutive numbers are represented as follows: 0 -
	 * k, 1 - l, 2 - m, 3 - lk, 4 - ll, 5 - lm, etc...
	 * 
	 * @param n
	 * @return
	 */
	private String num2str(int n) {
		StringBuffer result = new StringBuffer();
		do {
			result.append(base[n % base.length]);
			n /= base.length;
		} while (n > 0);
		return prefix + result.reverse().toString();
	}

	/**
	 * This function removes duplicates from a string and shuffles characters of
	 * that string.
	 * 
	 * @param s
	 * @return
	 */
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
