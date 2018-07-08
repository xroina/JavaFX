package jp.naixrosoft.xronia.javafx.script;

import java.util.EventObject;
import java.util.concurrent.BlockingQueue;

import jp.naixrosoft.xronia.javafx.controller.GcController;
import jp.naixrosoft.xronia.javafx.event.ClsEvent;
import jp.naixrosoft.xronia.javafx.event.LocateEvent;
import jp.naixrosoft.xronia.javafx.event.PrintEvent;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.State;
import jp.naixrosoft.xronia.script.bytecode.ByteCode;
import jp.naixrosoft.xronia.script.exception.ScriptException;

public class Execute extends jp.naixrosoft.xronia.script.execute.Execute
		implements BaseDefine, Runnable {

	private State ss;
	private final BlockingQueue<EventObject> queue;
	private GcController gcc;

	public Execute(State ss, BlockingQueue<EventObject> queue, GcController gcc, ByteCode c) {
		super(c);

		this.ss = ss;
		this.queue = queue;
		this.gcc = gcc;
	}

	@Override
	public void run() {
		try {
			this.execute();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return;
	}

	@Override
	protected void print(String str) {
		try {
			queue.put(new PrintEvent(this, str));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void cls() {

		try {
			queue.put(new ClsEvent(this));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void locate(int x, int y) {

		try {
			queue.put(new LocateEvent(this, x, y));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected double stickX() {
		return ss.x;
	}

	@Override
	protected double stickY() {
		return ss.y;
	}

	@Override
	protected long button() {
		long ret = 0;
		for(int i = 0; i < ss.button.length; i++)
			ret |= ss.button[i] ? (1L << i) : 0;
		return ret;
	}

	@Override
	protected String getCharacter(int x, int y) {
		return gcc.getCharacter(x, y);
	}
}
