package org.jeesl.interfaces.rest.system;

import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;

@FunctionalInterface
public interface RestFactoryFunction<T>
{
	public T restByCredential(Class<T> c, JsonSsiCredential credential);
}