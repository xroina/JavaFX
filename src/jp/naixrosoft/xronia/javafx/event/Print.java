package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

public class Print extends EventObject {
	private String str;

	public Print(Object source, String str) {
		super(source);
		this.str = str;
	}

	public String getString() {
		return str;
	}
}
