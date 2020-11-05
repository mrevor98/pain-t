package paintproject;

import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.TRANSPARENT;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public class DropperTool {

	private double x;
	private double y;
	private static WritableImage canvImg = new WritableImage((int) App.getCurrentTab().getCanvas().getWidth(), (int) App.getCurrentTab().getCanvas().getHeight());

	/**
	 * {@link An EventHandler} that calls the {@link #setColor(MouseEvent) setColor(MouseEvent)} method on the primary DropperTool object.
	 */
	private final EventHandler<MouseEvent> COLORCLICKED = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
			App.getControls().getDropperTool().setColor(event);
		}
	};

	/**
	 * Default constructor for the DropperTool object - sets each property to 0.
	 */
	public DropperTool() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Sets the color on the primary {@link ColorPicker} and the color of the fill and stroke of the primary {@link GraphicsContext} to the color that was clicked.
	 * @param event The location at which the mouse was clicked.
	 */
	private void setColor(MouseEvent event) {
		App.getCurrentTab().getCanvas().setOnMousePressed(null);
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(TRANSPARENT);
		canvImg = App.getCurrentTab().getCanvas().snapshot(params, null);
		this.x = event.getX();
		this.y = event.getY();
		PixelReader reader = canvImg.getPixelReader();
		Color color = reader.getColor((int) this.x, (int) this.y);
		App.getControls().getColorPicker().setValue(color);
		App.getCurrentTab().getGc().setFill(color);
		App.getCurrentTab().getGc().setStroke(color);
		App.getControls().getDropper().setSelected(false);
	}

	/**
	 * Sets up the listeners on the {@link Canvas}.
	 */
	public void initDropper() {
		if (App.getControls().getTool() != 7) {
			App.getCurrentTab().getCanvas().setOnMousePressed(null);
			App.getCurrentTab().getCanvas().setOnMouseReleased(null);
			App.getCurrentTab().getCanvas().setOnMouseDragged(null);
			App.getCurrentTab().getCanvas().setOnMousePressed(COLORCLICKED);
			App.getControls().setTool(7);
			App.getControls().setToolName("Dropper");
		} else {
			App.getCurrentTab().getCanvas().setOnMousePressed(null);
			App.getControls().setTool(0);
			App.getControls().setToolName("None");
		}
	}

}
