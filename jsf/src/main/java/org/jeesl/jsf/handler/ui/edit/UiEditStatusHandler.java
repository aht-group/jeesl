package org.jeesl.jsf.handler.ui.edit;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jeesl.interfaces.controller.handler.UiEditHandler;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//jeesl.highlight:showcase
public class UiEditStatusHandler <S extends JeeslStatus<?,?,S>> implements UiEditHandler
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(UiEditStatusHandler.class);

	private final Set<S> setAllow;
	private final Set<S> setDeny;
	
	private boolean allow;
	
	public boolean isAllow() {return allow;}
	public boolean isDeny() {return !allow;}

	public static <S extends JeeslStatus<?,?,S>> UiEditStatusHandler<S> instance() {return new UiEditStatusHandler<S>();}
	public UiEditStatusHandler()
	{
		setAllow = new HashSet<>();
		setDeny = new HashSet<>();
		allow = false;
	}
	

	public void addAllow(S status) {setAllow.add(status);}
	public void addDeny(S status) {setDeny.add(status);}
	
	
	public <T extends JeeslWithStatus<S>> void update(T ejb) {update(ejb.getStatus());}
	
	public void update(S status)
	{
		if(Objects.isNull(status)) {allow=false;}
		
		boolean statusInAllow;
		if(setAllow.isEmpty()) {statusInAllow = true;}
		else {statusInAllow = setAllow.contains(status);}
		
		boolean statusInDeny;
		if(setDeny.isEmpty()) {statusInDeny = false;}
		else {statusInDeny = setDeny.contains(status);}
		
//		logger.info(status.getCode()+" statusInAllow:"+statusInAllow+" statusInDeny:"+statusInDeny);
		
		allow = statusInAllow && !statusInDeny;
	}
}
//jeesl.highlight:showcase