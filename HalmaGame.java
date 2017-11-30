import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HalmaGame extends Application{
	
	public static int tileSize = 100, tileWidth = 8, tileHeight = 8;
	
	private Group pieceGroup = new Group(); //http://tutorials.jenkov.com/javafx/group.html
	private Group tileGroup = new Group();

	public static void main(String[] args) {
		launch();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Backgrounds
		Rectangle background = new Rectangle(600, 600);
		background.setFill(Color.WHITE);		
		
		Rectangle gameBackground = new Rectangle(500, 500);
		gameBackground.setFill(Color.BROWN);
		
		//Create Board
		Pane board = new Pane();
		board.setPrefSize((tileWidth * tileSize), (tileHeight * tileSize));
		
		for(int x = 0; x < tileHeight; x++) {
			for(int y = 0; y < tileWidth; y++) {
				Tiles tile = new Tiles((x + y) % 2 == 0, x, y);
				
				tileGroup.getChildren().add(tile);
			}
		} 
		board.getChildren().addAll(tileGroup);
		
		//Add text
		Text titleText = new Text("Halma -- by Zachary Reese");
		titleText.setFont(Font.font("Bungee", FontWeight.BOLD, 25));
		
		Text p1Text = new Text("Player 1's turn");
		
		//Buttons
		Button exitBtn = new Button("Exit");
		exitBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});
		
		Button resetBtn = new Button("Reset");
		
		//HBox for buttons
		HBox buttons = new HBox(20);
		buttons.getChildren().addAll(resetBtn, exitBtn);
		buttons.setAlignment(Pos.CENTER);
		
		VBox holder = new VBox(20);
		holder.getChildren().addAll(titleText, board, buttons);
		holder.setAlignment(Pos.CENTER);
		
		
		StackPane pane = new StackPane(background, holder); //https://stackoverflow.com/questions/23576044/how-to-center-a-node-within-a-pane-javafx
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Halma Game - Zachary Reese");
		primaryStage.getIcons().add(new Image("https://imgur.com/HopKEzI.jpg"));
		primaryStage.show();
	}

}
