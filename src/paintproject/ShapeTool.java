package paintproject;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public abstract class ShapeTool {
	
	/**
	 * An EventHandler that calls the {@link #pressed(MouseEvent) pressed MouseEvent)} method.
	 */
	protected final EventHandler<MouseEvent> pressedHandler = new EventHandler<MouseEvent>(){
		public void handle(MouseEvent e){
			pressed(e);
		}
	};
	
	/**
	 * An EventHandler that calls the {@link #dragged(MouseEvent) dragged(MouseEvent)} method.
	 */
	protected final EventHandler<MouseEvent> draggedHandler = new EventHandler<MouseEvent>(){
		public void handle(MouseEvent e){
			dragged(e);
		}
	};
	
	/**
	 * An EventHandler that calls the {@link #released(MouseEvent) released(MouseEvent)} method.
	 */
	protected final EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>(){
		public void handle(MouseEvent e){
			released(e);
		}
	};
	
	/**
	 * Sets up the listeners on the {@link Canvas}.
	 */
	public abstract void initialize();

	/**
	 * Puts the preview shape on the primary {@link Group} and sets the shape's parameters according to the position of the mouse click.
	 * @param e the {@link MouseEvent} corresponding to a mouse press
	 */
	public abstract void pressed(MouseEvent e);

	/**
	 * Sets the properties of the shape according to the position of the mouse drag.
	 * @param e the {@link MouseEvent} corresponding to a mouse drag
	 */
	public abstract void dragged(MouseEvent e);

	/**
	 * Removes the preview shape and calls the {@link #draw(GraphicsContext) draw(GraphicsContext)} method.
	 * @param e the {@link MouseEvent} corresponding to a mouse release
	 */
	public abstract void released(MouseEvent e);

	/**
	 * Sends a command to a {@link GraphicsContext} to draw a filled shape on the corresponding {@link Canvas}.
	 * @param gc the {@link GraphicsContext} to send drawing commands to
	 */
	public abstract void drawFill(GraphicsContext gc);

	/**
	 * Sends a command to a {@link GraphicsContext} to draw an unfilled shape on the corresponding {@link Canvas}.
	 * @param gc the {@link GraphicsContext} to send drawing commands to
	 */
	public abstract void drawStroke(GraphicsContext gc);

	/**
	 * Calls either {@link #drawFill(GraphicsContext) drawFill} or {@link #drawStroke(GraphicsContext) drawStroke} depending on whether the "Fill" control is toggled.
	 * @param gc the {@link GraphicsContext} to send drawing commands to
	 */
	public void draw(GraphicsContext gc){
		App.getCurrentTab().getFileMenuHandler().setSaved(false);
		try {
			App.getCurrentTab().getMisc().addToUndoHistory();
		} catch (Exception e) {
			System.err.println(e);
		}
		if (App.getControls().getFill().isSelected()) {
			drawFill(gc);
		} else {
			drawStroke(gc);
		}
	};
}
