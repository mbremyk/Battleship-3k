package effects;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class DownScaler {
    private ScaleTransition scaleTransition;

    public DownScaler(Node node) {
        scaleTransition = new ScaleTransition(Duration.millis(50),node);
        scaleTransition.setFromX(4);
        scaleTransition.setFromY(4);
        scaleTransition.setByX(-3);
        scaleTransition.setByY(-3);
//        scaleTransition.setCycleCount(4);
//        scaleTransition.setAutoReverse(true);
    }

    public void play(){
        scaleTransition.playFromStart();
    }
}
