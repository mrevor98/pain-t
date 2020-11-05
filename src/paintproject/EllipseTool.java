package paintproject;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.StrokeLineCap;
import paintproject.Misc.DragContext;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public class EllipseTool extends ShapeTool {
	/**
	 * The starting x coordinate of the ellipse.
	 */
	private double x;
	/**
	 * The starting y coordinate of the ellipse.
	 */
	private double y;
	/**
	 * The width of the rectangle enclosing the ellipse.
	 */
	private double w;
	/**
	 * The height of the rectangle enclosing the ellipse.
	 */
	private double h;
	/**
	 * A {@link Ellipse} object representing the ellipse to be drawn.
	 */
	private Ellipse ellipse;
	/**
	 * A {@link DragContext} to anchor the initial (x,y) position that the mouse is pressed on.
	 */
	private DragContext dragContext;

	/**
	 * Default constructor for the EllipseTool object - sets each property to 0 or equivalent.
	 */
	public EllipseTool() {
		this.x = 0;
		this.y = 0;
		this.w = 0;
		this.h = 0;
		this.ellipse = new Ellipse();
		this.dragContext = new DragContext();
	}

	@Override
	public void drawFill(GraphicsContext gc) {
		gc.fillOval(x, y, w, h);
	}

	@Override
	public void drawStroke(GraphicsContext gc) {
		gc.strokeOval(x, y, w, h);
	}

	@Override
	public void pressed(MouseEvent e) {
		Group group = App.getCurrentTab().getMainGroup();

		ellipse.setStrokeWidth(App.getCurrentTab().getGc().getLineWidth());
		ellipse.setStroke(App.getCurrentTab().getGc().getStroke());
		ellipse.setStrokeLineCap(StrokeLineCap.ROUND);
		if (App.getControls().getFill().isSelected()) {
			ellipse.setFill(App.getCurrentTab().getGc().getFill());
		} else {
			ellipse.setFill(Color.TRANSPARENT);
		}

		ellipse.setRadiusX(0);
		ellipse.setRadiusY(0);
		group.getChildren().add(ellipse);
		dragContext.mouseAnchorX = e.getX();
		dragContext.mouseAnchorY = e.getY();
		x = e.getX();
		y = e.getY();
	}

	@Override
	public void released(MouseEvent e) {
		Group group = App.getCurrentTab().getMainGroup();
		group.getChildren().remove(ellipse);
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
		ellipse.setRadiusX(w / 2);
		ellipse.setRadiusY(h / 2);
		ellipse.setCenterX((w + 2 * x) / 2);
		ellipse.setCenterY((h + 2 * y) / 2);
	}

	@Override
	public void initialize() {
		Canvas canv = App.getCurrentTab().getCanvas();
		//Configures the ellipse tool
		if (App.getControls().getTool() != 4) {
			canv.setOnMousePressed(pressedHandler);
			canv.setOnMouseReleased(releasedHandler);
			canv.setOnMouseDragged(draggedHandler);
			App.getControls().setTool(4);
			App.getControls().setToolName("Ellipse");
		} else {
			canv.setOnMousePressed(null);
			canv.setOnMouseReleased(null);
			canv.setOnMouseDragged(null);
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
		}
	}

}
