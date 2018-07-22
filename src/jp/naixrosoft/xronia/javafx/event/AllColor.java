package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

/**
 * カラーイベントクラス
 *
 * @author xronia
 *
 */
public class AllColor extends EventObject {

	private static final long serialVersionUID = 1L;

	private int fgcolor;		// フォアグラウンドカラー
	private int bgcolor;		// バックグラウンドカラー

	/**
	 * コントラクタ
	 *
	 * @param source	イベントを生成したオブジェクト
	 * @param fgcolor	フォアグラウンドカラー
	 * @param bgcolor	バックグラウンドカラー
	 */
	public AllColor(Object source, int fgcolor, int bgcolor) {
		super(source);
		this.fgcolor = fgcolor;
		this.bgcolor = bgcolor;
	}

	/**
	 * フォアグラウンドカラー取得
	 *
	 * @return	フォアグラウンドカラー
	 */
	public int getFgColor() {
		return fgcolor;
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
