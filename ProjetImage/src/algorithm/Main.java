package algorithm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

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
	
	public static int algorithm(BufferedImage img, boolean afficheImages) throws IOException{
		
		BufferedImage imgNivGris = ImgUtil.duplicate(img);
		BufferedImage imgHistProj = ImgUtil.duplicate(img);
		BufferedImage imgSeuillage = ImgUtil.duplicate(img);
		int nbreDeMarche = 0;
		int seuil = 0;
		int largeur = 5;
		
		
		// Niveau de gris
		
		ImgUtil.enNiveauDeGris(img);
		ImgUtil.enNiveauDeGris(imgNivGris);
		ImgUtil.enNiveauDeGris(imgSeuillage);

		// Regler le contraste / Addition
		
		
		// Seuillage / Methode Otsu
		
		img = ImgUtil.otsuMethod4(img);
		imgSeuillage = ImgUtil.otsuMethod4(imgSeuillage);


		// Reduction du bruit / Convolution / Gaussian blur (taille du noyau a determiner)
		
		
		
		// Recherche de contours / Convolution / Sobel Operator
			// ImgUtil.convolution(imgBuff, matriceConv);
		
		// remplissage (correction de l'image)		
		
		//fermeture
		BufferedImage imgOuverture = ImgUtil.open(nvImgConv, 2);
		
		//ouverture
		BufferedImage imgFermeture = ImgUtil.close(imgOuverture, 3);
		
		// compter le nombre de composantes connexes
		nbreDeMarche = ImgUtil.nbLignes(imgFermeture,largeur,1000,img.getWidth()/4);
	
		
		if(afficheImages) {
			ImgUtil.imshow(ImgUtil.histogrammeProjection(imgFermeture, ImgUtil.histProj(imgFermeture)));
			ImgUtil.imshow(imgSeuillage);
			ImgUtil.imshow(imgNivGris);
			ImgUtil.imshow(imgFermeture);
			ImgUtil.imshow(imgOuverture);
		}
		
		return nbreDeMarche;
	}
	
}
