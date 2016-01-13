package net.nfrese.wifkit.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WftCall {
	
	public static ThreadLocal<WftCall> currentCall =
	        new ThreadLocal<WftCall>();
	
	private List<ObjectNode> incomingEvents = new ArrayList<ObjectNode>();
	private List<ObjectNode> outboundEvents = new ArrayList<ObjectNode>();
	
	public JsonNodeFactory getNodeFactory()
	{
		return new JsonNodeFactory(true);
	}
	
	public void addIncomingEvent(ObjectNode event)
	{
		incomingEvents.add(event);
	}
	
	public Iterator<ObjectNode> iterateIncoming()
	{
		return incomingEvents.iterator();
	}
	
	public void addOutboundEvent(ObjectNode event)
	{
		outboundEvents.add(event);
	}
	
	public JsonNode getResult()
	{
		ObjectNode result = new ObjectNode(getNodeFactory());
		ArrayNode events = new ArrayNode(getNodeFactory());
		for (ObjectNode event : outboundEvents)
		{
			events.add(event);
		}
		result.set("events", events);
		return result;
	}
	

}
