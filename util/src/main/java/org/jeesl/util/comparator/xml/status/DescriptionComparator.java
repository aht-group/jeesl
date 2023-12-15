package org.jeesl.util.comparator.xml.status;

import java.util.Comparator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.model.xml.io.locale.status.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DescriptionComparator
{
	final static Logger logger = LoggerFactory.getLogger(DescriptionComparator.class);

    public enum Type {key};

    public static Comparator<Description> factory(Type type)
    {
        Comparator<Description> c = null;
        DescriptionComparator factory = new DescriptionComparator();
        switch (type)
        {
            case key: c = factory.new KeyLangComparator();break;
        }

        return c;
    }

    private class KeyLangComparator implements Comparator<Description>
    {
        public int compare(Description a, Description b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  if(ObjectUtils.allNotNull(a.getKey(),b.getKey())) {ctb.append(a.getKey(), b.getKey());}
			  return ctb.toComparison();
        }
    }
}