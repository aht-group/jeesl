package org.jeesl.controller.report.domain.finance;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.factory.xml.domain.finance.XmlSignatureFactory;
import org.jeesl.factory.xml.domain.finance.XmlSignaturesFactory;
import org.jeesl.model.xml.module.finance.Signature;
import org.jeesl.model.xml.module.finance.Signatures;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSignatureReport extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestSignatureReport.class);
	
	public static Signatures structure()
	{
		Signatures sig = XmlSignaturesFactory.build();
		sig.getSignatures().add(level("tech","prep","app","verif"));
		sig.getSignatures().add(level("legal","app","verif"));
		sig.getSignatures().add(level("finance","app","verif"));
		sig.getSignatures().add(level("dg","app","verif"));
		return sig;
	}
	
	private static Signatures level(String level, String... action)
	{
		Signatures sig = XmlSignaturesFactory.build();
		sig.setCode(level);
		
		for(String a : action)
		{
			Signature s = XmlSignatureFactory.build();
			s.setCode(a);
			sig.getSignature().add(s);
		}
		
		return sig;
	}
	
	public static void main(String[] args) throws Exception
    {
		JeeslBootstrap.init();
    	JaxbUtil.info(TestSignatureReport.structure());
    }
}