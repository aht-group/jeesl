package org.jeesl.factory.json.io.maven;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import org.jeesl.model.json.io.maven.JsonFont;
import org.jeesl.model.json.io.maven.JsonMavenGraph;

public class JeeslFontFactory
{
	public static JsonMavenGraph build()
	{
		JsonMavenGraph json = new JsonMavenGraph();
		json.setFonts(new ArrayList<>());
		
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		System.out.println("Graphics Env is headless? " +e.isHeadlessInstance());
		for(String font : e.getAvailableFontFamilyNames())
		{
			JsonFont f = new JsonFont();
			f.setCode(font);
			json.getFonts().add(f);
		}
		
		return json;
	}
}