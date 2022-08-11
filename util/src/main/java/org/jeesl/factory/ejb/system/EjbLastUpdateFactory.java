package org.jeesl.factory.ejb.system;

import java.util.Date;

import org.jeesl.interfaces.model.io.label.revision.data.JeeslLastUpdate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

public class EjbLastUpdateFactory
{
	public static <USER extends JeeslUser<?>> void update(JeeslLastUpdate<USER> ejb, USER user)
	{
		ejb.setLastUpdateAt(new Date());
		ejb.setLastUpdateBy(user);
	}
}