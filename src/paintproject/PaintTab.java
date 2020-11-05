package paintproject;

import java.io.File;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import static javafx.scene.paint.Color.WHITE;

public class PaintTab extends Tab {
	private AnchorPane anchorDisplay;
	private ScrollPane canvasDisplay;
	private Pane canvasPane;
	private Group mainGroup;
	private FileMenu fileMenuHandler;
	private Canvas canv;
	private StackPane stackPane;
	private GraphicsContext gc;
	private File initializedFile;
	private Misc miscFunctionHandler;
	private Zoom zoomHandler;

	
	/**
	 * Default constructor for a PaintTab object. Stores the necessary information for each tab, and creates the GUI for the tab.
	 */
	public PaintTab(){
		super("Untitled");
		this.anchorDisplay = new AnchorPane();
		this.canvasDisplay = new ScrollPane();
		this.canvasPane = new Pane();
		this.mainGroup = new Group();
		this.fileMenuHandler = new FileMenu();
		this.canv = new Canvas(800,500);
		this.gc = canv.getGraphicsContext2D();
		this.miscFunctionHandler = new Misc();
		this.stackPane = new StackPane();
		this.zoomHandler = new Zoom();
		
		gc.setFill(WHITE);
		gc.fillRect(0,0,800,500);
		
		this.stackPane.getChildren().addAll(canv);
		this.mainGroup.getChildren().addAll(this.stackPane);
		this.canvasPane.getChildren().add(this.mainGroup); //Put the canvas in a pane so that zoom doesn't cover the controls
		this.canvasDisplay.setContent(this.canvasPane); //Put this pane in a scrollpane.
		this.anchorDisplay.getChildren().add(this.canvasDisplay);
		this.setContent(this.canvasDisplay);
	}
	
	public Zoom getZoom(){
		return this.zoomHandler;
	}
	
	public Misc getMisc(){
		return this.miscFunctionHandler;
	}
	
	public GraphicsContext getGc() {
		return this.gc;
	}

	public StackPane getStackPane(){
		return this.stackPane;
	}
	
	public Canvas getCanvas() {
		return this.canv;
	}

	public File getInitializedFile() {
		return this.initializedFile;
	}

	public void setInitializedFile(File file) {
		this.initializedFile = file;
	}

	public void setGc(GraphicsContext newGc) {
		this.gc = newGc;
	}
	
	public FileMenu getFileMenuHandler(){
		return this.fileMenuHandler;
	}
	
	public Group getMainGroup() {
		return this.mainGroup;
	}

	public AnchorPane getAnchorDisplay() {
		return this.anchorDisplay;
	}

	public ScrollPane getCanvasDisplay() {
		return this.canvasDisplay;
	}

	public Pane getCanvasPane() {
		return this.canvasPane;
	}
	
	
}
