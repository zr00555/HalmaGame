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

public class HalmaGame extends Application {

	public static int tileSize = 100, tileWidth = 8, tileHeight = 8; // Dimensions for board
																		
	private int redCounter = 3, blueCounter = 11; // Counters for placing pieces

	public static Group pieceGroup = new Group(); // http://tutorials.jenkov.com/javafx/group.html
	public static Group tileGroup = new Group();

	private Tiles[][] boardArr = new Tiles[tileWidth][tileHeight];

	public static void main(String[] args) {
		launch();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Backgrounds
		Rectangle background = new Rectangle(600, 600);
		background.setFill(Color.WHITE);

		Rectangle gameBackground = new Rectangle(500, 500);
		gameBackground.setFill(Color.BROWN);

		// Create Board and pieces
		Pane board = new Pane();
		board.setPrefSize((tileWidth * tileSize), (tileHeight * tileSize));

		for (int x = 0; x < tileHeight; x++) {
			for (int y = 0; y < tileWidth; y++) {
				Tiles tile = new Tiles((x + y) % 2 == 0, x, y);
				boardArr[x][y] = tile;

				tileGroup.getChildren().add(tile);

				Piece piece = null;

				if (y <= redCounter && x <= 3 && redCounter >= 0) { //Add red pieces
					piece = createPiece(Piece.PieceColor.RED, x, y);
				}

				if (y >= blueCounter && x > 3 && x <= 7 && blueCounter >= 4) { //Add blue pieces
					piece = createPiece(Piece.PieceColor.BLUE, x, y);
				}

				if (piece != null) {
					tile.setPiece(piece);
					pieceGroup.getChildren().add(piece);
				}

			}

			redCounter--;
			blueCounter--;

		}
		board.getChildren().addAll(tileGroup, pieceGroup);

		// Add text
		Text titleText = new Text("Halma -- by Zachary Reese");
		titleText.setFont(Font.font("Bungee", FontWeight.BOLD, 25));

		Text p1Text = new Text("Player 1's turn");

		// Buttons
		Button exitBtn = new Button("Exit");
		exitBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});

		Button resetBtn = new Button("Reset");
		
		Button instructionsBtn = new Button("Instructions");
		instructionsBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				
			}
		});

		// HBox for buttons
		HBox buttons = new HBox(20);
		buttons.getChildren().addAll(resetBtn, exitBtn);
		buttons.setAlignment(Pos.CENTER);

		VBox holder = new VBox(20);
		holder.getChildren().addAll(titleText, board, buttons);
		holder.setAlignment(Pos.CENTER);

		StackPane pane = new StackPane(background, holder); // https://stackoverflow.com/questions/23576044/how-to-center-a-node-within-a-pane-javafx
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Halma Game - Zachary Reese");
		primaryStage.getIcons().add(new Image("https://imgur.com/HopKEzI.jpg"));
		primaryStage.show();
	}

	private Piece createPiece(Piece.PieceColor color, int x, int y) {
		Piece piece = new Piece(color, x, y);
		
		piece.setOnMouseReleased(event -> {
			int newX = toBoard(piece.getLayoutX());
			int newY = toBoard(piece.getLayoutY());
			
			MoveResult result = tryMove(piece, newX, newY);
			
			int x0 = toBoard(piece.getOldX());
			int y0 = toBoard(piece.getOldY());
			
			switch (result.getType()) {
			case NONE: piece.abortMove(); break;
			
			case NORMAL: piece.move(newX, newY);
			boardArr[x0][y0].setPiece(null);
			boardArr[newX][newY].setPiece(piece); break;
			
			case JUMP: piece.move(newX, newY);
			boardArr[x0][y0].setPiece(null);
			boardArr[newX][newY].setPiece(piece); break;
			}
		});
		
		return piece;

	}
	
	private MoveResult tryMove(Piece piece, int newX, int newY) {
		if (boardArr[newX][newY].hasPiece()) {
			return new MoveResult(MoveType.NONE);
		}
		
		int x0 = toBoard(piece.getOldX());
		int y0 = toBoard(piece.getOldY());
		
		if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getColor().moveDir) {
			return new MoveResult(MoveType.NORMAL);
		} else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getColor().moveDir * 2) {
			
			int x1 = x0 + (newX - x0) / 2;
			int y1 = y0 + (newY - y0) / 2;
			
			if(boardArr[x1][y1].hasPiece() && boardArr[x1][y1].getPiece().getColor() != piece.getColor()) {
				return new MoveResult(MoveType.JUMP);
			}
		}
		return new MoveResult(MoveType.NONE);
	}
	
	private int toBoard(double pixel) { //Converts screen pixels to board tiles
		return (int)(pixel + tileSize / 2) / tileSize;
	}

}
