package net.sf.ahtutils.doc.ofx;

import org.openfuxml.content.ofx.Listing;
import org.openfuxml.interfaces.DefaultSettingsManager;
import org.openfuxml.processor.settings.AbstractDefaultSettingsManager;

public class AbstractUtilsOfxSettingsManager extends AbstractDefaultSettingsManager implements DefaultSettingsManager
{
	public AbstractUtilsOfxSettingsManager()
	{
		initListing();
	}
	
	private void initListing()
	{
		initListingSql();
		initListingShell();
	}
	
	private void initListingSql()
	{
		Listing xml = new Listing();
		xml.setSetting("sql");
		xml.setCodeLang("SQL");
		xml.setNumbering(true);
		xml.setLinebreak(true);
		this.addSetting(Listing.class.getName(), xml.getSetting(), xml);
	}
	
	private void initListingShell()
	{
		Listing xml = new Listing();
		xml.setSetting("shell");
		xml.setCodeLang("SHELL");
		xml.setNumbering(true);
		xml.setLinebreak(true);
		this.addSetting(Listing.class.getName(), xml.getSetting(), xml);
	}

}
