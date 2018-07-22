package jp.naixrosoft.xronia.javafx.stick;

/**
 * コントローラー入力の状態クラス
 *
 * @author xronia
 *
 */
public class State {
	public static final int BUTTON_MAX = 16;

	private double x = 0.0;								// スティックのX方向
	private double y = 0.0;								// スティックのY方向
	private boolean[] button = new boolean[BUTTON_MAX];	// ボタンの状態

	/**
	 * コントラクタ
	 */
	public State() {}

	/**
	 * コントラクタ
	 *
	 * @param x			スティックのX方向
	 * @param y			スティックのY方向
	 * @param button	ボタンの状態
	 */
	public State(double x, double y, boolean[] button) {
		this.setX(x);
		this.setY(y);
		for(int i = 0; i < this.button.length; i++)
			this.setButton(i, button[i]);
	}

	/**
	 * スティックのX方向取得
	 *
	 * @return		スティックのX方向
	 */
	public synchronized double getX() {
		return x;
	}

	/**
	 * スティックのX方向設定
	 *
	 * @param x		スティックのX方向
	 */
	public synchronized void setX(double x) {
		this.x = x;
	}

	/**
	 * スティックのY方向取得
	 *
	 * @return		スティックのY方向
	 */
	public synchronized double getY() {
		return y;
	}

	/**
	 * スティックのY方向設定
	 *
	 * @param y		スティックのY方向
	 */
	public synchronized void setY(double y) {
		this.y = y;
	}

	/**
	 * ボタンの状態取得
	 *
	 * @param idx	ボタン番号
	 * @return		ボタンの状態	True:押されてる		False:押されていない
	 */
	public synchronized boolean getButton(int idx) {
		return button[idx];
	}

	/**
	 * ボタンの状態設定
	 *
	 * @param idx		ボタン番号
	 * @param button	ボタンの状態	True:押されてる		False:押されていない
	 */
	public synchronized void setButton(int idx, boolean button) {
		this.button[idx] = button;
	}
}
