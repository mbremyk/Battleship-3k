/**
 * Adds a scaling effect to a Node. When play() is called this effect instantly scales the Node to 4 times its size
 * and then shrinks it to normal size in the next 100 milliseconds.
 *
 * @author Thorkildsen
 */

package effects;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class DownScaler {
    private ScaleTransition scaleTransition;

    /**
     * Initializes a new DownScaler connected to a Node which will get the effect
     *
     * @param node the Node that will have the scaling effect applied to it
     */
    public DownScaler(Node node) {
        scaleTransition = new ScaleTransition(Duration.millis(100),node);
        scaleTransition.setFromX(4);
        scaleTransition.setFromY(4);
        scaleTransition.setByX(-3);
        scaleTransition.setByY(-3);
    }

    /**
     * Plays the scaling effect on the Node
     */
    public void play(){
        scaleTransition.playFromStart();
    }
}
