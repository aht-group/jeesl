package org.jeesl.controller.processor.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.controller.processor.JeeslDeduplicator;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.graphic.core.JeeslIcon;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDeduplicator <RE extends JeeslRevisionEntity<?,?,?,?,?,?>, ICON extends JeeslStatus<?,?,ICON>, EJB extends EjbWithId>
					implements JeeslDeduplicator<RE,ICON,EJB>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractDeduplicator.class);
	
	private final JeeslFacade facade;
	
	private final Class<RE> cEntity;
	
	protected final Map<RE,Integer> mapOriginal; @Override public Map<RE,Integer> getMapOriginal() {return mapOriginal;}
	protected final Map<RE,Integer> mapDuplicate; @Override public Map<RE,Integer> getMapDuplicate() {return mapDuplicate;}
	protected final Map<RE,List<ICON>> mapAction; public Map<RE,List<ICON>> getMapAction() {return mapAction;}
	
	protected final List<RE> entities; @Override public List<RE> getEntities() {return entities;}
	
	protected final ICON iconLeft; public ICON getIconLeft() {return iconLeft;}
	protected final ICON iconDelete; public ICON getIconDelete() {return iconDelete;}
	
	protected EJB ejbOriginal; @Override public EJB getEjbOriginal() {return ejbOriginal;}  public void setEjbOriginal(EJB ejbOriginal) {this.ejbOriginal = ejbOriginal;}
	protected EJB ejbDuplicate; @Override public EJB getEjbDuplicate() {return ejbDuplicate;} public void setEjbDuplicate(EJB projectDuplicate) {this.ejbDuplicate = projectDuplicate;}

	public AbstractDeduplicator(JeeslFacade facade, final Class<RE> cEntity, final Class<ICON> cIcon)
	{
		this.facade=facade;
		this.cEntity=cEntity;
		
		mapOriginal = new HashMap<>();
		mapDuplicate = new HashMap<>();
		mapAction = new HashMap<>();
		
		iconLeft = facade.fByEnum(cIcon, JeeslIcon.Arrow.jeeslArrowLeft);
		iconDelete = facade.fByEnum(cIcon, JeeslIcon.Crud.jeeslDelete);
		
		entities = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	protected void registerHandler(Class<?> c, ICON... icons) throws JeeslNotFoundException
	{
		RE e = facade.fByCode(cEntity,c.getName());
		entities.add(e);
		mapAction.put(e,Arrays.asList(icons));
	}
	
	public void analyse(EJB projectOriginal, EJB projectDuplicate)
	{
		this.ejbOriginal=projectOriginal;
		this.ejbDuplicate=projectDuplicate;
		analyse();
	}
	
	public void analyse()
	{
		mapOriginal.clear();
		mapDuplicate.clear();
		
		if(ejbOriginal!=null && ejbDuplicate!=null) 
		{
			for(RE e : entities)
			{
				analyse(ejbOriginal,e,mapOriginal);
				analyse(ejbDuplicate,e,mapDuplicate);
			}
		}
	}
	
	protected abstract void analyse(EJB ejb,RE e,Map<RE,Integer> map);
	
	public void action(RE entity, ICON icon) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(entity.toString()+" "+icon.toString());
		try
		{
			Class<?> c = Class.forName(entity.getCode());
			this.action(c,icon);
		}
		catch (ClassNotFoundException e){throw new JeeslNotFoundException(e.getMessage());}
		analyse();
	}
	protected abstract void action(Class<?> c, ICON icon) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException;
}