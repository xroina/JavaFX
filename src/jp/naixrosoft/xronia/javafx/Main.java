package jp.naixrosoft.xronia.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import jp.naixrosoft.xronia.javafx.application.Button;
import jp.naixrosoft.xronia.javafx.application.Canvas;
import jp.naixrosoft.xronia.javafx.application.Scene;
import jp.naixrosoft.xronia.javafx.application.Stick;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.Controller;
import jp.naixrosoft.xronia.javafx.stick.State;

public class Main extends Application implements BaseDefine {

	private State ss;									// スティックステータス
	private Button[] button = new Button[BUTTON_MAX];	// ボタン
	private Stick stick = null;							// スティック

	/**
	 * メイン処理
	 *
	 * @param args
	 */
	public static void main(String[] args) {
        launch(args);
	}

	/**
	 * 描画開始
	 *
	 *　@param stage
	 */
	@Override
	public void start(Stage stage) {

		ss = new State();

		// ウインドウサイズを固定にする
		stage.setResizable(false);

		// ペインの定義
		Pane root = new Pane();
/*		try {
			pane = (AnchorPane)FXMLLoader.load(getClass()
					.getResource("AnchorPane.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
*/
		// キャンバス
		Canvas canvas = new Canvas(root, stage, ss);

		// ボタン
		for(int i = 0; i < BUTTON_MAX; i++) {
			double x = WINDOW_WIDTH - BOX_RIGHT * (BUTTON_MAX - i);
			button[i] = new Button(root, ss, i, x);
		}

		// スティック
		stick = new Stick(root, ss, button);

		// シーンの生成
		stage.setScene(new Scene(root, ss, stick, button));
		stage.setTitle("XroniaScript");

		// イベント登録
		// ウインドウを閉じる
		stage.showingProperty().addListener((observable, oldValue, newValue)->{
			if (oldValue == true && newValue == false) {
				canvas.stop();
			}
		});

		// ジョイスティック入力
		Controller ctrl = new Controller();
		if(ctrl.available()) {
			Timeline timer = new Timeline(
					new KeyFrame(Duration.millis(TIME_OUT),
							new StickEvent(ctrl)));
			timer.setCycleCount(Timeline.INDEFINITE);
			timer.play();
		}

		// ウインドウ表示
		stage.show();
    }

	/**
	 * ジョイスティックのタイマーイベントクラス
	 *
	 * @author xronia
	 *
	 */
	private class StickEvent implements EventHandler<ActionEvent> {
		Controller ctrl = null;

		/**
		 * コントラクタ
		 *
		 * @param ctrl	コントローラー
		 */
		public StickEvent(Controller ctrl) {
			this.ctrl = ctrl;
		}

		/**
		 * イベントハンドラ
		 *
		 * @param event	イベント(使わないけど)
		 */
		@Override
		public void handle(ActionEvent event) {

			State s = ctrl.getState();		// ジョイスティックの情報取得
			if(s == null) return;			// nullの場合は変化ないため何もしない

			// スティックの位置を設定
			stick.setCenter(s);

			ss.setX(s.getX());
			ss.setY(s.getY());

			// ボタンのステータスを変更
			for(int i = 0; i < State.BUTTON_MAX; i++) {
				if(i < BUTTON_MAX)
					button[i].setFill(s.getButton(i) ? PUSH : RELEAS);
				ss.setButton(i, s.getButton(i));
			}
		}
	}
}