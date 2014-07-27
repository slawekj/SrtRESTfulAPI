package com.slawek.janusz.srt.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.slawek.janusz.srt.db.UrlDatabase;
import com.slawek.janusz.srt.rest.SrtService;

public class SrtServiceUnitTest {

	@Test
	public void testServiceCreation() {
		SrtService s = new SrtService();
		ReflectionTestUtils.setField(s, "db", new UrlDatabase("http://prefix/",
				"alphabet"));
		assertEquals(200, s.shorten("http://test/a").getStatus());
	}

	@Test
	public void testSimpleMapping() {
		SrtService s = new SrtService();
		ReflectionTestUtils.setField(s, "db", new UrlDatabase("http://prefix/",
				"ab"));
		String shortUrl1 = (String) s.shorten("http://test/a").getEntity();
		String shortUrl2 = (String) s.shorten("http://test/b").getEntity();
		String shortUrl3 = (String) s.shorten("http://test/c").getEntity();

		assertEquals("http://test/a", (String) s.lookup(shortUrl1).getEntity());
		assertEquals("http://test/b", (String) s.lookup(shortUrl2).getEntity());
		assertEquals("http://test/c", (String) s.lookup(shortUrl3).getEntity());
	}

	@Test
	public void testMultipleServletsUsingSingleDB() {
		UrlDatabase soleDb = new UrlDatabase("http://prefix/", "ab");
		SrtService s1 = new SrtService();
		ReflectionTestUtils.setField(s1, "db", soleDb);
		SrtService s2 = new SrtService();
		ReflectionTestUtils.setField(s2, "db", soleDb);

		String shortUrl1 = (String) s1.shorten("http://test/a").getEntity();
		String shortUrl2 = (String) s1.shorten("http://test/b").getEntity();
		String shortUrl3 = (String) s2.shorten("http://test/c").getEntity();
		String shortUrl4 = (String) s2.shorten("http://test/d").getEntity();

		assertEquals("http://test/a", (String) s2.lookup(shortUrl1).getEntity());
		assertEquals("http://test/b", (String) s2.lookup(shortUrl2).getEntity());
		assertEquals("http://test/c", (String) s1.lookup(shortUrl3).getEntity());
		assertEquals("http://test/d", (String) s1.lookup(shortUrl4).getEntity());
	}
}
