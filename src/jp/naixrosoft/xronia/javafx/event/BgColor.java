package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

/**
 * カラーイベントクラス
 *
 * @author xronia
 *
 */
public class BgColor extends EventObject {

	private static final long serialVersionUID = 1L;

	private int bgcolor;		// バックグラウンドカラー

	/**
	 * コントラクタ
	 *
	 * @param source	イベントを生成したオブジェクト
	 * @param bgcolor	バックグラウンドカラー
	 */
	public BgColor(Object source, int bgcolor) {
		super(source);
		this.bgcolor = bgcolor;
	}

	/**
	 * バックグラウンドカラー取得
	 *
	 * @return	バックグラウンドカラー
	 */
	public int getBgColor() {
		return bgcolor;
	}
}
