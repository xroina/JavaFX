package jp.naixrosoft.xronia.javafx.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import jp.naixrosoft.xronia.javafx.event.Cls;
import jp.naixrosoft.xronia.javafx.event.Locate;
import jp.naixrosoft.xronia.javafx.event.Print;
import jp.naixrosoft.xronia.javafx.event.ScrollLeft;
import jp.naixrosoft.xronia.javafx.event.ScrollNext;
import jp.naixrosoft.xronia.javafx.event.ScrollPrev;
import jp.naixrosoft.xronia.javafx.event.ScrollRight;
import jp.naixrosoft.xronia.javafx.impl.BaseDefine;

public class GcController implements BaseDefine {

	private static final int COLS = (int)WIDTH / 8;
	private static final int ROWS = (int)HEIGHT / 16;

	private final BlockingQueue<EventObject> queue;

	private final GraphicsContext gc;

	private char[][] charactor = new char[COLS][ROWS];
	private int x = 0;
	private int y = 0;

	/**
	 * グラフィックコンテキストコントローラーのコンストラクタ
	 *
	 * @param cvs	描画キャンバス
	 * @param queue	イベントキュー
	 */
	public GcController(Canvas cvs, BlockingQueue<EventObject> queue) {

		this.queue = queue;

		// グラフィックコンテキストの取得
		GraphicsContext gc = cvs.getGraphicsContext2D();
		gc.setFont(Font.font("MONOSPACE", FontWeight.NORMAL, FONT_SIZE));
		gc.setTextBaseline(VPos.TOP);
		this.gc = gc;

		this.cls();

		Timeline timer = new Timeline(
				new KeyFrame(Duration.millis(TIME_OUT), new TimeEvent()));
		timer.setCycleCount(Timeline.INDEFINITE);
//		timer.setAutoReverse(true);
		timer.play();
	}

	/**
	 * タイムラインの実行クラス
	 *
	 * @author xronia
	 *
	 */
	private class TimeEvent implements EventHandler<ActionEvent> {

		/**
		 * タイムラインイベントハンドラ
		 *
		 * @param event	イベントオブジェクト(使わないけど)
		 */
		@Override
		public void handle(ActionEvent event) {

			final LocalDateTime baseTime = LocalDateTime.now();
//			System.out.println("start:" + baseTime);

			EventObject evt = null;

			do {
				try {
					evt = queue.poll(0, TimeUnit.NANOSECONDS);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
//				System.out.println("poll:" + ChronoUnit.MICROS.between(baseTime, LocalDateTime.now()) + " evt:"+evt);
				if(evt == null) break;

				if(evt.getClass() == Cls.class) cls();
				else if(evt.getClass() == Print.class) print((Print)evt);
				else if(evt.getClass() == Locate.class) locate((Locate)evt);
				else if(evt.getClass() == ScrollNext.class)
					scrollNextLine((ScrollNext)evt);
				else if(evt.getClass() == ScrollPrev.class)
					scrollPrevLine((ScrollPrev)evt);
				else if(evt.getClass() == ScrollLeft.class)
					scrollLeftColumn((ScrollLeft)evt);
				else if(evt.getClass() == ScrollRight.class)
					scrollRightColumn((ScrollRight)evt);

			} while(ChronoUnit.MILLIS.between(baseTime, LocalDateTime.now())
					< TIME_OUT);

		}
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
	 * @param evt	プリントイベント
	 */
	private void print(Print evt) {
		String str = evt.getString();

		for (char c : str.toCharArray()) {
			switch(c) {
			case '\n':
				x = 0;
				y++;
				break;

			case '\u001c':
				x++;
				break;

			case '\u001d':
				x--;
				break;

			case '\u001e':
				y--;
				break;

			case '\u001f':
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
			} else if(x < 0) {
				x = 0;
			}
			if(y >= ROWS) {
				y = ROWS - 1;
				scrollNextLine(0, y);
			} else if(y < 0) {
				y = 0;
			}
		}
	}

	/**
	 * 座標設定
	 *
	 * @param evt	ロケールイベント(座標)
	 */
	private void locate(Locate evt) {
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
	 *
	 * @param evt	上スクロールイベント(開始位置,終了位置)
	 */
	private void scrollNextLine(ScrollNext evt) {
		scrollNextLine(evt.getY1(), evt.getY2());
	}

	/**
	 * 上スクロール
	 *
	 * @param y1	スクロール開始位置
	 * @param y2	スクロール終了位置
	 */
	private void scrollNextLine(int y1, int y2) {
		for(int j = y1 + 1; j <= y2; j++) {
			for(int i = 0; i < COLS; i++) {
				char c = charactor[i][j];
				charactor[i][j - 1] = c;
				printCharactor(c, i, j - 1);
				if(!isHankaku(c)) {
					i++;
					charactor[i][j - 1] = c;
				}
			}
		}
		for(int k = 0; k < COLS; k++) {
			char c = 0;
			printCharactor(c, k, y2);
			charactor[k][y2] = c;
		}
	}

	/**
	 * 下スクロール
	 *
	 * @param evt	下スクロールイベント(開始位置,終了位置)
	 */
	private void scrollPrevLine(ScrollPrev evt) {
		int y1 = evt.getY1();
		int y2 = evt.getY2();
		for(int j = y2 - 1; j >= y1; j--) {
			for(int i = 0; i < COLS; i++) {
				char c = charactor[i][j];
				charactor[i][j + 1] = c;
				printCharactor(c, i, j + 1);
				if(!isHankaku(c)) {
					i++;
					charactor[i][j + 1] = c;
				}
			}
		}
		for(int k = 0; k < COLS; k++) {
			char c = 0;
			printCharactor(c, k, y1);
			charactor[k][y1] = c;
		}
	}

	/**
	 * 左スクロール
	 *
	 * @param evt	左スクロールイベント(開始位置,終了位置)
	 */
	private void scrollLeftColumn(ScrollLeft evt) {
		int x1 = evt.getX1();
		int x2 = evt.getX2();
		for(int j = 0; j > ROWS; j--) {
			for(int i = x1 + 1; i <= x2; i++) {
				char c = charactor[i][j];
				charactor[i - 1][j] = c;
				printCharactor(c, i - 1, j);
				if(!isHankaku(c)) {
					i++;
					if(i <= COLS) {
						charactor[i - 1][j] = c;
					}
				}
			}
		}
		for(int k = 0; k < ROWS; k++) {
			char c = 0;
			printCharactor(c, x2, k);
			charactor[x2][k] = c;
		}
	}

	/**
	 * 右スクロール
	 *
	 * @param evt	左スクロールイベント(開始位置,終了位置)
	 */
	private void scrollRightColumn(ScrollRight evt) {
		int x1 = evt.getX1();
		int x2 = evt.getX2();
		for(int j = 0; j > ROWS; j--) {
			for(int i = x2 - 1; i >= x1; i--) {
				char c = charactor[i][j];
				charactor[i + 1][j] = c;
				if(!isHankaku(c)) {
					i--;
				}
				if(i > 0) {
					printCharactor(c, i + 1, j);
				}
			}
		}
		for(int k = 0; k < ROWS; k++) {
			char c = 0;
			printCharactor(c, x2, k);
			charactor[x1][k] = c;
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
