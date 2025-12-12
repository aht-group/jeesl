package org.jeesl.model.ejb.system.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStore;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSessionKeystore <KEY extends JeeslIoCryptoKey<?,?>,
												KT extends JeeslIoCryptoKeyState<?,?,KT,?>,
												ST extends JeeslIoCryptoStoreType<?,?,ST,?>>
						implements JeeslIoCryptoStore<KEY,KT,ST>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSessionKeystore.class);
	public static final long serialVersionUID=1;
	
	private final Map<KEY,SecretKey> mapKey;
	private final Map<KEY,KT> mapState; @Override public Map<KEY,KT> getMapState() {return mapState;}

	private final List<KEY> keys; public List<KEY> getKeys() {return keys;}

	private ST type; public ST getType() {return type;} public void setType(ST type) {this.type = type;}

	public AbstractSessionKeystore()
	{
		mapKey = new HashMap<>();
		mapState = new HashMap<>();
		keys = new ArrayList<>();
	}
	
	@Override public void enable(KEY key, KT state, SecretKey secret)
	{
		if(!keys.contains(key)) {keys.add(key);}
//		AbstractSessionKeyStore.getKeyFromPassword(mapPassword.get(key),key.getPwdSalt());
		if(secret!=null){mapKey.put(key,secret);}
		mapState.put(key,state);
	}
	
	public void disable(KEY key, KT state)
	{
		mapState.put(key,state);
		if(!keys.contains(key)) {keys.add(key);}
		if(mapKey.containsKey(key)) {mapKey.remove(key);}
	}
	
	@Override public boolean isUnlocked(KEY key)
	{
		return mapKey.containsKey(key);
	}
	
	@Override public SecretKey getSecretKey(KEY key)
	{
		if(mapKey.containsKey(key)) {return mapKey.get(key);}
		return null;
	}
}