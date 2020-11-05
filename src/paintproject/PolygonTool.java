package paintproject;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import paintproject.Misc.DragContext;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public class PolygonTool extends ShapeTool {

	private double x, y, w, h, rad, centerX, centerY;
	
	private static Canvas upperCanv = new Canvas(App.getCurrentTab().getCanvas().getWidth(), App.getCurrentTab().getCanvas().getHeight());
	
	private static GraphicsContext upperCanvGc = upperCanv.getGraphicsContext2D();
	
	/**
	 * Contains the x coordinates of each point in the chosen polygon.
	 */
	private double xPoints[];
	
	/**
	 * Contains the y coordinates of each point in the chosen polygon.
	 */
	private double yPoints[];
	
	private int numSides;
	
	private DragContext dragContext;
	
	/**
	 * Default constructor for the PolygonTool object - sets each property to 0 or equivalent.
	 */
	public PolygonTool() {
		this.x = 0;
		this.y = 0;
		this.w = 0;
		this.h = 0;
		this.rad = 0;
		this.centerX = 0;
		this.centerY = 0;
		this.xPoints = new double[0];
		this.yPoints = new double[0];
		this.dragContext = new DragContext();
	}

	@Override
	public void drawFill(GraphicsContext gc) {
		gc.fillPolygon(xPoints, yPoints, numSides);
	}

	@Override
	public void drawStroke(GraphicsContext gc) {
		gc.strokePolygon(xPoints, yPoints, numSides);
	}

	@Override
	public void pressed(MouseEvent e) {
		xPoints = new double[0];
		yPoints = new double[0];
		App.getCurrentTab().getStackPane().getChildren().add(upperCanv);
		x = e.getX();
		y = e.getY();
		dragContext.mouseAnchorX = e.getX();
		dragContext.mouseAnchorY = e.getY();
		numSides = Integer.parseInt(App.getControls().getPolySides().getText());
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
		this.centerX = (2 * x + w) / 2;
		this.centerY = (2 * y + h) / 2;
		this.rad = w / 2;
		calculatePoints();
		upperCanvGc.clearRect(0, 0, upperCanv.getWidth(), upperCanv.getHeight());
		if(App.getControls().getFill().isSelected()){
			upperCanvGc.fillPolygon(xPoints, yPoints, numSides);
		}
		else{
			upperCanvGc.strokePolygon(xPoints,yPoints,numSides);
		}
	}

	@Override
	public void released(MouseEvent e) {
		upperCanvGc.clearRect(0, 0, upperCanv.getWidth(), upperCanv.getHeight());
		App.getCurrentTab().getStackPane().getChildren().remove(upperCanv);
		draw(App.getCurrentTab().getGc());
	}

	@Override
	public void initialize() {
		//This section initializes the polygon tool
		if (App.getControls().getTool() != 10) {
			App.getCurrentTab().getCanvas().setOnMousePressed(pressedHandler);
			App.getCurrentTab().getCanvas().setOnMouseReleased(releasedHandler);
			App.getCurrentTab().getCanvas().setOnMouseDragged(draggedHandler);
			App.getControls().setTool(10);
			App.getControls().setToolName("Polygon");
		} else {
			App.getCurrentTab().getCanvas().setOnMousePressed(null);
			App.getCurrentTab().getCanvas().setOnMouseReleased(null);
			App.getCurrentTab().getCanvas().setOnMouseDragged(null);
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
		}
	}
	/**
	 * Appends a double to the end of an existing double[].
	 * @param n The number of points in the array arr
	 * @param arr The double[] to add a value to.
	 * @param x The value to be added to the array arr
	 * @return The double[] resulting from adding the parameter x to the parameter arr.
	 */
	public static double[] addToPoints(int n,
		double arr[],
		double x) {
		int i;
		double newarr[] = new double[n + 1];
		for (i = 0; i < n; i++) {
			newarr[i] = arr[i];
		}
		newarr[n] = x;
		return newarr;
	}

	/**
	 * Computes the (x,y) coordinates of each point in the polygon and adds them to the corresponding arrays.
	 */
	private void calculatePoints() {
		for (int i = 0; i < numSides; i++) {
			xPoints = addToPoints(i,
				xPoints,
				centerX + rad * Math.cos(2 * Math.PI * i / numSides));
			yPoints = addToPoints(i,
				yPoints,
				centerY + rad * Math.sin(2 * Math.PI * i / numSides));
		}
	}
}
