package jp.naixrosoft.xronia.javafx;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.naixrosoft.xronia.javafx.application.Button;
import jp.naixrosoft.xronia.javafx.application.Canvas;
import jp.naixrosoft.xronia.javafx.application.Scene;
import jp.naixrosoft.xronia.javafx.application.Stick;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.stick.Controller;
import jp.naixrosoft.xronia.javafx.stick.State;

public class Main extends Application implements BaseDefine {

	public static void main(String[] args) {
        launch(args);
	}

	/**
	 * 描画開始
	 *
	 */
	@Override
	public void start(Stage stage) {

		State ss = new State();

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
		Button[] button = new Button[BUTTON_MAX];
		for(int i = 0; i < BUTTON_MAX; i++) {
			double x = WINDOW_WIDTH - BOX_RIGHT * (BUTTON_MAX - i);
			button[i] = new Button(root, ss, i, x);
		}

		// スティック
		Stick stick = new Stick(root, ss, button);

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
		Controller stickInput = new Controller();

		Consumer<State> changeState = e->{
//			System.out.println("stick:"+Thread.currentThread().getName());
			stick.setCenter(e);

			ss.x = e.x;
			ss.y = e.y;
			for(int i = 0; i < ss.button.length; i++) {
				if(i < BUTTON_MAX)
					button[i].setFill(e.button[i] ? PUSH : RELEAS);
				ss.button[i] = e.button[i];
			}
		};

		if (stickInput.available()) {
			ForkJoinPool.commonPool().execute(()->{
				while(true) {
					stickInput.getState().ifPresent(changeState);
					try {
						Thread.sleep(TIME_OUT);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}

		// ウインドウ表示
		stage.show();
    }

}