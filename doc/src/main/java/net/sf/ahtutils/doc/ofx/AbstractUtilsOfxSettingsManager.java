package net.sf.ahtutils.doc.ofx;

import org.openfuxml.interfaces.configuration.DefaultSettingsManager;
import org.openfuxml.model.xml.core.ofx.Listing;
import org.openfuxml.util.configuration.settings.AbstractDefaultSettingsManager;

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
		initListingXml();
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

	private void initListingXml()
	{
		Listing xml = new Listing();
		xml.setSetting("xml");
		xml.setCodeLang("XML");
		xml.setNumbering(true);
		xml.setLinebreak(true);
		this.addSetting(Listing.class.getName(), xml.getSetting(), xml);
	}
}
