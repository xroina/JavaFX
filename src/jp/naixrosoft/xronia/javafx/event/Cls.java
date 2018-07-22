package jp.naixrosoft.xronia.javafx.event;

import java.util.EventObject;

/**
 * クリアスクリーンイベントクラス
 *
 * @author xronia
 *
 */
public class Cls extends EventObject {

	private static final long serialVersionUID = 1L;

	/**
	 * コントラクタ
	 *
	 * @param source	イベントを生成したオブジェクト
	 */
	public Cls(Object source) {
		super(source);
	}
}
