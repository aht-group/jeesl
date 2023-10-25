package org.jeesl.controller.handler.io.crypto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.jeesl.interfaces.bean.system.io.crypto.JeeslIoCryptoOptionBean;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKey;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoKeyState;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStore;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMemoryKeyStore<KEY extends JeeslIoCryptoKey<?,?>,
									KT extends JeeslIoCryptoKeyState<?,?,KT,?>,
									ST extends JeeslIoCryptoStoreType<?,?,ST,?>>
							implements Serializable,JeeslIoCryptoStore<KEY,KT,ST>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(JeeslMemoryKeyStore.class);

	private static final Map<Long,SecretKey> map = new HashMap<>();
	
	private Map<KEY,KT> mapState; @Override public Map<KEY,KT> getMapState() {return mapState;}
	
	private ST type; @Override public ST getType() {return type;}
	
	public static <KEY extends JeeslIoCryptoKey<?,?>,
					KT extends JeeslIoCryptoKeyState<?,?,KT,?>,
					ST extends JeeslIoCryptoStoreType<?,?,ST,?>>
			JeeslMemoryKeyStore<KEY,KT,ST> instance(){return new JeeslMemoryKeyStore<>();}
	private JeeslMemoryKeyStore()
	{
		
	}
	
	public JeeslMemoryKeyStore<KEY,KT,ST> init(JeeslIoCryptoOptionBean<KT,ST> bCrypto, List<KEY> keys)
	{
		logger.info("Checking state for "+keys.size()+" Keys");
		type = bCrypto.getTypeMemory();
		mapState = new HashMap<>();
		for(KEY key : keys)
		{
			if(map.containsKey(key.getId())) {mapState.put(key,bCrypto.getStateUnlocked());}
			else {mapState.put(key,bCrypto.getStateLocked());}
			
			logger.info(key.toString()+" "+mapState.get(key).getCode());
		}
		return this;
	}
	
	@Override public void enable(KEY key, KT state, SecretKey secret)
	{
		map.put(key.getId(),secret);
		mapState.put(key,state);
	}
	@Override public void disable(KEY key, KT state)
	{
		if(map.containsKey(key.getId())) {map.remove(key.getId());}
		mapState.put(key,state);
		
	}

	@Override public SecretKey getSecretKey(KEY key)
	{
		if(map.containsKey(key.getId())) {return map.get(key.getId());}
		return null;
	}

	@Override public boolean isUnlocked(KEY key) {return map.containsKey(key.getId());}

}