import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HalmaGame extends Application {

	public static int tileSize = 100, tileWidth = 8, tileHeight = 8; // Dimensions for board
																		
	private int redCounter = 3, blueCounter = 11; // Counters for placing pieces
	private int turnCounter = 1;
	private int moveCounter = 0;
	private int jumpCounter = 0;
	private int x0, y0, x1, y1, tempX, tempY;
	private int winCounter1 = 3, winCounter2 = 7;
	
	private static Text p1Text, p2Text, redWinText, blueWinText;

	public static Group pieceGroup = new Group(); // http://tutorials.jenkov.com/javafx/group.html
	public static Group tileGroup = new Group();
	public static Group resetPieceGroup = new Group();
	public static Group resetTileGroup = new Group();

	private Tiles[][] boardArr = new Tiles[tileWidth][tileHeight];
	
	private Pane mainPane;
	private StackPane pane;
	private Scene scene;
	private Pane board = new Pane();

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
		createBoard();

		// Add text
		Text titleText = new Text("Halma by Zachary Reese");
		titleText.setFont(Font.font("Bungee", FontWeight.BOLD, 25));

		p1Text = new Text("-- Player 1's turn (Red) --"); //PLayer 1
		p1Text.setFont(Font.font("Bungee", FontWeight.BOLD, 15));
		p1Text.setFill(Color.RED);
		p1Text.setX(45);
		p1Text.setY(38);
		
		p2Text = new Text("-- Player 2's turn (Blue) --"); //Player 2
		p2Text.setFont(Font.font("Bungee", FontWeight.BOLD, 15));
		p2Text.setFill(Color.ROYALBLUE);
		p2Text.setX(580);
		p2Text.setY(38);
		
		redWinText = new Text("RED TEAM VICTORY!");
		redWinText.setFont(Font.font("Bungee", FontWeight.BOLD, 55));
		redWinText.setFill(Color.RED);
		redWinText.setStroke(Color.BLACK);
		
		blueWinText = new Text("BLUE TEAM VICTORY!");
		blueWinText.setFont(Font.font("Bungee", FontWeight.BOLD, 55));
		blueWinText.setFill(Color.ROYALBLUE);
		blueWinText.setStroke(Color.BLACK);

		// Buttons
		Button exitBtn = new Button("Exit");
		exitBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});

		Button resetBtn = new Button("Reset");
		resetBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				primaryStage.close();
				redCounter = 3;
				blueCounter = 11;
				turnCounter = 1;
				moveCounter = 0;
				jumpCounter = 0;
				board.getChildren().clear();
				if(p2Text.isVisible() == true) {
					mainPane.getChildren().remove(p2Text);
					mainPane.getChildren().remove(p1Text);
					mainPane.getChildren().add(p1Text);
				}
				if(redWinText.isVisible() == true) {
					pane.getChildren().remove(redWinText);
				}
				if(blueWinText.isVisible() == true) {
					pane.getChildren().remove(blueWinText);
				}
				resetBoard();
				primaryStage.show();
			}
		});
		
		
		Button instructionsBtn = new Button("Instructions");
		instructionsBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                dialog.setTitle("Instructions");
                Label label = new Label("The aim of the game is to be the first to player to move all pieces across the board and into opposing corner. "
                		+ "Players take turns to move a single piece of their own colour. The piece may either be simply moved into an adjacent square "
                		+ "OR it may make one or more hops over other pieces.  Where a hopping move is made, each hop must be over an adjacent piece "
                		+ "and into the vacant square directly beyond it.  A hop may be over any coloured piece including the player's own and can"
                		+ " proceed in any one of the eight directions.  After each hop, the player may either finish or, if possible and desired, "
                		+ "continue by hopping over another piece. \n \n (FOR JUMPING: PLEASE JUMP ACROSS EACH PIECE ONE AT A TIME)"
                		+ " \n \n https://www.mastersofgames.com/rules/halma-rules.htm");
                label.setFont(Font.font("Default", FontWeight.BOLD, 15));
                label.setWrapText(true);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(label);
                Scene dialogScene = new Scene(dialogVbox, 700, 200);
                dialog.setScene(dialogScene);
                dialog.show();
			}
		});
		
		Button endTurnBtn = new Button("End Turn");
		endTurnBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				moveCounter = 0;
				jumpCounter = 0;
				checkForWin();
				getTurn();
			}
		});

		// HBox for buttons
		HBox buttons = new HBox(20);
		buttons.getChildren().addAll(endTurnBtn, instructionsBtn, resetBtn, exitBtn);
		buttons.setAlignment(Pos.CENTER);

		VBox holder = new VBox(30);
		holder.getChildren().addAll(titleText, board, buttons);
		holder.setAlignment(Pos.CENTER);

		pane = new StackPane(background, holder); // https://stackoverflow.com/questions/23576044/how-to-center-a-node-within-a-pane-javafx
		mainPane = new Pane(pane, p1Text);
		scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Halma Game - Zachary Reese");
		primaryStage.getIcons().add(new Image("https://imgur.com/HopKEzI.jpg"));
		primaryStage.show();
	}

	//Methods
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
			boardArr[newX][newY].setPiece(piece);
			jumpCounter++;
			moveCounter++; break;
			
			case JUMP: piece.move(newX, newY);
			boardArr[x0][y0].setPiece(null);
			boardArr[newX][newY].setPiece(piece);
			moveCounter++; break;
			}
		});
		
		return piece;

	}
	
	private MoveResult tryMove(Piece piece, int newX, int newY) {
		
		if (turnCounter % 2 != 0) {
			if (piece.getColor() == Piece.PieceColor.BLUE) { //Cannot move blue pieces during red turn
				return new MoveResult(MoveType.NONE);
			} else {

				if (boardArr[newX][newY].hasPiece()) {
					return new MoveResult(MoveType.NONE);
				}

				int x0 = toBoard(piece.getOldX());
				int y0 = toBoard(piece.getOldY());

				if ((Math.abs(newX - x0) == 1 && jumpCounter == 0 && moveCounter == 0) || (Math.abs(newY - y0) == 1 && jumpCounter == 0 && moveCounter == 0)) {
					while ((Math.abs(newX - x0) + Math.abs(newY - y0) <= 2)) { // Make sure you cant move more than 2 spaces
						return new MoveResult(MoveType.NORMAL);
					}
				} else if ((Math.abs(newX - x0) == 2) || (Math.abs(newY - y0) == 2)) {

					int x1 = x0 + (newX - x0) / 2;
					int y1 = y0 + (newY - y0) / 2;

					if(jumpCounter == 0 || (x0 == tempX && y0 == tempY)) {
					if (boardArr[x1][y1].hasPiece()) {
						jumpCounter++;
						tempX = newX;
						tempY = newY;
						return new MoveResult(MoveType.JUMP);
					}
					}
				}
			}

		}

		if (turnCounter % 2 == 0) {
			if (piece.getColor() == Piece.PieceColor.RED) { //cannot move red pieces
				return new MoveResult(MoveType.NONE);
			} else {

				if (boardArr[newX][newY].hasPiece()) {
					return new MoveResult(MoveType.NONE);
				}

				int x0 = toBoard(piece.getOldX());
				int y0 = toBoard(piece.getOldY());

				if ((Math.abs(newX - x0) == 1 && jumpCounter == 0 && moveCounter == 0) || (Math.abs(newY - y0) == 1 && jumpCounter == 0 && moveCounter == 0)) {
					while ((Math.abs(newX - x0) + Math.abs(newY - y0) <= 2)) { // Make sure you cant move more than 2 space
						return new MoveResult(MoveType.NORMAL);
					}
				} else if ((Math.abs(newX - x0) == 2) || (Math.abs(newY - y0) == 2)) {

					int x1 = x0 + (newX - x0) / 2;
					int y1 = y0 + (newY - y0) / 2;

					if(jumpCounter == 0 || (x0 == tempX && y0 == tempY)) {
						if (boardArr[x1][y1].hasPiece()) {
							jumpCounter++;
							tempX = newX;
							tempY = newY;
							return new MoveResult(MoveType.JUMP);
						}
					}
				}
			}

		}
		return new MoveResult(MoveType.NONE);

	}
	
	
	private int toBoard(double pixel) { //Converts screen pixels to board tiles
		return (int)(pixel + tileSize / 2) / tileSize;
	}
	
	private void getTurn() {
		if(turnCounter % 2 == 0) { //Player 1's turn
			if(p2Text.isVisible() == true) {
				mainPane.getChildren().remove(p2Text);
				mainPane.getChildren().add(p1Text);
			}
		} else {
			if(p1Text.isVisible() == true) {
				mainPane.getChildren().remove(p1Text);
				mainPane.getChildren().add(p2Text);
			} else {
				mainPane.getChildren().add(p2Text);
			}
		}
		turnCounter++;
	}
	
	public void checkForWin() {

		int counter = 0;
		winCounter1 = 3;
		winCounter2 = 7;

		for (int x = 0; x < 4; x++) {
			for (int y = winCounter1; y >= 0; y--) {
				while (boardArr[x][y].hasPiece() == true) {
					if (boardArr[x][y].hasBluePiece() == true && boardArr[x][y].hasPiece() == true) {
						counter++;
						if (counter == 10) {
							pane.getChildren().add(blueWinText);
						} else {
							break;
						}
					} else {
						break;
					}
				}
			}
			winCounter1--;

		}
		counter = 0;

		for (int x = 4; x < 8; x++) {
			for (int y = 7; y >= winCounter2; y--) {
				while (boardArr[x][y].hasPiece() == true) {
					if (boardArr[x][y].hasRedPiece() == true && boardArr[x][y].hasPiece() == true) {
						counter++;
						if (counter == 10) {
							pane.getChildren().add(redWinText);
						} else {
							break;
						}
					} else {
						break;
					}
				}
			}
			winCounter2--;
		}

	}
	
	public void createBoard() {
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
	}
	
	public void resetBoard() {
		pieceGroup.getChildren().clear();
		tileGroup.getChildren().clear();
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
	}

}
