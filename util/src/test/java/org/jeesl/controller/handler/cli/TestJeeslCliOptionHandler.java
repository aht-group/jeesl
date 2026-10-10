package org.jeesl.controller.handler.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
	
	@Test
	public void parseErrorPrintsHelpAndExitsWithZero() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		AtomicInteger exitCode = new AtomicInteger(Integer.MIN_VALUE);
		jco.setExitHandler(exitCode::set);
		
		boolean rejected = false;
		try {this.parse(jco,"-unknownOption");}
		catch (ParseException e) {rejected = true;jco.help();}
		
		Assertions.assertTrue(rejected,"the argument parser rejects the command line");
		Assertions.assertEquals(0,exitCode.get(),"the process ends with exit code 0");
	}
	
	@Test
	public void configFileIsLoaded() throws Exception
	{
		Path dir = Files.createTempDirectory("jeesl-cli-config-");
		Path file = dir.resolve("override.properties");
		Files.write(file,("test.override=yes"+System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
		
		JeeslCliOptionHandler jco = this.build();
		CommandLine line = this.parse(jco,"-config",file.toString());
		
		Assertions.assertEquals("yes",jco.config2Wrapper(line,"").getString("test.override"),"the configuration of the file is loaded");
	}
	
	@Test
	public void exlpSelectsCentralConfiguration() throws Exception
	{
		Path dir = Files.createTempDirectory("jeesl-cli-config-");
		Path file = dir.resolve("central.properties");
		Files.write(file,("test.central=yes"+System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
		
		Path home = Files.createTempDirectory("jeesl-cli-home-");
		Path m2 = Files.createDirectories(home.resolve(".m2"));
		String pointer = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			+"<io:dir xmlns:io=\"http://exlp.sf.net/io\">"
			+"<io:dir code=\"TESTAPP\"><io:file code=\"TESTCODE\" name=\""+file.toString()+"\"/></io:dir>"
			+"</io:dir>";
		Files.write(m2.resolve("exlp.xml"),pointer.getBytes(StandardCharsets.UTF_8));
		
		JeeslCliOptionHandler jco = this.build();
		jco.setExlpApp("TESTAPP");
		jco.setExlpCode("TESTCODE");
		CommandLine line = this.parse(jco,"-config","exlp");
		
		String userHome = System.getProperty("user.home");
		String central;
		System.setProperty("user.home",home.toString());
		try {central = jco.config2Wrapper(line,"").getString("test.central");}
		finally {System.setProperty("user.home",userHome);}
		
		Assertions.assertEquals("yes",central,"the central configuration is selected");
	}
	
	@Test
	public void missingConfigExitsWithNonZero() throws Exception
	{
		JeeslCliOptionHandler jco = this.build();
		AtomicInteger exitCode = new AtomicInteger(Integer.MIN_VALUE);
		jco.setExitHandler(exitCode::set);
		
		Path missing = Files.createTempDirectory("jeesl-cli-config-").resolve("missing.properties");
		CommandLine line = this.parse(jco,"-config",missing.toString());
		
		jco.config2Wrapper(line,"");
		
		Assertions.assertEquals(-1,exitCode.get(),"the process ends with a non-zero exit code");
	}
}
