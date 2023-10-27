package org.jeesl.factory.json.io.ssi.core;

import java.util.Objects;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.nat.JeeslIoSsiNat;
import org.jeesl.model.json.io.ssi.core.JsonSsiNat;

public class JsonSsiNatFactory <HOST extends JeeslIoSsiHost<?,?,?>,
								NAT extends JeeslIoSsiNat<?,?,HOST>>
{
	private JsonSsiNat q;
	private JsonSsiNat json;
	
	private JsonSsiHostFactory<HOST> jfHostListen;
	private JsonSsiHostFactory<HOST> jfHostDestination;
	
	public static <HOST extends JeeslIoSsiHost<?,?,?>, NAT extends JeeslIoSsiNat<?,?,HOST>> JsonSsiNatFactory<HOST,NAT> instance() {return new JsonSsiNatFactory<>(null);}
	public static <HOST extends JeeslIoSsiHost<?,?,?>, NAT extends JeeslIoSsiNat<?,?,HOST>> JsonSsiNatFactory<HOST,NAT> instance(JsonSsiNat q) {return new JsonSsiNatFactory<>(q);}
	private JsonSsiNatFactory(JsonSsiNat q)
	{
		this.q=q;
		json = new JsonSsiNat();
		if(Objects.nonNull(q))
		{
			if(Objects.nonNull(q.getListenHost())) {jfHostListen = new JsonSsiHostFactory<>(q.getListenHost());}
			if(Objects.nonNull(q.getDestinationHost())) {jfHostDestination = new JsonSsiHostFactory<>(q.getDestinationHost());}
		}
	}
	
//	public JsonSsiNatFactory<HOST,NAT> code(String code) {json.setCode(code); return this;}
//	public JsonSsiNatFactory<HOST,NAT> url(String url) {json.setUrl(url); return this;}
	public JsonSsiNat build() {return json;}
	
	public static JsonSsiNat create() {return new JsonSsiNat();}
//	public static JsonSsiCredential build(String code) {JsonSsiCredential json = JsonSsiNatFactory.build(); json.setCode(code); return json;}
//	public static JsonSsiCredential build(String user, String pwd){return build(null,null,user,pwd);}
//	public static JsonSsiCredential build(String host, String url, String user, String pwd)
//	{
//		JsonSsiCredential json = new JsonSsiCredential();
//		json.setUser(user);
//		json.setPassword(pwd);
//		json.setUrl(url);
//		json.setHost(host);
//		return json;
//	}
	
	public JsonSsiNat build(NAT ejb)
	{
		JsonSsiNat json = JsonSsiNatFactory.create();
		
		if(Objects.nonNull(q.getId())) {json.setId(ejb.getId());}
		
		if(Objects.nonNull(q.getListenHost())) {json.setListenHost(jfHostListen.build(ejb.getHost()));}
		if(Objects.nonNull(q.getListenPort())) {json.setListenPort(ejb.getPort());}
		
		if(Objects.nonNull(q.getDestinationPort())) {json.setDestinationPort(ejb.getDestinationPort());}
		if(Objects.nonNull(q.getDestinationHost())) {json.setDestinationHost(jfHostDestination.build(ejb.getDestinationHost()));}
		
		return json;
	}
}