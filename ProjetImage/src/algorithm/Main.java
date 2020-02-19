package algorithm;

import java.awt.image.BufferedImage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) {
		stage.setTitle("NumberOfSteps");
		Pane pane = new PrincipalPane();
		
		Scene scene = new Scene(pane, 800, 700);
		
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public static int algorithm(BufferedImage imgBuff) {
		int nbreDeMarche = 0;
		
		return nbreDeMarche;
	}
}
