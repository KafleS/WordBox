import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Words {
    // Pane (https://openjfx.io/javadoc/18/javafx.graphics/javafx/scene/layout/Pane.html)
    // which represents the floating words part of the game
    private final Pane wordsPane;
    // List of all available words
    private final List<String> words;
    // List of all JavaFX floating words currently on the screen
    private final List<WordBox> activeWords;
    // List of all keys that have been pressed since the last correct word
    private final List<KeyCode> typed;
    // JavaFX Label which shows the score on the screen
    private final Label scoreLabel;
    // Keeps track of the number of correct words
    private int score = 0;
    // JavaFX Label which shows what the user has typed since the last correct word
    private final Label typedLabel;
    // Width/height of the screen
    private final double width;
    private final double height;

    public Words(String path, double width, double height,
                 Label scoreLabel, Label typedLabel) throws FileNotFoundException {
        wordsPane = new Pane();
        wordsPane.setPrefWidth(width);
        wordsPane.setPrefHeight(height);

        this.words = Utils.readWords(path);

        activeWords = new ArrayList<>();
        typed = new ArrayList<>();

        this.scoreLabel = scoreLabel;
        this.typedLabel = typedLabel;

        this.width = width;
        this.height = height;
    }

    public Pane getWordsPane() {

        return wordsPane;
    }

    /**
     * Removes the wordBox from the wordsPane as well as
     * removing it from activeWords.
     * @param wordBox WordBox to remove
     */
    // to remove the word form the wordbox and from the active word form the screen
    private void removeWord(WordBox wordBox) {
        activeWords.remove(wordBox);
        wordsPane.getChildren().remove(wordBox.getWordBox());
        FillTransition fillTransition = new FillTransition();

    }

    /**
     * Creates a random floating word.
     * Choses a random word from the list of words.
     * Then chooses a starting point on any edge of the screen.
     * Then creates a Timeline (https://openjfx.io/javadoc/18/javafx.graphics/javafx/animation/Timeline.html)
     * that moves the WordBox from its starting point to a random ending
     * point over 10 seconds.
     */
    // to create the random word from the group of the word
    // to move the active word on the screen through the different path
    public void createWord() {
        Random random = new Random();
        String randomWord = words.get(random.nextInt(words.size()));
        WordBox word = new WordBox(55, randomWord, null);
        wordsPane.getChildren().add(word.getWordBox());
        activeWords.add(word);
        // to move the word in dirrent path in screen
        Path wordPath = new Path();
        MoveTo start = new MoveTo(100, ThreadLocalRandom.current().nextInt(0,(int) height - 200));
        LineTo end =new LineTo(ThreadLocalRandom.current().nextInt(0,(int) width-100), ThreadLocalRandom.current().nextInt(0, (int) height - 200));
        wordPath.getElements().add(start);
        wordPath.getElements().add(end);
        PathTransition pathTransition = new PathTransition(Duration.seconds(10), wordPath, word.getWordBox());
        pathTransition.play();
        // to move the word for 10 second in the screen
        Timeline removeWord = new Timeline(new KeyFrame(
            Duration.millis(10000),
            event -> {
                removeWord(word);
                }
            )
        );
        removeWord.play();
    }

    // method is created to get the score
    public  int getScore(){
        return score;
    }

    /**
     * Adds the keyCode to typed if it is a letter key.
     * Removes the first element of typed if it is the backspace key.
     * Either way it checks for a correct word and updates the typedLabel.
     * @param keyCode KeyCode to add to the state
     */
    // to add the letter to tpyed word if the letter matches to the word
    // if backspace is pressed first element which is typed will be removed
    public void addTypedLetter(KeyCode keyCode) {
        if (keyCode.isLetterKey()){
            typed.add(keyCode);
            typedLabel.setText(Utils.combineList(typed));
        }
        if(keyCode.equals(KeyCode.BACK_SPACE)){
            typed.remove(typed.size()-1);
            typedLabel.setText(Utils.combineList(typed));
        }
        checkForCorrectWord(typedLabel.getText());
    }

    /**
     * Checks if the given String is equal to any of the currently
     * active words. If it is then it updates the score and scoreLabel.
     * It also removes the wordBox and clears the typed list.
     * @param s Word to check
     */
    // to check for all the active word in the wordbox
    // to increse the level if the word is present and to remove the word from the screen
    private void checkForCorrectWord(String s) {
        for (WordBox currentWord : activeWords){
            if(s.equals(currentWord.getWord())) {
                score++;
                scoreLabel.setText(Integer.toString(score));
                removeWord(currentWord);
                typedLabel.setText(null);
                typed.clear();
                return;
            }
        }
    }
}
