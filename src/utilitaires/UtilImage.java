package utilitaires;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class UtilImage {

	/**
	 * affiche imahe
	 * @param image
	 * @throws IOException
	 */
	public static void imshow(BufferedImage image) throws IOException {
		//Instantiate JFrame 
		JFrame frame = new JFrame(); 

		//Set Content to the JFrame 
		frame.getContentPane().add(new JLabel(new ImageIcon(image))); 
		frame.pack(); 
		frame.setVisible(true);
	}
	/**
	 * dessine une ligne courbe
	 * @param img
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param c
	 * @return
	 */
	static BufferedImage bresenham(BufferedImage img, int x1, int y1, int x2, int y2, Color c) 
	{ 
		int m_new = 2 * (y2 - y1); 
		int slope_error_new = m_new - (x2 - x1); 

		for (int x = x1, y = y1; x <= x2; x++) 
		{ 
			//System.out.print("(" +x + "," + y + ")\n"); 
			img.setRGB(x, y, c.getRGB());
			// Add slope to increment angle formed 
			slope_error_new += m_new; 

			// Slope error reached limit, time to 
			// increment y and update slope error. 
			if (slope_error_new >= 0) 
			{ 
				y++; 
				slope_error_new -= 2 * (x2 - x1); 
			} 
		} 
		return img;
	}

	/**
	 * dessine un rectangle
	 * @param img
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param c
	 * @return
	 */
	static BufferedImage drawRect(BufferedImage img, int x1, int y1, int x2, int y2, Color c) 
	{ 
		int width  = img.getWidth();
		int height = img.getHeight();

		if (x1 > height && x2 > height) {
			throw new java.lang.Error("x1 ou x2 sont superieur a la hauteur de l'image");
		}

		if (y1 > width && y2 > width) {
			throw new java.lang.Error("y1 ou y2 sont superieur a la largeur de l'image");
		}

		for (int x = x1; x < x2; x++) {
			for (int y = y1; y < y2 ; y++) {
				img.setRGB(x, y, c.getRGB());
			}
		}

		return img;
	}

	/**
	 * dessine un carre plein
	 * @param img
	 * @param x1
	 * @param y1
	 * @param size
	 * @param c
	 * @return
	 */
	static BufferedImage drawCube(BufferedImage img, int x1, int y1, int size, Color c) 
	{ 
		int width  = img.getWidth();
		int height = img.getHeight();

		if (x1 > width && x1+size > width) {
			throw new java.lang.Error("x1 ou x2 sont superieur a la largeur de l'image");
		}

		if (y1 > height && y1+size > height) {
			throw new java.lang.Error("y1 ou y2 sont superieur a la hauteur de l'image");
		}
		if (x1 <0 ) {
			throw new java.lang.Error("x1 trop petit");
		}

		if (y1 <0) {
			throw new java.lang.Error("y1 trop petit");
		}

		for (int x = x1; x < x1+size; x++) {
			for (int y = y1; y < y1+size ; y++) {
				img.setRGB(x, y, c.getRGB());
			}
		}

		return img;
	}
	
	/**
	 * retourne une image dont les �l�ment � partir d'un certain seuil sont color�e de la couleur c
	 * @param img
	 * @param c
	 * @return
	 */
	static BufferedImage coloreSeuil(BufferedImage img, Color c) {
		BufferedImage imgc = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < img.getHeight() ; y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x,y); // r�cup�ration des couleurs RGB du pixel a la position (x, y)
				int r = (p>>16)&0xff; 
				if (r==0)
					imgc.setRGB(x,y,c.getRGB());
				else
					imgc.setRGB(x,y,Color.white.getRGB());
			}
		}
		return imgc;
	}

	/**
	 * cr�e une image d�grad�e de gris
	 * @param height
	 * @param width
	 * @return
	 */
	static BufferedImage gris(int height, int width) {
		int[] g = {0,0,0,255};
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
		for (int y = 0; y < height ; y++){
			for (int x = 0; x < width; x++){
				img.getRaster().setPixel(x, y, g);
			}
			g[0]+=1;
			g[1]=g[0];
			g[2]=g[0];
		}
		return img;
	}

	/**
	 * binarise une image avec selon un seuil
	 * @param img
	 * @param seuil
	 * @return
	 */
	static BufferedImage seuillage(BufferedImage img, int seuil) {
		int[] noir = {0,0,0,255};
		int[] blanc = {255,255,255,255};
		for (int y = 0; y < img.getHeight() ; y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x,y); // r�cup�ration des couleurs RGB du pixel a la position (x, y)
				int r = (p>>16)&0xff; 
				if (r<seuil)
					img.getRaster().setPixel(x, y, noir);
				else
					img.getRaster().setPixel(x, y, blanc);
			}
		}
		return img;
	}

	/**
	 * binarise l'image avec un seuil min et un seuil max
	 * @param img
	 * @param seuilbas
	 * @param seuilhaut
	 * @return
	 */
	static BufferedImage seuillage2(BufferedImage img, int seuilbas, int seuilhaut) {
		int[] noir = {0,0,0,255};
		int[] blanc = {255,255,255,255};
		for (int y = 0; y < img.getHeight() ; y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x,y); // r�cup�ration des couleurs RGB du pixel a la position (x, y)
				int r = (p>>16)&0xff; 
				if (r<seuilbas || r>seuilhaut)
					img.getRaster().setPixel(x, y, blanc);
				else
					img.getRaster().setPixel(x, y, noir);
			}
		}
		return img;
	}

	/**
	 * retourne le max de deux nombre
	 * @param a
	 * @param b
	 * @return
	 */
	static int max(int a, int b) {
		int max =a;
		if (b>a)
			max=b;
		return max;
	}

	/**
	 * retourne le max d'un tableau
	 * @param tab
	 * @return
	 */
	static int maxTab(int[] tab) {
		int max=tab[0];
		for(int i=1;i<tab.length;i++) {
			if (tab[i]>max)
				max=tab[i];
		}
		return max;
	}

	/**
	 * retrourne un tableau avec l'histogramme d'un image
	 * @param img
	 * @return
	 */
	static int[] hist(BufferedImage img) {
		int tab[]=new int[256];
		for(int i=0;i<tab.length;i++) {
			tab[i]=0;
		}
		for (int y = 0; y < img.getHeight() ; y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x,y); // r�cup�ration des couleurs RGB du pixel a la position (x, y)
				int r = (p>>16)&0xff;
				tab[r]+=1;
			}
		}


		return tab;
	}


	/**
	
	/**
	 * transforme une image couleur en niveau de gris
	 * @param img
	 * @return
	 */
	static BufferedImage colorVGris(BufferedImage img) {
		int[] c = {255,255,255,255};
		for (int y = 0; y < img.getHeight() ; y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x,y); // r�cup�ration des couleurs RGB du pixel a la position (x, y)

				int r = (p>>16)&0xff; 
				int g = (p>>8)&0xff; 
				int b = p&0xff;
				c[0]= (r+g+b)/3;
				c[1]=c[0];
				c[2]=c[0];
				img.getRaster().setPixel(x, y, c);
			}
		}
		return img;
	}

	/**
	 * renvoie le negatif d'une image
	 * @param img
	 * @return
	 */
	static BufferedImage negative(BufferedImage img) {
		int[] c = {255,255,255,255};
		for (int y = 0; y < img.getHeight() ; y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x,y); // r�cup�ration des couleurs RGB du pixel a la position (x, y)

				int r = (p>>16)&0xff; 
				int g = (p>>8)&0xff; 
				int b = p&0xff;
				c[0]= 255-r;
				c[1]=255-g;
				c[2]=255-b;
				img.getRaster().setPixel(x, y, c);
			}
		}
		return img;
	}

	/**
	


	/**
	

	/**
	 * Implemente l'algorithme Otsu. Aucune optimisation n'est faite.
	 * @param histogramme
	 * @return
	 */
	public static int otsuMethod(int [] histogramme) {

		// varSeuil correspond a la valeur de la variance intra-classe pour le parametre seuil
		int seuil = 0;
		double varSeuil = 0;

		// Pour chaque seuil possible t, la boucle calcule la variance intra-classe. La plus petite valeur de toutes ces variances est affectee a "seuil"

		for(int t=0; t<256; t++) {
			// varA et varB designent les variances des classes A et B, A et B etant interchangeablement le fond et l'objet
			int totalA = 0, totalB = 0, totalPixel = 0;
			double  weightA = 0, weightB =0, moyA = 0, moyB = 0, varA = 0, varB = 0;
			double varIntra = 0;

			for(int i=0; i<t; i++) {
				moyA += i*histogramme[i];
				weightA += histogramme[i];
			}	
			for(int j=t; j<256; j++) {
				moyB += j*histogramme[j];
				weightB += histogramme[j];
			}

			// calcule les moyennes

			totalA = (int) weightA;
			totalB = (int) weightB;
			moyA /= totalA;
			moyB /= totalB;
			totalPixel = totalA+totalB;
			weightA /= totalPixel; 
			weightB /= totalPixel;

			// calcule la variance

			for(int i=0; i<t; i++) {
				varA += ( (i-moyA)*(i-moyA)*histogramme[i] ) / totalA;
			}		
			for(int j=t; j<256; j++) {
				varB += ( (j-moyB)*(j-moyB)*histogramme[j] ) / totalB;
			}

			varIntra = (weightA*varA) + (weightB*varB);

			if(t == 0) varSeuil =  varIntra;
			if (varIntra < varSeuil) {
				varSeuil = varIntra;
				seuil = t;
			}
		}

		return seuil;
	}

}
