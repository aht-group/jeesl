package net.sf.ahtutils.controller.bl;

import java.util.Hashtable;

import javax.persistence.EntityManager;

import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;

import net.sf.ahtutils.interfaces.bl.UtilsStatusBl;

public class UtilsStatusBlBean extends JeeslFacadeBean implements UtilsStatusBl
{	
	private static final long serialVersionUID = 1L;

	public UtilsStatusBlBean(EntityManager em)
	{
		super(em);
	}

//	@Override
	public <T extends EjbWithLangDescription<L, D>, L extends JeeslLang, D extends JeeslDescription>
			T verifiyLangs(Class<T> cl, Class<D> clD, T t, String[] langs)
	{
		t = em.find(cl, t.getId());
		if(t.getDescription()==null){t.setDescription(new Hashtable<String,D>());}
		for(String key : langs)
		{
			if(!t.getDescription().containsKey(key))
			{
				try
				{
					D d = clD.newInstance();
					d.setLkey(key);
					d.setLang("");
					
					t.getDescription().put(key, d);
					t = this.update(t);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
				catch (InstantiationException e) {e.printStackTrace();}
				catch (IllegalAccessException e) {e.printStackTrace();}
			}
		}
		return t;
	}
}