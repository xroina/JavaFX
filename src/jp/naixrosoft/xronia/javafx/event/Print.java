package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

/**
 * プリントイベントクラス
 *
 * @author xronia
 *
 */
public class Print extends EventObject {

	private static final long serialVersionUID = 1L;

	private String str;		// 出力文字列

	/**
	 * コントラクタ
	 *
	 * @param source	イベントを生成したオブジェクト
	 * @param str		出力文字列
	 */
	public Print(Object source, String str) {
		super(source);
		this.str = str;
	}

	/**
	 * 出力文字列取得
	 *
	 * @return		出力文字列
	 */
	public String getString() {
		return str;
	}
}
