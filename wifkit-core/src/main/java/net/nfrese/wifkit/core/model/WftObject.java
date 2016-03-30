package net.nfrese.wifkit.core.model;

import java.beans.PropertyChangeListener;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public abstract class WftObject {

	public static final String WIIDFIELD = "wiid";
	public static final String ETYPEFIELD = "etype";
	public static final String CLIENTTYPEFIELD = "tp";
	public static final String EPARAMSFIELD = "pa";
	public static final String INITPROPS = "initprops";

	public static final String OBJFIELD = "o";
	public static final String INDEXFIELD = "ix";
	public static final String TOINDEXFIELD = "toix";
	//public static final String NUMBERFIELD = "n";
	
	public static final String ETYPE_ADD = "s_add";
	public static final String ETYPE_INSERT = "s_ins";
	public static final String ETYPE_MOVE = "s_mv";
	
	public static final String ETYPE_SETPROPERTY = "s_putprop";
	public static final String PROPERTY_NAMEFIELD = "k";
	public static final String PROPERTY_VALUEFIELD = "v";

	public static final String ETYPE_SUBSCRIBE = "s_subscribe";
	public static final String ETYPE_UNSUBSCRIBE = "s_unSubscribe";
	public static final String PROPERTY_ENAMEFIELD = "ename";
	public static final String PROPERTY_PROPNAMEFIELD = "propname";
	public static final String PROPERTY_PRIORITYFIELD = "prio";
	public static final String PROPERTY_DELAYMILLISECONDS = "delayms";

	public static final String ETYPE_REMOVE = "s_rm";  // for lists
	public static final String ETYPE_REMOVEENTRY = "s_rme"; // for maps
	public static final String ETYPE_ALERT = "s_alert";
	public static final int DELAY_IMMEDIATE = 0; 
	public static final int DELAY_POSTPONED = -1; 

	private final String tp;

	private final String wiid = UUID.randomUUID().toString();
	protected WftPage<WftObject> page;

	protected WftProperties properties = new WftProperties(this);
	protected WftSubscriptions subscriptions = new WftSubscriptions(this);
	
	protected WftObject parent = null;

	public WftObject(String abstractType) {
		super();
		this.tp = abstractType;
	}

	public String getWiid() {
		return wiid;
	}

	private static class PMethod
	{
		private String method;
		PMethod(String method) { this.method = method; }

		public String getField()
		{
			if (method.contains(":"))
			{
				return method.split(":")[1];
			}
			else
			{
				return null;
			}
		}

		public String getBaseName()
		{
			if (method.contains(":"))
			{
				return method.split(":")[0];
			}
			else
			{
				return method;
			}
		}
	}

	private boolean argsAreKeyValue(JsonNode args)
	{
		return args != null && args.has("k");
	}
	
	public void call(String method, JsonNode args) {
		PMethod pm = new PMethod(method);

		if ("init".equals(method))
		{
			init();
		}
		else if ("set".equals(pm.getBaseName()) || argsAreKeyValue(args))
		{
			String k = args.get(PROPERTY_NAMEFIELD).textValue();
			JsonNode v = args.get(PROPERTY_VALUEFIELD);

			String pmField = pm.getField();

			if (pmField != null && !k.equals(pmField))
			{
				throw new RuntimeException("key "+ k + "!= ename " + method);
			}

			setProperty(k, v);
			System.out.println(args);
		}
		else 
		{
			fireAction(method, args);
		}

	}

	public void setPage(WftPage<WftObject> page) {
		this.page = page;
	}

	public String getStrProperty(String propertyName) {
		JsonNode prop = properties.get(propertyName);
		if (prop != null && prop.isTextual())
		{
			return prop.asText();
		}
		return null;
	}

	public boolean getBooleanProperty(String propertyName)
	{
		JsonNode prop = properties.get(propertyName);
		if (prop != null && prop.isBoolean())
		{
			return prop.asBoolean();
		}
		return false;
	}

	public void setProperty(String propertyName, JsonNode value)
	{
		properties.put(propertyName, value);

		if (page != null)
		{
			ObjectNode on = new ObjectNode(page.getNodeFactory());
			on.put(PROPERTY_NAMEFIELD, propertyName);
			on.set(PROPERTY_VALUEFIELD, value);
			say(ETYPE_SETPROPERTY, on);
		}
	}

	//protected EventListenerList listenerList = new EventListenerList();

	public void monitor(String propertyName, int delayMilliseconds)
	{
		subscriptions.subscribe("set:" + propertyName, delayMilliseconds);
	}
	
	public void monitor(String eventName, String propertyName, int delayMilliseconds)
	{
		subscriptions.subscribe(eventName, propertyName, delayMilliseconds);
	}

	public void addPropertyChangedListener(String propertyName, PropertyChangeListener l) {
		properties.on(propertyName, l);
	}

	public void removePropertyChangedListener(PropertyChangeListener l) {
		properties.un(l);
	}

	protected void fireAction(String actionName, JsonNode args) {
		for (WftActionListener l : subscriptions.getListeners(actionName, WftActionListener.class))
		{
			l.onAction(args);
		}
	}

	public void addActionListener(String actionName, WftActionListener l, int delayMilliseconds)
	{
		subscriptions.addListener(actionName, WftActionListener.class, l, delayMilliseconds);
	}

	public void removeActionListener(WftActionListener l) {
		subscriptions.removeListener(WftActionListener.class, l);
	}

	public void setProperty(String name, String string) {
		setProperty(name, new TextNode(string));
	}

	public void setProperty(String name, boolean  value) {
		setProperty(name, BooleanNode.valueOf(value));
	}	

	////

	public void say(String etype, JsonNode value)
	{
		page.say(this.wiid, etype, value);
	}

	public JsonNode serialize()
	{
		ObjectNode on = new ObjectNode(page.getNodeFactory());
		on.put(WIIDFIELD, wiid);
		on.put(CLIENTTYPEFIELD, tp);
		on.set(INITPROPS, properties.serialize(page.getNodeFactory()));
		return on;
	}

	public void init()
	{

	}


}
