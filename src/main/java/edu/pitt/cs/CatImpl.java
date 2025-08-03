
package edu.pitt.cs;

public class CatImpl implements Cat {

	private int id;
	private String name;
	private boolean rented;

	public CatImpl(int id, String name) {
		this.id = id;
		this.name = name;
		this.rented = false; // Initial state: not rented
	}

	@Override
	public void rentCat() {
		this.rented = true;
	}

	@Override
	public void returnCat() {
		this.rented = false;
	}

	@Override
	public void renameCat(String name) {
		if (name != null) {
			this.name = name;
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public boolean getRented() {
		return this.rented;
	}

	@Override
	public String toString() {
		return "ID " + this.id + ". " + this.name;
	}
}