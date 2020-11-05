package paintproject;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ToolsMenu extends Menu {

	private MenuItem resize, newTab, showTimer;

	public ToolsMenu(){
		super("Tools");
		resize = new MenuItem("Resize");
		newTab = new MenuItem("New Tab");
		showTimer = new MenuItem("Show Autosave Timer");
		this.getItems().addAll(resize, newTab, showTimer);
	}

	
	public MenuItem getNewTab(){
		return newTab;
	}
	
	public MenuItem getResize() {
		return resize;
	}
	
	public MenuItem getShowTimer(){
		return showTimer;
	}
	
	public void resizeDialog(Stage stage) { //Oopens dialog box for creating a fresh canvas.
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("Resize");
		VBox vBox = new VBox(20);
		VBox dimensions = new VBox();
		HBox buttons = new HBox(10);
		HBox xDims = new HBox(20);
		HBox yDims = new HBox(20);
		Button createButton = new Button("Resize Image");
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
			int xCoord = Integer.parseInt(xDim.getText());
			int yCoord = Integer.parseInt(yDim.getText());
			App.getCurrentTab().getMisc().resizeCanvas(xCoord, yCoord);
			dialog.close();
		});
		buttons.getChildren().addAll(createButton, cancelButton);
		vBox.getChildren().addAll(new Text("Enter the dimensions:"), dimensions, buttons);
		Scene dialogScene = new Scene(vBox);
		dialog.setScene(dialogScene);
		dialog.show();
	}

}
