package paintproject;

import static java.lang.Math.abs;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import paintproject.Misc.DragContext;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public class RectangleTool extends ShapeTool {
	/**
	 * The starting x coordinate of the rectangle.
	 */
	private double x;
	/**
	 * The starting y coordinate of the rectangle.
	 */
	private double y;
	/**
	 * A {@link Rectangle} object representing the rectangle to be drawn.
	 */
	private Rectangle rect;
	
	/**
	 * A {@link DragContext} to anchor the initial (x,y) position that the mouse is pressed on.
	 */
	private DragContext dragContext;

	/**
	 * Default constructor for the RectangleTool object - sets each property to 0 or equivalent.
	 */
	public RectangleTool() {
		this.x = 0;
		this.y = 0;
		this.rect = new Rectangle();
		this.dragContext = new DragContext();

	}

	
	@Override
	public void drawFill(GraphicsContext gc) {
		gc.fillRect(
			rect.getX(),
			rect.getY(),
			rect.getWidth(),
			rect.getHeight()
		);
	}

	@Override
	public void drawStroke(GraphicsContext gc) {
		gc.strokeRect(
			rect.getX(),
			rect.getY(),
			rect.getWidth(),
			rect.getHeight()
		);
	}

	@Override
	public void pressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		Group group = App.getCurrentTab().getMainGroup();

		rect.setStrokeWidth(App.getCurrentTab().getGc().getLineWidth());
		rect.setStroke(App.getCurrentTab().getGc().getStroke());
		rect.setStrokeLineCap(StrokeLineCap.ROUND);
		if (App.getControls().getFill().isSelected()) {
			rect.setFill(App.getCurrentTab().getGc().getFill());
		} else {
			rect.setFill(Color.TRANSPARENT);
		}

		dragContext.mouseAnchorX = e.getX();
		dragContext.mouseAnchorY = e.getY();

		rect.setX(dragContext.mouseAnchorX);
		rect.setY(dragContext.mouseAnchorY);
		rect.setWidth(0);
		rect.setHeight(0);

		group.getChildren().add(rect);
	}

	@Override
	public void dragged(MouseEvent e) {
		double offsetX = e.getX() - dragContext.mouseAnchorX;
		double offsetY = e.getY() - dragContext.mouseAnchorY;

		if (offsetX > 0) {
			rect.setWidth(offsetX);
		} else {
			rect.setX(e.getX());
			rect.setWidth(dragContext.mouseAnchorX - rect.getX());
		}
		if (offsetY > 0) {
			rect.setHeight(offsetY);
		} else {
			rect.setY(e.getY());
			rect.setHeight(dragContext.mouseAnchorY - rect.getY());
		}
	}

	@Override
	public void released(MouseEvent e) {
		rect.setWidth(abs(e.getX() - x));
		rect.setHeight(abs(e.getY() - y));
		if (e.getX() - x < 0) {
			rect.setX(e.getX());
		} else {
			rect.setX(x);
		}
		if (e.getY() - y < 0) {
			rect.setY(e.getY());
		} else {
			rect.setY(y);
		}
		Group group = App.getCurrentTab().getMainGroup();
		group.getChildren().remove(rect);
		draw(App.getCurrentTab().getGc());

	}

	@Override
	public void initialize() {
		Canvas canv = App.getCurrentTab().getCanvas();
		if (App.getControls().getTool() != 3) {
			canv.setOnMousePressed(pressedHandler);
			canv.setOnMouseReleased(releasedHandler);
			canv.setOnMouseDragged(draggedHandler);
			App.getControls().setTool(3);
			App.getControls().setToolName("Rectangle");
		} else {
			canv.setOnMousePressed(null);
			canv.setOnMouseReleased(null);
			canv.setOnMouseDragged(null);
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
		}
	}

}
