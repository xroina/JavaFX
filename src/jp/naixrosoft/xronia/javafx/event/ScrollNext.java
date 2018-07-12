package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

public class ScrollNext extends EventObject {
	private int y1;
	private int y2;

	public ScrollNext(Object source, int y1, int y2) {
		super(source);
		this.y1 = y1;
		this.y2 = y2;
	}

	public int getY1() {
		return y1;
	}

	public int getY2() {
		return y2;
	}
}
