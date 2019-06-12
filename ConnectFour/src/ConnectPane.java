import java.io.File;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ConnectPane extends Pane {

	Integer[][] slots = new Integer[6][7];
	Circle[][] circles = new Circle[6][7];
	VBox[] columns = new VBox[7];
	boolean p1Turn = true;
	boolean playingVsHuman = false;
	boolean playingVsAI = false;
	int winner = 0;

	ConnectPane() {
		draw();
	}

	private void draw() {
		Rectangle rec = new Rectangle(500, 450);
		rec.setFill(Color.BLUE);
		getChildren().add(rec);

		for (int j = 0; j < 7; j++) {
			columns[j] = new VBox(10);
			columns[j].setTranslateX(70 * j + 10);
			columns[j].setTranslateY(10);
			getChildren().add(columns[j]);
		}

		for (int j = 0; j < 7; j++) {
			for (int i = 0; i < 6; i++) {
				circles[i][j] = new Circle(31, Color.WHITE);
				columns[j].getChildren().add(circles[i][j]);
			}

		}
	}

	public void playHuman() {
		for (int a = 0; a < 6; a++) {
			for (int b = 0; b < 7; b++) {
				slots[a][b] = 0;
			}
		}

		for (int a = 0; a < 7; a++) {
			// make sure row isnt full
			if (slots[0][a] == 0) {
				final int c = a;
				columns[a].setOnMouseClicked(e -> {
					if (slots[0][c] == 0)
						addDisc(circles, c);
				});
			}

		}
	}

	public void gameOver() {
		setDisable(true);
	}

	public void restart() {
		getChildren().clear();
		setDisable(false);
		p1Turn = true;
		this.setDisable(true);
		ConnectFour.ai.setDisable(false);
		ConnectFour.two.setDisable(false);
		for (int a = 0; a < 6; a++) {
			for (int b = 0; b < 7; b++) {
				slots[a][b] = 0;
			}
		}
		draw();
	}

	public void win(int color) {
		// horizontal
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 6; i++) {
				if (slots[i][j] == color && slots[i][j + 1] == color && slots[i][j + 2] == color
						&& slots[i][j + 3] == color) {
					winner = color;

					flash(circles[i][j]);
					flash(circles[i][j + 1]);
					flash(circles[i][j + 2]);
					flash(circles[i][j + 3]);
					gameOver();

				}
			}
		}

		// vertical
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 7; j++) {
				if (slots[i][j] == color && slots[i + 1][j] == color && slots[i + 2][j] == color
						&& slots[i + 3][j] == color) {
					winner = color;
					flash(circles[i][j]);
					flash(circles[i + 1][j]);
					flash(circles[i + 2][j]);
					flash(circles[i + 3][j]);
					gameOver();
				}
			}
		}

		// diagonal /
		for (int i = 3; i < 6; i++) {
			for (int j = 3; j < 7; j++) {
				if (slots[i][j] == color && slots[i - 1][j - 1] == color && slots[i - 2][j - 2] == color
						&& slots[i - 3][j - 3] == color) {
					winner = color;
					flash(circles[i][j]);
					flash(circles[i - 1][j - 1]);
					flash(circles[i - 2][j - 2]);
					flash(circles[i - 3][j - 3]);
					gameOver();
				}
			}
		}

		// diagonal \
		for (int i = 3; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				if (slots[i][j] == color && slots[i - 1][j + 1] == color && slots[i - 2][j + 2] == color
						&& slots[i - 3][j + 3] == color) {
					winner = color;
					flash(circles[i][j]);
					flash(circles[i - 1][j + 1]);
					flash(circles[i - 2][j + 2]);
					flash(circles[i - 3][j + 3]);
					gameOver();
				}
			}
		}

	}

	void AI() {
		this.setDisable(false);
		playingVsAI = true;
		playingVsHuman = false;
	}

	void Human() {
		this.setDisable(false);
		playingVsHuman = true;
		playingVsAI = false;
	}

	public void addDisc(Circle[][] cel, int column) {
		Color color = Color.WHITE;
		if (playingVsHuman) {
			if (p1Turn) {
				color = Color.YELLOW;
			} else {
				color = Color.RED;
			}
		} else {

			if (p1Turn) {
				color = Color.YELLOW;
				p1Turn = false;
			}
		}
		boolean found = false;
		int foundIt = 0;
		for (int a = 5; found == false && a > -1; a--) {
			if (cel[a][column].getFill() == Color.WHITE) {
				foundIt = a;
				found = true;
			}
		}
		cel[foundIt][column].setFill(color);
		if (cel[foundIt][column].getFill() == Color.YELLOW) {
			slots[foundIt][column] = 1;
			win(1);

			p1Turn = false;
		} else {
			if (playingVsHuman) {
				slots[foundIt][column] = 2;
				p1Turn = true;
				win(2);
			}
		}
		if (isDraw()) {
			gameOver();
		}

	}

	void playAI() {
		for (int a = 0; a < 6; a++) {
			for (int b = 0; b < 7; b++) {
				slots[a][b] = 0;
			}
		}

		for (int a = 0; a < 7; a++) {
			// make sure row isnt full
			if (slots[0][a] == 0) {
				final int c = a;

				columns[a].setOnMouseClicked(e -> {
					if (slots[0][c] == 0) {
						addDisc(circles, c);
						Point2D[] moves = getPosMoves();
						move(moves);
						win(2);
						p1Turn = true;

					}
				});
			}

		}

	}

	private boolean isDraw() {

		for (Circle[] circle : circles) {

			for (Circle c : circle) {

				if (c.getFill() == Color.WHITE) {
					return false;
				}

			}

		}

		// game is a draw, flash everything
		for (Circle[] circle : circles) {

			for (Circle c : circle) {
				flash(c);
			}
		}
		return true;

	}

	private Point2D[] getPosMoves() {
		Point2D[] returnable;
		ArrayList<Point2D> list = new ArrayList<>();
		boolean troo = false;
		for (int a = 0; a < 7; a++) {
			troo = false;
			for (int b = 5; b > -1 && !troo; b--) {
				if (slots[b][a] == 0) {
					list.add(new Point2D(b, a));
					troo = true;
				}

			}
		}

		returnable = (Point2D[]) list.toArray(new Point2D[list.size()]);
		return returnable;
	}

	private void move(Point2D[] moves) {
		for (Point2D move : moves) {
			if (make4(1, move) || make4(2, move)) {
				slots[(int) move.getX()][(int) move.getY()] = 2;
				circles[(int) move.getX()][(int) move.getY()].setFill(Color.RED);
				return;
			}
		}
		for (Point2D move : moves) {
			if (make3(1, move) || make3(2, move)) {
				slots[(int) move.getX()][(int) move.getY()] = 2;
				circles[(int) move.getX()][(int) move.getY()].setFill(Color.RED);
				return;
			}
		}
		int rand = (int) (Math.random() * moves.length);
		slots[(int) moves[rand].getX()][(int) moves[rand].getY()] = 2;
		circles[(int) moves[rand].getX()][(int) moves[rand].getY()].setFill(Color.RED);

	}

	private boolean make4(int x, Point2D move) {
		// shallow copy??
		Integer[][] slot = new Integer[6][7];
		for (int i = 0; i < slots.length; i++) {
			for (int j = 0; j < slots[i].length; j++) {
				slot[i][j] = slots[i][j];
			}
		}
		int z = (int) move.getX();
		int y = (int) move.getY();
		slot[z][y] = x;
		// horizontal
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 6; i++) {
				if (slot[i][j] == x && slot[i][j + 1] == x && slot[i][j + 2] == x && slot[i][j + 3] == x) {
					return true;
				}
			}
		}

		// vertical
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 7; j++) {
				if (slot[i][j] == x && slot[i + 1][j] == x && slot[i + 2][j] == x && slot[i + 3][j] == x) {
					return true;
				}
			}
		}

		// diagonal /
		for (int i = 3; i < 6; i++) {
			for (int j = 3; j < 7; j++) {
				if (slot[i][j] == x && slot[i - 1][j - 1] == x && slot[i - 2][j - 2] == x && slot[i - 3][j - 3] == x) {
					return true;
				}
			}
		}

		// diagonal \
		for (int i = 3; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				if (slot[i][j] == x && slot[i - 1][j + 1] == x && slot[i - 2][j + 2] == x && slot[i - 3][j + 3] == x) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean make3(int x, Point2D move) {
		Integer[][] slot = new Integer[6][7];
		for (int i = 0; i < slots.length; i++) {
			for (int j = 0; j < slots[i].length; j++) {
				slot[i][j] = slots[i][j];
			}
		}
		int z = (int) move.getX();
		int y = (int) move.getY();
		slot[z][y] = x;
		// horizontal
		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < 6; i++) {
				if (slot[i][j] == x && slot[i][j + 1] == x && slot[i][j + 2] == x) {
					return true;
				}
			}
		}

		// vertical
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 7; j++) {
				if (slot[i][j] == x && slot[i + 1][j] == x && slot[i + 2][j] == x) {
					return true;
				}
			}
		}

		// diagonal /
		for (int i = 2; i < 6; i++) {
			for (int j = 2; j < 7; j++) {
				if (slot[i][j] == x && slot[i - 1][j - 1] == x && slot[i - 2][j - 2] == x) {
					return true;
				}
			}
		}

		// diagonal \
		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				if (slot[i][j] == x && slot[i - 1][j + 1] == x && slot[i - 2][j + 2] == x)
					;
			}
		}
		return false;
	}

	private void flash(Circle circle) {

		if (winner == 1) {
			final Color col = Color.YELLOW;
			Timeline time = new Timeline(new KeyFrame(Duration.millis(500), e -> {
				circle.setFill((circle.getFill() != Color.ORANGE) ? Color.ORANGE : col);
			}));
			time.setCycleCount(Timeline.INDEFINITE);
			time.play();
		} else {
			final Color col = Color.RED;
			Timeline time = new Timeline(new KeyFrame(Duration.millis(500), e -> {
				circle.setFill((circle.getFill() != Color.ORANGE) ? Color.ORANGE : col);
			}));
			time.setCycleCount(Timeline.INDEFINITE);
			time.play();
		}
	}
}