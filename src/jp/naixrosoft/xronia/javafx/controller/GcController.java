package jp.naixrosoft.xronia.javafx.controller;

import java.util.EventObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import jp.naixrosoft.xronia.javafx.event.ClsEvent;
import jp.naixrosoft.xronia.javafx.event.LocateEvent;
import jp.naixrosoft.xronia.javafx.event.PrintEvent;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;

public class GcController implements BaseDefine {

	private static final int COLS = (int)WIDTH / 8;
	private static final int ROWS = (int)HEIGHT / 16;

	private final BlockingQueue<EventObject> queue;

	private final GraphicsContext gc;
	private Timeline timer = null;

	private char[][] charactor = new char[COLS][ROWS];
	private int x = 0;
	private int y = 0;

	public GcController(Canvas cvs, BlockingQueue<EventObject> queue) {

		this.queue = queue;

		// グラフィックコンテキストの取得
		GraphicsContext gc = cvs.getGraphicsContext2D();
		gc.setFont(Font.font("MONOSPACE", FontWeight.NORMAL, FONT_SIZE));
		gc.setTextBaseline(VPos.TOP);
		this.gc = gc;

		timer = new Timeline(
				new KeyFrame(Duration.millis(TIME_OUT),
						new TimeEvent()));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();

		this.cls();
	}

	private class TimeEvent implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			EventObject evt = null;
			try {
				evt = queue.poll(0, TimeUnit.MICROSECONDS);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			if(evt == null) return;
			if(evt.getClass() == ClsEvent.class) cls();
			else if(evt.getClass() == PrintEvent.class) print((PrintEvent)evt);
			else if(evt.getClass() == LocateEvent.class) locate((LocateEvent)evt);
		}
	}

	public void stop() {
		if(timer != null) timer.stop();
		System.out.println("GCC Stopped");
		timer = null;
	}

	/**
	 * クリアスクリーン
	 */
	private void cls() {
		gc.setFill(BG);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		for(int i = 0; i < COLS; i++) {
			for(int j = 0; j < ROWS; j++) {
				charactor[i][j] = 0;
			}
		}
		x = 0;
		y = 0;
	}

	/**
	 * プリント
	 *
	 * @param str 出力対象文字列
	 */
	private void print(PrintEvent evt) {
		String str = evt.getString();

		for (char c : str.toCharArray()) {
			switch(c) {
			case '\n':
				x = 0;
				y++;
				break;
			default:
				printCharactor(c, x, y);
				if(isHankaku(c)) x++;
				else x += 2;
				break;
			}
			if(x >= COLS) {
				x = 0;
				y++;
			}
			if(y >= ROWS) {
				y = ROWS - 1;
				scrollNextLine();
			}
		}
	}

	/**
	 * 座標設定
	 *
	 * @param x 座標
	 * @param y 座標
	 */
	private void locate(LocateEvent evt) {
		this.x = evt.getX();
		this.y = evt.getY();
	}

	/**
	 * キャラクタ取得
	 *
	 * @param x	座標
	 * @param y	座標
	 * @return	取得文字
	 */
	public synchronized String getCharacter(int x, int y) {
		if(charactor[x][y] == 0) return "";
		return String.valueOf(charactor[x][y]);
	}
	/**
	 * 上スクロール
	 */
	private void scrollNextLine() {
		for(int j = 1; j < ROWS; j++) {
			for(int i = 0; i < COLS; i++) {
				char c = charactor[i][j];
				printCharactor(c, i, j - 1);
				charactor[i][j - 1] = c;
				if(!isHankaku(c)) i++;
			}
		}
		for(int k = 0; k < COLS; k++) {
			char c = 0;
			printCharactor(c, k, ROWS -1);
			charactor[k][ROWS -1] = c;
		}
	}

	/**
	 * 文字を指定の座標に出力する
	 *
	 * @param c		出力する文字
	 * @param x		X座標
	 * @param y		Y座標
	 */
	private void printCharactor(char c, int x, int y) {
		charactor[x][y] = c;

		gc.setFill(BG);
		gc.fillRect(x * 8, y * 16, isHankaku(c) ? 8 : 16, 16);

		gc.setFill(FG);
		gc.fillText(String.valueOf(c), x * 8, y * 16);

		if(!isHankaku(c) && x + 1 < COLS) {
			charactor[x + 1][y] = c;
		}
	}

	/**
	 * キャラクタ文字が半角文字であるかを判断する
	 *
	 * @param c		判断対象文字
	 * @return		true:半角	false:全角
	 */
	private boolean isHankaku(char c) {
		return  (c <= '\u007e') ||					// 英数字
				(c == '\u00a5') ||					// \記号
				(c == '\u203e') ||					// ~記号
				(c >= '\uff61' && c <= '\uff9f');	// 半角カナ

	}
}
