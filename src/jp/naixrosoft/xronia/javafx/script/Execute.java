package jp.naixrosoft.xronia.javafx.script;

import java.util.EventObject;
import java.util.concurrent.BlockingQueue;

import jp.naixrosoft.xronia.javafx.controller.GcController;
import jp.naixrosoft.xronia.javafx.event.Cls;
import jp.naixrosoft.xronia.javafx.event.Locate;
import jp.naixrosoft.xronia.javafx.event.Print;
import jp.naixrosoft.xronia.javafx.event.ScrollLeft;
import jp.naixrosoft.xronia.javafx.event.ScrollNext;
import jp.naixrosoft.xronia.javafx.event.ScrollPrev;
import jp.naixrosoft.xronia.javafx.event.ScrollRight;
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
			try {
				Thread.sleep(TIME_OUT);
			} catch (InterruptedException e1) {
				throw new RuntimeException(e1);
			}
			gcc.stop();
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(TIME_OUT);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		gcc.stop();
		return;
	}

	/**
	 * Print
	 *
	 * @param str 出力文字列
	 */
	@Override
	public void doPrint(String str) {
		try {
			queue.put(new Print(this, str));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(0, 1);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * クリアスクリーン
	 */
	@Override
	public void doCls() {
		try {
			queue.put(new Cls(this));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(0, 1);
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
	public void setLocate(int x, int y) {
		try {
			queue.put(new Locate(this, x, y));
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
	public double getStickX() {
		return ss.getX();
	}

	/**
	 * スティックY座標取得
	 *
	 * @return	Y座標
	 */
	@Override
	public double getStickY() {
		return ss.getY();
	}

	/**
	 * スティックボタン取得
	 *
	 * @return	ボタンビットマップ
	 */
	@Override
	public long getButton() {
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
	public String getCharacter(int x, int y) {
		return gcc.getCharacter(x, y);
	}

	/**
	 * 上スクロール
	 *
	 * @param y1	スクロール開始位置
	 * @param y2	スクロール終了位置
	 */
	@Override
	public void scrollNext(int y1, int y2) {
		try {
			queue.put(new ScrollNext(this, y1, y2));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(0, 1);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 下スクロール
	 *
	 * @param y1	スクロール開始位置
	 * @param y2	スクロール終了位置
	 */
	@Override
	public void scrollPrev(int y1, int y2) {
		try {
			queue.put(new ScrollPrev(this, y1, y2));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(0, 1);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 左スクロール
	 *
	 * @param x1	スクロール開始位置
	 * @param x2	スクロール終了位置
	 */
	@Override
	public void scrollLeft(int x1, int x2) {
		try {
			queue.put(new ScrollLeft(this, x1, x2));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(0, 1);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 右スクロール
	 *
	 * @param x1	スクロール開始位置
	 * @param x2	スクロール終了位置
	 */
	@Override
	public void scrollRight(int x1, int x2) {
		try {
			queue.put(new ScrollRight(this, x1, x2));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(0, 1);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
