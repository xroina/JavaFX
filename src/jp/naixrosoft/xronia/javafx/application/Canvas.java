package jp.naixrosoft.xronia.javafx.application;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.State;

/**
 * キャンバス描画オブジェクトクラス
 *
 * @author xronia
 *
 */
public class Canvas extends javafx.scene.canvas.Canvas implements BaseDefine {

	private Menu menu = null;	// メニュー構成オブジェクト

	/**
	 * コントラクタ
	 *
	 * @param root		ルートペイン
	 * @param stage		メニューを開くベースとなるステージ
	 * @param ss		スティック状態
	 */
	public Canvas(Pane root, Stage stage, State ss) {
		// 自身のサイズ指定
		super(WIDTH, HEIGHT);

		root.getChildren().add(this);	// 自信をルートペインに追加

		// メニュー
		menu = new Menu(stage, this, ss);

		// キャンバスでクリックイベント->メニュー表示
		this.setOnMousePressed(e->showMenu(root, e));
	}

	/**
	 * スクリプト停止処理
	 */
	public void stop() {
		menu.stop();
	}

	/**
	 * メニュー表示
	 *
	 * @param root	ルートペイン
	 * @param e		マウスイベント
	 */
	private void showMenu(Pane root, MouseEvent e) {
		if(e.isSecondaryButtonDown()) {
			menu.show(root, e.getScreenX(), e.getScreenY());
		} else {
			menu.hide();
		}
		e.consume();
	}

}
