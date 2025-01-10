package org.jeesl.controller.web.module.mmg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoFileRepositoryFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.mmg.JeeslMmgItemCallback;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMmgMetaController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										FRS extends JeeslFileStorage<L,D,?,?,?>,
										FRC extends JeeslFileContainer<FRS,FRM>,
										FRM extends JeeslFileMeta<D,FRC,?,?>>
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslMmgMetaController.class);
	
	private final IoFileRepositoryFactoryBuilder<L,D,LOC,?,FRS,?,?,FRC,?,?,?,?,?> fbFile;
		
	private final List<FRM> items; public List<FRM> getItems() {return items;}

	private FRM item; public FRM getItem() {return item;} public void setItem(FRM item) {this.item = item;}

	public JeeslMmgMetaController(final JeeslMmgItemCallback callback,
									final IoFileRepositoryFactoryBuilder<L,D,LOC,?,FRS,?,?,FRC,?,?,?,?,?> fbFile)
	{
		super(fbFile.getClassL(),fbFile.getClassD());
		this.fbFile=fbFile;

		items = new ArrayList<>();
	}
	
	public void postConstructMmgItem(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		
	}
	
	private void reset(boolean rItmes, boolean rItem)
	{
		if(rItmes) {items.clear();}
		if(rItem) {item=null;}
	}
	
	
	
	public void selectItem(FRM item) throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(item));}
		this.item = item;
	}
	
	
}