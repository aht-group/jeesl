package org.jeesl.controller.handler.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
		Assertions.assertTrue(options.hasOption("help"),"the option set carries help");
		Assertions.assertTrue(options.hasOption("debug"),"the option set carries debug");
		Assertions.assertTrue(options.hasOption("logFile"),"the option set carries logFile");
		Assertions.assertTrue(options.hasOption("config"),"the option set carries config");
	}
	
	@Test
	public void profileDefault() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		Assertions.assertEquals("app.log4j2.xml",jco.loggingProfile(this.parse(jco)));
	}
	
	@Test
	public void profileDebug() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		Assertions.assertEquals("debug.log4j2.xml",jco.loggingProfile(this.parse(jco,"-debug")));
	}
	
	@Test
	public void profileLogFile() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		Assertions.assertEquals("file.log4j2.xml",jco.loggingProfile(this.parse(jco,"-logFile")));
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
		
		Assertions.assertEquals(0,exitCode.get(),"the process ends with exit code 0");
		Assertions.assertTrue(help.contains("help"),"the help text lists the help option");
		Assertions.assertTrue(help.contains("debug"),"the help text lists the debug option");
	}
}
