package org.jeesl.web.rest.system.io;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.api.rest.i.io.JeeslIoSsiRestInterface;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiCoreFactoryBuilder;
import org.jeesl.factory.json.io.ssi.core.JsonSsiCredentialFactory;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoSsiRestHandler <L extends JeeslLang, D extends JeeslDescription,
								SYSTEM extends JeeslIoSsiSystem<L,D>,
								CRED extends JeeslIoSsiCredential<SYSTEM>>
					implements JeeslIoSsiRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(IoSsiRestHandler.class);

	private final IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,?> fbSsi;
	private final JeeslIoSsiFacade<SYSTEM,CRED,?,?,?,?,?,?,?,?> fSsi;
	
	public IoSsiRestHandler(IoSsiCoreFactoryBuilder<L,D,SYSTEM,CRED,?> fbSsi, JeeslIoSsiFacade<SYSTEM,CRED,?,?,?,?,?,?,?,?> fSsi)
	{
		this.fbSsi=fbSsi;
		this.fSsi=fSsi;
	}
	
	@Override public JsonSsiSystem getCredentials(String code)
	{
		JsonSsiSystem jSystem = new JsonSsiSystem();
		
		try
		{
			SYSTEM eSystem = fSsi.fByCode(fbSsi.getClassSystem(),code);
			jSystem.setCode(eSystem.getCode());
			jSystem.setCredentials(new ArrayList<>());
			
			List<CRED> credentials = fSsi.allForParent(fbSsi.getClassCredential(),eSystem);
			logger.info(fbSsi.getClassCredential().getSimpleName()+": "+credentials.size());
			for(CRED eCredential : credentials)
			{
				JsonSsiCredential jCredential = JsonSsiCredentialFactory.build();
				jCredential.setCode(eCredential.getCode());
				jCredential.setUrl(eCredential.getUrl());
				jSystem.getCredentials().add(jCredential);
			}
		}
		catch (JeeslNotFoundException e) {}
		
		return jSystem;
	}
}