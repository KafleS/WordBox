import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Keyboard {
    // 2 Dimensional list representing the rows of keys on the keyboard
    // Letter keys only
    private final List<List<KeyCode>> keyCodes;
    // Map that is used to access the keys JavaFX representation
    private final Map<KeyCode, WordBox> keyCodeToWordBox;
    // JavaFX control that represents the keyboard on the screen
    private final VBox keyboard;
    // Color that the keys are by default
    private static final Color from = Color.color(0.9, 0.9, 0.9);
    // Color that the keys become when pressed
    private static final Color to = Color.color(0.3, 0.3, 0.8);

    public Keyboard(double width, double height, double spacing) {
        keyCodes = initializeKeys();
        keyCodeToWordBox = new HashMap<>();

        keyboard = initializeKeyboard(width, height, keyCodes, spacing);
    }

    public VBox getKeyboard() {
        return keyboard;
    }

    /**
     * First checks if the given keyCode exists in the keyCodeToWordBox.
     * If it does then it starts a FillTransition (https://openjfx.io/javadoc/18/javafx.graphics/javafx/animation/FillTransition.html)
     * to go from the from color to the to color.
     * If the keyCode does not exist then it does nothing.
     * @param keyCode KeyCode to lookup in the map and flash
     */
    // to check if the tpyed word exits int the wordbox and to fill the color from white to blue
    // if the tpyed word is not in the wordbox it will do nothing
    public void startFillTransition(KeyCode keyCode) {
        if ( keyCodeToWordBox.containsKey(keyCode)){
            FillTransition fillTransition = new FillTransition(Duration.millis(100),keyCodeToWordBox.get(keyCode).getRect(),from,to);
            fillTransition.setCycleCount(2);
            fillTransition.setAutoReverse(true);
            fillTransition.play();
        }
    }

    /**
     * Simply creates the 2D list that represents the keyboard.
     * Each row is an element of the outer list and each inner liste
     * contains all the letter keys in that row. Only contains
     * 3 rows. All letters are uppercase.
     * @return 2D list representing the letters on the keyboard
     */
    // to create the  list of 2D array of the keyboard only containing the letters
    private List<List<KeyCode>> initializeKeys() {
        List<List<KeyCode>> keyBoard = new ArrayList<>();
        String [][] keys = {{ "Q","W","E","R","T","Y","U","I","O","P"},
                {"A","S","D","F","G","H","J","K","L"},
                {"Z","X","C","V","B","N","M"}
        };
        for (int i = 0; i < 3; i++){
            List<KeyCode> line = new ArrayList<>();
            for (int j = 0; j < keys[i].length; j++){
                line.add(j, KeyCode.getKeyCode(keys[i][j]));
            }
            keyBoard.add(i, line);
        }
        return keyBoard;
    }

    /**
     * Creates the JavaFX control that visualized the keyboard on the screen
     * Also initializes the keyCodeToWordBox map as it goes.
     * It deduces the size of each key using the 2D list and the
     * width parameter. Then creates a VBox and sets its width/height
     * and centers it. Then loops over the 2D list and creates JavaFX
     * controls, WordBox, to represent each key and adds them to HBoxes.
     * The adds the row HBox to the VBox. It also adds the WordBox to the
     * map. Then it moves on to the next row.
     * @param width Width of the screen
     * @param height Height of the screen
     * @param keyCodes 2D list that holds all the letters on the keyboard
     * @param spacing Space between each key
     * @return JavaFX control that visualizes the keyboard on the screen
     */
    // to make the keyboard and to make the blue color indicating the letter pressed in the virtual keyboard
    private VBox initializeKeyboard(double width, double height, List<List<KeyCode>> keyCodes, double spacing) {
        VBox vbox = new VBox();
        for (int i = 0; i < 3; i++){
            HBox hbox =  new HBox(spacing);
            hbox.setAlignment(Pos.CENTER);
            for (int j = 0; j < keyCodes.get(i).size(); j++){
                // to get the blue color when the letter is pressed
                WordBox label = new WordBox(35,keyCodes.get(i).get(j).toString(),from);
                hbox.getChildren().add(label.getWordBox());
                keyCodeToWordBox.put(keyCodes.get(i).get(j),label);
            }
            vbox.getChildren().add(hbox);
        }
       vbox.setPrefWidth(width);
       vbox.setPrefHeight(height);

        return vbox;
    }
}