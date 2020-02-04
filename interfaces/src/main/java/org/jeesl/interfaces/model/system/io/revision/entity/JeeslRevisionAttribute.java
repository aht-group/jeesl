package org.jeesl.interfaces.model.system.io.revision.entity;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithSymbol;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.code.EjbWithCode;
import org.jeesl.interfaces.model.with.position.EjbWithPosition;

import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.model.interfaces.with.EjbWithDescription;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.model.interfaces.with.EjbWithLang;

public interface JeeslRevisionAttribute<L extends JeeslLang,D extends JeeslDescription,
										RE extends JeeslRevisionEntity<L,D,?,?,?,?>,
										RER extends JeeslStatus<RER,L,D>,
										RAT extends JeeslStatus<RAT,L,D>>
		extends Serializable,EjbRemoveable,EjbPersistable,JeeslStatusWithSymbol,EjbWithId,
				EjbWithCode,EjbWithPosition,
				EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes{entity};
	public static enum Type{text,number,date,amount,bool}
	
	RAT getType();
	void setType(RAT type);
	
	String getXpath();
	void setXpath(String xpath);
	
	boolean isShowPrint();
	void setShowPrint(boolean showPrint);
	
	boolean isShowWeb();
	void setShowWeb(boolean showWeb);
	
	boolean isShowName();
	void setShowName(boolean showName);
	
	boolean isShowEnclosure();
	void setShowEnclosure(boolean showEnclosure);
	
	Boolean getUi();
	void setUi(Boolean ui);
	
	Boolean getBean();
	void setBean(Boolean bean);
	
	Boolean getConstruction();
	void setConstruction(Boolean construction);
	
	Boolean getManualUser();
	void setManualUser(Boolean manualUser);
	
	Boolean getManualAdmin();
	void setManualAdmin(Boolean manualAdmin);
	
	String getDeveloperInfo();
	void setDeveloperInfo(String developerInfo);
	
	RER getRelation();
	void setRelation(RER relation);
	
	RE getEntity();
	void setEntity(RE entity);
	
	Boolean getRelationOwner();
	void setRelationOwner(Boolean relationOwner);
	
	Boolean getStatusTableDoc();
	void setStatusTableDoc(Boolean statusTableDoc);
	
	Boolean getDocOptionsInline();
	void setDocOptionsInline(Boolean docOptionsInline);
	
}