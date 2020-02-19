package algorithm;

import java.awt.image.BufferedImage;
import java.io.File;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class PrincipalPane extends BorderPane {
	
	Image img;
	
	public PrincipalPane() {
		
		// Partie droite de la fenetre
		FlowPane buttonPane = new FlowPane();
		buttonPane.setOrientation(Orientation.VERTICAL);
		buttonPane.setHgap(100);
		buttonPane.setVgap(60);
		
		Button btnSelectFile = new Button("Select File");
		Button btnAnalyse = new Button("Analyse");
		Label nombreDeMarcheAnalyse = new Label();
		// augmente la taille du texte
		nombreDeMarcheAnalyse.setFont(new Font(30));
		
		// initialisation de l'image de depart
		this.img= null;
		try {
			this.img = new Image("file:"+System.getProperty("user.dir")+"/test_images_escaliers/1.jpg");
		} catch(Exception e) {
			e.printStackTrace();
		}
		ImageView imgView = new ImageView();
		imgView.setImage(this.img);
		
		
		btnSelectFile.setOnAction((event)->{
			// permet de choisir un fichier sur son ordinateur
			FileChooser fileChooser = new FileChooser();
			File selectedFile = fileChooser.showOpenDialog(null);
			
			if (selectedFile != null) {
				/*
				 	Test a ajouter : le fichier selectionne doit etre une image.
				 	Verifier les extentions (.jpg, .png, etc)
				*/
				this.img = new Image("file:"+selectedFile.getAbsolutePath());
				imgView.setImage(this.img);
			}
			else {
				System.out.println("File is not valid !");
			}
			
		});
		
		btnAnalyse.setOnAction((event)->{
			// convertit Image en bufferedImage
			BufferedImage imgBuff = SwingFXUtils.fromFXImage(this.img, null);
			int nbreMarche = Main.algorithm(imgBuff);
			nombreDeMarcheAnalyse.setText(""+nbreMarche);
		});
		
		buttonPane.getChildren().add(btnAnalyse);
		buttonPane.getChildren().add(btnSelectFile);
		buttonPane.getChildren().add(nombreDeMarcheAnalyse);
		this.setLeft(imgView);
		this.setCenter(buttonPane);
		
	}

}
