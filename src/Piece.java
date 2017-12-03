import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece extends StackPane{
	
	private double mouseX, mouseY;
	private double oldX, oldY;
	
	public double getOldX() {
		return oldX;
	}

	public double getOldY() {
		return oldY;
	}

	public PieceColor getColor() {
		return color;
	}

	public enum PieceColor {
		RED, BLUE;
		
		static int moveDir;
		
		public void pieceType(int moveDir) {
			this.moveDir = moveDir;
		}
	}
	
	private PieceColor color;
	
	public Piece(PieceColor color, int x, int y) {
		this.color = color;
		
		move(x, y);
		
		Circle gamePiece = new Circle(HalmaGame.tileSize * .3);
		gamePiece.setFill(color == PieceColor.RED ? Color.RED : Color.ROYALBLUE);
		gamePiece.setStroke(Color.BLACK);
		gamePiece.setStrokeWidth(HalmaGame.tileSize * 0.03);
		
		gamePiece.setTranslateX((HalmaGame.tileSize - HalmaGame.tileSize * 0.3125 * 2) / 2);
		gamePiece.setTranslateY((HalmaGame.tileSize - HalmaGame.tileSize * 0.26 * 2) / 2);
		
		getChildren().addAll(gamePiece);
		
		setOnMousePressed(event -> {
			mouseX = event.getSceneX();
			mouseY = event.getSceneY();
		});
		
		setOnMouseDragged(event -> {
			relocate(event.getSceneX() - mouseX + oldX, event.getSceneY() - mouseY + oldY);
		});
	}
	
	public void move(int x, int y) {
		oldX = x * HalmaGame.tileSize;
		oldY = y * HalmaGame.tileSize;
		relocate(oldX, oldY);
	}
	
	public void abortMove() {
		relocate(oldX, oldY);
	}

}
