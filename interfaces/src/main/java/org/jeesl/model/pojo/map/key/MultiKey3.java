package org.jeesl.model.pojo.map.key;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiKey3 <K1 extends EjbWithId, K2 extends EjbWithId, K3 extends EjbWithId> extends MultiKey2<K1,K2>
{
    final static Logger logger = LoggerFactory.getLogger(MultiKey3.class);

    protected final K3 k3; public K3 getK3() {return k3;}
    
    public MultiKey3(final K1 k1, final K2 k2, final K3 k3)
    {
		super(k1,k2);
		this.k3=k3;
    }
    
	@SuppressWarnings("unchecked")
	@Override public boolean equals(Object object)
	{
		if (object == null) {return false;}
		if (object == this) {return true;}
		if (object.getClass() != this.getClass()) {return false;}
		MultiKey3<K1,K2,K3> other = (MultiKey3<K1,K2,K3>)object;
		
		return new EqualsBuilder().append(this.hashCode(),other.hashCode()).isEquals();
	}
	@Override public int hashCode()
	{
		return new HashCodeBuilder(17,43).append(k1.hashCode()).append(k2.hashCode()).append(k3.hashCode()).toHashCode();
	}
}