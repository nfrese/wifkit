package net.nfrese.wifkit.core.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class WftContainer<W extends WftObject> extends WftObject {

	public WftContainer(String abstractType) {
		super(abstractType);
	}
	
	@Override
	public void init() {
		super.init();
		Iterator<W> it = iterator();
		while (it.hasNext())
		{
			it.next().init();
		}
	}
	
	public abstract Iterator<W> iterator();
	
	public void remove(W obj)
	{
		if(obj instanceof WftContainer)
		{
			((WftContainer<?>)obj).unregisterChildren();
		}
		
		page.unRegister(obj);
		obj.parent = null;
		
		ObjectNode on = new ObjectNode(page.getNodeFactory());
		on.put(WIIDFIELD, obj.getWiid());
		say(ETYPE_REMOVE, on);		
	}
	
	private void unregisterChildren()
	{
		Iterator<W> it = iterator();
		
		ArrayList<W> listcopy = new ArrayList<>();
		while (it.hasNext())
		{
			listcopy.add(it.next());
		}
		listcopy.forEach(item -> remove(item));
	}

}
