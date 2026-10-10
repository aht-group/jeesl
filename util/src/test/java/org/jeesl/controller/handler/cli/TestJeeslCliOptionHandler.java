package org.jeesl.controller.handler.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJeeslCliOptionHandler
{
	final static Logger logger = LoggerFactory.getLogger(TestJeeslCliOptionHandler.class);
	
	private JeeslCliOptionHandler build()
	{
		JeeslCliOptionHandler jco = new JeeslCliOptionHandler("0.0.0-test");
		jco.buildHelp();
		jco.buildDebug();
		jco.buildLogFile();
		jco.buildConfig();
		return jco;
	}
	
	private CommandLine parse(JeeslCliOptionHandler jco, String... args) throws Exception
	{
		return new DefaultParser().parse(jco.getOptions(), args);
	}
	
	@Test
	public void options()
	{
		Options options = this.build().getOptions();
		Assert.assertTrue("the option set carries help",options.hasOption("help"));
		Assert.assertTrue("the option set carries debug",options.hasOption("debug"));
		Assert.assertTrue("the option set carries logFile",options.hasOption("logFile"));
		Assert.assertTrue("the option set carries config",options.hasOption("config"));
	}
	
	@Test
	public void profileDefault() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		Assert.assertEquals("app.log4j2.xml",jco.loggingProfile(this.parse(jco)));
	}
	
	@Test
	public void profileDebug() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		Assert.assertEquals("debug.log4j2.xml",jco.loggingProfile(this.parse(jco,"-debug")));
	}
	
	@Test
	public void profileLogFile() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		Assert.assertEquals("file.log4j2.xml",jco.loggingProfile(this.parse(jco,"-logFile")));
	}
	
	@Test
	public void helpPrintsAndExitsWithZero() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		AtomicInteger exitCode = new AtomicInteger(Integer.MIN_VALUE);
		jco.setExitHandler(exitCode::set);
		
		CommandLine line = this.parse(jco,"-help");
		
		PrintStream original = System.out;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String help;
		System.setOut(new PrintStream(baos,true,StandardCharsets.UTF_8.name()));
		try {jco.handleHelp(line);}
		finally {System.setOut(original);}
		help = new String(baos.toByteArray(),StandardCharsets.UTF_8);
		
		Assert.assertEquals("the process ends with exit code 0",0,exitCode.get());
		Assert.assertTrue("the help text lists the help option",help.contains("help"));
		Assert.assertTrue("the help text lists the debug option",help.contains("debug"));
	}
}
