package org.jeesl.jsf.functions;

import org.apache.commons.lang.StringEscapeUtils;

public final class StringEscape
{
	 public static String escapeJavaScript(String value)
	 {
		return StringEscapeUtils.escapeJavaScript(value);
	 }

  //--------------------------------------------------------------------------
    /**
     * <p>Escapes the characters in a <code>String</code> using Java String rules.</p>
     *
     * <p>Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     *
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote must be escaped.</p>
     *
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, \"Stop!\"
     * </pre>
     * </p>
     *
     * @param str  String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string input
     */
	 public static String escapeJava(String value)
	 {
		return StringEscapeUtils.escapeJava(value);
	 }

	 public static String escapeHtml(String value)
	 {
		return StringEscapeUtils.escapeHtml(value);
	 }

	 /**
     * <p>Escapes the characters in a <code>String</code> to be suitable to pass to
     * an SQL query.</p>
     *
     * <p>For example,
     * <pre>statement.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" +
     *   StringEscapeUtils.escapeSql("McHale's Navy") +
     *   "'");</pre>
     * </p>
     *
     * <p>At present, this method only turns single-quotes into doubled single-quotes
     * (<code>"McHale's Navy"</code> => <code>"McHale''s Navy"</code>). It does not
     * handle the cases of percent (%) or underscore (_) for use in LIKE clauses.</p>
     *
     * see http://www.jguru.com/faq/view.jsp?EID=8881
     * @param str  the string to escape, may be null
     * @return a new String, escaped for SQL, <code>null</code> if null string input
     */
	 public static String escapeSql(String value)
	 {
		return StringEscapeUtils.escapeSql(value);
	 }

}
