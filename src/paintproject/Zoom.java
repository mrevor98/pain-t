package paintproject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Zoom {

	private EventHandler<ActionEvent> zoomInClick;
	private EventHandler<ActionEvent> zoomOutClick;

	public Zoom() {
		zoomInClick = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				zoomInPressed();
			}
		};
		zoomOutClick = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				zoomOutPressed();
			}
		};
	}

	private void zoomInPressed() { //Simple enough - sets the scale accordingly. 
		double zoomFactor = 1.15; //Rather low zoom factor in order to keep it from being too wild.
		App.getCurrentTab().getCanvasPane().setScaleX(App.getCurrentTab().getCanvasPane().getScaleX() * zoomFactor);
		App.getCurrentTab().getCanvasPane().setScaleY(App.getCurrentTab().getCanvasPane().getScaleY() * zoomFactor);
	}

	public EventHandler<ActionEvent> getZoomInHandler() {
		return this.zoomInClick;
	}

	private void zoomOutPressed() {
		double zoomFactor = 1.15;
		App.getCurrentTab().getCanvasPane().setScaleX(App.getCurrentTab().getCanvasPane().getScaleX() / zoomFactor); //Almost the same as the zoom in, but we divide here, since we're zooming out.
		App.getCurrentTab().getCanvasPane().setScaleY(App.getCurrentTab().getCanvasPane().getScaleY() / zoomFactor);
	}

	public EventHandler<ActionEvent> getZoomOutHandler() {
		return this.zoomOutClick;
	}

}
