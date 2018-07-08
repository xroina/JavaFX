package jp.naixrosoft.xronia.javafx.stick;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Optional;

import org.lwjgl.glfw.GLFW;

/**
 * ゲームパッドコントローラー入力。
 */
public final class StickController {

	public static final int BUTTON_MAX = GLFW.GLFW_JOYSTICK_LAST + 1;
	private double x = 0.0;
	private double y = 0.0;
	private boolean[] button = new boolean[BUTTON_MAX];

	private int stick = 0;

	public StickController() {
		// GLFWを初期化します。これを行う前に、ほとんどのGLFW関数は機能しません。
		if(!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		GLFW.glfwPollEvents();

		for (stick = 0; stick < BUTTON_MAX; stick++) {
			if(!GLFW.glfwJoystickPresent(stick)) continue;
			System.out.println("JoyStick(" + stick + ")Name:" +
					GLFW.glfwGetJoystickName(stick) + " " +
					GLFW.glfwGetGamepadName(stick));
			break;
		}
		if(stick >= BUTTON_MAX) stick = -1;
    }

	public boolean available() {
		return stick >= 0;
	}

	public Optional<State> getState() {
		double x = 0.0;
		double y = 0.0;
		boolean[] button = new boolean[BUTTON_MAX];

		int count1 = 0;
		FloatBuffer stickBuffer = GLFW.glfwGetJoystickAxes(0);
		while (stickBuffer.hasRemaining()) {
			float axes = stickBuffer.get();
			if(count1 == 0) x = axes;
			if(count1 == 1) y = axes;

			count1++;
		}

		if(Math.abs(x) < 0.01) x = 0;
		if(Math.abs(y) < 0.01) y = 0;

		int count2 = 0;
		ByteBuffer triggerBuffer = GLFW.glfwGetJoystickButtons(0);
		while (triggerBuffer.hasRemaining()) {
			byte trigger = triggerBuffer.get();
			button[count2] = trigger != 0;
			count2++;
		}

		boolean flag = false;
		for(int i = 0; i < button.length; i++) {
			if(this.button[i] != button[i]) {
				flag = true;
				break;
			}
		}

		if (x != this.x || y != this.y || flag) {
			this.x = x;
			this.y = y;
			for(int i = 0; i < this.button.length; i++) this.button[i] = button[i];

			return Optional.of(new State(x, y, button));
		}

		return Optional.empty();
	}

}
