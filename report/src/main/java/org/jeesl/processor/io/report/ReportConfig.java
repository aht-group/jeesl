package org.jeesl.processor.io.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportConfig
{
	final static Logger logger = LoggerFactory.getLogger(ReportConfig.class);

    public static String cliDebug = "report.cli.debug";
    public static String cliObfuscate = "report.cli.obfuscate";
    public static String cliSave = "report.cli.save";
    public static String cliLang = "report.cli.lang";
    public static String cliOutputDir = "report.cli.dir";
      
    public static void debugCliConfig(org.exlp.interfaces.system.property.Configuration config)
    {
        logger.info(cliDebug+" "+config.getBoolean(cliDebug));
        logger.info(cliObfuscate+" "+config.getBoolean(cliObfuscate));
        logger.info(cliSave+" "+config.getBoolean(cliSave));
        logger.info(cliLang+" "+config.getString(cliLang));
        logger.info(cliOutputDir+" "+config.getString(cliOutputDir));
    }
}
