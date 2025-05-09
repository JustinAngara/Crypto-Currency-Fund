import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Label;
import java.awt.Font;

public class GUI {

  private JFrame frame;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          GUI window = new GUI();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public GUI() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 598, 482);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    
    Label label = new Label("Coin Disparities between exchanges");
    label.setFont(new Font("Arial", Font.PLAIN, 18));
    label.setAlignment(Label.CENTER);
    label.setBounds(0, 0, 582, 44);
    frame.getContentPane().add(label);
  }
  
  @SuppressWarnings("unused")
  private JFrame getFrame() {
    return this.frame;
  }
}
