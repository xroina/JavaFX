package jp.naixrosoft.xronia.javafx.stick;

/**
 * コントローラー入力の状態。
 */
public class State {
	public static final int BUTTON_MAX = 16;

	private double x = 0.0;
	private double y = 0.0;
	private boolean[] button = new boolean[BUTTON_MAX];

	public State() {}

	public State(double x, double y, boolean[] button) {
		this.setX(x);
		this.setY(y);
		for(int i = 0; i < this.button.length; i++)
			this.setButton(i, button[i]);
	}

	public synchronized double getX() {
		return x;
	}

	public synchronized void setX(double x) {
		this.x = x;
	}

	public synchronized double getY() {
		return y;
	}

	public synchronized void setY(double y) {
		this.y = y;
	}

	public synchronized boolean getButton(int idx) {
		return button[idx];
	}

	public synchronized void setButton(int idx, boolean button) {
		this.button[idx] = button;
	}
}
