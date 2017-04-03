package org.jeesl.util.comparator.xml.status;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Lang;

public class LangComparator
{
	final static Logger logger = LoggerFactory.getLogger(LangComparator.class);

    public enum Type {key};

    public static Comparator<Lang> factory(Type type)
    {
        Comparator<Lang> c = null;
        LangComparator factory = new LangComparator();
        switch (type)
        {
            case key: c = factory.new KeyLangComparator();break;
        }

        return c;
    }

    private class KeyLangComparator implements Comparator<Lang>
    {
        public int compare(Lang a, Lang b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  if(a.isSetKey() && b.isSetKey()){ctb.append(a.getKey(), b.getKey());}
			  return ctb.toComparison();
        }
    }
}