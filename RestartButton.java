import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Separate class for the Restart Button
//hello
public class RestartButton extends JButton {

    public GamePanel gamePanel;

    // Constructor for the restart button
    public RestartButton(GamePanel gamePanel) {
        super("RESTART");
        this.gamePanel = gamePanel;  // Reference to the game panel

        // Set properties for the rectangular button
        this.setFont(new Font("SansSerif", Font.PLAIN, 15 ));  // Set the font
        this.setFocusable(false);  // Disable focus on the button
        this.setBackground(Color.LIGHT_GRAY);  // Set background color
        this.setForeground(Color.BLACK);  // Set text color

        // Set size and position for the rectangular button
        this.setBounds((GamePanel.SCREEN_WIDTH - 200) / 2, GamePanel.SCREEN_HEIGHT / 2 + 50, 200, 50);
        
        // Add a border to emphasize the button's shape (optional)
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Add ActionListener for the button
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.restartGame();  // Call the restart method on the game panel
            }
        });
    }
}

