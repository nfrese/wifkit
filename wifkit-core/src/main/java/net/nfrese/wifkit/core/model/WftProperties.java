package net.nfrese.wifkit.core.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.event.EventListenerList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.nfrese.wifkit.core.utils.WftUtils;

public class WftProperties {
	
	private final WftObject obj;
	
	private final HashMap<String, JsonNode> map = new HashMap<>();
	private final HashMap<String, EventListenerList> listenerMap = new HashMap<>();
	
	public WftProperties(WftObject obj) {
		super();
		this.obj = obj;
	}
	
	public void on(String propertyName, PropertyChangeListener l) {
		
		if (!listenerMap.containsKey(propertyName))
		{
			listenerMap.put(propertyName, new EventListenerList());
		}
		listenerMap.get(propertyName).add(PropertyChangeListener.class, l);
	}
	
	

	public void un(String propertyName, PropertyChangeListener l) {
		
		if (listenerMap.containsKey(propertyName))
		{
			listenerMap.get(propertyName).remove(PropertyChangeListener.class, l);
		}
	}
	
	public void un(PropertyChangeListener l) {
		listenerMap.keySet().forEach((propertyName) -> un(propertyName,l));
	}
	
	public void fire(String propertyName, PropertyChangeEvent evt)
	{
		if (listenerMap.containsKey(propertyName))
		{
			PropertyChangeListener[] listeners = listenerMap.get(propertyName).getListeners(PropertyChangeListener.class);
			for (PropertyChangeListener l : listeners)
			{
				l.propertyChange(evt);
			}
		}
	}
	
	public void put(String propertyName, JsonNode value)
	{
		JsonNode oldValue = map.get(propertyName);

		map.put(propertyName, value);

		if (!WftUtils.nullSafeEquals(oldValue,value))
		{
			fire(propertyName, new PropertyChangeEvent(obj, propertyName, oldValue, value));
		}
	}

	public JsonNode get(String propertyName) {
		return map.get(propertyName);
	}
	
	public JsonNode serialize(JsonNodeFactory nodeFactory)
	{
		ObjectNode props = new ObjectNode(nodeFactory);
		for (Entry<String, JsonNode> entry : map.entrySet())
		{
			props.set(entry.getKey(), entry.getValue());
		}
		return props;
	}


}
