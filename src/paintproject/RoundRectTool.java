package paintproject;

import static java.lang.Math.abs;
import javafx.event.EventHandler;
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
public class RoundRectTool extends ShapeTool {

	private double x1, y1;
	private Rectangle rect;
	private DragContext dragContext;

	/**
	 * Default constructor for the RoundRectTool object - sets each property to 0 or equivalent.
	 */
	public RoundRectTool() {
		this.x1 = 0;
		this.y1 = 0;
		this.rect = new Rectangle();
		this.dragContext = new DragContext();
	}

	public void drawFill(GraphicsContext gc) {
		gc.fillRoundRect(
			rect.getX(),
			rect.getY(),
			rect.getWidth(),
			rect.getHeight(),
			rect.getArcWidth(),
			rect.getArcHeight()
		);
	}

	public void drawStroke(GraphicsContext gc) {
		gc.strokeRoundRect(
			rect.getX(),
			rect.getY(),
			rect.getWidth(),
			rect.getHeight(),
			rect.getArcWidth(),
			rect.getArcHeight()
		);
	}

	public void pressed(MouseEvent e) {
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
		x1 = e.getX();
		y1 = e.getY();
		rect.setArcWidth(50);
		rect.setArcHeight(50);
	}

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

	public void released(MouseEvent e) {
		rect.setWidth(abs(e.getX() - x1));
		rect.setHeight(abs(e.getY() - y1));
		if (e.getX() - x1 < 0) {
			rect.setX(e.getX());
		} else {
			rect.setX(x1);
		}
		if (e.getY() - y1 < 0) {
			rect.setY(e.getY());
		} else {
			rect.setY(y1);
		}
		Group group = App.getCurrentTab().getMainGroup();
		group.getChildren().remove(rect);
		draw(App.getCurrentTab().getGc());
	}
    
     public void initialize() {
		Canvas canv = App.getCurrentTab().getCanvas();
		//This section configures the rectangle tool
		if (App.getControls().getTool() != 11) {
			canv.setOnMousePressed(pressedHandler);
			canv.setOnMouseReleased(releasedHandler);
			canv.setOnMouseDragged(draggedHandler);
			App.getControls().setTool(11);
			App.getControls().setToolName("Rounded Rectangle");
		} else {
			canv.setOnMousePressed(null);
			canv.setOnMouseReleased(null);
			canv.setOnMouseDragged(null);
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
		}
	}
}
