package paintproject;

//Used this for draw function: https://stackoverflow.com/questions/43429251/how-to-draw-a-continuous-line-with-mouse-on-javafx-canvas
//Used this for help menu dialog box: https://stackoverflow.com/questions/22166610/how-to-create-a-popup-windows-in-javafx
//Used this for getExtension function: https://www.baeldung.com/java-file-extension
//Used this for part of select tool: https://stackoverflow.com/questions/30993681/how-to-make-a-javafx-image-crop-app/30994313
//Also used it^^ for figuring out the previews for (most of) the shapes.

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import static javafx.scene.paint.Color.BLACK;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {
	private static final ProgressIndicator autoSaveProgress = new ProgressIndicator();
	private static final TabPane tabPane = new TabPane(new PaintTab());
	private static final ControlPanel controls = new ControlPanel();
        private static final Timer loggerTimer = new Timer();
	private static final Timer autoSaveTimer = new Timer();
	
	public static PaintTab getCurrentTab(){
		return (PaintTab) tabPane.getSelectionModel().getSelectedItem();
	}
	
	public static ProgressIndicator getAutoSaveProgress(){
		return autoSaveProgress;
	}
	
        public static Timer getLoggerTimer() {
		return loggerTimer;
        }	
	
	public static Timer getAutoSaveTimer() {
		return autoSaveTimer;
	}
        
	public static ControlPanel getControls() {
		return controls;
	}

	@Override
	public void start(Stage stage) {
		Canvas canv = getCurrentTab().getCanvas(); //defining important objects.
		autoSaveProgress.setProgress(0);
		HelpMenu helpMenu = new HelpMenu();
		ToolsMenu toolsMenu = new ToolsMenu();
		Menu filemenu = getCurrentTab().getFileMenuHandler();
		FileChooser saveImageAs = FileMenu.getSaveChooser();
		FileChooser openImage = FileMenu.getOpenChooser();
		getCurrentTab().getMisc().addToUndoHistory();
	

		VBox root = new VBox();
		HBox mainHbox = new HBox();

                Scene scene = new Scene(root, 1150, 700);

		MenuBar menuBar = new MenuBar();
		menuBar.setTranslateX(3);
		menuBar.setTranslateY(3);
		menuBar.getMenus().addAll(filemenu, helpMenu, toolsMenu); //This will be extended to include all of the menus that are constructed.       
		
                TimerTask loggerTask = new TimerTask() {
			@Override
			public void run() {
				System.out.println("#####LOG INFO#####");
				System.out.println("Selected Tool: " + controls.getToolName());
				String fileName = "";
				if(getCurrentTab() != null && getCurrentTab().getInitializedFile() != null){
					fileName = getCurrentTab().getInitializedFile().getName();
				}
				else{
					fileName = "Untitled";
				}
				System.out.println("File Name: " + fileName);
				System.out.println("Date and Time: " + new Date());
                    }
                };
		
		TimerTask autoSaveTask = new TimerTask() {
			@Override
			public void run() {
				if (autoSaveProgress.getProgress() >= 1) {
					System.out.println("#####ATTEMPTING TO AUTOSAVE#####");
					if (getCurrentTab() != null && getCurrentTab().getInitializedFile() != null){
						Platform.runLater(new Runnable(){
							public void run() {
								System.out.println("Autosaving to " + getCurrentTab().getInitializedFile().getName());
								getCurrentTab().getFileMenuHandler().saveToFile(canv, getCurrentTab().getInitializedFile(), stage);
								autoSaveProgress.setProgress(0);
							}
						});
					}
					else{
						System.out.println("No file initialized. Did not save.");
						autoSaveProgress.setProgress(0);
					}	
				}				
				autoSaveProgress.setProgress(autoSaveProgress.getProgress() + (1.0 / 60.0));
			}
		};
		
                loggerTimer.schedule(loggerTask, 0, 60000);
                autoSaveTimer.schedule(autoSaveTask, 0, 1000);
		
		getCurrentTab().getFileMenuHandler().getNewFile().setOnAction((ActionEvent e) -> {
			if (getCurrentTab() != null && getCurrentTab().getFileMenuHandler().checkSaved() == false) {
				getCurrentTab().getFileMenuHandler().newSaveDialog(stage);
			} else {
				getCurrentTab().getFileMenuHandler().newDialog(stage);
			}
		});

		getCurrentTab().getFileMenuHandler().configureOpen(openImage);
		getCurrentTab().getFileMenuHandler().getOpen().setOnAction((ActionEvent e) -> { //This block of code handles opening the file.
			if (getCurrentTab() != null && getCurrentTab().getFileMenuHandler().checkSaved() == false) {
				getCurrentTab().getFileMenuHandler().openSaveDialog(stage);
			} else {
				File inputFile = openImage.showOpenDialog(stage);
				if (inputFile != null) {
					try {
						getCurrentTab().getFileMenuHandler().openFile(canv, inputFile, stage);
					} catch (Exception ex) {
						System.err.println(ex.toString());
					}
				}
			}
		});

		getCurrentTab().getFileMenuHandler().getSave().setOnAction((ActionEvent e) -> { //These next two blocks of code handle saving the file.
			if (getCurrentTab().getInitializedFile() != null) {
				getCurrentTab().getFileMenuHandler().saveToFile(canv, getCurrentTab().getInitializedFile(), stage);
			} else {
				getCurrentTab().getFileMenuHandler().saveToFile(canv, saveImageAs.showSaveDialog(stage), stage);
			}
		});
		getCurrentTab().getFileMenuHandler().configureSave(saveImageAs);
		getCurrentTab().getFileMenuHandler().getSaveAs().setOnAction((ActionEvent e) -> {
			try {
				File file = saveImageAs.showSaveDialog(stage);
				getCurrentTab().getFileMenuHandler().saveToFile(canv, file, stage);
			} catch (Exception ex) {
				System.err.println(ex.toString());
			}
		});

		getCurrentTab().getFileMenuHandler().getExit().setOnAction(e -> {
			if (getCurrentTab() != null && getCurrentTab().getFileMenuHandler().checkSaved() == false) {
				getCurrentTab().getFileMenuHandler().exitSaveDialog(stage); //Open save dialog if it hasn't been saved.
			} else {
				Platform.exit();
                                loggerTimer.cancel();
				autoSaveTimer.cancel();
			}
		}); //Simply close if it has been saved.

		helpMenu.getAbout().setOnAction((ActionEvent event) -> {
			helpMenu.aboutDialog(stage);
		});

		helpMenu.getReleaseNotes().setOnAction((ActionEvent event) -> {
			helpMenu.releaseNotes(stage);
		});

		helpMenu.getToolsHelp().setOnAction((ActionEvent event) -> {
			helpMenu.toolsHelp(stage);
		});

		toolsMenu.getResize().setOnAction((ActionEvent event) -> {
			toolsMenu.resizeDialog(stage);
		});
		
		toolsMenu.getNewTab().setOnAction((ActionEvent event) -> {
			tabPane.getTabs().add(new PaintTab());
			tabPane.getSelectionModel().selectNext();
			controls.getLineWidthPicker().getItems().set(0, "1 px");
			controls.getColorPicker().setValue(BLACK);
			getCurrentTab().getGc().setFill(BLACK);
			getCurrentTab().getGc().setStroke(BLACK);
			controls.setTool(0);
			controls.getGroup().selectToggle(null);
		});
		
		toolsMenu.getShowTimer().setOnAction((ActionEvent event) -> {
			if(root.getChildren().contains(autoSaveProgress)){
				root.getChildren().remove(autoSaveProgress);
			}
			else{
				root.getChildren().add(autoSaveProgress);
			}
		});

		controls.getColorPicker().setOnAction((ActionEvent t) -> { //Makes color picker work
			getCurrentTab().getGc().setStroke(controls.getColorPicker().getValue());
			getCurrentTab().getGc().setFill(controls.getColorPicker().getValue());
		});
		controls.getLineWidthPicker().setOnAction((Event t) -> { //Makes line width control work
			getCurrentTab().getGc().setLineWidth((double) Character.digit(controls.getLineWidthPicker().getValue().toString().charAt(0), 10));
		});

		//From here on are the initializations of the various tools when the buttons are pressed
		controls.getPencil().setOnAction((ActionEvent t) -> {
			controls.getDrawTool().initialize();
		});

		controls.getEraser().setOnAction((ActionEvent t) -> {
			controls.getEraserTool().initialize();
		});

		controls.getRectangle().setOnAction((ActionEvent t) -> {
			controls.getRectTool().initialize();
		});

		controls.getRoundRect().setOnAction((ActionEvent t) -> {
			controls.getRoundRectTool().initialize();
		});

		controls.getEllipse().setOnAction((ActionEvent t) -> {
			controls.getEllipseTool().initialize();
		});

		controls.getCircle().setOnAction((ActionEvent t) -> {
			controls.getCircleTool().initialize();
		});

		controls.getSquare().setOnAction((ActionEvent t) -> {
			controls.getSquareTool().initialize();
		});

		controls.getLine().setOnAction((ActionEvent t) -> {
			controls.getLineTool().initialize();
		});

		controls.getZoomIn().setOnAction(App.getCurrentTab().getZoom().getZoomInHandler());

		controls.getZoomOut().setOnAction(App.getCurrentTab().getZoom().getZoomOutHandler());

		controls.getUndo().setOnAction((ActionEvent t) -> {
			getCurrentTab().getMisc().undo();
			getCurrentTab().getFileMenuHandler().setSaved(false);
		});

		controls.getRedo().setOnAction((ActionEvent t) -> {
			getCurrentTab().getMisc().redo();
			getCurrentTab().getFileMenuHandler().setSaved(false);
		});

		controls.getDropper().setOnAction((ActionEvent t) -> {
			controls.getDropperTool().initDropper();
		});

		controls.getText().setOnAction((ActionEvent t) -> {
			controls.getTextTool().textButtonPressed(stage);
		});

		controls.getPolygon().setOnAction((ActionEvent t) -> {
			controls.getPolyTool().initialize();
		});

		controls.getSelect().setOnAction((ActionEvent t) -> {
			controls.getGroup().selectToggle(null);
			SelectTool.RubberBandSelection.initialize();
		});
		
		controls.getCrop().setOnAction((ActionEvent t) -> {
			controls.getGroup().selectToggle(null);
			CropTool.CropSelection.initialize();
		});

		//Keyboard Shortcut initializations from here on:
		KeyCodeCombination AltF = new KeyCodeCombination(KeyCode.F, KeyCombination.ALT_DOWN);
		scene.setOnKeyPressed((KeyEvent ke) -> {
			if (AltF.match(ke)) {
				filemenu.show();
			}
		});
		getCurrentTab().getFileMenuHandler().getSave().setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		helpMenu.getAbout().setAccelerator(new KeyCodeCombination(KeyCode.F1));
		getCurrentTab().getFileMenuHandler().getSaveAs().setAccelerator(new KeyCodeCombination(KeyCode.F12));

		stage.setOnCloseRequest((WindowEvent t) -> { //Ask to save when someone hits the X button without having saved.
			t.consume();
			if (getCurrentTab() != null && getCurrentTab().getFileMenuHandler().checkSaved() == false) {
				getCurrentTab().getFileMenuHandler().exitSaveDialog(stage);
			} else {
				Platform.exit();
                                loggerTimer.cancel();
				autoSaveTimer.cancel();
			}
		});
		
		mainHbox.getChildren().addAll(controls,tabPane);
		root.getChildren().addAll(menuBar, mainHbox);
		
		stage.setTitle("Pain(t) - Untitled");
		stage.setScene(scene);

		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
