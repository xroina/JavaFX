package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

public class ScrollLeft extends EventObject {
	private int x1;
	private int x2;

	public ScrollLeft(Object source, int x1, int x2) {
		super(source);
		this.x1 = x1;
		this.x2 = x2;
	}

	public int getX1() {
		return x1;
	}

	public int getX2() {
		return x2;
	}
}
