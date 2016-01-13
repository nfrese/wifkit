package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftList;
import net.nfrese.wifkit.core.model.WftObject;
import net.nfrese.wifkit.demo.widgets.MyWftSelect.SelectionChangedListener;

public class MyWftList extends WftList<WftObject> {

	public MyWftList(String abstractType) {
		super(abstractType);
	}

	@Override
	public void init() {

		final MyWftLabel welcomelabel = new MyWftLabel("Welcome to Wft");
		this.add(welcomelabel);
		
		final MyWftLabel sizelabel = new MyWftLabel("Number of Characters");
		this.add(sizelabel);

		final MyWftTextField textField = new MyWftTextField("Edit me!");
		this.add(textField);

		textField.addPropertyChangedListener("text", (evt) -> {
			sizelabel.setText("Number of Characters: " + textField.getText().length());
		}, 1000);

		final MyWftButton button = new MyWftButton("Click me");
		this.add(button);	

		button.addActionListener("clicked", (args) -> {
			if (contains(welcomelabel))
			{
				remove(welcomelabel);
			}
			page.alert("Button clicked. " + textField.getText().toUpperCase());
		}, WftObject.DELAY_IMMEDIATE);
		
		MyWftSelect select = new MyWftSelect();
		this.add(select);
		{
			MyWftOption option = new MyWftOption("Monday");
			select.add(option);
		}
		{
			MyWftOption option = new MyWftOption("Tuesday");
			select.add(option);
		}
		{
			MyWftOption option = new MyWftOption("Wednesday");
			select.add(option);
		}
		{
			MyWftOption option = new MyWftOption("Thursday");
			select.add(option);
		}
		{
			MyWftOption option = new MyWftOption("Friday");
			select.add(option);
			option.setSelected(true);
		}
		{
			MyWftOption option = new MyWftOption("Saturday");
			select.add(option);
		}
		{
			MyWftOption option = new MyWftOption("Sunday");
			select.add(option);
		}
		
		select.addSelectionChangedListener(new SelectionChangedListener() {
			
			@Override
			public void selected(MyWftOption option) {
				page.put("notification_area", new MyWftNotification("Changed " + option.getStrProperty("text")));
			}
			
			@Override
			public void deSelected(MyWftOption option) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//select.subscribe("change", 500);
		
		select.addActionListener("change", (args)  -> {
				//page.put("notification_area", new MyWftNotification("Changed"));
				
		}, 500);
		
		MyWftOrderListWidget listWidget = new MyWftOrderListWidget();
		this.add(listWidget);
		
		super.init();
	}
}
