package effects;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shaker {
    private TranslateTransition translateTransition;

    public Shaker(Node node) {
        double startX = node.getTranslateX();
        double startY = node.getTranslateY();
        translateTransition = new TranslateTransition(Duration.millis(100),node);
        translateTransition.setFromX(startX);
        translateTransition.setFromY(startY);
        translateTransition.setByX((1-Math.round(Math.random())*2)*(10f+Math.random()*30));
        translateTransition.setByY((1-Math.round(Math.random())*2)*(10f+Math.random()*30));
        translateTransition.setCycleCount(4);
        translateTransition.setAutoReverse(true);
    }

    public void shake(){
        translateTransition.playFromStart();
    }
}
