/**
 * Adds a scaling effect to a Node. When play() is called this effect just about doubles the Node's scale and then
 * shrinks it to normal size 2 times in the next 100 milliseconds.
 *
 * @author Thorkildsen
 */


package effects;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Scaler {
    private ScaleTransition scaleTransition;

    /**
     * Initializes a new Scaler connected to a Node which will get the effect
     *
     * @param node the Node that will have the scaling effect applied to it
     */
    public Scaler(Node node) {
        scaleTransition = new ScaleTransition(Duration.millis(100),node);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setByX(0.8);
        scaleTransition.setByY(0.8);
        scaleTransition.setCycleCount(4);
        scaleTransition.setAutoReverse(true);
    }

    /**
     * Plays the scaling effect on the Node
     */
    public void play(){
        scaleTransition.playFromStart();
    }
}
