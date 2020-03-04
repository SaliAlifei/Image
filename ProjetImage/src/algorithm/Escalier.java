package imgProcsJava;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Escalier {
	public static void main(String[] args) {

		File path = new File("C:/Users/thaly/Desktop/FAC/S6/image/escaliers/1.jpg");

		//niveau de gris
		BufferedImage img = null;

		try {
			img = ImageIO.read(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// nv de gris
		UtilImage.colorVGris(img);
		try {
			UtilImage.imshow(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedImage img2 = null;

		try {
			img2 = ImageIO.read(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//nv gris
		UtilImage.colorVGris(img2);
		// calcul le seuil avec otsu
		int seuil =UtilImage.otsuMethod(UtilImage.hist(img2));

		//binarise avec le seuil calculé
		UtilImage.seuillage(img2, 110);
		try {
			UtilImage.imshow(img2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// calcul l'histogramme de projection
		BufferedImage hist = UtilImage.histogrammeProjection(img2, UtilImage.histProj(img2));
		
		try {
			UtilImage.imshow(hist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* compte le nombre de marche 
		 * a partir de l'histogramme de projection on regarde si les pixels sont blanc jusqu'au tier d el'image si oui on les garde sinon on met a 0 (noir)
		 * on compte le nombre de bandes blanches
		 */
		int nbLignes = UtilImage.nbLignes(img2,5,100,img2.getWidth()/3);
		System.out.println(nbLignes);
	}

}
