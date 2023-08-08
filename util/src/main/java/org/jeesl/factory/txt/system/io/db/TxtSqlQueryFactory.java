package org.jeesl.factory.txt.system.io.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

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
		// Mark keywords for partial statements
		Matcher matcher = Pattern.compile("\\b(select|from|where|order by|(left |inner )?join|alter table|add constraint|foreign key|match simple|(add|rename) column|update|insert into|values|commit|rollback|begin|show)\\b", Pattern.CASE_INSENSITIVE).matcher(query);
		StringBuffer result = new StringBuffer();
		
		while (matcher.find()) {
			matcher.appendReplacement(result, "<b>" + matcher.group(1).toUpperCase() + "</b>"); 
		}
		matcher.appendTail(result);
		
		// Mark partial statements as div
		matcher = Pattern.compile("(^|<b>).+?(?=<b>|$)", Pattern.DOTALL).matcher(result.toString());
		result.delete(0, result.length());
		
		while (matcher.find()) {
			result.append("<div>" + matcher.group() + "</div>");
		}
		
		// Mark keywords inside statements
		matcher = Pattern.compile("\\b(as|and|or|exists|all|desc|is null|not null|not in|on( update| delete)?|no action|if exists|references|to|collate|set( role)?|rename to|returning|transaction isolation level|read committed|session characteristics|count)\\b", Pattern.CASE_INSENSITIVE).matcher(result.toString());
		result.delete(0, result.length());
		
		while (matcher.find()) {
			matcher.appendReplacement(result, "<b>" + matcher.group(1).toUpperCase() + "</b>"); 
		}
		matcher.appendTail(result);
		
		Set<String> synonymSet = new HashSet<String>();
		matcher = Pattern.compile("\\w+(?=\\.\\w+)").matcher(result.toString());
		while (matcher.find()) {
			synonymSet.add(matcher.group());
		}
		List<String> synonymList = new ArrayList<String>(synonymSet);
		IntStream.range(0, synonymSet.size())
				 .forEach(index -> {
					 Matcher sMatcher = Pattern.compile("(?<!\\.)\\b" + synonymList.get(index) + "\\b").matcher(result.toString());
					 result.delete(0, result.length());
					 
					 while (sMatcher.find()) {
						 sMatcher.appendReplacement(result, "<span class=\"jeesl-brewer-" + (index + 1) + "\">" + sMatcher.group() + "</span>");
					 }
					 sMatcher.appendTail(result);
				 });

//		query = query.replaceAll("select ", "<span class=\"jeesl-sql-reserved\">SELECT</span> ");
//		query = query.replaceAll(" from ", " <span class=\"jeesl-sql-reserved\">FROM</span> ");
		return result.toString();
	}
}