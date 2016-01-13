package net.nfrese.wifkit.core.model;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WftSubscriptions {

	private final WftObject obj;
	
	private Map<String,SubScription> map = new HashMap<>();
	
	public WftSubscriptions(WftObject obj) {
		super();
		this.obj = obj;
	}

	public <T extends EventListener> void removeListener(Class<T> t, T l) {
		map.forEach( (propertyName, subScription) -> { 
			subScription.listeners.remove(t, l); 
		});
	}

	public <T extends EventListener> void addListener(String eventName, 
			Class<T> t, T l, int delayMilliseconds)
	{
		SubScription newSubScription = new SubScription(eventName, delayMilliseconds);
		subscribe(newSubScription).listeners.add(t, l);;
	}

	public <T extends EventListener> void subscribe(String eventName, int delayMilliseconds)
	{
		SubScription newSubScription = new SubScription(eventName, delayMilliseconds);
		subscribe(newSubScription);
	}
	
	private SubScription subscribe(SubScription newSubscription)
	{
		SubScription subScription = map.get(newSubscription.eventName);
		
		if (subScription != null)
		{
			if (subScription.delayMilliseconds == newSubscription.delayMilliseconds)
			{
				return subScription;
			}
			else
			{
				unSubscribe(subScription.eventName); // re-register to change delay
				sendSubscription(newSubscription);
				return subScription;
			}
		}
		else
		{
			map.put(newSubscription.eventName, newSubscription);
			sendSubscription(newSubscription);
			return newSubscription;
		}
	}

	private void sendSubscription(SubScription newSubscription)
	{
		obj.say(WftObject.ETYPE_SUBSCRIBE, newSubscription.serialize(obj.page.getNodeFactory()));
	}
	
	private void unSubscribe(String eventName)
	{
		ObjectNode on = new ObjectNode(obj.page.getNodeFactory());
		on.put(WftObject.PROPERTY_ENAMEFIELD, eventName);
		obj.say(WftObject.ETYPE_UNSUBSCRIBE, on);
	}		
	
	public <T extends EventListener> T[] getListeners(String eventName, Class<T> t)
	{
		SubScription subScription = map.get(eventName);
		
		if (subScription != null)
		{
			return subScription.listeners.getListeners(t);
		}
		else
		{
			return new EventListenerList().getListeners(t);
		}
	}
	
	////////////////////
	
	public static class SubScription
	{
		public SubScription(String eventName, Integer delayMilliseconds) {
			super();
			this.eventName = eventName;
			this.delayMilliseconds = delayMilliseconds;
		}

		public final String eventName;
		public Integer delayMilliseconds;
		public EventListenerList listeners = new EventListenerList();
		
		public ObjectNode serialize(JsonNodeFactory nodeFactory)
		{
			ObjectNode on = new ObjectNode(nodeFactory);
			on.put(WftObject.PROPERTY_ENAMEFIELD, eventName);
			on.put(WftObject.PROPERTY_DELAYMILLISECONDS, delayMilliseconds);
			return on;
		}
	}
}
