package spacegame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Timo Strating on 19-Oct-16.
 */
public class Window extends Canvas {
//    private static final long serialVersionUID = 1248653701434724L;

    public Window(Game game, String title) {
        JFrame frame = new JFrame(title);

        frame.setPreferredSize(game.bounds);
        frame.setMaximumSize(game.bounds);
        frame.setMinimumSize(game.bounds);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener( game );  // InputManager.getInstance()
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.add(game);
        frame.setVisible(true);
    }
}
