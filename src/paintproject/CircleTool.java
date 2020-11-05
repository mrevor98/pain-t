package paintproject;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import paintproject.Misc.DragContext;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public class CircleTool extends ShapeTool {
	/**
	 * The starting x coordinate of the circle.
	 */
	private double x;
	/**
	 * The starting y coordinate of the circle.
	 */
	private double y;
	/**
	 * The width of the square enclosing by the circle.
	 */
	private double w;
	/**
	 * The height of the square enclosing the circle.
	 */
	private double h;
	/**
	 * A {@link Circle} object representing the circle to be drawn.
	 */
	private Circle circle;
	/**
	 * A {@link DragContext} to anchor the initial (x,y) position that the mouse is pressed on.
	 */
	private DragContext dragContext;
	
	/**
	 * Default constructor for the CircleTool object - sets each property to 0 or equivalent.
	 */
	public CircleTool() {
		this.x = 0;
		this.y = 0;
		this.w = 0;
		this.h = 0;
		this.circle = new Circle();
		this.dragContext = new DragContext();
	}

	@Override
	public void drawFill(GraphicsContext gc) {
		gc.fillOval(this.x, this.y, this.w, this.h);
	}

	@Override
	public void drawStroke(GraphicsContext gc) {
		gc.strokeOval(this.x, this.y, this.w, this.h);
	}

	@Override
	public void pressed(MouseEvent e) {
		Group group = App.getCurrentTab().getMainGroup();

		circle.setStrokeWidth(App.getCurrentTab().getGc().getLineWidth());
		circle.setStroke(App.getCurrentTab().getGc().getStroke());
		circle.setStrokeLineCap(StrokeLineCap.ROUND);
		if (App.getControls().getFill().isSelected()) {
			circle.setFill(App.getCurrentTab().getGc().getFill());
		} else {
			circle.setFill(Color.TRANSPARENT);
		}

		circle.setRadius(0);
		group.getChildren().add(circle);
		dragContext.mouseAnchorX = e.getX();
		dragContext.mouseAnchorY = e.getY();
		x = e.getX();
		y = e.getY();
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
		circle.setRadius(w / 2);
		circle.setCenterX((2 * x + w) / 2);
		circle.setCenterY((2 * y + h) / 2);
	}

	@Override
	public void released(MouseEvent e) {
		Group group = App.getCurrentTab().getMainGroup();
		group.getChildren().remove(circle);
		draw(App.getCurrentTab().getGc());
	}

	@Override
	public void initialize() {
		if (App.getControls().getTool() != 5) {
			App.getCurrentTab().getCanvas().setOnMousePressed(pressedHandler);
			App.getCurrentTab().getCanvas().setOnMouseReleased(releasedHandler);
			App.getCurrentTab().getCanvas().setOnMouseDragged(draggedHandler);
			App.getControls().setTool(5);
			App.getControls().setToolName("Circle");
		} else {
			App.getCurrentTab().getCanvas().setOnMousePressed(null);
			App.getCurrentTab().getCanvas().setOnMouseReleased(null);
			App.getCurrentTab().getCanvas().setOnMouseDragged(null);
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
		}
	}

}
