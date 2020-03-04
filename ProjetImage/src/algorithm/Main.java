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
	
	public static int algorithm(BufferedImage img, boolean afficheImages) throws IOException{
		
		BufferedImage imgNivGris = ImgUtil.duplicate(img);
		BufferedImage imgHistProj = ImgUtil.duplicate(img);
		BufferedImage imgSeuillage = ImgUtil.duplicate(img);
		int nbreDeMarche = 0;
		int seuil = 0;
		int largeur = 5;
		
		// niveau de gris
		ImgUtil.enNiveauDeGris(img);
		ImgUtil.enNiveauDeGris(imgNivGris);

		// regler le contraste / Addition
		
		
		// recherche d'un bon seuil / Methode Otsu
		seuil = ImgUtil.otsuMethod(ImgUtil.histogrammeEn255NivDeGris(img));
		
		// Seuillage
		ImgUtil.seuillage(img, seuil);						
		ImgUtil.seuillage(imgSeuillage, seuil);						

		// Reduction du bruit / Convolution / Gaussian blur (taille du noyau a determiner)
		
		
		// Recherche de contours / Convolution / Sobel Operator
			// ImgUtil.convolution(imgBuff, matriceConv);
		
		// remplissage (correction de l'image)		
		
		// compter le nombre de composantes connexes
		nbreDeMarche = ImgUtil.nbLignes(img,largeur,100,img.getWidth()/3);
	
		
		if(afficheImages) {
			ImgUtil.imshow(ImgUtil.histogrammeProjection(imgHistProj, ImgUtil.histProj(imgHistProj)));
			ImgUtil.imshow(imgSeuillage);
			ImgUtil.imshow(imgNivGris);
		}
		
		return nbreDeMarche;
	}
	
}
