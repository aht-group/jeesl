package net.sf.ahtutils.web.mbean.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractLogMessage <L extends JeeslLang,D extends JeeslDescription,C extends JeeslSecurityCategory<L,D>,R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,V extends JeeslSecurityView<L,D,C,R,U,A>,U extends JeeslSecurityUsecase<L,D,C,R,V,A>,A extends JeeslSecurityAction<L,D,R,V,U,AT>,AT extends JeeslSecurityTemplate<L,D,C>,USER extends JeeslUser<R>>
		implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(AbstractLogMessage.class);
	private static final long serialVersionUID = 1L;
	
	public static String postConstruct(){return "@PostConstruct";}
	
	public static String postConstruct(ProcessingTimeTracker ptt)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(postConstruct());
		sb.append(ptt(ptt));
		return sb.toString();
	}
	
	public static <USER extends JeeslUser<?>> String postConstruct(USER user)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(postConstruct());
		sb.append(user(user));
		return sb.toString();
	}
	
	public static <USER extends JeeslUser<?>> String postConstruct(ProcessingTimeTracker ptt, USER user)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(postConstruct());
		sb.append(ptt(ptt));
		sb.append(user(user));
		return sb.toString();
	}
	
    public static <USER extends JeeslUser<?>> String postConstruct(USER user, long urlCode){return postConstruct(user, ""+urlCode);}
	public static <USER extends JeeslUser<?>> String postConstruct(USER user, String urlCode)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("@PostConstruct");
		if(user!=null){sb.append(" {").append(user.toString()).append("}");}
		if(urlCode!=null){sb.append(" urlCode:").append(urlCode);}
		return sb.toString();
	}
	
	public static <USER extends JeeslUser<?>>
		String user(USER user)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" [");
		if(user==null){sb.append("---");}
		else{sb.append(user.toString());}
		sb.append("]");
		return sb.toString();
	}
	
	public static String ptt(ProcessingTimeTracker ptt)
	{
		if(ptt!=null)
		{
			ptt.stop();
			StringBuffer sb = new StringBuffer();
			sb.append(" in "+ptt.toTotalTime());
			return sb.toString();
		}
		return "";
	}
	
	public static <USER extends JeeslUser<?>> String preDestroy(USER user)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("@PreDestroy");
		sb.append(" {").append(user.toString()).append("}");
		return sb.toString();
	}
	
	public static String createEntity(Class<?> c){return createEntity(null,c);}
	public static <USER extends JeeslUser<?>> String createEntity(USER user, Class<?> c)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Creating ");
		sb.append(c.getSimpleName());
		if(user!=null){sb.append(" (").append(user.toString()).append(")");}
		return sb.toString();
	}
	

	 public static <T extends EjbWithId> String addEntity(T t){return addEntity(null,t);}
	 public static <USER extends JeeslUser<?>, T extends EjbWithId> String addEntity(USER user, T t)
	 {
		 StringBuffer sb = new StringBuffer();
		 sb.append("Adding ").append(t.getClass().getSimpleName());
		 if(user!=null){sb.append(" {").append(user.toString()).append("}");}
		 sb.append(": ").append(t.toString());
		 return sb.toString();
	 }
	
	public static <T extends EjbWithId> String deleteEntities(Class<?> c, List<T> list)
	{
		 StringBuilder sb = new StringBuilder();
		 sb.append("Deleting ");
		 sb.append(list.size());
		 sb.append(" ").append(c.getClass().getSimpleName());
		 return sb.toString();
	}
	public static <T extends EjbWithId> String rmEntity(T t)
	{
		 return rmEntity(null,t);
	}
	public static <USER extends JeeslUser<?>, T extends EjbWithId> String rmEntity(USER user, T t)
	{
		 StringBuilder sb = new StringBuilder();
		 sb.append("Removing ").append(t.getClass().getSimpleName());
		 if(user!=null){sb.append(" {").append(user.toString()).append("}");}
		 sb.append(": ").append(t.toString());
		 return sb.toString();
	}

	 // Select
	 public static <T extends EjbWithId> String selectEntity(T t)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Selecting ");
        if(t!=null) {sb.append(t.getClass().getSimpleName());} else {sb.append("NULL");}
        sb.append(": ");
        if(t!=null) {sb.append(t.toString());} else {sb.append("NULL");}
        return sb.toString();
	 }
	 public static <A extends EjbWithId,B extends EjbWithId> String selectEntity(A a, B b)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Selecting ").append(a.getClass().getSimpleName());
        sb.append(": ").append(a.toString());
        if(b!=null)
        {
        	sb.append(" --- ");
        	sb.append(b.getClass().getSimpleName()).append(": ");
        	sb.append(b.toString());
        }
        return sb.toString();
	 }
	 public static <T extends EjbWithId> String selectEntities(Class<?> c, List<T> list)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Selecting ").append(c.getSimpleName());
        sb.append(": ").append(list.size());
        return sb.toString();
	 }
	 
	 public static <T extends EjbWithId> String selectOneMenuChange(T t)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Change selectOneMenu ");
        if(t!=null){sb.append(t.getClass().getSimpleName()).append(": ").append(t.toString());}
        else{sb.append("null");}
        return sb.toString();
	 }
	 public static <T extends EjbWithId> String selectOverlayPanel(T t)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Select OverlayPanel ").append(t.getClass().getSimpleName());
        sb.append(": ").append(t.toString());
        return sb.toString();
	 }
	 
	 public static <T extends EjbWithId> String addOpEntity(T t)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Adding from Overlay-Selection ").append(t.getClass().getSimpleName());
        sb.append(": ").append(t.toString());
        return sb.toString();
	 }
	 
	 public static <T extends EjbWithId> String rmOpEntity(T t)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Removing from Overlay-Selection ").append(t.getClass().getSimpleName());
        sb.append(": ").append(t.toString());
        return sb.toString();
	 }
	 
	 public static <T extends EjbWithId> String reorder(Class<T> c, List<T> list)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Reordering ");
        sb.append(" ").append(c.getSimpleName());
        if(list!=null){sb.append(" ").append(list.size()).append(" elements");}
        return sb.toString();
	}
	 
	 public static <T extends EjbWithId> String wizard(T t, String description)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Wizard ").append(t.getClass().getSimpleName());
        sb.append(" (").append(t.toString()).append(")");
        sb.append(": ").append(description);
        return sb.toString();
	 }
	 
	 //Toggle
	 public static <T extends EjbWithId> String toggle(T t)
	 {
        StringBuffer sb = new StringBuffer();
        sb.append("Toggeling ");
        if(t!=null)
        {
        	sb.append(t.getClass().getSimpleName());
        	sb.append(" ... ").append(t.toString());
        }
        return sb.toString();
	}
	public static String toggled(Class<?> c)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Toggled ");
		sb.append(c.getSimpleName());
		return sb.toString();
	}

	public static <T extends EjbWithId> String saveEntity(T t){return saveEntity(null,t);}
	public static <USER extends JeeslUser<?>, T extends EjbWithId> String saveEntity(USER user, T t)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(user(user));
        sb.append(" Save ").append(t.getClass().getSimpleName());
        sb.append(": ").append(t.toString());
        return sb.toString();
    }
	public static <USER extends JeeslUser<?>, T extends EjbWithId, X extends EjbWithId>
	String saveEntity(USER user, T t, X x)
{
    StringBuffer sb = new StringBuffer();
    sb.append(user(user));
    sb.append(" Save ").append(t.getClass().getSimpleName());
    sb.append(": ").append(t.toString());
    sb.append(" (").append(x.toString()).append(")");
    return sb.toString();
}
    public static <T extends EjbWithId> String savedEntity(T t)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Saved ").append(t.getClass().getSimpleName());
        sb.append(": ").append(t.toString());
        return sb.toString();
    }
    
    public static  String autoComplete(Class<?> cl, String query, int results)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("AutoComplete");
        sb.append(" ").append(cl.getSimpleName());
        sb.append(" results:").append(results);
        sb.append(" query: ").append(query);
        return sb.toString();
    }
    
    public static <T extends EjbWithId> String autoCompleteSelect(T t)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("AutoComplete Select; ");
        sb.append(t.toString());
        return sb.toString();
    }
    
	public static <T extends EjbWithId> String reloadEntity(T t)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Reloading ").append(t.toString());
		return sb.toString();
	}
	
	public static <T extends EjbWithId> String reloaded(Class<T> c, List<T> list){return AbstractLogMessage.reloaded(c,list,null);}
	public static <T extends EjbWithId> String reloaded(Class<T> c, ProcessingTimeTracker ptt, List<T> list){return AbstractLogMessage.reloaded(c,ptt,list,null);}
	public static <T extends EjbWithId> String reloaded(Class<T> c, Set<T> list){return AbstractLogMessage.reloaded(c,new ArrayList<T>(list),null);}
	public static <T extends EjbWithId> String reloaded(Class<T> c, Collection<T> list){return AbstractLogMessage.reloaded(c,new ArrayList<T>(list),null);}
	public static <T extends EjbWithId> String reloaded(Class<T> c, List<T> list, EjbWithId ejb){return AbstractLogMessage.reloaded(c,null,new ArrayList<T>(list),null);}
	public static <T extends EjbWithId> String reloaded(Class<T> c, ProcessingTimeTracker ptt, List<T> list, EjbWithId ejb)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Reloaded List ");
		sb.append(c.getSimpleName());
		sb.append(" with ").append(list.size()).append(" elements");
		if(ejb!=null) {sb.append(" for "+ejb.toString());}
		sb.append(ptt(ptt));
		return sb.toString();
	}
	
	public static <T extends EjbWithId> String multiStatus(Class<T> c, List<T> all, List<T> selected)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("StatusBar ");
		sb.append(c.getSimpleName());
		sb.append(" ").append(selected.size()).append("/").append(all.size());
		return sb.toString();
	}
	
	public static String time(String msg, ProcessingTimeTracker ptt)
	{
		ptt.stop();
		StringBuffer sb = new StringBuffer();
		sb.append(msg);
		sb.append(" ").append(ptt.toTotalTime());
		return sb.toString();
	}
	
	public static String cacheSearch()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Searching Cache");
		return sb.toString();
	}
}
