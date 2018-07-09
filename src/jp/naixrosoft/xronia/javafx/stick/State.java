package jp.naixrosoft.xronia.javafx.stick;

/**
 * コントローラー入力の状態。
 */
public class State {
	protected static final int BUTTON_MAX = 16;

	public volatile double x = 0.0;
	public volatile double y = 0.0;
	public volatile boolean[] button = new boolean[BUTTON_MAX];

	public State() {}

	public State(double x, double y, boolean[] button) {
		this.x = x;
		this.y = y;
		for(int i = 0; i < this.button.length; i++)
			this.button[i] = button[i];
	}
}
