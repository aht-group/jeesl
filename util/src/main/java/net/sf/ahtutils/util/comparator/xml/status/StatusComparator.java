package net.sf.ahtutils.util.comparator.xml.status;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
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
			  if(a.isSetGroup() && b.isSetGroup()){ctb.append(a.getGroup(), b.getGroup());}
			  if(a.isSetParent() && b.isSetParent())
			  {
//				  if(a.getParent().isSetPosition() && b.getParent().isSetPosition()){ctb.append(a.getParent().getPosition(),b.getParent().getPosition());}
				  if(a.getParent().isSetCode() && b.getParent().isSetCode()){ctb.append(a.getParent().getCode(),b.getParent().getCode());}
			  }
			  if(a.isSetPosition() && b.isSetPosition()){ctb.append(a.getPosition(), b.getPosition());}
			  if(a.isSetCode() && b.isSetCode()){ctb.append(a.getCode(), b.getCode());}
			  return ctb.toComparison();
        }
    }
}