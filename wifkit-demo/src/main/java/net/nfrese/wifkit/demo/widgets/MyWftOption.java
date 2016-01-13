package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftObject;

public class MyWftOption extends WftObject {

	public MyWftOption(String text) {
		super("Option");
		setProperty("text", text);
		setProperty("selected", false);
	}

	@Override
	public void init() {
		super.init();
		monitor("selected", DELAY_POSTPONED);
	}

	public void setText(String newText) {
		setProperty("text", newText);
	}
	
	public void setSelected(boolean b)
	{
		setProperty("selected", b);
	}
	
	public boolean isSelected()
	{
		return getBooleanProperty("selected");
	}

}
