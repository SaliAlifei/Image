package utilitaires;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferFloat;
import java.awt.image.ImageObserver;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
	
	/**
	 * !!! Retourne une bufferedImage !!!
	 * Methode Otsu optimisee. L'image d'entree est decoupee en 4 parties auxquelles sont appliquees otsuMethod().
	 * 
	 * @param imgNivGris
	 * @return
	 */
	public static BufferedImage otsuMethod4(BufferedImage imgNivGris) {
		ArrayList<BufferedImage> array = diviserEn4(imgNivGris);
		ArrayList<BufferedImage> arrayImgSeuillees = new ArrayList<BufferedImage>();

		for(int i=0; i<array.size(); i++) {
			seuillage(array.get(i), otsuMethod(histogrammeEn255NivDeGris(array.get(i))));
			arrayImgSeuillees.add(array.get(i));
		}
		BufferedImage imgSeuillee = rassembler(arrayImgSeuillees);
		return imgSeuillee;
	}
	
	/**
	 * Retourne un arrayList contenant 4 bufferedImage a peu pres egales
	 * @param img
	 * @return
	 */
	public static ArrayList<BufferedImage> diviserEn4(BufferedImage img) {
		ArrayList<BufferedImage> array = new ArrayList<BufferedImage>();
		int width = img.getWidth();
		int height = img.getHeight();
		System.out.println("width : "+width+"\theight: "+height+"\twidht/2 : "+ ((int) width/2) + "\theight/2 : "+((int) height/2));
		/*
		 
		 	*................*..................
		 	....................................
		 	....................................
		 	........1..................2........
		 	....................................
		 	....................................
		 	*................+.................+
		 	....................................
		 	....................................
		 	........3..................4........
		 	....................................
		 	.................+.................+
		 	
		 
		*/
		int[][] debut = { 
							{0,0}, 							// carre 1
							{(int) width/2, 0},				// carre 2
							{0, (int) height/2},			// carre 3
							{(int) width/2, (int) height/2} // carre 4
						};

		int[][] fin = { 
							{(int) width/2, (int) height/2}, // carre 1
							{width, (int) height/2},		 // carre 2
							{(int) width/2, height},	     // carre 3
							{width, height}                  // carre 4
					  };	
		
		
		BufferedImage image1 = new BufferedImage(debut[1][0]-debut[0][0], (int) height/2, BufferedImage.TYPE_BYTE_GRAY);
		BufferedImage image2 = new BufferedImage(width - ((int) width/2), (int) height/2, BufferedImage.TYPE_BYTE_GRAY);
		BufferedImage image3 = new BufferedImage(debut[1][0]-debut[0][0], height- ((int) height/2), BufferedImage.TYPE_BYTE_GRAY);
		BufferedImage image4 = new BufferedImage(width - ((int) width/2), height- ((int) height/2), BufferedImage.TYPE_BYTE_GRAY);
		
		for (int w=0; w<width; w++) {
			for (int h=0; h<height; h++) {
				
				int rgb = (img.getRGB(w, h)>>8)&0xff;
				int [] color = {rgb, rgb, rgb, 255};
				
				// 1 et 3
				if (w>=0 && w<debut[1][0]) {
					//1
					if (h>=0 && h<debut[2][1]) {
						image1.getRaster().setPixel(w, h, color);
					}
					//3
					else {
						image3.getRaster().setPixel(w, h-((int) height/2), color);
					}
				}
				// 2 et 4
				else {
					//2
					if (h>=0 && h<debut[2][1]) {
						image2.getRaster().setPixel(w-((int) width/2), h, color);
					}
					//4
					else {
						image4.getRaster().setPixel(w-((int) width/2), h-((int) height/2), color);
					}
				}
			}
		}
		array.add(image1);
		array.add(image2);
		array.add(image3);
		array.add(image4);
		
		return array;
	}
	
	/**
	 * Permet de joindre deux images l'un a cote de l'autre
	 * @param img1
	 * @param img2
	 * @param thiss
	 * @return
	 */
	public static BufferedImage joindreHorizentalement(BufferedImage img1, BufferedImage img2, ImageObserver thiss) {
		BufferedImage img = new BufferedImage( img1.getWidth() + img2.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_ARGB ) ;
		Graphics g = img.getGraphics() ;
		g.drawImage( img1, 0, 0, thiss) ;
		g.drawImage( img2, img1.getWidth(), 0, thiss) ;
		
		return img;
	}
	
	/**
	 * Permet de joindre deux images l'un sur l'autre
	 * Source : https://stackoverflow.com/questions/20826216/copy-two-bufferedimages-into-one-image-side-by-side
	 * 
	 * @param img1
	 * @param img2
	 * @return
	 */
	public static BufferedImage joindreVerticalement(BufferedImage img1, BufferedImage img2) {
		int width = img1.getWidth();
	    int height = img1.getHeight() + img2.getHeight();
	    BufferedImage newImage = new BufferedImage(width, height,
	        BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = newImage.createGraphics();
	    Color oldColor = g2.getColor();
	    g2.setPaint(Color.WHITE);
	    g2.fillRect(0, 0, width, height);
	    g2.setColor(oldColor);
	    g2.drawImage(img1, null, 0, 0);
	    g2.drawImage(img2, null, 0, img1.getHeight());
	    g2.dispose();
	    return newImage;
	}
	
	/**
	 * Retourne une image reconstituee a partir de 4 autre images passees en arrayList
	 * @param array
	 * @return
	 */
	public static BufferedImage rassembler(ArrayList<BufferedImage> array) {
		return ImgUtil.joindreVerticalement(
				ImgUtil.joindreHorizentalement(array.get(0), array.get(1), null), 
				ImgUtil.joindreHorizentalement(array.get(2), array.get(3), null)
				);
	}
	
	/**
	 * retourne l'histogramme sous forme d'image à partir du tableau contenant les valeurs de l'histogramme
	 * @param histogramme tableau
	 * @return image
	 */
	public static BufferedImage histToImg(int [] histogramme) {
		// 256*2 = 512
		int iter = 2 ;
		int margeWidth = 5;
		int width = iter*256 + margeWidth;
		int height = 300;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		int[] gris = {128,128,128,255};
		int[] noir = {0,0,0,255};
		int max = max(histogramme);
		
		// Convertir l'image en blanc
		for(int w=0; w<width; w++) {
			for(int h=0; h<height; h++) {
				img.getRaster().setPixel(w, h, gris);
			}
		}
		
		// Avoir des valeurs entre 0 et height		
		for(int i = 0; i < histogramme.length; i++){
			histogramme[i]  = (int) ( (height* histogramme[i])/max );
		}

		for (int w=margeWidth; w<width-margeWidth; w += 2) {
			for(int h=height-1; h>(height-histogramme[w/2]); h--) {
				img.getRaster().setPixel(w, h, noir);
				img.getRaster().setPixel(w+1, h, noir);
			}
		}
		return img;
	}
	
	public static BufferedImage convolution (BufferedImage img, double [][] matConv) throws IOException  {
		BufferedImage imgConv = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		if (matConv[0].length != matConv.length) throw new RuntimeException("La matrice de convolution doit etre carree");
		int nConv = matConv[0].length;
		int marge = ((int) (nConv/2));
				
		// creer un nv bufferedImage pour gerer les contours
		BufferedImage imgPlusContours = new BufferedImage(img.getWidth()+(2*marge), img.getHeight()+(2*marge), BufferedImage.TYPE_BYTE_GRAY);
		for (int w=0; w<img.getWidth(); w++) {
			for(int h=0; h<img.getHeight(); h++) {
				double rgb = img.getRGB(w, h)&0xff;
				double [] color = {rgb,rgb,rgb};
				imgPlusContours.getRaster().setPixel(w+marge, h+marge, color);
			}
		}
		
		// concolution sur la nouvelle image
		
		for (int w=0; w<img.getWidth(); w++) {
			for(int h=0; h<img.getHeight(); h++) {
				double colr = multiplicationIndv(getMatrice(imgPlusContours, w+marge, h+marge, nConv), matConv);
				double [] color = {colr,colr,colr};
				imgConv.getRaster().setPixel(w, h, color);
				float [] out = new float[3];
				imgConv.getRaster().getDataElements(w, h, out);
			}
		}
	
	
		// rescaling
		
		/*
		 
		int min = minImg(imgConv);
		int max = maxImg(imgConv);
		
		for (int w=0; w<imgConv.getWidth(); w++) {
			for(int h=0; h<imgConv.getHeight(); h++) {
				int col = imgConv.getRGB(w, h)&0xff;
				int colr = scale(col, min, max, 0, 255);
				int [] color = {colr, colr, colr, 255};
				imgConv.getRaster().setPixel(w, h, color);
			}
		}
	
		*/
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
	public static double[][] getMatrice(BufferedImage img, int x, int y, int taille) {
		if(taille%2 == 0) throw new RuntimeException("Taille doit etre impaire");
		double [][] mat = new double [taille][taille];
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
	public static double multiplicationIndv(double [][] matrice1, double [][] matrice2) {
		if ((matrice1.length != matrice2.length) && (matrice1[0].length != matrice2[0].length)) throw new RuntimeException("Matrice non carree"); 
		double somme = 0;
		for (int row = 0; row < matrice1.length; row++) {
            for (int col = 0; col < matrice2[0].length; col++) {
            	somme += matrice1[row][col] * matrice2[row][col];
            }
        }
		return somme;
	}
	
	public static double scale(double nbre, double minAvant, double maxAvant, int minApres, int maxApres) {
		double nvNbre = 0;
		nvNbre = ( ((maxApres-minApres)/(maxAvant-minAvant))*(nbre - minAvant) ) + minApres;
		return (nvNbre);
	}
	
	/**
	 * Affiche la matrice entree dans la console
	 * @param prod
	 */
	public static void afficheMatConsole1 (int[][] prod) {
        for (int row = 0; row < prod.length; row++) {
            for (int col = 0; col < prod[row].length; col++) {
                System.out.print(prod[row][col] + " ");
            }
            System.out.println();
        }
	}
	
	public static void afficheMatConsole (double [][] prod) {
        for (int row = 0; row < prod.length; row++) {
            for (int col = 0; col < prod[row].length; col++) {
                System.out.print(prod[row][col] + " ");
            }
            System.out.println();
        }
	}
	
	public static double minImg(BufferedImage img) {
		double min = 0;
		for (int w=0; w<img.getWidth(); w++) {
			for(int h=0; h<img.getHeight(); h++) {
				double value = img.getRGB(w, h)&0xff;
				if (value < min) min = value;
			}
		}
		return min;
	}
	
	public static double maxImg(BufferedImage img) {
		double max = 0;
		for (int w=0; w<img.getWidth(); w++) {
			for(int h=0; h<img.getHeight(); h++) {
				double value = img.getRGB(w, h)&0xff;
				if (value > max) max = value;
			}
		}
		return max;
	}
	
	public static int max(int [] array) {
		int maxVal = array[0];
		for(int i = 0; i < array.length; i++){
	         if(array[i] > maxVal)
	           maxVal = array[i];
	     }
		return maxVal;
	}
	
	public static BufferedImage convolve(BufferedImage img, double [][] mask, int mask_size) {
		
		int rows = img.getHeight();
		int cols = img.getWidth();
		
		BufferedImage output = getFloatBuuffredImage(cols, rows);
		
			
		for(int i=1; i<rows-1 ;i++){
	        for(int j=1; j<cols-1 ;j++){
	        	
	            float conv_pix=0;
	            for(int x=-1; x<2;x++){
	                for(int y=-1;y<2;y++){
	                	float pixel = (float) ((img.getRGB(j+y, i+x) >> 16) & 0xff);
	                	conv_pix = (float) (conv_pix + (pixel * mask[x+1][y+1]));
	                	//System.out.println("\tmask " + mask[x+1][y+1] + "; conv pix " +conv_pix);
	                }
	            }
	            //System.out.println("; pixel value = " + conv_pix);
	            float[] new_pixel_value = {conv_pix, conv_pix, conv_pix, 255};
	            output.getRaster().setPixel(j, i, new_pixel_value);
	        }
	    }
		
		return output;
	}
	
	public static BufferedImage getFloatBuuffredImage(int w, int h) {
        int bands = 4; // 4 bands for ARGB, 3 for RGB etc
        int[] bandOffsets = {0, 1, 2, 3}; // length == bands, 0 == R, 1 == G, 2 == B and 3 == A

        // Create a TYPE_FLOAT sample model (specifying how the pixels are stored)
        SampleModel sampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_FLOAT, w, h, bands, w  * bands, bandOffsets);
        // ...and data buffer (where the pixels are stored)
        DataBuffer buffer = new DataBufferFloat(w * h * bands);

        // Wrap it in a writable raster
        WritableRaster raster = Raster.createWritableRaster(sampleModel, buffer, null);

        // Create a color model compatible with this sample model/raster (TYPE_FLOAT)
        // Note that the number of bands must equal the number of color components in the 
        // color space (3 for RGB) + 1 extra band if the color model contains alpha 
        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel colorModel = new ComponentColorModel(colorSpace, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_FLOAT);

        // And finally create an image with this raster
        BufferedImage image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);

        //System.out.println("image = " + image);
        
        return image;
	}
	
	
	public static BufferedImage  mettreEnNoirEtBlanc(BufferedImage img) {
		int height = img.getHeight();
		int width =img.getWidth();
		System.out.println("dans la méthode mettre en Noir et blanc \nheight : "+height+", width : "+width);
		
		BufferedImage nvImg = ImgUtil.zeros(height, width);
		
		int[] noir = {0,0,0,255};
		//int blanc = Color.WHITE.getRGB();
		int [] blanc = {255,255,255,255};
		
		for(int i = 0 ; i < height ; i++) {
			for(int j = 0 ; j<width;j++) {
				
				int p = img.getRGB(j,i); 
			int r = (p>>16)&0xff;
				if(r==255) {
					nvImg.getRaster().setPixel(j, i, blanc);
				}else {
					nvImg.getRaster().setPixel(j, i, noir);
				}
				
			}
		}
		
		return nvImg;
	}
	

	/**
	 * compte le nombre de ligne d'un texte (ou le nombre de marche)
	 * @param img
	 * @param interlignemin
	 * @param interlignemax
	 * @param nbPix
	 * @return
	 */
	public static int nbLignes(BufferedImage img, int interlignemin, int interlignemax, int nbPix) throws IOException {
		int nbLignes=0;
		int[] tab = histProj(img);
		int[] tab2 = new int[tab.length];

		for(int i=0;i<tab.length;i++) {
			if (tab[i]>nbPix)
				tab2[i]=nbPix;
			else
				tab2[i]=0;
		}
		

		try {
			File outputfile = new File("/Users/Salimata/Desktop/1_2.jpg");
			ImageIO.write(img, "jpg", outputfile);
		} catch (Exception e) {
			System.out.println("Probleme avec le fichier -- nbLignes -- ");
			e.printStackTrace();
		}
		


		imshow(ImgUtil.histogrammeProjection(img, tab2));

		int debut=0,fin=0;
		if (tab2[1]==nbPix && tab2[2]==nbPix) {
			nbLignes ++;
			debut=1;
		}
		for(int i=1;i<tab2.length;i++) {
			if (tab2[i]==0 && tab2[i-1]==nbPix) {
				debut=i;
			}
			if (tab2[i]==nbPix && tab2[i-1]==0) {
				fin=i;
				if (fin-debut>interlignemin && fin-debut<interlignemax) {
					nbLignes++;
				}


			}
		}
		return nbLignes;
	}
	
	/**
	 * renvoie le tableau de l'hstogramme de projection
	 * @param img
	 * @return
	 */
	public static int[] histProj(BufferedImage img) {
		int tab[]=new int[img.getHeight()];
		for(int i=0;i<tab.length;i++) {
			tab[i]=0;
		}
		for (int y = 0; y < img.getHeight() ; y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x,y); // r�cup�ration des couleurs RGB du pixel a la position (x, y)
				int r = (p>>16)&0xff;
				if (r==0)
					tab[y]+=1;
			}
		}
		return tab;
	}
	
	/**
	 * affiche l'histogramme de projection
	 * @param img
	 * @param tab
	 * @return
	 */
	public static BufferedImage histogrammeProjection(BufferedImage img, int[] tab) {

		BufferedImage hist = zeros(img.getHeight(),max(tab));
		int[] blanc = {255,255,255,255};
		for (int y = 0; y < hist.getHeight() ; y++){
			for (int x=0; x<tab[y]; x++){
				hist.getRaster().setPixel(x, y, blanc);
			}
		}
		return hist;
	}
	
	/**
	 * etire l'histogramme d'une image et retourne l'image avec ces changements
	 * @param img
	 * @return
	 */
	public static BufferedImage etir(BufferedImage img) {
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
		int i=0;
		while(tab[i]==0)
			i++;
		int minG = i;
		i=255;
		while(tab[i] == 0)
			i--;
		int maxG= i;
		int[] c = {255,255,255,255};
		for (int y = 0; y < img.getHeight() ; y++){
			for (int x = 0; x < img.getWidth(); x++){
				int p = img.getRGB(x,y); // r�cup�ration des couleurs RGB du pixel a la position (x, y)
				int r = (p>>16)&0xff;
				c[0]= ((r-minG)*255)/(maxG-minG);
				c[1]=c[0];
				c[2]=c[0];
				img.getRaster().setPixel(x, y, c);
			}
		}
		return img;
	}
	
	/**
	 * retourne un tableau histogramme cummul�
	 * @param img
	 * @return
	 */
	static int[] histCum(BufferedImage img) {
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
		for(int i=1;i<tab.length;i++) {
			tab[i]+=tab[i-1];
		}

		return tab;
	}

	/**
	 * normalise un histogramme et retourne l'image de l'histogramme
	 * @param tab
	 * @return
	 */
	public static BufferedImage histogrammeNormalise(int[] tab) {
		int max=max(tab);
		int histn[]=new int[256];
		for (int i=0;i<tab.length;i++) {
			histn[i]=(tab[i]*100)/max;
		}


		BufferedImage hist = zeros(100,256);
		int[] blanc = {255,255,255,255};
		for (int x = 0; x < hist.getWidth() ; x++){
			for (int y=99; y>100-histn[x]; y--){
				hist.getRaster().setPixel(x, y, blanc);
			}
		}
		return hist;
	}

	/**
	 * retourne l'image de l'histogramme cumul�
	 * @param tab
	 * @return
	 */
	public static BufferedImage histogrammeCumule (int[] tab) {
		int max=max(tab);
		int histn[]=new int[256];
		for (int i=0;i<tab.length;i++) {
			histn[i]=(tab[i]*100)/max;
		}

		BufferedImage hist = zeros(100,256);
		int[] blanc = {255,255,255,255};
		for (int x = 0; x < hist.getWidth() ; x++){
			for (int y=99; y>100-histn[x]; y--){
				hist.getRaster().setPixel(x, y, blanc);
			}
		}
		return hist;
	}
	
	/**
	 * cr�e une image noire
	 * @param height
	 * @param width
	 * @return
	 */
	public static BufferedImage zeros(int height, int width) {

		int[] noir = {0,0,0,255};
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
		//WritableRaster raster = img.getRaster();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height ; y++) {
				img.getRaster().setPixel(x, y, noir);
			}
		}
		return img; 
	}

	/**
	 * Duplique l'image passee en parametre
	 * Lien : https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	 * @param bi
	 * @return
	 */
	public static BufferedImage duplicate(BufferedImage img) {
		 ColorModel cm = img.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = img.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
	
	/**
     * Fonction blur : Définition floue de l'élément structurant des opérations de dilatation et d'érosion
     * source: M. Chelali
     * @param t la distance entre le pixel en cours de traitement (centre de l'élément structurant) et du point de l'élément structurant courant
     * @param a le diamètre du carré constituant l'élément structurant
     * @return le degré d'appartenance du point à l'élément structurant
     */
    private static double blur(int t, int a) {
        double res = Math.min(1, Math.max(0, 1 - (t / a)));
        return res;
    }

    /**
     * Fonction hamming : Calcul de la norme Hamming entre le pixel en cours de traitement et le point de l'élément structurant courant
     * source: M. Chelali
     * @param x la différence en abscisse entre les deux points
     * @param y la différence en abscisse entre les deux points
     * @return la norme Hamming entre les deux points
     */
    private static int hamming(int x, int y) {
        int res = Math.max(Math.abs(x), Math.abs(y));
        return res;
    }


    /**
     * Fonction convertToGrayscale : Conversion d'un objet BufferedImage en niveau de gris
     * source: M. Chelali
     * @param source la BufferedImage correspondant à l'image que l'utilisateur a choisi
     * @return la copie de la BufferedImage en niveau de gris
     */
    public static BufferedImage convertToGrayscale(BufferedImage source) {
        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return op.filter(source, null);
    }
    /**
     * Fonction erode : Erosion de la BufferedImage courante
     * source: M. Chelali
     * @param img la BufferedImage source, qui ne sera pas modifié à l'intérieur de la fonction
     * @param a le rayon de l'élément structurant
     * @return la nouvelle BufferedImage après érosion de la BufferedImage source
     */
    public static BufferedImage erode(BufferedImage img, int a) {
        // On crée la BufferedImage destination, de même dimension que la source, et en niveau de gris
        BufferedImage newImgGray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        int newGrayLevel = 0;
        int newRGB = 0;

        // On parcourt l'image
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                // On récupère la valeur de gris du pixel courant après érosion
                newGrayLevel = mask_erode(j, i, img, a);
                // On la transforme en couleur RGB, afin de pouvoir l'utiliser via setRGB
                newRGB = (0xFF << 24) | ((newGrayLevel & 0xFF) << 16) | ((newGrayLevel & 0xFF) << 8) | ((newGrayLevel & 0xFF));
                // Et on affecte cette couleur au pixel courant
                newImgGray.setRGB(j, i, newRGB);
            }
        }
        return newImgGray;
    }

    /**
     * Fonction mask_erode : Calcul du niveau de gris à affecter au pixel courant à partir des niveaux de gris des voisins
     * source: M. Chelali
     * @param x l'abscisse du pixel courant
     * @param y l'ordonnée du pixel courant
     * @param img l'image d'origine en niveau de gris
     * @param a le rayon de l'élément structurant
     * @return le niveau de gris "érodé" du pixel courant
     */
    private static int mask_erode(int x, int y, BufferedImage img, int a) {
        // Variables temporaires stockant les deux valeurs calculées de niveau de gris, pour chaque pixel de l'élément structurant
        double tempMuGL, tempFGL;
        // Variable temporaire stockant le maximum des deux variables temporaires ci-dessus
        int maxGL;
        // Le niveau de gris final, initialisé à la valeur maximale autorisée
        int finalGL = 255;

        // On parcourt la zone de l'image que forme l'élément structurant
        for (int i = -a; i <= a; i++) {
            for (int j = -a; j <= a; j++) {
                boolean imgEdge = false;

                // On vérifie que l'on n'est pas en dehors de l'image
                if (x + j < 0 || x + j > img.getWidth() - 1) {
                    imgEdge = true;
                }
                if (y + i < 0 || y + i > img.getHeight() - 1) {
                    imgEdge = true;
                }

                if (!imgEdge) {
                    // Récupération de la valeur par la fonction d'appartenance de l'ensemble flou de l'image
                    tempMuGL = (double) new Color(img.getRGB(x + j, y + i)).getBlue() / 255;
                    // Récupération de la valeur par l'élément structurant
                    tempFGL = 1 - blur(hamming(j, i), a);
                    // On utilise la t-norme de Zadeh comme critère de sélection
                    maxGL = (int) (Math.max(tempMuGL, tempFGL) * 255);

                    // Et on stocke finalement la valeur calculée si elle est plus faible que la valeur récupérée dans les tours de boucles précédents
                    if (maxGL < finalGL) {
                        finalGL = maxGL;
                    }
                }
            }
        }
        return finalGL;
    }

    /**
     * Fonction dilate : Dilatation de la BufferedImage courante
     * source: M. Chelali
     * @param img la BufferedImage source, qui ne sera pas modifié à l'intérieur de la fonction
     * @param a le rayon de l'élément structurant
     * @return la nouvelle BufferedImage après dilatation de la BufferedImage source
     */
    public static BufferedImage dilate(BufferedImage img, int a) {
        // On crée la BufferedImage destination, de même dimension que la source, et en niveau de gris
        BufferedImage newImgGray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        int newGrayLevel = 0;
        int newRGB = 0;

        // On parcourt l'image
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                // On récupère la valeur de gris du pixel courant après dilatation
                newGrayLevel = mask_dilate(j, i, img, a);
                // On la transforme en couleur RGB, afin de pouvoir l'utiliser via setRGB
                newRGB = (0xFF << 24) | ((newGrayLevel & 0xFF) << 16) | ((newGrayLevel & 0xFF) << 8) | ((newGrayLevel & 0xFF));
                // Et on affecte cette couleur au pixel courant
                newImgGray.setRGB(j, i, newRGB);
            }
        }
        return newImgGray;
    }

    /**
     * Fonction mask_dilate : Calcul du niveau de gris à affecter au pixel courant à partir des niveaux de gris des voisins
     * source: M. Chelali
     * @param x l'abscisse du pixel courant
     * @param y l'ordonnée du pixel courant
     * @param img l'image d'origine en niveau de gris
     * @param a le rayon de l'élément structurant
     * @return le niveau de gris "dilaté" du pixel courant
     */
    private static int mask_dilate(int x, int y, BufferedImage img, int a) {
        // Variables temporaires stockant les deux valeurs calculées de niveau de gris, pour chaque pixel de l'élément structurant
        double tempMuGL, tempFGL;
        // Variable temporaire stockant le minimum des deux variables temporaires ci-dessus
        int minGL;
        // Le niveau de gris final, initialisé à la valeur minimale autorisée
        int finalGL = 0;

        // On parcourt la zone de l'image que forme l'élément structurant
        for (int i = -a; i <= a; i++) {
            for (int j = -a; j <= a; j++) {
                boolean imgEdge = false;

                // On vérifie que l'on n'est pas en dehors de l'image
                if (x + j < 0 || x + j > img.getWidth() - 1) {
                    imgEdge = true;
                }
                if (y + i < 0 || y + i > img.getHeight() - 1) {
                    imgEdge = true;
                }

                if (!imgEdge) {
                    // Récupération de la valeur par la fonction d'appartenance de l'ensemble flou de l'image
                    tempMuGL = (double) new Color(img.getRGB(x + j, y + i)).getBlue() / 255;
                    // Récupération de la valeur par l'élément structurant
                    tempFGL = blur(hamming(j, i), a);
                    // On utilise la co-norme de Zadeh comme critère de sélection
                    minGL = (int) (Math.min(tempMuGL, tempFGL) * 255);

                    // Et on stocke finalement la valeur calculée si elle est plus forte que la valeur récupérée dans les tours de boucles précédents
                    if (minGL > finalGL) {
                        finalGL = minGL;
                    }
                }
            }
        }
        return finalGL;
    }

    /**
     * Fonction open : Ouverture mathématique de l'image, soit une érosion, puis une dilatation successive de l'image
     * source: M. Chelali
     * @param img la BufferedImage ouverte par l'utilisateur
     * @param a le rayon de l'élément structurant
     * @return la BufferedImage éordée et dilatée correspondante
     */
    public static BufferedImage open(BufferedImage img, int a) {
        // On définie une BufferedImage vide, aux dimensions de la BufferedImage source, et en niveau de gris
        BufferedImage tmpImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        // On effectue l'ouverture
        tmpImg = dilate(erode(img, a), a);

        return tmpImg;
    }

    /**
     * Fonction close : Fermeture mathématique de l'image, soit une dilatation, puis une érosion successive de l'image
     * source: M. Chelali
     * @param img la BufferedImage ouverte par l'utilisateur
     * @param a le rayon de l'élément structurant
     * @return la BufferedImage dilatée et érodée correspondante
     */
    public static BufferedImage close(BufferedImage img, int a) {
        // On définie une BufferedImage vide, aux dimensions de la BufferedImage source, et en niveau de gris
        BufferedImage tmpImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        // On effectue l'ouverture
        tmpImg = erode(dilate(img, a), a);

        return tmpImg;
    }
	
}


