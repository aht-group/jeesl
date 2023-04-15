package org.jeesl.interfaces.model.module.aom.event;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithRemark;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;

public interface JeeslAomEvent <COMPANY extends JeeslAomCompany<?,?>,
								ASSET extends JeeslAomAsset<?,ASSET,COMPANY,?,?>,
								ETYPE extends JeeslAomEventType<?,?,ETYPE,?>,
								ESTATUS extends JeeslAomEventStatus<?,?,ESTATUS,?>,
								M extends JeeslIoMarkup<?>,
								USER extends JeeslSimpleUser,
								FRC extends JeeslFileContainer<?,?>>
			extends Serializable,EjbSaveable,
					EjbWithRecord,EjbWithRemark,EjbWithName,
					JeeslWithType<ETYPE>,JeeslWithStatus<ESTATUS>,
					JeeslWithFileRepositoryContainer<FRC>
{
	public enum Attributes{assets,status}
	
	List<ASSET> getAssets();
	void setAssets(List<ASSET> assets);
	
	COMPANY getCompany();
	void setCompany(COMPANY vendor);
	
	Double getAmount();
	void setAmount(Double amount);
	
	M getMarkup();
	public void setMarkup(M markup);
}