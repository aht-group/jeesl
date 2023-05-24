package org.jeesl.system;

import java.awt.GraphicsEnvironment;

public class TestFontsOnSystem {
	
	public static void main(String[] args)
	{
		System.setProperty("java.awt.headless", "true");
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		System.out.println("Graphics Env is headless? " +e.isHeadlessInstance());
		for(String font:e.getAvailableFontFamilyNames()){
			System.out.println(font);
		}
	}
	
}
