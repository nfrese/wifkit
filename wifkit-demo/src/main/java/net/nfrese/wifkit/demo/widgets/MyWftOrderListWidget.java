package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftList;
import net.nfrese.wifkit.core.model.WftObject;
import net.nfrese.wifkit.demo.widgets.MyWftSelect.SelectionChangedListener;

public class MyWftOrderListWidget extends WftList<MyWftOrderedListItem> {

	
	public MyWftOrderListWidget() {
		super("ListPanel");
	}

	@Override
	public void init() {
		{
			MyWftLabel label = new MyWftLabel("One");
			
			MyWftOrderedListItem item = new  MyWftOrderedListItem(label);
			this.add(item);
		}
		{
			MyWftLabel label = new MyWftLabel("Two");
			MyWftOrderedListItem item = new  MyWftOrderedListItem(label);
			this.add(item);
		}
		{
			MyWftLabel label = new MyWftLabel("Three");
			MyWftOrderedListItem item = new  MyWftOrderedListItem(label);
			this.add(item);
		}
		super.init();
	}
}
