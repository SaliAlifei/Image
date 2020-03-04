package algorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utilitaires.ImgUtil;

public class Main extends Application {

	public static void main(String[] args) throws IOException {
		
	// --------------- Convolution ----------------
	 
		File path = new File("/Users/Salimata/Documents/Fac/Cours/Semestre 6/Images/ImageL3-master/Test_Images/shapesGray.jpg");
		BufferedImage img = null;
		try {
			img = ImageIO.read(path);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		double [][] matConv = { {1, 0, -1},
							    {2, 0, -2}, 
							    {1, 0, -1} };
		ImgUtil.imshow(img);
		ImgUtil.imshow(ImgUtil.convolution(img, matConv));
	
		
	/* // --------------- OtsuMethod -------------------
		
		
		File path = new File("/Users/Salimata/Documents/Fac/Cours/Semestre 6/Images/ImageL3-master/Test_Images/shapesGray.jpg");
		BufferedImage img = null;
		try {
			img = ImageIO.read(path);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		// BufferedImage img = ImgUtil.chargerImage("1.jpg");
		int [] histogramme = ImgUtil.histogrammeEn255NivDeGris(img);
		ImgUtil.imshow(ImgUtil.histToImg(histogramme));
		
		int seuil = ImgUtil.otsuMethod(histogramme);
		ImgUtil.seuillage(img, seuil);
		ImgUtil.imshow(img);

		
	*/
	//	launch(args);
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
		double [][] matriceConv = { {1,0,-1}, {1,0,-1}, {1,0,-1} };
		
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
