package net.nfrese.wifkit.demo.widgets;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import net.nfrese.wifkit.core.model.WftList;

public class MyWftSelect extends WftList<MyWftOption> {

	private EventListenerList listenerList = new EventListenerList();
	
	public static interface SelectionChangedListener extends EventListener {
		void selected(MyWftOption option);
		void deSelected(MyWftOption option);
	}
	
	public MyWftSelect() {
		super("Select");
	}

	@Override
	public void init() {

		super.init();
		
		
	}

	public void addSelectionChangedListener(SelectionChangedListener l)
	{
		this.listenerList.add(SelectionChangedListener.class, l);
	}
	
	private void fireSelectionChangedListenersSelected(MyWftOption obj)
	{
		for ( SelectionChangedListener l : this.listenerList.getListeners(SelectionChangedListener.class))
		{
			l.selected(obj);
		}
	}
	
	private void fireSelectionChangedListenersDeSelected(MyWftOption obj)
	{
		for ( SelectionChangedListener l : this.listenerList.getListeners(SelectionChangedListener.class))
		{
			l.deSelected(obj);
		}
	}
	
	
	@Override
	public void add(final MyWftOption obj) {
		super.add(obj);

		obj.addPropertyChangedListener("selected", (evt) -> {

			if (evt.getNewValue().equals(new JsonNodeFactory(true).booleanNode(true)))
			{
				fireSelectionChangedListenersSelected(obj);
			}
			else
			{
				fireSelectionChangedListenersDeSelected(obj);
			}
		}, -1);
		
//		obj.addActionListener(new WftActionListener() {
//			
//			@Override
//			public void onAction(JsonNode args) {
//				if (args.get("v").asBoolean())
//				{
//					fireSelectionChangedListenersSelected(obj);
//				}
//				else
//				{
//					fireSelectionChangedListenersDeSelected(obj);
//				}
//			}
//			
//			@Override
//			public int getDelay() {
//				return -1;
//			}
//			
//			@Override
//			public String getAction() {
//				return "set:selected";
//			}
//		});
	}
	
}
