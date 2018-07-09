package jp.naixrosoft.xronia.javafx.application;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.State;

public class Button extends Rectangle implements BaseDefine {

	private static final double BOX_WIDTH = 100;
	private static final double BOX_HEIGHT = 60;
	private static final double BOX_TOP =
			HEIGHT + (BOTTOM_HEIGHT - BOX_HEIGHT) / 2;

	public Button(Pane root, State ss, int idx, double x) {
		super(x, BOX_TOP, BOX_WIDTH, BOX_HEIGHT);

		this.setFill(RELEAS);
		root.getChildren().add(this);

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
