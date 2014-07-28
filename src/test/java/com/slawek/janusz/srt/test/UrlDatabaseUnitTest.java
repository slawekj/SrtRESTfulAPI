package com.slawek.janusz.srt.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.slawek.janusz.srt.db.Database;
import com.slawek.janusz.srt.db.UrlDatabase;
import com.slawek.janusz.srt.rest.SrtService;

public class UrlDatabaseUnitTest {

	@Test(expected = RuntimeException.class)
	public void testInvalidPrefix() {
		Database db = new UrlDatabase(null, "alphabet");
	}

	@Test(expected = RuntimeException.class)
	public void testInvalidAlphabet() {
		Database db = new UrlDatabase("http://srt.com/", "a");
	}

	@Test(expected = RuntimeException.class)
	public void testInvalidAlphabet2() {
		Database db = new UrlDatabase("http://srt.com/", "aaa");
	}

	@Test
	public void testBasicDifference() {
		Database db = new UrlDatabase("http://srt.com/", "alphabet");
		assertNotSame(db.map("http://a"),
				db.map("http://b"));
	}

	@Test
	public void testSimpleMapping() {
		Database db = new UrlDatabase("http://srt.com/", "alphabet");
		String shortUrl = db.map("http://a");
		assertEquals("http://a", db.lookup(shortUrl));
	}
}
