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
	
	public boolean hasPiece() {
		return piece != null;
	}

	public Tiles(boolean color, int x, int y) {
		setWidth(HalmaGame.tileSize);
		setHeight(HalmaGame.tileSize);
		
		relocate((x * HalmaGame.tileSize), (y * HalmaGame.tileSize));
		
		setFill(color ? Color.ANTIQUEWHITE : Color.DARKSEAGREEN);
	}

}
