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
	 * Retourne un bufferedImage correspondant au nom de l'image passe en parametre. L'image devra se trouver dans le dossier test_images_escaliers
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
	
	public static BufferedImage convolution (BufferedImage img, int[][] matConv) throws IOException  {
		BufferedImage imgConv = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		if (matConv[0].length != matConv.length) throw new RuntimeException("La matrice de convolution doit etre carree");
		int nConv = matConv[0].length;
		int marge = ((int) (nConv/2));
				
		// creer un nv bufferedImage pour gerer les contours
		BufferedImage imgPlusContours = new BufferedImage(img.getWidth()+(2*marge), img.getHeight()+(2*marge), BufferedImage.TYPE_BYTE_GRAY);
		for (int w=0; w<img.getWidth(); w++) {
			for(int h=0; h<img.getHeight(); h++) {
				int rgb = img.getRGB(w, h)&0xff;
				int [] color = {rgb,rgb,rgb};
				imgPlusContours.getRaster().setPixel(w+marge, h+marge, color);
			}
		}
		
		// concolution sur la nouvelle image
		
		for (int w=0; w<img.getWidth(); w++) {
			for(int h=0; h<img.getHeight(); h++) {
				int colr = multiplicationIndv(getMatrice(imgPlusContours, w+marge, h+marge, nConv), matConv);
				int [] color = {colr,colr,colr};
				imgConv.getRaster().setPixel(w, h, color);
			}
		}
		return imgConv;
	}
	
	/**
	 * Retourne une matrice de taille "taille x taille" avec le point (x,y) au milieu
	 * @param img
	 * @param x
	 * @param y
	 * @param taille
	 * @return
	 */
	public static int[][] getMatrice(BufferedImage img, int x, int y, int taille) {
		if(taille%2 == 0) throw new RuntimeException("Taille doit etre impaire");
		int[][] mat = new int [taille][taille];
		int [] depart = { x- ((int) (taille/2)) , y- ((int) (taille/2)) };
				
		for (int i=0; i<taille; i++) {
			for (int j=0; j<taille; j++) {
				int w = depart[0] + i;
				int h = depart[1] + j;
				mat[i][j] = img.getRGB(w, h)&0xff;
			}
		}
		return mat;
	}
	
	/**
	 * Multiplication terme a terme puis somme
	 * @param matrice1
	 * @param matrice2
	 * @return
	 */
	public static int multiplicationIndv(int [][] matrice1, int [][] matrice2) {
		if ((matrice1.length != matrice2.length) && (matrice1[0].length != matrice2[0].length)) throw new RuntimeException("Matrice non carree"); 
		int somme = 0;
		
		for (int row = 0; row < matrice1.length; row++) {
            for (int col = 0; col < matrice2[0].length; col++) {
            	somme += matrice1[row][col] * matrice2[row][col];
            }
        }
		return somme;
	}
	
	/**
	 * Affiche la matrice entree dans la console
	 * @param prod
	 */
	public static void afficheMatConsole (int[][] prod) {
        for (int row = 0; row < prod.length; row++) {
            for (int col = 0; col < prod[row].length; col++) {
                System.out.print(prod[row][col] + " ");
            }
            System.out.println();
        }
	}
}
