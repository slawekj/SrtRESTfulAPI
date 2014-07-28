package com.slawek.janusz.srt.db;

/**
 * This interface defines what a database do. There are two methods, one for
 * mapping input string to another (shorter) string, and one for looking up what
 * a short string maps to. Both operations are guaranteed to have constant time
 * complexity.
 * 
 * @author janusz
 * 
 */
public interface Database {

	public String map(String longString);

	public String lookup(String shortString);

}
