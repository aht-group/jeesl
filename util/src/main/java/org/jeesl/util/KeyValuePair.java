package org.jeesl.util;

public class KeyValuePair<K,V>
{
	private K key; public K getKey() { return this.key; }
	private V value; public V getValue() { return this.value; } public void setValue(V value) { this.value = value; }
	
	public KeyValuePair(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
}
