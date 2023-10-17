/**
 * Name : Suman Kafle
 * CS251 Project4 "Key Shooter"
 *
 * Project Description: All the methods has been filled as instructed. One  additional getScore method is created
 * which return the integer score.
 *
 * for the extra credit the random path is created and implemented at the createWord method.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import jdk.jfr.Event;

import java.time.Duration;

public class Main extends Application {
    long now = System.currentTimeMillis();
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Setups up all the JavaFX GUI controls and creates instances of
     * all the helper classes.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        long startTime = System.nanoTime();
        // Always make sure to set the title of the window
        primaryStage.setTitle("Key Shooter");
        // Width/height variables so that we can mess with the size of the window
        double width = 600;
        double height = 600;
        // BorderPane (https://openjfx.io/javadoc/18/javafx.graphics/javafx/scene/layout/BorderPane.html)
        // Provides the basis which we basis the rest of the GUI on
        BorderPane window = new BorderPane();

        //Exit button is made at the top left side of the scene
        Button button = new Button("EXIT");
        HBox hBox = new HBox();
        hBox.getChildren().add(button);
        VBox topVBox = new VBox(5);

        // VBox for the top part of the GUI
        topVBox.setAlignment(Pos.CENTER);
        // Label which displays the score
        Label scoreLabel = new Label("0");
        scoreLabel.setFont(new Font(40));
        // Label which displays the currently typed letters
        Label typedLabel = new Label();
        typedLabel.setFont(new Font(40));
        // Add them all to the VBox
        topVBox.getChildren().addAll(hBox,scoreLabel, typedLabel);

        // Put them in the top of the BorderPane
        window.setTop(topVBox);
        // Create an instance of our helper Words class
        Words words = new Words("rsc/words.txt", width, (height * 3) / 4,
                                scoreLabel, typedLabel);
        // Put it in the middle of the BorderPane

        window.setCenter(words.getWordsPane());



        // Create a VBox for the keyboard
        VBox keyBoardWindow = new VBox(10);
        // Create an instance of our helper class Keyboard
        Keyboard keyboard = new Keyboard(width, height / 4, 10);
        // Add a horizontal line above the keyboard to create clear seperation
        keyBoardWindow.getChildren().addAll(new Separator(Orientation.HORIZONTAL), keyboard.getKeyboard());
        // Put it in the bottom of the BorderPane

        window.setBottom(keyBoardWindow);
        // Create the scene
        Scene scene = new Scene(window, width, height);
        // The scene is the best place to capture keyboard input
        // First get the KeyCode of the event
        // Then start the fill transition, which blinks the key
        // Then add it to the typed letters


        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            keyboard.startFillTransition(keyCode);
            words.addTypedLetter(keyCode);
        });
        // Set the scene

        // We also need an AnimationTimer to create words on the
        // screen every 3 seconds. This is done by call createWord
        // from the Words class.

        AnimationTimer timer = new AnimationTimer() {
            private long next = 0; 
            @Override
            public void handle(long now) {
                // to get the createWord method every there second
                // to show many fast the words were swap per minute
                if(now > next){
                    next = now + Duration.ofSeconds(3).toNanos();
                    words.createWord();
                    button.setOnAction(Event ->{this.stop();
                    double totalTime = Duration.ofNanos(System.nanoTime()-startTime).toSeconds();
                    words.getScore();
                    double wordsPerMinute = words.getScore()/(totalTime/60);
                    Label label = new Label("wordsPerMinute: "+(wordsPerMinute));
                    label.setFont(new Font(45));
                    window.setCenter(label);
                    });
                }
            }
        };

        timer.start();
        primaryStage.setScene(scene);
        // Showtime!
        primaryStage.show();
    }
}
