package org.jeesl.interfaces.model.io.label.entity;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslRevisionAttribute<L extends JeeslLang, D extends JeeslDescription,
										RE extends JeeslRevisionEntity<L,D,?,?,?,?>,
										RER extends JeeslStatus<L,D,RER>,
										RAT extends JeeslStatus<L,D,RAT>>
		extends Serializable,EjbRemoveable,EjbPersistable,EjbWithId,
				EjbWithCode,EjbWithPosition,
				EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Attributes {code,ownerEntity,entity};
	public static enum Type {text,number,date,amount,bool}
	
//	RE getOwnerEntity();
//	void setOwnerEntity(RE ownerEntity);
	
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