package net.nfrese.wifkit.dom.demo.advanced;

import net.nfrese.wifkit.core.model.WftObject;
import net.nfrese.wifkit.impl.dom.WftDomElement;
import net.nfrese.wifkit.impl.dom.WftDomRoot;

public class AdvancedDomDemoRoot extends WftDomRoot {
	
	@Override
	public void init() {

		WftDomElement div = new WftDomElement("div");
		this.setContent(div);
		
		/// Heading
		
		WftDomElement h1 = new WftDomElement("h1", "Advanced Wft-DOM Demo");
		h1.setAttribute("style", "color:green;");
		
		div.add(h1);
		
		final WftDomElement welcomelabel = new WftDomElement("p", "Welcome to Wft");
		welcomelabel.setAttribute("style", "color:blue;");
		div.add(welcomelabel);
		
		/// Text field

		WftDomElement span = new WftDomElement("span");
		span.setProperty("text", "Please enter text:");
		div.add(span);
		
		WftDomElement br = new WftDomElement("br");
		div.add(br);
		
		WftDomElement textInput = new WftDomElement("input");
		div.add(textInput);
		
		textInput.setAttribute("type", "text");
		textInput.monitor("input", "attr:value", 500);
		
		textInput.addPropertyChangedListener("attr:value", (evt)->{
			System.out.println("hello");
			String value = textInput.getAttribute("value");
			span.setProperty("text", "Number of Characters: " + value.length());
		}, 500);

		div.add(new WftDomElement("br"));
		
		/// Button
		
		WftDomElement button = new WftDomElement("input");
		div.add(button);

		button.setAttribute("type", "button");
		button.setAttribute("value", "Click me");
		
		button.addActionListener("click", (evt)-> {
			
			alert("Contents of the Text Field: " + textInput.getAttribute("value"));
			
			// Hide the Welcome-Label
			if (div.contains(welcomelabel))
			{
				div.remove(welcomelabel);
			}
			
			
		}, WftObject.DELAY_IMMEDIATE);
		
		div.add(new WftDomElement("br"));
		
		/// select
		
		WftDomElement select = new WftDomElement("select");
		div.add(select);
		
		select.addActionListener("change", (evt)-> {
			
			System.out.println("change");
			
		}, 100);
		
		{
			WftDomElement option = new WftDomElement("option", "Monday");
			select.add(option);
			option.monitor("watch", "selected", WftObject.DELAY_POSTPONED);
		}
		{
			WftDomElement option = new WftDomElement("option", "Tuesday");
			select.add(option);
			option.monitor("watch", "selected", WftObject.DELAY_POSTPONED);
		}
		{
			WftDomElement option = new WftDomElement("option", "Wednesday");
			select.add(option);
			option.monitor("watch", "selected", WftObject.DELAY_POSTPONED);
		}
		{
			WftDomElement option = new WftDomElement("option", "Thursday");
			select.add(option);
			option.monitor("watch", "selected", WftObject.DELAY_POSTPONED);
		}
		{
			WftDomElement option = new WftDomElement("option", "Friday");
			option.setAttribute("selected", "true");
			select.add(option);
			option.monitor("watch", "selected", WftObject.DELAY_POSTPONED);
		}
		{
			WftDomElement option = new WftDomElement("option", "Saturday");
			select.add(option);
			option.monitor("watch", "selected", WftObject.DELAY_POSTPONED);
		}
		{
			WftDomElement option = new WftDomElement("option", "Sunday");
			select.add(option);
			option.monitor("watch", "selected", WftObject.DELAY_POSTPONED);
		}
		
		super.init();
	}
}
