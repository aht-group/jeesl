package org.jeesl.factory.txt.system.io.db;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSqlQueryFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtSqlQueryFactory.class);

	public static String shortenIn(String query)
	{
		// Query pattern eg : in ($1 , $2 , $3 , $4 , $5 , $6 , $7 , $8 , $9))
		String regex = " [iI][nN] \\(\\$[0-9]+([^)]*)\\$[0-9]+\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(query);

		boolean hasInQueryQuestionMarks = false;
		if (!matcher.find())
		{
			// Query pattern eg: IN (?,?,?)
			regex = " [iI][nN] \\(([\\?,]+)\\)";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(query);

			if (matcher.find()) {
				hasInQueryQuestionMarks = true;
			}
		}

		Stack<Integer> startPositions = new Stack<>();
		Stack<Integer> endPositions = new Stack<>();
		matcher.reset();

		while (matcher.find()) {
			startPositions.push(matcher.start(1));
			endPositions.push(matcher.end(1));
		}

		StringBuilder sb = new StringBuilder(query);
		while (!startPositions.isEmpty())
		{
			int start = startPositions.pop();
			int end = endPositions.pop();

			if (start >= 0 && end >= 0) {
				if (hasInQueryQuestionMarks) {
					String questionMarks = sb.substring(start, end);
					int numberOfQuestionMarks = questionMarks.replaceAll("[^\\?]", "").length();
					sb.replace(start, end, "1?..." + numberOfQuestionMarks + "?");
				} else {
					sb.replace(start, end, "...");
				}
			}
		}
		return sb.toString();
	}
	
	public static String toXhtml(String query)
	{
		query = query.replaceAll("select ", "<span class=\"jeesl-sql-reserved\">SELECT</span> ");
		query = query.replaceAll(" from ", " <span class=\"jeesl-sql-reserved\">FROM</span> ");
		return query;
	}
}