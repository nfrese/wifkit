package net.nfrese.wifkit.impl.dom;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.nfrese.wifkit.core.model.WftList;
import net.nfrese.wifkit.core.model.WftObject;

public class WftDomElement extends WftList<WftObject> {

	public WftDomElement(String name) {
		super("DomElement");
		this.setProperty("name", name);
	}

	public WftDomElement(String name, String text) {
		super("DomElement");
		this.setProperty("name", name);
		this.setProperty("text", text);
	}

	public void setAttribute(String attrName, String value) {
		this.setProperty("attr:" + attrName, value);
	}

	public String getAttribute(String attrName) {
		return this.getStrProperty("attr:" + attrName);
	}
	
	public void domInvoke(String methodName, ArrayNode params)
	{
		JsonNodeFactory nf = this.page.getNodeFactory();
		
		ObjectNode call = nf.objectNode();
		call.set("method", nf.textNode(methodName));
		call.set("pa", params);
		this.say("dominvoke", call);		
	}

	
}
