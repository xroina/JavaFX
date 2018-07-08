package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

public class PrintEvent extends EventObject {
	private String str;

	public PrintEvent(Object source, String str) {
		super(source);
		this.str = str;
	}

	public String getString() {
		return str;
	}
}
