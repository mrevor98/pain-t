package paintproject;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpMenu extends Menu {

	private MenuItem about, releaseNotes, tools;
	public HelpMenu(){
		super("Help");
		this.about = new MenuItem("About");
		this.releaseNotes = new MenuItem("Release Notes");
		this.tools = new MenuItem("About the Tools");
		this.getItems().addAll(about, releaseNotes, tools);
	}
	
	public void aboutDialog(Stage stage) { //Simple enough - makes a help menu actually pop up.
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("About Pain(t)");
		VBox aboutVbox = new VBox(20);
		aboutVbox.getChildren().add(new Text("Welcome to Pain v0.4! \nUse the \"File\""
			+ "menu to open an image to draw\n"
			+ "on. \n"
			+ "Otherwise, you can start drawing \n"
			+ "On the blank canvas \n"
			+ "with the various drawing tools.\n"
			+ "Save is on the \"File\" menu."));
		Scene dialogScene = new Scene(aboutVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public void releaseNotes(Stage stage) { //Shows release notes in tabs
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("Release notes");
		TabPane mainTabPane = new TabPane();
		mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		Tab vOneTab = new Tab("v0.1");
		Tab vTwoTab = new Tab("v0.2");
		Tab vThreeTab = new Tab("v0.3");
		Tab vFourTab = new Tab("v0.4");
		ScrollPane vOnePane = new ScrollPane();
		ScrollPane vTwoPane = new ScrollPane();
		ScrollPane vThreePane = new ScrollPane();
		ScrollPane vFourPane = new ScrollPane();
		vOnePane.setContent(new Text("Paint Release Notes v0.1 \n"
			+ "\n"
			+ "-Added menu bar.\n"
			+ "-Added a menu option to open and display a chosen image.\n"
			+ "-Added a menu option to exit smoothly.\n"
			+ "\n"
			+ "Known Issues:\n"
			+ "-Will not work properly if an attempt is made to open a file that is not an image.\n"
			+ "-Save function seems not to work properly (Has worked on one of my computers, and not on the other).\n"
			+ "\n"
			+ "Expected next features:\n"
			+ "-Fix save function\n"
			+ "-Include 'save as' function\n"
			+ "-Make it so there is an initial canvas.\n"
			+ "\n"
			+ "Links:\n"
			+ "Demo - https://youtu.be/hSK6DtkcSBI"));
		vTwoPane.setContent(new Text("Paint Release Notes v0.2\n"
			+ "\n"
			+ "-Added Save As function.\n"
			+ "-Added ability to draw.\n"
			+ "-Added (functional) color picker and width control for drawing.\n"
			+ "-Added initial canvas.\n"
			+ "-Added help menu and 'about' dialog.\n"
			+ "\n"
			+ "Known Issues:\n"
			+ "-Will not work properly if an attempt is made to open a file that is not an image.\n"
			+ "\n"
			+ "Expected next features:\n"
			+ "-Include ability to draw straight line.\n"
			+ "-Include ability to draw rectangle.\n"
			+ "\n"
			+ "Links:\n"
			+ "Demo - https://youtu.be/hBF9qBwOuWc"));
		vThreePane.setContent(new Text("Paint Release Notes v0.3\n"
			+ "\n"
			+ "-Added Line, Rectangle, Ellipse, Square, and Circle tools, with fill\n"
			+ "-Added \"Smart\" save (prompts user when new/open/exit without saving)\n"
			+ "-Supports JPG, PNG, GIF, and BMP files now\n"
			+ "-Added toolbar to the side to select which tool is used to draw\n"
			+ "-Added color grabber tool\n"
			+ "-Added resize function\n"
			+ "-Added zoom in/out functions\n"
			+ "-Added undo function\n"
			+ "\n"
			+ "Known Issues:\n"
			+ "-Resize doesn't scale image to new canvas size\n"
			+ "-Severe color distortion when saving as jpg\n"
			+ "-Undo changes \"transparent\" color to \"white\" color\n"
			+ "\n"
			+ "Expected next features:\n"
			+ "-Redo function\n"
			+ "-Select and drag tool\n"
			+ "-Text addition tool\n"
			+ "-\"Previews\" on drawing tools\n"
			+ "-Extended help menu\n"
			+ "-Eraser tool\n"
			+ "\n"
			+ "Links:\n"
			+ "Demo - https://youtu.be/Lv7m4r96HII"));
		vFourPane.setContent(new Text("Paint Release Notes v0.4\n"
			+ "\n"
			+ "-Added text tool\n"
			+ "-Added rounded rectangle tool\n"
			+ "-Added eraser tool\n"
			+ "-Added redo button\n"
			+ "-Added \"preview animations\" for drawing tools\n"
			+ "-Added release notes and tools to help menu\n"
			+ "-Added select and drag tool\n"
			+ "-Added regular polygon tool\n"
			+ "\n"
			+ "Known Issues:\n"
			+ "\n"
			+ "Expected next features:\n"
			+ "-Autosave\n"
			+ "-Javadoc\n"
			+ "-Save in file format other than original\n"
			+ "\n"
			+ "Links:\n"
			+ "Demo - https://youtu.be/Kx4Ej-1q9yA"));
		vOneTab.setContent(vOnePane);
		vTwoTab.setContent(vTwoPane);
		vThreeTab.setContent(vThreePane);
		vFourTab.setContent(vFourPane);
		mainTabPane.getTabs().addAll(vOneTab, vTwoTab, vThreeTab, vFourTab);
		Scene dialogScene = new Scene(mainTabPane, 600, 600);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public void toolsHelp(Stage stage) {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("About the Tools");
		ScrollPane aboutPane = new ScrollPane();
		aboutPane.setContent(new Text("Pencil: Draws a continuous line by dragging the cursor\n"
			+ "Eraser: Erases anything drawn on the canvas\n"
			+ "Line: Draws a straight line by dragging the cursor\n"
			+ "Fill: Toggles whether shapes are filled\n"
			+ "Dropper: Used to grab a color from the canvas\n"
			+ "Shape Tools: Draws the described shape by dragging the cursor\n"
			+ "Select: Selects a rectangular region, can then be dragged and moved\n"
			+ "Polygon: Draws a regular polygon with the given number of sides"));
		Scene dialogScene = new Scene(aboutPane, 475, 250);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public MenuItem getReleaseNotes() {
		return this.releaseNotes;
	}

	public MenuItem getToolsHelp() {
		return this.tools;
	}

	public MenuItem getAbout() {
		return this.about;
	}

}
