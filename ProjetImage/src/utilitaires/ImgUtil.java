package utilitaires;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class ImgUtil {
	
	/**
	 * Retourne un bufferedImage correspondant au nom de l'imae passe en parametre. L'image devra se trouver dans le dossier test_images_escaliers
	 * @param nom
	 * @return img
	 */
	public static BufferedImage chargerImage(String nom) {
		File path = new File(System.getProperty("user.dir")+"/test_images_escaliers/"+nom);
		BufferedImage img = null;
		try {
			img = ImageIO.read(path);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	/*
	 * Affiche l'image entree en parametre dans une nouvelle fenetre
	 * 
	 * params : BufferedImage
	 * 
	 */
	public static void imshow(BufferedImage img) throws IOException {
	      //Instantiate JFrame 
	      JFrame frame = new JFrame(); 
	      //Set Content to the JFrame 
	      frame.getContentPane().add(new JLabel(new ImageIcon(img))); 
	      frame.pack(); 
	      frame.setVisible(true);
	      // operation lorsque l'utilisateur ferme la fenetre
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 }

	/*
	 * Effectue un seuillage : les pixels avec un niveau de gris inferieur au seuil sont mis a 0 et le reste a 255
	 * 
	 * params : BufferedImage, int
	 * 
	 */
	public static void seuillage(BufferedImage img, int seuil) {
		int[] noir = {0,0,0,255};
		int[] blanc = {255,255,255,255};
		for (int h=0; h<img.getHeight(); h++) {
			for (int w=0; w<img.getWidth(); w++) {
				
				if ((img.getRGB(w, h)&0xff) <= seuil)
					img.getRaster().setPixel(w, h, noir);
				else 
					img.getRaster().setPixel(w, h, blanc);
			}
		}
	}
	
	/*
	 * Retourne un tableau d'entiers. A chaque indice i du tableau (0 a 255), est lie 
	 * le nombre de pixel possedant le niveau de gris i
	 * 
	 * params : BufferedImage
	 * return : int []
	 */
	public static int [] histogrammeEn255NivDeGris (BufferedImage img) {
		 int [] histo = new int [256];
		 // Initialisation du tableau
		 for (int i=0; i<256; i++) histo[i] = 0;
		 
		 for (int h=0; h<img.getHeight(); h++) {
			for (int w=0; w<img.getWidth(); w++) {
				int g = img.getRGB(w,h)&0xff;
				histo[g]++;
			}
		 }
		return histo;
	}
	
	/**
	 * Transforme l'image RGB en niveau de gris
	 * @param imageEntree
	 */
	public static void enNiveauDeGris(BufferedImage imageEntree) {
		for (int h=0; h<imageEntree.getHeight(); h++) {
			for (int w=0; w<imageEntree.getWidth(); w++) {
				
				int rgb = imageEntree.getRGB(w, h);
				int r = (rgb>>16)&0xff;
				int g = (rgb>>8)&0xff;
				int b = rgb&0xff;
				int moy = (r+g+b)/3;
				int [] gris = { moy, moy, moy, 255};
				imageEntree.getRaster().setPixel(w, h, gris);
			}
		}
	}
	
	/**
	 * Transforme l'image RGB en son image negative
	 * @param img
	 */
	public static void enImageNegative(BufferedImage img) {
		for (int h=0; h<img.getHeight(); h++) {
			for (int w=0; w<img.getWidth(); w++) {
				
				int rgb = img.getRGB(w, h);
				int r = (rgb>>16)&0xff;
				int g = (rgb>>8)&0xff;
				int b = rgb&0xff;
				int rgbN = ((255-r)*65536)+((255-g)*256)+(255-b);
				img.setRGB(w, h, rgbN);
			
			}
		}
	}

}
