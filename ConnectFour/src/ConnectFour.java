import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConnectFour extends Application {
	public static Button ai = new Button("Computer");
	public static Button two = new Button("Play with friend");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane set = new BorderPane();
		HBox bottom = new HBox(10);

		Button restart = new Button("Restart");
		bottom.setAlignment(Pos.CENTER);
		bottom.getChildren().addAll(ai, two, restart);
		bottom.setPadding(new Insets(10));
		set.setBottom(bottom);
		ConnectPane pane = new ConnectPane();
		set.setCenter(pane);

		ai.setOnAction(e -> {
			pane.AI();
			pane.playAI();
			ai.setDisable(true);
			two.setDisable(true);
		});

		two.setOnAction(e -> {
			pane.Human();
			pane.playHuman();
			ai.setDisable(true);
			two.setDisable(true);
		});

		restart.setOnAction(e -> {
			pane.restart();
			if (pane.playingVsAI)
				pane.playAI();
			else
				pane.playHuman();
		});

		Scene scene = new Scene(set, 490, 500);
		scene.getStylesheets().add("style.css");
		primaryStage.setTitle("Connect Four");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("Connect_4x4_logo.jpg"));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}