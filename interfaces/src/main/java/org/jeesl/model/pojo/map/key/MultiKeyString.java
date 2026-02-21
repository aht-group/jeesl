package org.jeesl.model.pojo.map.key;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiKeyString <K1 extends EjbWithId>
{
    final static Logger logger = LoggerFactory.getLogger(MultiKeyString.class);

    protected final K1 k1; public K1 getK1() {return k1;}
    protected final String k2; public String getK2() {return k2;}
    
    public static <K1 extends EjbWithId> MultiKeyString<K1> instance(final K1 k1, final String k2) {return new MultiKeyString<>(k1,k2);}
    private MultiKeyString(final K1 k1, final String k2)
    {
		this.k1=k1;
		this.k2=k2;
    }
    
	@SuppressWarnings("unchecked")
	@Override public boolean equals(Object object)
	{
		if (object == null) {return false;}
		if (object == this) {return true;}
		if (object.getClass() != this.getClass()) {return false;}
		MultiKeyString<K1> other = (MultiKeyString<K1>)object;
		return new EqualsBuilder().append(k1,other.k1).append(k2,other.k2).isEquals();
	}
	@Override public int hashCode()
	{
		return new HashCodeBuilder(17,43).append(k1.hashCode()).append(k2.hashCode()).toHashCode();
	}
}