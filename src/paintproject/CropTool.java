package paintproject;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import paintproject.Misc.DragContext;

public class CropTool {
	private static CropSelection cropSelection;
	private static WritableImage img;
	
	public CropSelection getSelection(){
		return this.cropSelection;
	}

	public static void crop(Bounds bounds){
		App.getCurrentTab().getMisc().addToUndoHistory();
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();
		Group group = App.getCurrentTab().getMainGroup();

		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		sp.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

		img = new WritableImage(width, height);
		App.getCurrentTab().getCanvas().snapshot(sp, img);
		
		App.getCurrentTab().getGc().clearRect(0, 0, 
			App.getCurrentTab().getCanvas().getWidth(), 
			App.getCurrentTab().getCanvas().getWidth());
		App.getCurrentTab().getMisc().resizeCanvas(width, height);
		App.getCurrentTab().getGc().drawImage(img, 0, 0);
		group.setOnMousePressed(null);
		group.setOnMouseDragged(null);
		group.setOnMouseReleased(null);
		group.getChildren().remove(CropSelection.getRect());
	}
	
	public static class CropSelection {
		private static final DragContext dragContext = new DragContext();
		private static Rectangle rect = new Rectangle();
		private static Group group = new Group();
		
		public static Rectangle getRect() {
			return CropTool.CropSelection.rect;
		}

		public static Bounds getBounds() {
			return rect.getBoundsInParent();
		}
		
		public CropSelection(Group group) {
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
			cropSelection = new CropTool.CropSelection(group);
			group.setOnMousePressed(pressedHandler);
			group.setOnMouseDragged(draggedHandler);
			group.setOnMouseReleased(releasedHandler);
			ControlPanel.setToolName("Crop");
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
				CropTool.crop(CropTool.CropSelection.getBounds());
			}
		};
	}
}
