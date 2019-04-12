/**
 * Adds a shaking effect to a Node. When play() is called this effect move the Node 30 pixels to the right, then
 * back to its original position 2 times in the next 100 milliseconds.
 *
 * @author Thorkildsen
 */

package effects;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shaker {
    private TranslateTransition translateTransition;

    /**
     * Initializes a new Shaker connected to a Node which will get the effect
     *
     * @param node the Node that will have the shaking effect applied to it
     */
    public Shaker(Node node) {
        double startX = node.getTranslateX();
        double startY = node.getTranslateY();
        translateTransition = new TranslateTransition(Duration.millis(100), node);
        translateTransition.setFromX(startX);
        translateTransition.setFromY(startY);
        translateTransition.setByX(30);
        translateTransition.setByY(0);
        translateTransition.setCycleCount(4);
        translateTransition.setAutoReverse(true);
    }

    /**
     * Plays the shaking effect on the Node
     */
    public void play() {
        translateTransition.playFromStart();
    }
}
