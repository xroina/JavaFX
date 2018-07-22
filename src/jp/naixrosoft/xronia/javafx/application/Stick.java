package jp.naixrosoft.xronia.javafx.application;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.State;

/**
 * スティック描画オブジェクトクラス
 *
 * @author xronia
 *
 */
public class Stick extends Rectangle implements BaseDefine {

	// ベースの四角形の座標とサイズ
	private static final double BASE_WIDTH = BOTTOM_HEIGHT - 40;
	private static final double BASE_HEIGHT = BASE_WIDTH;
	private static final double BASE_LEFT = 20;
	private static final double BASE_TOP = HEIGHT + 20;

	// スティック本体の座標とサイズ
	private static final double CIRCLE_LEFT = BOTTOM_HEIGHT / 2;
	private static final double CIRCLE_TOP = HEIGHT + BOTTOM_HEIGHT / 2;
	private static final double CIRCLE_SIZE = 30;

	private State ss = null;					// スティックの状態
	private Circle stick = null;				// ステック円
	private Button[] button = new Button[2];	// ボタン

	/**
	 * コントラクタ
	 *
	 * @param root		ルートペイン
	 * @param ss		スティック状態
	 * @param button	ボタン描画オブジェクト
	 */
	public Stick(Pane root, State ss, Button[] button) {
		// 自身の座標とサイズ設定
		super(BASE_LEFT, BASE_TOP, BASE_WIDTH, BASE_HEIGHT);

		this.setFill(Color.BLACK);		// 自身の色
		root.getChildren().add(this);	// 自身をルートペインに追加

		// スティックの座標とサイズ設定
		stick = new Circle(CIRCLE_LEFT, CIRCLE_TOP, CIRCLE_SIZE);
		stick.setFill(RELEAS);			// スティックの色
		root.getChildren().add(stick);	// スティックをルートペインに追加

		this.ss = ss;
		this.button = button;


		// スティッククリック
		this.setOnMousePressed(e->{
			this.setMouseTransparent(true);
			stickClick(e, true);
			stickDrag(e);
		});
		this.setOnMouseReleased(e->{
			this.setMouseTransparent(false);
			stickClick(e, false);
			stickReleased(e);
		});

		// スティックドラック開始
		this.setOnDragDetected(e->{
			if(e.getButton().equals(MouseButton.PRIMARY)) {
				this.startFullDrag();
			}
			e.consume();
		});

		// ドラック中
		this.setOnMouseDragOver(e->stickDrag(e));

		// ドラック終了
		this.setOnMouseDragReleased(e->stickReleased(e));

		// スティッククリック
		stick.setOnMousePressed(e->{
			stickClick(e, true);
			stickDrag(e);
		});
		stick.setOnMouseReleased(e->{
			stickClick(e, false);
			stickReleased(e);
		});

		// スティックドラック開始
		stick.setOnDragDetected(e->{
			if(e.getButton().equals(MouseButton.PRIMARY)) {
				stick.startFullDrag();
			}
			e.consume();
		});

		// ドラック中
		stick.setOnMouseDragOver(e->stickDrag(e));

		// ドラック終了
		stick.setOnMouseDragReleased(e->stickReleased(e));

	}

	/**
	 * スティックの位置設定
	 *
	 * @param ss	スティックの状態
	 */
	public void setCenter(State ss) {
		stick.setCenterX(ss.getX() * BASE_WIDTH / 2 + CIRCLE_LEFT);
		stick.setCenterY(ss.getY() * BASE_HEIGHT / 2 + CIRCLE_TOP);
	}

	/**
	 * マウスクリックイベント
	 *
	 * @param e	マウスイベント
	 * @param c	スティックの色
	 */
	private void stickClick(MouseEvent e, boolean b) {
		switch(e.getButton()) {
		case PRIMARY:
			stick.setFill(b ? PUSH : RELEAS);
			break;
		case MIDDLE:
			button[0].setFill(b ? PUSH : RELEAS);
			ss.setButton(1, b);
			break;
		case SECONDARY:
			button[1].setFill(b ? PUSH : RELEAS);
			ss.setButton(0, b);
			break;
		default:
			break;
		}

		e.consume();
	}

	/**
	 * マウスドラックイベント
	 *
	 * @param e	マウスイベント
	 */
	private void stickDrag(MouseEvent e) {
		if(e.getButton().equals(MouseButton.PRIMARY)) {
			double x = e.getSceneX();
			double y = e.getSceneY();

			if(BASE_LEFT > x) x = BASE_LEFT;
			if(BASE_LEFT + BASE_WIDTH < x) x = BASE_LEFT + BASE_WIDTH;
			if(BASE_TOP > y) y = BASE_TOP;
			if(BASE_TOP + BASE_HEIGHT < y) y = BASE_TOP + BASE_HEIGHT;

			ss.setX((x - CIRCLE_LEFT) / BASE_WIDTH * 2);
			ss.setY((y - CIRCLE_TOP) / BASE_HEIGHT * 2);

			this.setCenter(ss);
		}

		e.consume();
	}

	/**
	 * マウスドラック終了イベント
	 *
	 * @param e	マウスイベント
	 */
	private void stickReleased(MouseEvent e) {
		if(e.getButton().equals(MouseButton.PRIMARY)) {
			ss.setX(0.0);
			ss.setY(0.0);

			this.setCenter(ss);
		}

		e.consume();
	}
}
