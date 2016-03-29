package net.nfrese.wifkit.dom.demo.simple;

import net.nfrese.wifkit.impl.dom.WftDomElement;
import net.nfrese.wifkit.impl.dom.WftDomRoot;

public class SimpleDomDemoRoot extends WftDomRoot {
	
	@Override
	public void init() {

		WftDomElement div = new WftDomElement("div");
		this.setContent(div);
		
		WftDomElement h1 = new WftDomElement("h1", "Simple Wft-DOM Demo");
		h1.setAttribute("style", "color:red;");
		
		div.add(h1);
		
		WftDomElement textInput = new WftDomElement("input");
		div.add(textInput);

		textInput.setAttribute("type", "text");
		textInput.monitor("input", "attr:value", 500);
		
		WftDomElement br = new WftDomElement("br");
		div.add(br);

		WftDomElement span = new WftDomElement("span");
		span.setProperty("text", "Number of Characters:");
		div.add(span);
		
		textInput.addPropertyChangedListener("attr:value", (evt)->{
			System.out.println("hello");
			String value = textInput.getAttribute("value");
			span.setProperty("text", "Number of Characters: " + value.length());
		}, 500);

		super.init();
	}
}
