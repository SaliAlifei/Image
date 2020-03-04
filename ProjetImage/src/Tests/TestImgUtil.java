package Tests;

import java.awt.image.BufferedImage;
import java.io.IOException;

import utilitaires.ImgUtil;

public class TestImgUtil {
	
	// ------------ Faire des tests unitaires -------------

	public static void testConvolution() throws IOException {
		BufferedImage img = ImgUtil.chargerImage("1.jpg");	
		ImgUtil.imshow(img);
		/**
		 * Masques : 
		 * 
		 * { {1,0,-1}, {1,0,-1}, {1,0,-1} }
		 * 
		 * { {1,1,1}, {0,0,0}, {-1,-1,-1} }
		 * 
		 * { {1/9,1/9,1/9}, {1/9,1/9,1/9}, {1/9,1/9,1/9} }
		 * 
		 */
		double [][] matConv = { {1,0,-1}, {1,0,-1}, {1,0,-1} };
		
		BufferedImage imgConv = ImgUtil.convolution(img, matConv);
		ImgUtil.imshow(imgConv);
	}
	
	
	public static void testGetMatrice() throws IOException {
		 
		BufferedImage img = new BufferedImage(5,5, BufferedImage.TYPE_BYTE_GRAY);
		int [] blanc = {255,255,255};
		img.getRaster().setPixel(1, 1, blanc);
		img.getRaster().setPixel(1, 2, blanc);
		img.getRaster().setPixel(1, 3, blanc);
		img.getRaster().setPixel(2, 1, blanc);
		img.getRaster().setPixel(3, 1, blanc);
		img.getRaster().setPixel(2, 3, blanc);
		img.getRaster().setPixel(3, 3, blanc);
		img.getRaster().setPixel(3, 2, blanc);
		
		ImgUtil.imshow(img);
		System.out.println("Milieu : ");
		ImgUtil.afficheMatConsole(ImgUtil.getMatrice(img, 2, 2, 3));
		System.out.println("Position (1,1) : ");
		ImgUtil.afficheMatConsole(ImgUtil.getMatrice(img, 1, 1, 3));

		 
	}
	

	public static void testMultiplicationInd() {
		 
		double [][] mat = new double [3][3];
		double [][] mat2 = new double [3][3];
		
		mat[0][0] = 1;
		mat[0][1] = 1;
		mat[0][2] =	1;	
		mat[1][0] = 1;
		mat[1][1] = 1;
		mat[1][2] =	1;	
		mat[2][0] = 1;
		mat[2][1] = 1;
		mat[2][2] = 1;
		
		mat2[0][0] = 1;
		mat2[0][1] = 2;
		mat2[0][2] = 3;
		mat2[1][0] = 4;
		mat2[1][1] = 5;
		mat2[1][2] = 6;
		mat2[2][0] = 7;
		mat2[2][1] = 8;
		mat2[2][2] = 9;
		
		System.out.println("Matrice 1");
		ImgUtil.afficheMatConsole(mat);
		System.out.println("------");
		System.out.println("Matrice 2");
		ImgUtil.afficheMatConsole(mat2);
		
		System.out.println("somme = " + ImgUtil.multiplicationIndv(mat, mat2));	
		
	}
	
}
