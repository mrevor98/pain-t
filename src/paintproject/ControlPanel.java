package paintproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Michael Revor
 * @version 0.5
 */
public class ControlPanel extends GridPane {

	private DropperTool mainDropperTool;
	private TextTool mainTextTool;
	private CircleTool circleTool;
	private DrawTool drawTool;
	private PolygonTool polyTool;
	private RectangleTool rectTool;
	private EraserTool eraserTool;
	private EllipseTool ellipseTool;
	private RoundRectTool roundRectTool;
	private SquareTool squareTool;
	private LineTool lineTool;

	private static int tool;
	private static String toolName;
	private ObservableList<String> POSSIBLEWIDTHS = FXCollections.observableArrayList(
		"1px", "3px", "5px", "7px", "9px");
	private ColorPicker COLORPICKER = new ColorPicker();
	private ComboBox LINEWIDTHPICKER = new ComboBox(POSSIBLEWIDTHS);
	private ToggleButton PENCIL = new ToggleButton("Pencil");
	private ToggleButton LINE = new ToggleButton("Line");
	private ToggleButton FILL = new ToggleButton("Fill");
	private ToggleButton ERASER = new ToggleButton("Eraser");
	private Button ZOOMIN = new Button("Zoom In");
	private Button ZOOMOUT = new Button("Zoom Out");
	private Button UNDO = new Button("Undo");
	private Tooltip undoTip = new Tooltip("Erases the previous action");
	private Button REDO = new Button("Redo");
	private ToggleButton RECTANGLE = new ToggleButton("Rectangle");
	private ToggleButton ROUNDRECT = new ToggleButton("Round Rectangle");
	private ToggleButton ELLIPSE = new ToggleButton("Ellipse");
	private ToggleButton CIRCLE = new ToggleButton("Circle");
	private ToggleButton SQUARE = new ToggleButton("Square");
	private ToggleButton DROPPER = new ToggleButton("Dropper");
	private Tooltip dropperTip = new Tooltip("Click on a color to select it");
	private ToggleButton POLYGON = new ToggleButton("Polygon");
	private Tooltip polyTip = new Tooltip("Click and drag to draw a regular polygon");
	private Label polySidesLabel = new Label("# of sides:");
	private Tooltip polySidesTip = new Tooltip("Enter how many sides you want your regular polygon to have");
	private TextField polySides = new TextField("3");
	private Button SELECT = new Button("Select");
	private Button TEXT = new Button("Text");
	private Button CROP = new Button("Crop");
	private ToggleGroup TOGGLEBUTTONS = new ToggleGroup();

	/**
	 * Default constructor for the ControlPanel object - calls default constructors for each tool object and stores them in the new ControlPanel object.
	 */
	public ControlPanel() {
		super();
		this.mainDropperTool = new DropperTool();
		this.mainTextTool = new TextTool();
		this.circleTool = new CircleTool();
		this.drawTool = new DrawTool();
		this.polyTool = new PolygonTool();
		this.rectTool = new RectangleTool();
		this.eraserTool = new EraserTool();
		this.ellipseTool = new EllipseTool();
		this.roundRectTool = new RoundRectTool();
		this.squareTool = new SquareTool();
		this.lineTool = new LineTool();

		this.setMinWidth(100); //Setting up the layout for the control panel to the side.
		this.setMinHeight(500);
		this.setMaxWidth(350);
		COLORPICKER.setValue(Color.BLACK);
		this.add(COLORPICKER, 0, 5);
		LINEWIDTHPICKER.setValue("1px");
		this.add(LINEWIDTHPICKER, 0, 15);
		TOGGLEBUTTONS.getToggles().addAll(PENCIL, LINE, ERASER, RECTANGLE, 
			ROUNDRECT, ELLIPSE, CIRCLE, SQUARE, DROPPER, POLYGON);
		DROPPER.setTooltip(dropperTip);
		POLYGON.setTooltip(polyTip);
		UNDO.setTooltip(undoTip);
		polySides.setTooltip(polySidesTip);
		this.add(PENCIL, 0, 30);
		this.add(ERASER, 0, 45);
		this.add(LINE, 0, 60);
		this.add(FILL, 0, 75);
		this.add(DROPPER, 0, 90);
		this.add(RECTANGLE, 0, 105);
		this.add(ROUNDRECT, 0, 120);
		this.add(ELLIPSE, 0, 135);
		this.add(CIRCLE, 0, 150);
		this.add(SQUARE, 0, 165);
		this.add(ZOOMIN, 0, 180);
		this.add(ZOOMOUT, 0, 195);
		this.add(UNDO, 0, 210);
		this.add(REDO, 0, 225);
		this.add(TEXT, 0, 240);
		this.add(SELECT, 0, 255);
		this.add(CROP,0,270);
		this.add(POLYGON, 0, 295);
		this.add(polySidesLabel, 0, 310);
		this.add(polySides, 5, 310);
	}

	public LineTool getLineTool() {
		return this.lineTool;
	}

	public SquareTool getSquareTool() {
		return this.squareTool;
	}

	public RoundRectTool getRoundRectTool() {
		return this.roundRectTool;
	}

	public EllipseTool getEllipseTool() {
		return this.ellipseTool;
	}

	public EraserTool getEraserTool() {
		return this.eraserTool;
	}

	public RectangleTool getRectTool() {
		return this.rectTool;
	}

	public DrawTool getDrawTool() {
		return this.drawTool;
	}

	public DropperTool getDropperTool() {
		return this.mainDropperTool;
	}

	public TextTool getTextTool() {
		return this.mainTextTool;
	}

	public CircleTool getCircleTool() {
		return this.circleTool;
	}

	public PolygonTool getPolyTool() {
		return this.polyTool;
	}

	public ToggleButton getRoundRect() {
		return this.ROUNDRECT;
	}

	public ToggleButton getPolygon() {
		return this.POLYGON;
	}

	public TextField getPolySides() {
		return this.polySides;
	}

	public ColorPicker getColorPicker() {
		return this.COLORPICKER;
	}

	public ComboBox getLineWidthPicker() {
		return this.LINEWIDTHPICKER;
	}

	public ToggleButton getPencil() {
		return this.PENCIL;
	}

	public ToggleButton getEraser() {
		return this.ERASER;
	}

	public ToggleButton getRectangle() {
		return this.RECTANGLE;
	}

	public ToggleButton getLine() {
		return this.LINE;
	}

	public ToggleButton getEllipse() {
		return this.ELLIPSE;
	}

	public ToggleButton getCircle() {
		return this.CIRCLE;
	}

	public ToggleButton getSquare() {
		return this.SQUARE;
	}

	public Button getZoomIn() {
		return this.ZOOMIN;
	}

	public Button getZoomOut() {
		return this.ZOOMOUT;
	}

	public Button getUndo() {
		return this.UNDO;
	}

	public Button getRedo() {
		return this.REDO;
	}

	public Button getSelect() {
		return this.SELECT;
	}

	public String getToolName(){
		return toolName;
	}
	
	public static int getTool() {
		return ControlPanel.tool;
	}

	public ToggleButton getFill() {
		return this.FILL;
	}

	public ToggleButton getDropper() {
		return this.DROPPER;
	}

	public Button getText() {
		return this.TEXT;
	}

	public Button getCrop(){
		return this.CROP;
	}
	
	public ToggleGroup getGroup() {
		return this.TOGGLEBUTTONS;
	}

	public static void setTool(int i) {
		ControlPanel.tool = i;
	}

	public static void setToolName(String s){
		ControlPanel.toolName = s;
	}

}
