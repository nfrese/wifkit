package net.nfrese.wifkit.core.model;

import java.util.Iterator;
import java.util.WeakHashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class WftPage<W extends WftObject> extends WftMap<W> {
	
	private final WeakHashMap<String, WftObject> registry = new WeakHashMap<String, WftObject>();
	
	public WftPage(String abstractType) {
		super(abstractType);
		register(this);
	}
	
	public JsonNodeFactory getNodeFactory()
	{
		return new JsonNodeFactory(true);
	}
	
	public void register(WftObject obj)
	{
		if(registry.containsKey(obj.getWiid()))
		{
			throw new RuntimeException(obj.getWiid() + " already registered!");
		}

		registry.put(obj.getWiid(), obj);
		obj.setPage((WftPage<WftObject>) this);
	}
	
	public JsonNode initPage()
	{
		ObjectNode node = new ObjectNode(getNodeFactory());
		node.put("etype", "init");
		
		WftCall call = new WftCall();
		call.addIncomingEvent(node);	
		processRequest(call);
		return call.getResult();
	}
	
	public void processRequest(WftCall call)
	{
		WftCall.currentCall.set(call);
		
		try {
			Iterator<ObjectNode> it = call.iterateIncoming();
			while (it.hasNext())
			{
				inboundCall(it.next());
			}
		} finally {
			WftCall.currentCall.remove();
		}
	}
	
	private void inboundCall(ObjectNode json)
	{
		String etype = json.get(WftObject.ETYPEFIELD).asText();
		JsonNode args = json.get("pa");

		if (json.has(WftObject.WIIDFIELD))
		{
			String fid = json.get(WftObject.WIIDFIELD).asText();
			WftObject o = registry.get(fid);
		
			if (o != null)
			{
				o.call(etype, args);
			}
		}
		else
		{
			if ("init".equals(etype))
			{
				say(null, "s_page", serialize());
			}
			
			call(etype, args);
		}
	}

	private WftCall getCurrentCall()
	{
		WftCall call = WftCall.currentCall.get();
		if (call == null)
		{
			throw new RuntimeException("No Wft call found on the current thread!");
		}
		else
		{
			return call;
		}
	}
	
	public void say(String fid, String etype, JsonNode args) {
		ObjectNode node = new ObjectNode(getNodeFactory());
		node.put(WftObject.WIIDFIELD, fid);
		node.put(WftObject.ETYPEFIELD, etype);
		node.set(WftObject.EPARAMSFIELD, args);
		
		getCurrentCall().addOutboundEvent(node);
	}

	public void unRegister(WftObject obj) {
		registry.remove(obj.getWiid());
		
	}
	
	public void alert(String message)
	{
		this.say(WftObject.ETYPE_ALERT, new TextNode(message));
	}
	
	

}
