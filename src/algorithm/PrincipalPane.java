package algorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class PrincipalPane extends BorderPane {
	public static final String DOSSIER_IMG = "/bdd/";
	Image img;
	
	public PrincipalPane() throws IOException {
		
		// Partie droite de la fenetre
		FlowPane buttonPane = new FlowPane();
		buttonPane.setOrientation(Orientation.VERTICAL);
		buttonPane.setHgap(100);
		buttonPane.setVgap(40);
		
		Button btnSelectFile = new Button("Select File");
		Button btnAnalyse = new Button("Analyse");
		Label nombreDeMarcheAnalyse = new Label();
		CheckBox afficheImages = new CheckBox("Afficher les images");
		
		// augmente la taille du texte
		nombreDeMarcheAnalyse.setFont(new Font(30));
		afficheImages.setFont(new Font(12));
		
		// permet de centrer les elements
		BorderPane paneBtnSelectFile = new BorderPane();
		BorderPane paneBtnAnalyse = new BorderPane();
		BorderPane paneNombreDeMarcheAnalyse = new BorderPane();
		BorderPane paneAfficheImages = new BorderPane();

		paneBtnSelectFile.setPrefSize(200, 100);
		paneBtnAnalyse.setPrefSize(200, 100);
		paneNombreDeMarcheAnalyse.setPrefSize(200, 100);
		paneAfficheImages.setPrefSize(200, 100);

		paneBtnSelectFile.setCenter(btnSelectFile);
		paneBtnAnalyse.setCenter(btnAnalyse);
		paneNombreDeMarcheAnalyse.setCenter(nombreDeMarcheAnalyse);
		paneAfficheImages.setCenter(afficheImages);
		
		// Couleur et taille des boutons
		btnAnalyse.setStyle("-fx-background-color: #4CAF50;" +
							"-fx-font-size: 20px;" +
							"-fx-background-radius: 6, 5;");
		
		btnSelectFile.setStyle("-fx-background-color: #008CBA;" +
							   "-fx-font-size: 20px;" +
							   "-fx-background-radius: 6, 5;");
	
		// initialisation de l'image de depart
		this.img= null;
		try {
			this.img = new Image("file:"+System.getProperty("user.dir")+DOSSIER_IMG+"1.jpg");
		} catch(Exception e) {
			e.printStackTrace();
		}
		ImageView imgView = new ImageView();
		imgView.setImage(this.img);
		imgView.setFitHeight(700);
		imgView.setFitWidth(600);
		imgView.setSmooth(true);
		
		
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
				imgView.setFitHeight(700);
				imgView.setFitWidth(600);
				imgView.setSmooth(true);
			}
			else {
				System.out.println("File is not valid !");
			}
			
		});
		
		btnAnalyse.setOnAction((event)->{
			// convertit Image en bufferedImage
			BufferedImage imgBuff = SwingFXUtils.fromFXImage(this.img, null);
			int nbreMarche = 0;
			try {
				nbreMarche = Main.algorithm(imgBuff,afficheImages.isSelected());
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			nombreDeMarcheAnalyse.setText("Resultat : "+nbreMarche);

		});
				
		buttonPane.getChildren().add(paneAfficheImages);
		buttonPane.getChildren().add(paneBtnAnalyse);
		buttonPane.getChildren().add(paneBtnSelectFile);
		buttonPane.getChildren().add(paneNombreDeMarcheAnalyse);

		this.setLeft(imgView);
		this.setCenter(buttonPane);
		
	}

}
