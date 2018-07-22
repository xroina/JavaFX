package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

/**
 * 右スクロールイベントクラス
 *
 * @author xronia
 *
 */
public class ScrollRight extends EventObject {

	private static final long serialVersionUID = 1L;

	private int x1;		// スクロール範囲X1
	private int x2;		// スクロール範囲X2

	/**
	 * コントラクタ
	 *
	 * @param source	イベントを生成したオブジェクト
	 * @param x1		スクロール範囲X1
	 * @param x2		スクロール範囲X2
	 */
	public ScrollRight(Object source, int x1, int x2) {
		super(source);
		this.x1 = x1;
		this.x2 = x2;
	}

	/**
	 * スクロール範囲X1取得
	 *
	 * @return		スクロール範囲X1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * スクロール範囲X2取得
	 *
	 * @return		スクロール範囲X2
	 */
	public int getX2() {
		return x2;
	}
}
