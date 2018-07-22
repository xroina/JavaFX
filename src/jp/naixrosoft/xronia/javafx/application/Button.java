package jp.naixrosoft.xronia.javafx.application;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.State;

/**
 * ボタン描画オブジェクトクラス
 *
 * @author xronia
 *
 */
public class Button extends Rectangle implements BaseDefine {

	// ボタンサイズなど定義
	private static final double BOX_WIDTH = 100;
	private static final double BOX_HEIGHT = 60;
	private static final double BOX_TOP =
			HEIGHT + (BOTTOM_HEIGHT - BOX_HEIGHT) / 2;

	/**
	 * コントラクタ
	 *
	 * @param root		ルートペイン
	 * @param ss		スティック状態
	 * @param idx		ボタン番号
	 * @param x			ボタン配置(X軸のみ)
	 */
	public Button(Pane root, State ss, int idx, double x) {
		// 自身の座標とサイズ指定
		super(x, BOX_TOP, BOX_WIDTH, BOX_HEIGHT);

		this.setFill(RELEAS);			// 自身の色設定
		root.getChildren().add(this);	// 自身をルートペインに追加

		// ボックスクリック
		this.setOnMousePressed(e->{
			this.setFill(PUSH);
			ss.setButton(idx, true);
			e.consume();
		});

		this.setOnMouseReleased(e->{
			this.setFill(RELEAS);
			ss.setButton(idx, false);
			e.consume();
		});

	}
}
