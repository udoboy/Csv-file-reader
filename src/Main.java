import org.w3c.dom.html.HTMLImageElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        //CSV = Comma-Separated values
        JFrame frame = new JFrame("Group 6 file reader");

        JPanel panel = new JPanel(new GridLayout(5,0,10,10));
        Color trans = new Color( 0, 0, 0, 0 );
        panel.setBackground(trans);

        JLabel title = new JLabel("C.S.V File reader");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        title.setForeground(Color.white);

        JLabel detOne = new JLabel("University of Nigeria Nsukka");
        detOne.setForeground(Color.white);
        JLabel detTwo = new JLabel("Department of computer science");
        detTwo.setForeground(Color.white);
        JLabel detThree = new JLabel("Group 6");
        detThree.setForeground(Color.white);

        JButton startBtn = new JButton("START");
        startBtn.setBackground(Color.white);
        startBtn.setFocusable(false);
        startBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                 new ReaderUi();
                frame.dispose();
            }
        });

        panel.add(title);
        panel.add(detOne);
        panel.add(detTwo);
        panel.add(detThree);
        panel.add(startBtn);

        //setting the background image
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src\\background_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image dimg = img.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);

        ImageIcon myIcon = new ImageIcon("src\\icon.png");

        //customizing the frame
        frame.setMinimumSize(new Dimension(500,500));
        frame.setContentPane(new JLabel(imageIcon));
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.blue);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setIconImage(myIcon.getImage());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}