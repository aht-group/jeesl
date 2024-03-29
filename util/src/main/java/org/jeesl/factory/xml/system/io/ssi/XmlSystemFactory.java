package org.jeesl.factory.xml.system.io.ssi;

import java.util.Objects;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSystemFactory<SYSTEM extends JeeslIoSsiSystem<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSystemFactory.class);

	private final org.jeesl.model.xml.io.ssi.core.System q;

	public XmlSystemFactory(org.jeesl.model.xml.io.ssi.core.System q)
	{
		this.q=q;
	}
	
	public static org.jeesl.model.xml.io.ssi.core.System build() {return new org.jeesl.model.xml.io.ssi.core.System ();}
	public static org.jeesl.model.xml.io.ssi.core.System build(String code) {org.jeesl.model.xml.io.ssi.core.System system = build(); system.setCode(code); return system;}
	
	public org.jeesl.model.xml.io.ssi.core.System build(SYSTEM system)
	{
		org.jeesl.model.xml.io.ssi.core.System xml = new org.jeesl.model.xml.io.ssi.core.System();

		if(Objects.nonNull(q.getId())) {xml.setId(system.getId());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(system.getCode());}
	
		return xml;
	}
}