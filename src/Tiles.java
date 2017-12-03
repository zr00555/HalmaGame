import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tiles extends Rectangle{
	
	private Piece piece;
	
	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	public boolean hasPiece() { //Is there a piece on this coordinate of the board
		return piece != null;
	}
	
	public boolean hasRedPiece() { //Is there a red piece on this coordinate of the board
		if (piece.getColor() == Piece.PieceColor.RED && piece != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hasBluePiece() { //IS there a blue piece on this coordinate of the board
		if (piece.getColor() == Piece.PieceColor.BLUE && piece != null) {
			return true;
		} else {
			return false;
		}
	}

	public Tiles(boolean color, int x, int y) { //creates checkered tiles (help from fxtutorials)
		setWidth(HalmaGame.tileSize);
		setHeight(HalmaGame.tileSize);
		
		relocate((x * HalmaGame.tileSize), (y * HalmaGame.tileSize));
		
		setFill(color ? Color.ANTIQUEWHITE : Color.DARKSEAGREEN);
	}

}
