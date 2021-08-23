package org.jeesl.util.comparator.ejb.module.asset;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAssetComparator<ASSET extends JeeslAomAsset<?,ASSET,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAssetComparator.class);

    public enum Type {position};
    
    public Comparator<ASSET> factory(Type type)
    {
        Comparator<ASSET> c = null;
        EjbAssetComparator<ASSET> factory = new EjbAssetComparator<ASSET>();
        switch (type)
        {
            case position: c = factory.new PositionComparator();break;
        }

        return c;
    }

    private class PositionComparator implements Comparator<ASSET>
    {
        @Override public int compare(ASSET a, ASSET b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  ctb.append(a.getPosition(),b.getPosition());
			  ctb.append(a.getId(),b.getId());
			  return ctb.toComparison();
        }
    }
}