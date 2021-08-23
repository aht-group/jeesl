package org.jeesl.model.pojo.map.key;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiKey2 <K1 extends EjbWithId, K2 extends EjbWithId>
{
    final static Logger logger = LoggerFactory.getLogger(MultiKey2.class);

    protected final K1 k1; public K1 getK1() {return k1;}
    protected final K2 k2; public K2 getK2() {return k2;}
    
    public MultiKey2(final K1 k1, final K2 k2)
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
		MultiKey2<K1,K2> other = (MultiKey2<K1,K2>)object;
		return new EqualsBuilder().append(this.hashCode(),other.hashCode()).isEquals();
	}
	@Override public int hashCode()
	{
		return new HashCodeBuilder(17,43).append(k1.hashCode()).append(k2.hashCode()).toHashCode();
	}
}