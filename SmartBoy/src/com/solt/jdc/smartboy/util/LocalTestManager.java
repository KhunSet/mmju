package com.solt.jdc.smartboy.util;

import java.util.ArrayList;
import java.util.List;

import com.solt.jdc.smartboy.dto.Category;
import com.solt.jdc.smartboy.dto.Item;
import com.solt.jdc.smartboy.dto.Table;

public class LocalTestManager implements LocalManager {
	
	private static final List<Item> ITEMS = new ArrayList<Item>();
	private static LocalManager local;
	 
	private LocalTestManager() {}
	
	public static LocalManager getTestLocalManaget() {
		local = (null != local)? local : new LocalTestManager();
		return local;
	}
	
	static {
		ITEMS.add(new Item(1, "Tea (Pot Kya)", 1, 250));
		ITEMS.add(new Item(2, "Tea (Cho Saint)", 1, 250));
		ITEMS.add(new Item(3, "Ice Coffee (black)", 2, 500));
		ITEMS.add(new Item(4, "Ice Tea", 2, 500));
	}

	@Override
	public Table getTableInfo(int tableId) {
		return new Table(tableId, String.valueOf(tableId), 4);
	}

	@Override
	public List<Table> getTables() {
		List<Table> tables = new ArrayList<Table>();
		for(int i=0; i < 30; i++) {
			Table t = new Table(i + 1, String.valueOf(i+1), 4);
			tables.add(t);
		}
		return tables;
	}

	@Override
	public List<Category> getCategories() {
		List<Category> list = new ArrayList<Category>();
		list.add(new Category(1, "Hot Drinks"));
		list.add(new Category(2, "Cold Drinks"));
		list.add(new Category(3, "Snacks"));
		list.add(new Category(4, "Lunch"));
		list.add(new Category(5, "Dinner"));
		return list;
	}

	@Override
	public List<Item> getItems(int category) {
		List<Item> list = new ArrayList<Item>();
		for(Item i : ITEMS) {
			if(i.getCategory() == category)
				list.add(i);
		}
		return list;
	}

	@Override
	public Item getItem(int id) {
		for(Item i : ITEMS) {
			if(i.getId()== id)
				return i;
		}
		return null;
	}

}
