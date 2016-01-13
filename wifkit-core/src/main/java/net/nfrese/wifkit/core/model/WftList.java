package net.nfrese.wifkit.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WftList<W extends WftObject> extends WftContainer<W> {
	
	private ArrayList<W> list = new ArrayList<W>();
	
	public WftList(String abstractType) {
		super(abstractType);
	}
	
	public void add(W obj)
	{
		obj.parent = this;
		list.add(obj);
		page.register(obj);
		say(ETYPE_ADD, obj.serialize());
	}
	
	public void insert(int index, W obj)
	{
		obj.parent = this;
		list.add(index, obj);
		page.register(obj);
		
		ObjectNode on = new ObjectNode(page.getNodeFactory());
		on.put(INDEXFIELD, index);
		on.set(OBJFIELD, obj.serialize());
		say(ETYPE_INSERT, on);
	}

	@Override
	public Iterator<W> iterator() {
		return list.iterator();
	}

	@Override
	public JsonNode serialize() {
		//if (list.size() > 0) throw new RuntimeException("serialize after add");
		//else return super.serialize();
		return super.serialize();
	}

	public boolean contains(WftObject obj)
	{
		return list.contains(obj);
	}
	
	public int indexOf(W obj)
	{
		return list.indexOf(obj);
	}
	
	public int size()
	{
		return list.size();
	}
	
	public boolean moveUp(W obj)
	{
		int ix = indexOf(obj);
		if (ix == -1)
		{
			throw new RuntimeException(obj.getWiid() + " not an element of the list");
		}
		
		if (ix > 0)
		{
			move(ix,ix-1);
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public boolean moveDown(W obj)
	{
		int ix = indexOf(obj);
		if (ix == -1)
		{
			throw new RuntimeException(obj.getWiid() + " not an element of the list");
		}
		
		if (ix < size()-1)
		{
			move(ix,ix+1);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @param index the index of the element to move
	 * @param toPos the index to insert the element after it has been removed
	 */
	public void move(int index, int toPos)
	{
		W cutElement = list.remove(index);
		list.add(toPos, cutElement);
		
		ObjectNode on = new ObjectNode(page.getNodeFactory());
		on.put(INDEXFIELD, index);
		on.put(TOINDEXFIELD, toPos);
		say(ETYPE_MOVE, on);
	}
	
	@Override
	public void remove(W obj) {
		
		boolean removed = list.remove(obj);
		if (!removed)
		{
			throw new RuntimeException("not found! cannot remove from list " + obj);
		}
		
		super.remove(obj);
		
	}
	
}
