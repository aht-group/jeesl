package org.jeesl.controller.io.mail;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliMail
{
	final static Logger logger = LoggerFactory.getLogger(CliMail.class);

    public static String cliDebug = "mail.cli.debug";
    public static String cliObfuscate = "mail.cli.obfuscate";
    public static String cliSave = "mail.cli.save";
    public static String cliLang = "mail.cli.lang";

    public static void debugCliConfig(Configuration config)
    {
        logger.warn(cliDebug+" "+config.getBoolean(cliDebug));
        logger.warn(cliObfuscate+" "+config.getBoolean(cliObfuscate));
        logger.warn(cliSave+" "+config.getBoolean(cliSave));
        logger.warn(cliLang+" "+config.getString(cliLang));
    }
}
