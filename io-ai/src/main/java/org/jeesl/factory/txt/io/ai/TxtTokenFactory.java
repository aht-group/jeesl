package org.jeesl.factory.txt.io.ai;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;

public class TxtTokenFactory
{	
	final static Logger logger = LoggerFactory.getLogger(TxtTokenFactory.class);
		
	public static String tikaParse(InputStream is) throws IOException, SAXException, TikaException
	{
	    AutoDetectParser parser = new AutoDetectParser();
	    BodyContentHandler handler = new BodyContentHandler(-1);
//	    ToXMLContentHandler handler = new ToXMLContentHandler();
	    Metadata metadata = new Metadata();
	    
	    parser.parse(is, handler, metadata);
        return handler.toString();
	}
	
	public static int tokens(String text) throws IOException, SAXException
	{		
		EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
		Encoding enc = registry.getEncoding(EncodingType.CL100K_BASE);
		List<Integer> encoded = enc.encode(text);
		logger.info("Size: "+encoded.size());
		return encoded.size();
	}
	
	public static int toContextWindow(IoOpenAiGeneration model)
	{
		if(model.getSymbol().equals("gpt-3.5-turbo-16k")) {return 16385;}
		else if(model.getSymbol().equals("gpt-4-1106-preview")) {return 128000;}
		else if(model.getSymbol().equals("gpt-4-0125-preview")) {return 128000;}
		
		return 4000;
	}
	
	public static int toMaxCompletion(IoOpenAiGeneration model, int requestTokens)
	{
		if(model.getSymbol().equals("gpt-4-0125-preview")) {return toMaxCompletion(toContextWindow(model),requestTokens,4096);}
		
		return 0;
	}
	
	private static int toMaxCompletion(int context, int used, int max)
	{
		if(context-used<max) {return context-used;}
		else {return max;}
	}
}