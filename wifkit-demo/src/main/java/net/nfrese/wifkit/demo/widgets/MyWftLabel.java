package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftObject;

public class MyWftLabel extends WftObject {

	public MyWftLabel(String text) {
		super("Label");
		setProperty("text", text);
	}

	@Override
	public void init() {
		super.init();
		
	}

	public void setText(String newText) {
		setProperty("text", newText);
	}

}
