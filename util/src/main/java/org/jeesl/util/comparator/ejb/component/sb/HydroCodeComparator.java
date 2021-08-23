package org.jeesl.util.comparator.ejb.component.sb;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HydroCodeComparator<T extends EjbWithCode> implements Comparator<T>
{
    final static Logger logger = LoggerFactory.getLogger(HydroCodeComparator.class);
    public enum Type {code};

	@Override
	public int compare(T a, T b) {

        CompareToBuilder ctb = new CompareToBuilder();
        ctb.append(b.getCode(),a.getCode());
        return ctb.toComparison();
	}
}
