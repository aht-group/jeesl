package org.jeesl.factory.txt.io.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSqlDbMetaFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtSqlDbMetaFactory.class);

	public static String dbFromPostgresUrl(String url)
	{
		int lastSlashIndex = url.lastIndexOf('/');
		if (lastSlashIndex >= 0)
		{
		    String afterSlash = url.substring(lastSlashIndex + 1);
		    int questionMarkIndex = afterSlash.indexOf('?');

		    return (questionMarkIndex >= 0)
		        ? afterSlash.substring(0, questionMarkIndex)
		        : afterSlash;
		}
		return null;
	}
}