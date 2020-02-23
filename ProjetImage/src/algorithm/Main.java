package algorithm;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utilitaires.ImgUtil;

public class Main extends Application {

	public static void main(String[] args) throws IOException {
		launch(args);
	}
	
	public void start(Stage stage) throws IOException {
		stage.setTitle("NumberOfSteps");
		Pane pane = new PrincipalPane();
		
		Scene scene = new Scene(pane, 800, 700);
		
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
	
	public static int algorithm(BufferedImage imgBuff) throws IOException{

		int nbreDeMarche = 0;
		int seuil = 140;
		int [][] matriceConv = { {1,0,-1}, {1,0,-1}, {1,0,-1} };
		
		// niveau de gris
		ImgUtil.enNiveauDeGris(imgBuff);
		
		// regler le contraste / Addition
		
		
		// recherche d'un bon seuil / Methode Otsu
		seuil = ImgUtil.otsuMethod(ImgUtil.histogrammeEn255NivDeGris(imgBuff));
		
		// Reduction du bruit / Convolution / Gaussian blur (taille du noyau a determiner)
		
		
		// Recherche de contours / Convolution / Sobel Operator
		ImgUtil.convolution(imgBuff, matriceConv);
		
		// remplissage (correction de l'image)

		
		// seuillage
		ImgUtil.seuillage(imgBuff, seuil);
		
		// remplissage (correction de l'image)
		
		
		// compter le nombre de composantes connexes
		
		
		return nbreDeMarche;
	}
}
