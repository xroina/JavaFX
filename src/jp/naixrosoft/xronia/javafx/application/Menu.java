package jp.naixrosoft.xronia.javafx.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jp.naixrosoft.xronia.javafx.controller.GcController;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;
import jp.naixrosoft.xronia.javafx.script.Execute;
import jp.naixrosoft.xronia.javafx.stick.State;
import jp.naixrosoft.xronia.script.Script;
import jp.naixrosoft.xronia.script.bytecode.ByteCode;
import jp.naixrosoft.xronia.script.exception.ScriptException;

/**
 * 右クリックメニュー構成オブジェクトクラス
 *
 * @author xronia
 *
 */
public class Menu extends ContextMenu implements BaseDefine {

	BlockingQueue<EventObject> queue = null;	// イベントキュー
	GcController gcc = null;					// グラフィックコンテキストコントローラー
	private Execute exe = null;					// スクリプト実行オブジェクト
	private State ss = null;					// スティック状態

	/**
	 * コントラクタ
	 *
	 * @param stage		メニューを開くベースとなるステージ
	 * @param cvs		キャンバス描画オブジェクト
	 * @param ss		スティック状態
	 */
	public Menu(Stage stage, Canvas cvs, State ss) {
		this.ss = ss;

		MenuItem open = new MenuItem("Open");
		open.setOnAction(e->openFileDialog(stage, cvs));

		MenuItem start = new MenuItem("Start");
		start.setOnAction(e->execute());

		MenuItem stop = new MenuItem("Stop");
		stop.setOnAction(e->stop());

		MenuItem help = new MenuItem("Help");
//		helpMenu.setOnAction(e->scrollNextLine());

		MenuItem close = new MenuItem("Close");
		close.setOnAction(e->stage.close());

		this.getItems().addAll(open, start, stop, help, close);
	}

	/**
	 * ファイル選択ダイアログ
	 *
	 * @param stage
	 */
	private void openFileDialog(Stage stage, Canvas cvs) {

		// 実行中なら開かない
		if(exe != null && exe.runnable()) {
			System.err.println("実行中");
			return;
		}

		// ファイル選択ダイアログを開く
		FileChooser fc = new FileChooser();
		fc.setTitle("Select File");

		File importFile = fc.showOpenDialog(stage);

		// ファイルが選択されてなければそのまま閉じる
		if (importFile == null) return;

		String file = importFile.getPath();

		// スクリプトの引数は空で定義
		List<String> arguments = new ArrayList<>();
		ByteCode code = new ByteCode();

		// スクリプトのコンパイル
		try {
			Script script = new Script(code);
			script.compile(file, arguments);
		} catch (IOException | ScriptException e) {
			e.printStackTrace();
			return;
		}

		queue = new LinkedBlockingQueue<>(1);		// データ受け渡し用のキュー
		gcc = new GcController(cvs, queue);			// グラフィックコンテキストコントローラー
		exe = new Execute(ss, queue, gcc, code);	// スクリプト実行オブジェクト
		execute();
	}

	/**
	 * スクリプト実行
	 */
	private void execute() {

		// 実行中なら開かない
		if(exe != null && exe.runnable()) {
			System.err.println("実行中");
			return;
		}
		if(exe == null) {
			System.err.println("スクリプトの実体がない");
			return;
		}

//		ForkJoinPool.commonPool().execute(()->exe.run());	// ForkPoolでの実装
		gcc.cls();
		gcc.start();

		Thread thread = new Thread(exe);			// スレッドでの実装
		thread.setPriority(Thread.MIN_PRIORITY);	// プライオリティは低い
		thread.start();								// スレッド開始
	}

	/**
	 * スクリプト停止処理
	 */
	public void stop() {
		if(exe == null || !exe.runnable()) return;

		exe.stop();		// スクリプトの停止

		gcc.stop();		// TimeLineの停止
	}

}
