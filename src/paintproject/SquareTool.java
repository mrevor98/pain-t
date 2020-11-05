package paintproject;

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
public class SquareTool extends ShapeTool {

	private double x, y, w, h;
	private Rectangle square;
	private DragContext dragContext;

	/**
	 * Default constructor for the SquareTool object - sets each property to 0 or equivalent.
	 */
	public SquareTool() {
		this.x = 0;
		this.y = 0;
		this.w = 0;
		this.h = 0;
		this.square = new Rectangle();
		this.dragContext = new DragContext();
	}

	@Override
	public void drawFill(GraphicsContext gc) {
		gc.fillRect(x, y, w, h);
	}

	@Override
	public void drawStroke(GraphicsContext gc) {
		gc.strokeRect(x, y, w, h);
	}

	@Override
	public void pressed(MouseEvent e) {
		dragContext.mouseAnchorX = e.getX();
		dragContext.mouseAnchorY = e.getY();
		x = e.getX();
		y = e.getY();
		square.setWidth(0);
		square.setHeight(0);
		square.setStrokeWidth(App.getCurrentTab().getGc().getLineWidth());
		square.setStroke(App.getCurrentTab().getGc().getStroke());
		square.setStrokeLineCap(StrokeLineCap.ROUND);
		if (App.getControls().getFill().isSelected()) {
			square.setFill(App.getCurrentTab().getGc().getFill());
		} else {
			square.setFill(Color.TRANSPARENT);
		}
		Group group = App.getCurrentTab().getMainGroup();
		group.getChildren().add(square);

	}

	@Override
	public void released(MouseEvent e) {
		Group group = App.getCurrentTab().getMainGroup();
		group.getChildren().remove(square);
		draw(App.getCurrentTab().getGc());
	}

	@Override
	public void dragged(MouseEvent e) {
		double offsetX = e.getX() - dragContext.mouseAnchorX;
		double offsetY = e.getY() - dragContext.mouseAnchorY;
		if (offsetX > 0) {
			w = offsetX;
		} else {
			x = e.getX();
			w = dragContext.mouseAnchorX - x;
		}
		if (offsetY > 0) {
			h = offsetY;
		} else {
			y = e.getY();
			h = dragContext.mouseAnchorY - y;
		}
		if (w < h) {
			h = w;
		} else {
			w = h;
		}
		square.setX(x);
		square.setY(y);
		square.setWidth(w);
		square.setHeight(h);
	}

	@Override
	public void initialize() {
		Canvas canv = App.getCurrentTab().getCanvas();
		//This section configures the square tool
		if (App.getControls().getTool() != 6) {
			canv.setOnMousePressed(pressedHandler);
			canv.setOnMouseReleased(releasedHandler);
			canv.setOnMouseDragged(draggedHandler);
			App.getControls().setTool(6);
			App.getControls().setToolName("Square");
		} else {
			canv.setOnMousePressed(null);
			canv.setOnMouseReleased(null);
			canv.setOnMouseDragged(null);
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
		}
	}

}
