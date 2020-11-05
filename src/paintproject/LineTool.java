package paintproject;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public class LineTool extends ShapeTool {
	private Line line;

	/**
	 * Default constructor for the LineTool object - sets each property to 0 or equivalent.
	 */
        public LineTool(){
            line = new Line();
        }
        
	@Override
	public void drawFill(GraphicsContext gc) {
            gc.strokeLine(
                    line.getStartX(),
                    line.getStartY(),
                    line.getEndX(),
                    line.getEndY());
	}

	@Override
	public void drawStroke(GraphicsContext gc) {
		gc.strokeLine(
			line.getStartX(),
			line.getStartY(),
			line.getEndX(),
			line.getEndY());
	}

	@Override
	public void pressed(MouseEvent e) {
		Group group = App.getCurrentTab().getMainGroup();
		line.setStrokeWidth(App.getCurrentTab().getGc().getLineWidth());
		line.setStroke(App.getCurrentTab().getGc().getStroke());
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		group.getChildren().add(line);
		line.setStartX(e.getX());
		line.setStartY(e.getY());
		line.setEndX(e.getX());
		line.setEndY(e.getY());
	}

	@Override
	public void dragged(MouseEvent e) {
		line.setEndX(e.getX());
		line.setEndY(e.getY());
	}

	@Override
	public void released(MouseEvent e) {
		Group group = App.getCurrentTab().getMainGroup();
		group.getChildren().remove(line);
		line.setEndX(e.getX());
		line.setEndY(e.getY());
		draw(App.getCurrentTab().getGc());
	}


	@Override
	public void initialize() {
		//This section configures the line tool
		if (ControlPanel.getTool() != 2) {
			App.getCurrentTab().getCanvas().setOnMousePressed(pressedHandler);
			App.getCurrentTab().getCanvas().setOnMouseReleased(releasedHandler);
			App.getCurrentTab().getCanvas().setOnMouseDragged(draggedHandler);
			ControlPanel.setTool(2);
			ControlPanel.setToolName("Line");
		} else {
			App.getCurrentTab().getCanvas().setOnMousePressed(null);
			App.getCurrentTab().getCanvas().setOnMouseReleased(null);
			App.getCurrentTab().getCanvas().setOnMouseDragged(null);
			ControlPanel.setTool(0);
			ControlPanel.setToolName("None");
		}
	}
}
