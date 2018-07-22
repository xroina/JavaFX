package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

/**
 * ロケーションイベントクラス
 *
 * @author xronia
 *
 */
public class Locate extends EventObject {

	private static final long serialVersionUID = 1L;

	private int x;		// X座標
	private int y;		// Y座標

	/**
	 * コントラクタ
	 *
	 * @param source	イベントを生成したオブジェクト
	 * @param x			X座標
	 * @param y			Y座標
	 */
	public Locate(Object source, int x, int y) {
		super(source);
		this.x = x;
		this.y = y;
	}

	/**
	 * X座標取得
	 *
	 * @return	X座標
	 */
	public int getX() {
		return x;
	}

	/**
	 * Y座標取得
	 *
	 * @return	Y座標
	 */
	public int getY() {
		return y;
	}
}
