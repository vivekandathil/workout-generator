package ca.vivek.cprogress;

import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Generator extends Application {

	public static void main(String[] args) {
		launch(args);

	}
	
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("JavaFX WebView Example");
        
        Reader reader = new Reader();
        
        Label label = new Label("Select Number of Exercises and Muscles to Target (comma-separated):");
        final Spinner<Integer> spinner = new Spinner<Integer>();
 
        final int initialValue = 6;
 
        // Value factory.
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, initialValue);
 
        spinner.setValueFactory(valueFactory);
        
        TextField textField = new TextField ();

        Button button = new Button("Generate Workout");
        button.setOnAction(e -> {
        	try {
				getHostServices().showDocument(reader.getPDF(spinner.getValue(), textField.getText().split(",")));
				primaryStage.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            
        });
        
        HBox hBox = new HBox(label, spinner, textField);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);

        VBox vBox = new VBox(hBox, button);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(10);
        Scene scene = new Scene(vBox, 880, 200);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
