package jp.naixrosoft.xronia.javafx.stick;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Optional;

import org.lwjgl.glfw.GLFW;

/**
 * ゲームパッドコントローラー入力。
 */
public final class Controller extends State {

	private int stick = -1;		// ジョイスティックNo

	/**
	 * コントラクタ
	 *
	 * この中でジョイスティックの取得を行い、無い場合はジョイスティックNoに-1を入れる。
	 * ジョイスティックありの場合はその名前を標準出力に表示して最初に見つかったジョイスティックを
	 * コントロール対象にする。
	 */
	public Controller() {
		// GLFWを初期化。これを行わないとほとんどのGLFW関数は機能しない。
		if(!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		GLFW.glfwPollEvents();

		for (stick = 0; stick <= GLFW.GLFW_JOYSTICK_LAST; stick++) {
			if(!GLFW.glfwJoystickPresent(stick)) continue;
			System.out.println("JoyStick(" + stick + ")Name:" +
					GLFW.glfwGetJoystickName(stick) + " " +
					GLFW.glfwGetGamepadName(stick));
			break;
		}
		if(stick > GLFW.GLFW_JOYSTICK_LAST) stick = -1;
    }

	/**
	 * コントロール可能なジョイスティックの有無を返す
	 *
	 * @return	ジョイスティックあり(true)/なし(false)
	 */
	public boolean available() {
		return stick >= 0;
	}

	/**
	 * ジョイスティックの状態を取得する。
	 *
	 * @return	ジョイスティックの状態、状態に変化がなければnullを返す
	 */
	public State getState() {
		if(!available()) return null;

		double x = 0.0;
		double y = 0.0;
		boolean[] button = new boolean[BUTTON_MAX];

		// スティックの状態を取得する
		int axes_count = 0;
		FloatBuffer stickBuffer = GLFW.glfwGetJoystickAxes(stick);
		while (stickBuffer.hasRemaining()) {
			float axes = stickBuffer.get();
			if(axes_count == 0) x = axes;
			if(axes_count == 1) y = axes;

			axes_count++;
		}
		// スティックのセンターを0にする
		x = Math.floor(Math.abs(x) * 100.0) / 100.0 * Math.signum(x);
		y = Math.floor(Math.abs(y) * 100.0) / 100.0 * Math.signum(y);
//		if(Math.abs(x) < 0.01) x = 0;
//		if(Math.abs(y) < 0.01) y = 0;

		// ボタンの状態を取得する
		int button_count = 0;
		ByteBuffer triggerBuffer = GLFW.glfwGetJoystickButtons(stick);
		while (triggerBuffer.hasRemaining()) {
			byte trigger = triggerBuffer.get();
			button[button_count] = trigger != 0;
			button_count++;
		}

		// ボタンの状態に変化があるかを判定する
		boolean flag = false;
		for(int i = 0; i < BUTTON_MAX; i++) {
			if(this.getButton(i) != button[i]) {
				flag = true;
				break;
			}
		}

		// スティックの状態に変化があるかボタンの状態に変化がある場合はそれを反映して返す。
		if (x != this.getX() || y != this.getY() || flag) {
			this.setX(x);
			this.setY(y);
			for(int i = 0; i < BUTTON_MAX; i++) this.setButton(i, button[i]);
			return this;
		}

		// スティックの状態にもボタンの状態にも変化が無い場合はnullを返す。
		return null;
	}

	public Optional<State> getStateOptional() {
		return Optional.ofNullable(getState());
	}


}
