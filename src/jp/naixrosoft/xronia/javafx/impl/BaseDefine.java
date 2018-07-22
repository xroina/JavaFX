package jp.naixrosoft.xronia.javafx.impl;

import javafx.scene.paint.Color;

public interface BaseDefine {

	static final double WIDTH = 640;
	static final double HEIGHT = 480;
	static final double WINDOW_WIDTH = WIDTH;
	static final double WINDOW_HEIGHT = WIDTH;

	static final double BOTTOM_HEIGHT = (WINDOW_HEIGHT - HEIGHT);

	static final double BOX_RIGHT = 120;

	static final int FONT_SIZE = 14;
	static final int BUTTON_MAX = 3;

	static final int TIME_OUT = 15;

	static final Color BG = Color.WHITE;
	static final Color FG = Color.BLACK;

	static final Color RELEAS = Color.RED;
	static final Color PUSH = Color.BLUE;
}
