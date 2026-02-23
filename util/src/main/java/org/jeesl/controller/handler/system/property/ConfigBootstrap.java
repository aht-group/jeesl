package org.jeesl.controller.handler.system.property;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.tree.NodeCombiner;
import org.apache.commons.configuration2.tree.UnionCombiner;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class ConfigBootstrap
{
	final static Marker fatal = MarkerFactory.getMarker("FATAL");
	final static Logger logger = LoggerFactory.getLogger(ExlpCentralConfigPointer.class);
	
	private enum Typ{UNKNOWN,XML,PROPERTIES}
	
	private final List<String> configurations;
	
	public static ConfigBootstrap instance()
	{
		return new ConfigBootstrap();
	}
	private ConfigBootstrap()
	{
		configurations = new ArrayList<>();
	}
	
	
	public ConfigBootstrap add(Path path)
	{
		if(Objects.isNull(path)) {logger.warn("Requested an additional config, but null provided");}
		else
		{
			logger.info("Adding "+path.toString());
			configurations.add(path.toFile().getAbsolutePath());
		}
		return this;
	}
	public ConfigBootstrap add(String s)
	{
		logger.info("Adding "+s);
		configurations.add(s);
		return this;
	}
	
	public org.exlp.interfaces.system.property.Configuration wrap() {return new ConfigWrapper(combine());}
	public static org.exlp.interfaces.system.property.Configuration wrap(org.apache.commons.configuration2.Configuration config) {return ConfigBootstrap.instance().new ConfigWrapper(config);}
	
	public org.apache.commons.configuration2.Configuration combine()
	{
		NodeCombiner combiner = new UnionCombiner();
		combiner.addListNode("table");  // mark table as list node
		
		Parameters params = new Parameters();
		
		CombinedConfiguration cc = new CombinedConfiguration(combiner);
		
		for(String configName : configurations)
		{
			try
			{
				Typ type = getTyp(configName);
				if(type.equals(Typ.XML))
				{
					FileBasedConfigurationBuilder<org.apache.commons.configuration2.XMLConfiguration> builder1 =
						    new FileBasedConfigurationBuilder<org.apache.commons.configuration2.XMLConfiguration>(org.apache.commons.configuration2.XMLConfiguration.class)
						    	.configure(params.xml().setFileName(configName));
					cc.addConfiguration(builder1.getConfiguration());
				}
				else if(type.equals(Typ.PROPERTIES))
				{
					FileBasedConfigurationBuilder<org.apache.commons.configuration2.FileBasedConfiguration> builder2 =
						    new FileBasedConfigurationBuilder<org.apache.commons.configuration2.FileBasedConfiguration>(org.apache.commons.configuration2.PropertiesConfiguration.class)
						    	.configure(params.properties().setFileName(configName));
					cc.addConfiguration(builder2.getConfiguration());
				}
				else
				{
					logger.warn("NYI: "+type);
				}
			}
			catch (org.apache.commons.configuration2.ex.ConfigurationException e) {e.printStackTrace();}
		}
		
		return cc;
	}
	
	private static Typ getTyp(String configName)
	{
		Typ typ = Typ.UNKNOWN;
		if(configName.endsWith(".xml")){typ=Typ.XML;}
		else if(configName.endsWith(".properties")){typ=Typ.PROPERTIES;}
		else if(configName.endsWith(".txt")){typ=Typ.PROPERTIES;}
		return typ;
	}
	
	private class ConfigWrapper implements org.exlp.interfaces.system.property.Configuration
	{
		private org.apache.commons.configuration2.Configuration config;
		
		public ConfigWrapper(org.apache.commons.configuration2.Configuration config)
		{
			this.config=config;
		}

		@Override public boolean containsKey(String key) {return config.containsKey(key);}
		@Override public void setProperty(String key, Object value) {config.setProperty(key, value);}
		
		@Override public String getString(String key) {return config.getString(key);}
		@Override public String getString(String key, String fallback) {return config.getString(key,fallback);}
		@Override public Integer getInteger(String key) {return config.getInt(key);}
		@Override public Integer getInteger(String key, Integer fallback) {return config.getInt(key,fallback);}
		@Override public int getInt(String key) {return config.getInt(key);}
		@Override public int getInt(String key, int fallback) {return config.getInt(key,fallback);}
		@Override public long getLong(String key) {return config.getLong(key);}
		@Override public Boolean getBoolean(String key) {return config.getBoolean(key);}
		@Override public Boolean getBoolean(String key, Boolean fallback) {return config.getBoolean(key,fallback);}
		
		@Override public Double getDouble(String key) {return config.getDouble(key);}
		@Override public Double getDouble(String key, Double fallback) {return config.getDouble(key,fallback);}
	}
}