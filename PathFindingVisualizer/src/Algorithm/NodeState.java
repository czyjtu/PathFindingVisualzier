package Algorithm;

import java.awt.*;

public enum NodeState{
    FREE(new Color(255,255,255)),
    WALL(new Color(0,0,0)),
    START(new Color(255, 165 ,0)),
    END(new Color(255, 0, 0)),
    CLOSED(new Color(64, 224, 208)),
    OPEN(new Color(0, 255, 0)),
    PATH(new Color(158, 66, 245)),
    ;

    private Color color;

    NodeState(Color color) {
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }


}
