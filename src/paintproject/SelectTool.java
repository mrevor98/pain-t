package paintproject;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import paintproject.Misc.DragContext;

public class SelectTool {

	private static RubberBandSelection rubberBandSelection;
	private static Canvas upperCanv = new Canvas(App.getCurrentTab().getCanvas().getWidth(), App.getCurrentTab().getCanvas().getHeight());
	private static GraphicsContext upperCanvGc = upperCanv.getGraphicsContext2D();
	private static WritableImage img;

	public RubberBandSelection getSelection() {
		return this.rubberBandSelection;
	}

	public static void drag(Bounds bounds) {
		App.getCurrentTab().getMisc().addToUndoHistory();
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();
		Group group = App.getCurrentTab().getMainGroup();

		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		sp.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

		img = new WritableImage(width, height);
		App.getCurrentTab().getCanvas().snapshot(sp, img);

		group.setOnMousePressed(null);
		group.setOnMousePressed(dragClicked);

	}

	private static EventHandler<MouseEvent> dragClicked = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			if (event.isSecondaryButtonDown()) {
				return;
			}
			Group group = App.getCurrentTab().getMainGroup();

			group.setOnMouseDragged(null);
			group.setOnMouseReleased(null);

			group.setOnMouseDragged(dragDragged);
			group.setOnMouseReleased(dragReleased);
			
			App.getCurrentTab().getStackPane().getChildren().add(upperCanv);
			App.getCurrentTab().getGc().setFill(Color.WHITE);
			App.getCurrentTab().getGc().fillRect(
				RubberBandSelection.getRect().getX(),
				RubberBandSelection.getRect().getY(),
				RubberBandSelection.getRect().getWidth(),
				RubberBandSelection.getRect().getHeight());
		}
	};

	private static EventHandler<MouseEvent> dragDragged = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			upperCanvGc.clearRect(0, 0, upperCanv.getWidth(), upperCanv.getHeight());
			upperCanvGc.drawImage(img, event.getX(), event.getY());
		}
	};
	
	private static EventHandler<MouseEvent> dragReleased = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			if (event.isSecondaryButtonDown()) {
				return;
			}
			Group group = App.getCurrentTab().getMainGroup();
			Rectangle rect = RubberBandSelection.getRect();
			
			App.getCurrentTab().getStackPane().getChildren().remove(upperCanv);
			upperCanvGc.clearRect(0, 0, upperCanv.getWidth(), upperCanv.getHeight());
			
			App.getCurrentTab().getGc().drawImage(img,
				event.getX(),
				event.getY(),
				rect.getWidth(),
				rect.getHeight());
			rect.setX(event.getX());
			rect.setY(event.getY());

			group.setOnMousePressed(null);
			group.setOnMouseDragged(null);
			group.setOnMouseReleased(null);
			rect.setX(0);
			rect.setY(0);
			rect.setWidth(0);
			rect.setHeight(0);
		}
	;

	};
	
	public static class RubberBandSelection {

		private static final DragContext dragContext = new DragContext();
		private static Rectangle rect = new Rectangle();
		private static Group group;

		public static Rectangle getRect() {
			return RubberBandSelection.rect;
		}

		public static Bounds getBounds() {
			return rect.getBoundsInParent();
		}

		public RubberBandSelection(Group group) {
			this.group = group;

			rect = new Rectangle(0, 0, 0, 0);
			rect.setStroke(Color.BLUE);
			rect.setStrokeWidth(1);
			rect.setStrokeLineCap(StrokeLineCap.ROUND);
			rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
		}

		private static EventHandler<MouseEvent> pressedHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					return;
				}
				rect.setX(0);
				rect.setY(0);
				rect.setWidth(0);
				rect.setHeight(0);

				App.getCurrentTab().getCanvas().setOnMousePressed(null);
				App.getCurrentTab().getCanvas().setOnMouseDragged(null);
				App.getCurrentTab().getCanvas().setOnMouseReleased(null);
				ControlPanel.setTool(0);
				ControlPanel.setToolName("None");

				group.getChildren().remove(rect);

				dragContext.mouseAnchorX = event.getX();
				dragContext.mouseAnchorY = event.getY();

				rect.setX(dragContext.mouseAnchorX);
				rect.setY(dragContext.mouseAnchorY);
				rect.setWidth(0);
				rect.setHeight(0);

				group.getChildren().add(rect);
			}
		};

		public static void initialize() {
			Group group = App.getCurrentTab().getMainGroup();
			group.getChildren().remove(rect);
			rubberBandSelection = new RubberBandSelection(group);
			group.setOnMousePressed(pressedHandler);
			group.setOnMouseDragged(draggedHandler);
			group.setOnMouseReleased(releasedHandler);
			ControlPanel.setToolName("Select");
		}

		private static EventHandler<MouseEvent> draggedHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					return;
				}
				double offsetX = event.getX() - dragContext.mouseAnchorX;
				double offsetY = event.getY() - dragContext.mouseAnchorY;

				if (offsetX > 0) {
					rect.setWidth(offsetX);
				} else {
					rect.setX(event.getX());
					rect.setWidth(dragContext.mouseAnchorX - rect.getX());
				}
				if (offsetY > 0) {
					rect.setHeight(offsetY);
				} else {
					rect.setY(event.getY());
					rect.setHeight(dragContext.mouseAnchorY - rect.getY());
				}
			}
		;
		};
		private static EventHandler<MouseEvent> releasedHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {

				if (event.isSecondaryButtonDown()) {
					return;
				}
				rect.setOnMousePressed((MouseEvent e) -> {
					SelectTool.drag(SelectTool.RubberBandSelection.getBounds());
				});
			}
		};
	}

}
