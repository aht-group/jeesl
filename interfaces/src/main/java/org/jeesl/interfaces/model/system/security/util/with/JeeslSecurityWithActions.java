package org.jeesl.interfaces.model.system.security.util.with;

import java.util.List;

import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;

public interface JeeslSecurityWithActions <A extends JeeslSecurityAction<?,?,?,?,?,?>>
{
	public List<A> getActions();
	public void setActions(List<A> actions);
}