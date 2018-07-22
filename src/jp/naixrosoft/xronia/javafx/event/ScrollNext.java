package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

/**
 * 上スクロールイベントクラス
 *
 * @author xronia
 *
 */
public class ScrollNext extends EventObject {

	private static final long serialVersionUID = 1L;

	private int y1;		// スクロール範囲Y1
	private int y2;		// スクロール範囲Y1

	/**
	 * コントラクタ
	 *
	 * @param source	イベントを生成したオブジェクト
	 * @param y1		スクロール範囲Y1
	 * @param y2		スクロール範囲Y2
	 */
	public ScrollNext(Object source, int y1, int y2) {
		super(source);
		this.y1 = y1;
		this.y2 = y2;
	}

	/**
	 * スクロール範囲Y1取得
	 *
	 * @return		スクロール範囲Y1
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * スクロール範囲Y2取得
	 *
	 * @return		スクロール範囲Y2
	 */
	public int getY2() {
		return y2;
	}
}
