package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftList;
import net.nfrese.wifkit.core.model.WftObject;
import net.nfrese.wifkit.demo.widgets.MyWftSelect.SelectionChangedListener;

public class MyWftOrderedListItem extends WftList<WftObject> {

	private final WftObject item;
	
	public MyWftOrderedListItem(WftObject item) {
		super("ListPanel");
		this.item = item;
	}

	@Override
	public void init() {
		
		super.init();
		
		{
			final MyWftButton button = new MyWftButton("UP");
			this.add(button);	

			button.addActionListener("clicked", (args) -> {
				
				MyWftOrderListWidget listWidget = ((MyWftOrderListWidget)parent);
				listWidget.moveUp(this);

			}, WftObject.DELAY_IMMEDIATE);
		}
		{
			final MyWftButton button = new MyWftButton("DOWN");
			this.add(button);	

			button.addActionListener("clicked", (args) -> {
				
				MyWftOrderListWidget listWidget = ((MyWftOrderListWidget)parent);
				listWidget.moveDown(this);

			}, WftObject.DELAY_IMMEDIATE);
		}
		
		this.add(item);
	}
}
