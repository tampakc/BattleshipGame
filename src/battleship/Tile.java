package battleship;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Tile extends Rectangle {
	private final int size = 37;
	private final int x;
	private final int y;
	private Color color;
				
	public void setColor(Color color) {
		this.color = color;
		setFill(color);
	}
				
	public Color getColor() {
		return this.color;
	}
	
	public int getx() {
		return x;
	}
	
	public int gety() {
		return y;
	}
				
	public Tile(Color c, int x, int y, GridPane parent)  {
					
		this.color = c;
		this.x = x;
		this.y = y;
					
		setFill(c);
		setHeight(this.size);
		setWidth(this.size);
		GridPane.setConstraints(this, x, y);
		parent.getChildren().add(this);
	}
}