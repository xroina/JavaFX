package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

public class Locate extends EventObject {
	private int x;
	private int y;

	public Locate(Object source, int x, int y) {
		super(source);
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}