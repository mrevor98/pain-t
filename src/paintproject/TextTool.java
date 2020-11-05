package paintproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextTool {

	private String text;
	private double x;
	private double y;

	public TextTool() {
		this.text = "";
		this.x = 0;
		this.y = 0;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public String getText() {
		return this.text;
	}

	private void setX(double x) {
		this.x = x;
	}

	private void setY(double y) {
		this.y = y;
	}

	private static final EventHandler<MouseEvent> CLICK = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			App.getControls().getTextTool().setX(event.getX());
			App.getControls().getTextTool().setY(event.getY());
			App.getControls().getTextTool().drawText();
			App.getCurrentTab().getCanvas().setOnMousePressed(null);
			App.getControls().setToolName("None");
			App.getControls().setTool(0);

		}
	};

	private void drawText() {
		try {
			App.getCurrentTab().getMisc().addToUndoHistory();
		} catch (Exception ex) {
			System.err.println(ex);
		}
		App.getCurrentTab().getFileMenuHandler().setSaved(false);
		App.getCurrentTab().getGc().fillText(this.text, this.x, this.y);
	}

	public void textButtonPressed(Stage stage) {
		App.getControls().setToolName("Text");
		App.getControls().getGroup().selectToggle(null);
		App.getCurrentTab().getMainGroup().setOnMousePressed(null);
		App.getCurrentTab().getMainGroup().setOnMouseReleased(null);
		App.getCurrentTab().getMainGroup().setOnMouseDragged(null);
		App.getCurrentTab().getCanvas().setOnMousePressed(null);
		App.getCurrentTab().getCanvas().setOnMouseReleased(null);
		App.getCurrentTab().getCanvas().setOnMouseDragged(null);
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("Text Entry");
		VBox vBox = new VBox(20);
		HBox buttons = new HBox(10);
		HBox Hbox = new HBox(20);
		Button continueButton = new Button("Continue");
		Button cancelButton = new Button("Cancel");
		TextField textField = new TextField("");
		Label textLabel = new Label("Text:");
		Hbox.getChildren().addAll(textLabel, textField);
		cancelButton.setOnAction((ActionEvent e) -> {
			dialog.close();
		});
		continueButton.setOnAction((ActionEvent e) -> {
			this.text = textField.getText();
			dialog.close();
		});
		buttons.getChildren().addAll(continueButton, cancelButton);
		vBox.getChildren().addAll(new Text("Enter the text:"),
			Hbox,
			new Text("Click the top left corner of where you want your text"),
			buttons);
		Scene dialogScene = new Scene(vBox);
		dialog.setScene(dialogScene);
		dialog.show();
		App.getCurrentTab().getCanvas().setOnMousePressed(CLICK);
	}
}
