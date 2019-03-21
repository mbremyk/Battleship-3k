package effects;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Scaler {
    private ScaleTransition scaleTransition;

    public Scaler(Node node) {
        scaleTransition = new ScaleTransition(Duration.millis(100),node);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setByX(0.8);
        scaleTransition.setByY(0.8);
        scaleTransition.setCycleCount(4);
        scaleTransition.setAutoReverse(true);
    }

    public void play(){
        scaleTransition.playFromStart();
    }
}
