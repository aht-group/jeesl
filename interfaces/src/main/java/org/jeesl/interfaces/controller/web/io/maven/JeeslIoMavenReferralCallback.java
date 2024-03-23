package org.jeesl.interfaces.controller.web.io.maven;

import java.util.List;

import org.jeesl.interfaces.model.io.maven.ee.JeeslIoMavenEeEdition;
import org.jeesl.interfaces.model.io.maven.module.JeeslMavenType;

public interface JeeslIoMavenReferralCallback <TYPE extends JeeslMavenType<?,?,TYPE,?>,
												EE extends JeeslIoMavenEeEdition<?,?,EE,?>>
			extends JeeslIoMavenTypeCallback<TYPE>
{
	List<EE> getPreselectionEeEditions();
	void setPreselectionEeEditions(List<EE> list);
}