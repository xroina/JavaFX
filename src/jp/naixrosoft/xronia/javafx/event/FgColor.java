package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

/**
 * カラーイベントクラス
 *
 * @author xronia
 *
 */
public class FgColor extends EventObject {

	private static final long serialVersionUID = 1L;

	private int fgcolor;		// フォアグラウンドカラー

	/**
	 * コントラクタ
	 *
	 * @param source	イベントを生成したオブジェクト
	 * @param fgcolor	フォアグラウンドカラー
	 */
	public FgColor(Object source, int fgcolor) {
		super(source);
		this.fgcolor = fgcolor;
	}

	/**
	 * フォアグラウンドカラー取得
	 *
	 * @return	フォアグラウンドカラー
	 */
	public int getFgColor() {
		return fgcolor;
	}

}
