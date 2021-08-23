package org.jeesl.model.pojo.map.key;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiKey4 <K1 extends EjbWithId, K2 extends EjbWithId, K3 extends EjbWithId, K4 extends EjbWithId> extends MultiKey3<K1,K2,K3>
{
    final static Logger logger = LoggerFactory.getLogger(MultiKey4.class);

    private final K4 k4; public K4 getK4() {return k4;}
    
    public MultiKey4(final K1 k1, final K2 k2, final K3 k3, final K4 k4)
    {
		super(k1,k2,k3);
		this.k4=k4;
    }
    
	@SuppressWarnings("unchecked")
	@Override public boolean equals(Object object)
	{
		if (object == null) {return false;}
		if (object == this) {return true;}
		if (object.getClass() != this.getClass()) {return false;}
		MultiKey4<K1,K2,K3,K4> other = (MultiKey4<K1,K2,K3,K4>)object;
		
		return new EqualsBuilder().append(this.hashCode(),other.hashCode()).isEquals();
	}
	@Override public int hashCode()
	{
		return new HashCodeBuilder(17,43).append(k1.hashCode()).append(k2.hashCode()).append(k3.hashCode()).append(k4.hashCode()).toHashCode();
	}
}