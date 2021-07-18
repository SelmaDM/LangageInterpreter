package ex;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

public class Interpreter {

    final double initDirection = 180;

    double x;
    double y;
    double direction;

    Graphics gc;

    HashMap<String, Node> procedures;

    Color colors[] = {
        Color.BLACK,
        Color.BLUE,
        Color.CYAN,
        Color.DARK_GRAY,
        Color.GRAY,
        Color.GREEN,
        Color.LIGHT_GRAY,
        Color.MAGENTA,
        Color.ORANGE,
        Color.PINK,
        Color.RED,
        Color.WHITE,
        Color.YELLOW
    };

    void interpreter(String s, double x, double y, Graphics gc) throws Exception {
        System.out.println();
        this.gc = gc;
        this.x = x;
        this.y = y;
        direction = initDirection;
        procedures = new HashMap();

        Lexer l = new Lexer();
        SourceReader S = new SourceReader(s);
        Parser p = new Parser();
        evalRoot(p.analyse(l.lexer(S)));

    }

    void evalRoot(Node root) {
        Iterator<Node> it = root.getChildren();
        while (it.hasNext()) {
            Node n = it.next();
            if (n.getCl() == TokenClass.nProc) {
                procedures.put(n.getValue(), n);
            } else {
                eval(n);
            }
        }
    }

    void eval(Node n) {
        Iterator<Node> it = n.getChildren();
        switch (n.getCl()) {
            case nBlock:
                while (it.hasNext()) {
                    eval(it.next());
                }
                break;
            case nForward:
                forward(Integer.valueOf(n.getValue()));
                break;
            case nLeft:
                direction = (direction + Integer.valueOf(n.getValue())) % 360;
                break;
            case nRight:
                direction = (direction - Integer.valueOf(n.getValue())) % 360;
                break;
            case nColor:
                gc.setColor(colors[Integer.valueOf(n.getValue())]);
                break;

            case nRepeat:
                int count = Integer.valueOf(n.getValue());
                Node nodeToRepeat = it.next();
                for (int i = 0; i < count; i++) {
                    eval(nodeToRepeat);
                }
            break;
            case nCall:
                Node np = procedures.get(n.getValue()).getChildren().next();
                eval(np);
                break;
        }
    }

    void forward(double length) {
        double destX = x + Math.sin(direction*Math.PI*2/360) * length;
        double destY = y + Math.cos(direction*Math.PI*2/360) * length;
        gc.drawLine((int)x, (int)y, (int)destX, (int)destY);
        x = destX;
        y = destY;
    }


}
