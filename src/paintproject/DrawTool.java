package paintproject;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public class DrawTool {

	/**
	 * Default constructor for the DrawTool object.
	 */
	public DrawTool() {
	}

	void pressed(GraphicsContext gc, MouseEvent event) {
		gc.beginPath();
		gc.moveTo(event.getX(), event.getY());
		gc.stroke();
		App.getCurrentTab().getFileMenuHandler().setSaved(false);
	}

	void dragged(GraphicsContext gc, MouseEvent event) {
		gc.lineTo(event.getX(), event.getY());
		gc.stroke();
	}


	private final EventHandler<MouseEvent> pressedHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			try {
				App.getCurrentTab().getMisc().addToUndoHistory();
			} catch (Exception e) {
				System.err.println(e);
			}
			pressed(App.getCurrentTab().getGc(), event);
		}
	};

	private final EventHandler<MouseEvent> draggedHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			dragged(App.getCurrentTab().getGc(), event);
		}
	};

	private final EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
		}
	};

	/**
	 * Sets the listeners on the primary {@link Canvas}.
	 */
	public void initialize() {
		GraphicsContext gc = App.getCurrentTab().getGc();
		Canvas canv = App.getCurrentTab().getCanvas();

		gc.setFill(App.getControls().getColorPicker().getValue());
		gc.setStroke(App.getControls().getColorPicker().getValue());
		gc.setLineWidth((double) Character.digit(App.getControls().getLineWidthPicker().getValue().toString().charAt(0), 10));
		if (App.getControls().getTool() != 1) {
			canv.setOnMousePressed(pressedHandler);
			canv.setOnMouseReleased(releasedHandler);
			canv.setOnMouseDragged(draggedHandler);
			App.getControls().setTool(1);
			App.getControls().setToolName("Pencil");
		} else {
			canv.setOnMousePressed(null);
			canv.setOnMouseReleased(null);
			canv.setOnMouseDragged(null);
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
		}
	}
}
