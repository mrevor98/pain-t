package paintproject;

import java.io.File;
import java.io.FileInputStream;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.scene.control.Label;
import static javafx.scene.paint.Color.WHITE;

public class FileMenu extends Menu {

	private Menu file;
	private MenuItem newfile, open, save, saveAs, exit;
	private Boolean saved;
	private final static File PICS = java.nio.file.Paths.get(System.getProperty("user.home"), "Pictures").toFile(); //Finds Pictures directory cross-platform.
	private final static FileChooser SAVEIMAGEAS = new FileChooser();
	private final static FileChooser OPENIMAGE = new FileChooser();

	public FileMenu() {
		super("File");
		this.saved = true;
		this.newfile = new MenuItem("New");
		this.open = new MenuItem("Open Image");
		this.save = new MenuItem("Save");
		this.saveAs = new MenuItem("Save As");
		this.exit = new MenuItem("Exit");
		this.getItems().addAll(newfile, open, save, saveAs, exit);
	}

	public void newSaveDialog(Stage stage) { //Handles when someone tries to create a new file without saving.
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("Save?");
		VBox openVbox = new VBox(20);
		HBox buttons = new HBox(10);
		Button newButton = new Button("Create New File");
		Button saveButton = new Button("Save");
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction((ActionEvent e) -> {
			dialog.close();
		});
		newButton.setOnAction((ActionEvent e) -> {
			newDialog(stage);
			dialog.close();
		});
		saveButton.setOnAction((ActionEvent e) -> {
			if (App.getCurrentTab().getInitializedFile() != null) {
				saveToFile(App.getCurrentTab().getCanvas(), App.getCurrentTab().getInitializedFile(), stage);
				dialog.close();
			} else {
				saveToFile(App.getCurrentTab().getCanvas(), SAVEIMAGEAS.showSaveDialog(stage), stage);
				dialog.close();
			}
			newDialog(stage);
		}
		);
		buttons.getChildren().addAll(saveButton, newButton, cancelButton);
		openVbox.getChildren().addAll(new Text("Create a new image without saving?"), buttons);
		Scene dialogScene = new Scene(openVbox);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public void newDialog(Stage stage) { //Opens dialog box for creating a fresh canvas.
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("New Image");
		VBox vBox = new VBox(20);
		VBox dimensions = new VBox();
		HBox buttons = new HBox(10);
		HBox xDims = new HBox(20);
		HBox yDims = new HBox(20);
		Button createButton = new Button("Create new Image");
		Button cancelButton = new Button("Cancel");
		TextField xDim = new TextField("800");
		TextField yDim = new TextField("500");
		Label width = new Label("Width:");
		Label height = new Label("Height:");
		xDims.getChildren().addAll(width, xDim);
		yDims.getChildren().addAll(height, yDim);
		dimensions.getChildren().addAll(xDims, yDims);
		cancelButton.setOnAction((ActionEvent e) -> {
			dialog.close();
		});
		createButton.setOnAction((ActionEvent e) -> {
			App.getCurrentTab().setInitializedFile(null);
			stage.setTitle("Pain(t) - Untitled");
			App.getCurrentTab().setText("Untitled");
			App.getAutoSaveProgress().setProgress(0);
			App.getCurrentTab().getMainGroup().getChildren().remove(SelectTool.RubberBandSelection.getRect());
			int xCoord = Integer.parseInt(xDim.getText());
			int yCoord = Integer.parseInt(yDim.getText());
			App.getCurrentTab().getCanvas().setWidth(xCoord);
			App.getCurrentTab().getCanvas().setHeight(yCoord);
			App.getCurrentTab().getGc().clearRect(0, 0, xCoord, yCoord);
			App.getCurrentTab().getGc().setFill(WHITE);
			System.out.println(xCoord);
			System.out.println(yCoord);
			App.getCurrentTab().getGc().fillRect(0, 0, xCoord, yCoord);
			dialog.close();
		});
		buttons.getChildren().addAll(createButton, cancelButton);
		vBox.getChildren().addAll(new Text("Enter the dimensions:"), dimensions, buttons);
		Scene dialogScene = new Scene(vBox);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public void exitSaveDialog(Stage stage) { //Handles when someone tries to quit without saving.
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("Exit?");
		VBox exitVbox = new VBox(20);
		HBox buttons = new HBox(10);
		Button exitButton = new Button("Exit");
		Button saveButton = new Button("Save");
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction((ActionEvent e) -> {
			dialog.close();
		});
		exitButton.setOnAction((ActionEvent e) -> {
                        Platform.exit();
                        App.getLoggerTimer().cancel();
			App.getAutoSaveTimer().cancel();
		});
		saveButton.setOnAction((ActionEvent e) -> {
			if (App.getCurrentTab().getInitializedFile() != null) {
				saveToFile(App.getCurrentTab().getCanvas(), App.getCurrentTab().getInitializedFile(), stage);
				Platform.exit();
				App.getLoggerTimer().cancel();
				App.getAutoSaveTimer().cancel();
			} else {
				saveToFile(App.getCurrentTab().getCanvas(), SAVEIMAGEAS.showSaveDialog(stage), stage);
				Platform.exit();
				App.getLoggerTimer().cancel();
				App.getAutoSaveTimer().cancel();
			}
		});
		buttons.getChildren().addAll(saveButton, exitButton, cancelButton);
		exitVbox.getChildren().addAll(new Text("Exit without saving?"), buttons);
		Scene dialogScene = new Scene(exitVbox);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public void openSaveDialog(Stage stage) { //Handles when someone tries to open/load a new file without saving.
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("Save?");
		VBox openVbox = new VBox(20);
		HBox buttons = new HBox(10);
		Button openButton = new Button("Open New File");
		Button saveButton = new Button("Save");
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction((ActionEvent e) -> {
			dialog.close();
		});
		openButton.setOnAction((ActionEvent e) -> {
			openFile(App.getCurrentTab().getCanvas(), OPENIMAGE.showOpenDialog(stage), stage);
			dialog.close();
		});
		saveButton.setOnAction((ActionEvent e) -> {
			if (App.getCurrentTab().getInitializedFile() != null) {
				saveToFile(App.getCurrentTab().getCanvas(), App.getCurrentTab().getInitializedFile(), stage);
				dialog.close();
			} else {
				saveToFile(App.getCurrentTab().getCanvas(), SAVEIMAGEAS.showSaveDialog(stage), stage);
				dialog.close();
			}
			openFile(App.getCurrentTab().getCanvas(), OPENIMAGE.showOpenDialog(stage), stage);
		}
		);
		buttons.getChildren().addAll(saveButton, openButton, cancelButton);
		openVbox.getChildren().addAll(new Text("Open a new file without saving?"), buttons);
		Scene dialogScene = new Scene(openVbox);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public void openFile(Canvas canv, File file, Stage stage) {
		try {
			/* First it takes the input file and gets the path/URL (String) from it,
			 * Next it converts that path/URL (String) to a FileInputStream object.
			 * Finally it constructs an Image object from that, and draws it.
			 */
			saved = true;
			Image img = new Image(new FileInputStream(file.getAbsolutePath()));
			canv.setWidth(img.getWidth());
			canv.setHeight(img.getHeight());
			App.getCurrentTab().getMisc().addToUndoHistory();
			GraphicsContext canvImg = canv.getGraphicsContext2D();
			App.getCurrentTab().setInitializedFile(file);
			canvImg.drawImage(img, 0, 0, img.getWidth(), img.getHeight());
			App.getCurrentTab().setGc(canvImg);
			stage.setTitle("Pain(t) - " + file.getName());
			App.getCurrentTab().setText(file.getName());
			App.getAutoSaveProgress().setProgress(0);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
	}

	public void saveToFile(Canvas canv, File file, Stage stage) { //Simple enough - handles the saving, sets the initialized file variable.
		if (file != null) {
			try {
				String format = App.getCurrentTab().getMisc().getExtension(file.getName()).get();
				ImageIO.write(SwingFXUtils.fromFXImage(canv.snapshot(new SnapshotParameters(), null), null), format, file);
				App.getCurrentTab().setInitializedFile(file);
				saved = true;
				App.getAutoSaveProgress().setProgress(0);
				stage.setTitle("Pain(t) - " + file.getName());
				App.getCurrentTab().setText(file.getName());
			} catch (Exception ex) {
				System.err.println(ex.toString());
			}
		}
	}

	public void configureOpen(FileChooser chooser) { //These two functions set up the file choosers.
		chooser.setTitle("Open Image");
		chooser.setInitialDirectory(PICS);
		chooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("PNG", "*.png"),
			new FileChooser.ExtensionFilter("JPG", "*.jpg"),
			new FileChooser.ExtensionFilter("BMP", "*.bmp"),
			new FileChooser.ExtensionFilter("GIF", "*.gif"),
			new FileChooser.ExtensionFilter("All Files", "*.*"));
	}

	public void configureSave(FileChooser chooser) {
		chooser.setTitle("Save As");
		chooser.setInitialDirectory(PICS);
		chooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("PNG", "*.png"),
			new FileChooser.ExtensionFilter("JPG", "*.jpg"),
			new FileChooser.ExtensionFilter("BMP", "*.bmp"),
			new FileChooser.ExtensionFilter("GIF", "*.gif"),
			new FileChooser.ExtensionFilter("All Files", "*.*"));
	}

	public void setSaved(Boolean bool) { //Set to false when things are changed, set to true on save.
		this.saved = bool;
	}

	public Menu getFile() { //A bunch of accessor functions
		return this.file;
	}

	public MenuItem getNewFile() {
		return this.newfile;
	}

	public MenuItem getOpen() {
		return this.open;
	}

	public MenuItem getSave() {
		return this.save;
	}

	public MenuItem getSaveAs() {
		return this.saveAs;
	}

	public MenuItem getExit() {
		return this.exit;
	}

	public static File getPics() {
		return FileMenu.PICS;
	}

	public Boolean checkSaved() {
		return this.saved;
	}

	public static FileChooser getOpenChooser() {
		return FileMenu.OPENIMAGE;
	}

	public static FileChooser getSaveChooser() {
		return FileMenu.SAVEIMAGEAS;
	}

}
