package paintproject;

import java.util.Optional;
import java.util.Stack;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

public class Misc {
	
	/**
	 * Stack containing the previous iterations of the canvas.
	 */
	private Stack<WritableImage> undoManager;
	/**
	 * Stack containing the iterations of the canvas before undo actions.
	 */
	private Stack<WritableImage> redoManager;
	/**
	 * Temporarily stores the snapshots of the canvas as a WritableImage object.
	 */

	public Misc(){
		undoManager = new Stack<WritableImage>();
		redoManager = new Stack<WritableImage>();
	}
	
	/**
	 * Gets the extension of a file whose name is given as a string.
	 * @param filename The name of the file whose extension is to be found
	 * @return An Optional object containing the file extension as its value
	 */
	public Optional<String> getExtension(String filename) {
		return Optional.ofNullable(filename)
			.filter(f -> f.contains("."))
			.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	/**
	 * Returns the canvas to the state before the most recent drawing action.
	 */
	public void undo() {
		try {
			addToRedoHistory();
			App.getCurrentTab().getGc().drawImage((undoManager.pop()), 0, 0, App.getCurrentTab().getCanvas().getWidth(), 
				App.getCurrentTab().getCanvas().getHeight());
		} catch (Exception e) {
			System.err.println("undo" + e);
		}
	}
	/**
	 * Returns the canvas to its previous state before the most recent undo action.
	 */
	public void redo() {
		try {
			addToUndoHistory();
			App.getCurrentTab().getGc().drawImage((redoManager.pop()), 0, 0, App.getCurrentTab().getCanvas().getWidth(), 
				App.getCurrentTab().getCanvas().getHeight());
		} catch (Exception e) {
			System.err.println("redo" + e);
		}
	}
	/**
	 * Changes the size of the current canvas.
	 * @param width The desired width of the canvas
	 * @param height The desired height of the canvas
	 */
	public void resizeCanvas(double width, double height) {
		try {
			App.getCurrentTab().getCanvas().setWidth(width);
			App.getCurrentTab().getCanvas().setHeight(height);
			App.getCurrentTab().getGc().drawImage(new WritableImage((int) App.getCurrentTab().getCanvas().getWidth(),
				(int) App.getCurrentTab().getCanvas().getHeight()), width, height);
		} catch (Exception e) {
			System.err.println("resize" + e);
		}
	}
	/**
	 * Adds a snapshot of the current canvas to the redoManager stack.
	 */
	public void addToRedoHistory() {
		WritableImage newImg = new WritableImage((int) App.getCurrentTab().getCanvas().getWidth(), (int) App.getCurrentTab().getCanvas().getHeight());
		SnapshotParameters params = new SnapshotParameters();
		redoManager.push(App.getCurrentTab().getCanvas().snapshot(params, newImg));
	}

	/**
	 * Adds a snapshot of the current canvas to the undoManager stack.
	 */
	public void addToUndoHistory() {
		WritableImage newImg = new WritableImage((int) App.getCurrentTab().getCanvas().getWidth(), (int) App.getCurrentTab().getCanvas().getHeight());
		SnapshotParameters params = new SnapshotParameters();
		undoManager.push(App.getCurrentTab().getCanvas().snapshot(params, newImg));
	}

	public static final class DragContext {
            
		/**
		 * Typically set to the initial x coordinate when drawing shapes.
		 */
		public double mouseAnchorX;
		/**
		 * Typically set to the initial y coordinate when drawing shapes.
		 */
		public double mouseAnchorY;
		/**
		 * Default constructor for a DragContext - sets both fields equal to 0.
		 */
		public DragContext() {
			this.mouseAnchorX = 0;
			this.mouseAnchorY = 0;
		}
	}

}
