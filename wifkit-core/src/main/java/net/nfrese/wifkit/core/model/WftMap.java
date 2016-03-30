package net.nfrese.wifkit.core.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class WftMap <W extends WftObject> extends WftContainer<W> {

	private Map<String, W> map = new LinkedHashMap<String, W>();
	
	
	public WftMap(String abstractType) {
		super(abstractType);
	}
	
	public void put(String key, W obj)
	{
		map.put(key, obj);
		obj.parent = this;
		page.register(obj);
		ObjectNode on = new ObjectNode(page.getNodeFactory());
		on.put(PROPERTY_NAMEFIELD, key);
		on.set(PROPERTY_VALUEFIELD, obj.serialize());
		say("s_put", on);
	}
	
	public W get(String key)
	{
		return map.get(key);
	}
	
	
	@Override
	public Iterator<W> iterator() {
		return map.values().iterator();
	}
	
	private String findMapKey(WftObject obj)
	{
		for (Entry<String, W> entry : map.entrySet())
		{
			if (entry.getValue().equals(obj))
			{
				return entry.getKey();
			}
		}
		return null;
	}
	
	public void remove(String key) {
		WftObject obj = map.get(key);
		if (obj == null)
		{
			throw new RuntimeException("not registered " + obj);
		}
		else
		{
			page.unRegister(obj);
			obj.parent = null;
		}
		
		map.remove(key);
		
		ObjectNode on = new ObjectNode(page.getNodeFactory());
		on.put(PROPERTY_NAMEFIELD, key);
		say(ETYPE_REMOVEENTRY, on);		
	}
	
	@Override
	public void remove(WftObject obj) {
		
		String k = findMapKey(obj);
		if (k != null)
		{
			remove(k); 
		}
	};
	
}
