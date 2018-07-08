package jp.naixrosoft.xronia.javafx.application;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.State;

public class Canvas extends javafx.scene.canvas.Canvas implements BaseDefine {

	private Menu menu = null;

	public Canvas(Pane root, Stage stage, State ss) {
		super(WIDTH, HEIGHT);

		root.getChildren().add(this);

		// メニュー
		menu = new Menu(stage, this, ss);

		// キャンバスでクリックイベント->メニュー表示
		this.setOnMousePressed(e->showMenu(root, e));
	}

	public void stop() {
		menu.stop();
	}

	private void showMenu(Pane root, MouseEvent e) {
		if(e.isSecondaryButtonDown()) {
			menu.show(root, e.getScreenX(), e.getScreenY());
		} else {
			menu.hide();
		}
		e.consume();
	}

}
