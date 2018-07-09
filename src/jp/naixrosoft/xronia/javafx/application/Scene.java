package jp.naixrosoft.xronia.javafx.application;

import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.State;

public class Scene extends javafx.scene.Scene implements BaseDefine {

	private volatile State ss = null;
	private volatile Stick stick = null;
	private volatile Button button[] = null;

	public Scene(Parent root, State ss, Stick stick, Button[] button) {
		super(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		this.ss = ss;
		this.stick =stick;
		this.button = button;

		// シーンでのキー押下
		this.setOnKeyPressed(e->{
			keybordOnKey(e, true);
			keybordOnTenKey(e);
		});

		this.setOnKeyReleased(e->{
			keybordOnKey(e, false);
			keybordOffTenKey(e);
		});

	}

	/**
	 * カーソルキー押下イベント
	 *
	 * @param e
	 */
	private void keybordOnTenKey(KeyEvent e) {
		switch(e.getCode()) {
		case LEFT:
			ss.setX(-1.0);
			break;
		case RIGHT:
			ss.setX(1.0);
			break;
		case UP:
			ss.setY(-1.0);
			break;
		case DOWN:
			ss.setY(1.0);
			break;
		default:
			break;
		}

		stick.setCenter(ss);

		e.consume();
	}

	/**
	 * カーソルキー離すイベント
	 *
	 * @param e
	 */
	private void keybordOffTenKey(KeyEvent e) {
		switch(e.getCode()) {
		case LEFT:
		case RIGHT:
			ss.setX(0.0);
			break;
		case UP:
		case DOWN:
			ss.setY(0.0);
			break;
		default:
			break;
		}

		stick.setCenter(ss);

		e.consume();
	}

	/**
	 * キーボードイベント
	 *
	 * @param e	キーボードイベント
	 * @param c	ボタンの色
	 * @param b
	 */
	private void keybordOnKey(KeyEvent e, boolean b) {
		switch(e.getCode()) {
		case Z:
			button[0].setFill(b ? PUSH : RELEAS);
			ss.setButton(0, b);
			break;
		case X:
			button[1].setFill(b ? PUSH : RELEAS);
			ss.setButton(1, b);
			break;
		case C:
			button[2].setFill(b ? PUSH : RELEAS);
			ss.setButton(2, b);
			break;
		default:
			break;
		}

		e.consume();
	}
}
