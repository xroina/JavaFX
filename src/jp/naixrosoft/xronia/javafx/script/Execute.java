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

/**
 * スクリプト実行クラス
 *
 * @author xronia
 *
 */
public class Execute extends jp.naixrosoft.xronia.script.execute.Execute
		implements BaseDefine, Runnable {

	private State ss;								// ジョイスティック状態
	private final BlockingQueue<EventObject> queue;	// イベントキュー
	private GcController gcc;						// グラフィックコンテキストコントローラー

	/**
	 * コントラクタ
	 *
	 * @param ss	ジョイスティック状態
	 * @param queue	イベントキュー
	 * @param gcc	グラフィックコンテキストコントローラー
	 * @param c		バイトコード
	 */
	public Execute(State ss, BlockingQueue<EventObject> queue,
			GcController gcc, ByteCode c) {
		super(c);

		this.ss = ss;
		this.queue = queue;
		this.gcc = gcc;
	}

	/**
	 * スレッド開始メソッド
	 */
	@Override
	public void run() {
		try {
			this.execute();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * Print
	 *
	 * @param str 出力文字列
	 */
	@Override
	protected void doPrint(String str) {
		try {
			queue.put(new PrintEvent(this, str));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * クリアスクリーン
	 */
	@Override
	protected void doCls() {
		try {
			queue.put(new ClsEvent(this));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 座標指定
	 *
	 * @param x	座標
	 * @param y 座標
	 */
	@Override
	protected void setLocate(int x, int y) {
		try {
			queue.put(new LocateEvent(this, x, y));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * スティックX座標取得
	 *
	 * @return	X座標
	 */
	@Override
	protected double getStickX() {
		return ss.getX();
	}

	/**
	 * スティックY座標取得
	 *
	 * @return	Y座標
	 */
	@Override
	protected double getStickY() {
		return ss.getY();
	}

	/**
	 * スティックボタン取得
	 *
	 * @return	ボタンビットマップ
	 */
	@Override
	protected long getButton() {
		long ret = 0;
		for(int i = 0; i < State.BUTTON_MAX; i++)
			ret |= ss.getButton(i) ? (1L << i) : 0;
		return ret;
	}

	/**
	 * キャラクタ取得
	 *
	 * @param x	座標
	 * @param y 座標
	 * @return	キャラクタ文字
	 */
	@Override
	protected String getCharacter(int x, int y) {
		return gcc.getCharacter(x, y);
	}
}
