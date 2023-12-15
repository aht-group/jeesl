package org.jeesl.util.comparator.xml.status;

import java.util.Comparator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;

public class StatusComparator
{
	final static Logger logger = LoggerFactory.getLogger(StatusComparator.class);

    public enum Type {export};

    public static Comparator<Status> factory(Type type)
    {
        Comparator<Status> c = null;
        StatusComparator factory = new StatusComparator();
        switch (type)
        {
            case export: c = factory.new GroupParentPositionCodeComparator();break;
        }

        return c;
    }

    private class GroupParentPositionCodeComparator implements Comparator<Status>
    {
        public int compare(Status a, Status b)
        {
			  CompareToBuilder ctb = new CompareToBuilder();
			  if(ObjectUtils.allNotNull(a.getGroup(),b.getGroup())) {ctb.append(a.getGroup(), b.getGroup());}
			  if(ObjectUtils.allNotNull(a.getParent(),b.getParent()))
			  {
				  if(ObjectUtils.allNotNull(a.getParent().getPosition(),b.getParent().getPosition())) {ctb.append(a.getParent().getPosition(),b.getParent().getPosition());}
				  if(ObjectUtils.allNotNull(a.getParent().getCode(),b.getParent().getCode())) {ctb.append(a.getParent().getCode(),b.getParent().getCode());}
			  }
			  if(ObjectUtils.allNotNull(a.getPosition(),b.getPosition())){ctb.append(a.getPosition(), b.getPosition());}
			  if(ObjectUtils.allNotNull(a.getCode(),b.getCode())) {ctb.append(a.getCode(), b.getCode());}
			  return ctb.toComparison();
        }
    }
}