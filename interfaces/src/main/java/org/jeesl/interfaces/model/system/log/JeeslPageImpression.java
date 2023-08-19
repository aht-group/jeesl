package org.jeesl.interfaces.model.system.log;

import java.util.Date;

import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;

public interface JeeslPageImpression <V extends JeeslSecurityView<?,?,?,?,?,?>>
{	
	public Date getRecord();
	public void setRecord(Date record);
	
	V getView();
	void setView(V view);
}