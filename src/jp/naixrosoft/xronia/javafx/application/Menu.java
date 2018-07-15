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

public class Menu extends ContextMenu implements BaseDefine {
	private Execute exe = null;
	private State ss = null;


	public Menu(Stage stage, Canvas cvs, State ss) {
		this.ss = ss;

		MenuItem open = new MenuItem("Open");
		open.setOnAction(e->openFileDialog(stage, cvs));

		MenuItem stop = new MenuItem("Stop");
		stop.setOnAction(e->stop());

		MenuItem help = new MenuItem("Help");
//		helpMenu.setOnAction(e->scrollNextLine());

		MenuItem close = new MenuItem("Close");
		close.setOnAction(e->stage.close());

		this.getItems().addAll(open, stop, help, close);
	}

	/**
	 * ファイル選択ダイアログ
	 *
	 * @param stage
	 */
	private void openFileDialog(Stage stage, Canvas cvs) {

		if(exe != null) {
			System.err.println("実行中");
			return;
		}

		FileChooser fc = new FileChooser();
		fc.setTitle("Select File");

		File importFile = fc.showOpenDialog(stage);

		if (importFile == null) return;

		String file = importFile.getPath();

		List<String> arguments = new ArrayList<>();
		ByteCode code = new ByteCode();
		try {
			Script script = new Script(code);
			script.compile(file, arguments);
		} catch (IOException | ScriptException e) {
			e.printStackTrace();
			return;
		}

		BlockingQueue<EventObject> queue = new LinkedBlockingQueue<>(1);
		GcController gcc = new GcController(cvs, queue);
		exe = new Execute(ss, queue, gcc, code);

//		ForkJoinPool.commonPool().execute(()->exe.run());

		Thread thread = new Thread(exe);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	public void stop() {
		if(exe != null) exe.stop();
		exe = null;
	}

}
