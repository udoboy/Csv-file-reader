import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReaderUi {
    JLabel filePathLabel;
    File selectedFile;
    Color backgroundColor;

    JLabel contentLabel;
    public JFrame frame = new JFrame("Group 6 " +
            " reader");
    public JPanel contentPanel = new JPanel();

    public ReaderUi() {
        ImageIcon myIcon = new ImageIcon("src\\icon.png");
        backgroundColor = Color.decode("#18AA57");

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBackground(backgroundColor);

        JLabel titleLabel = new JLabel("Csv Reader");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        titleLabel.setForeground(Color.white);

        JButton chooseButton = new JButton("Choose a file");
        chooseButton.setFocusable(false);

        JPanel detailsPanel = new JPanel(new GridLayout(1, 2));
        detailsPanel.setBackground(backgroundColor);

        filePathLabel = new JLabel("C:\\");
        filePathLabel.setPreferredSize(new Dimension(100, filePathLabel.getHeight()));
        detailsPanel.add(filePathLabel);

        JButton readBtn = new JButton("Read");
        readBtn.setEnabled(false);
        readBtn.setFocusable(false);
        detailsPanel.add(readBtn);

        mainPanel.add(titleLabel);
        mainPanel.add(chooseButton);
        mainPanel.add(detailsPanel);

        JPanel contentPanel = new JPanel();

        contentLabel = new JLabel();
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.getContentPane().setBackground(backgroundColor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(myIcon.getImage());
        frame.add(mainPanel);

        frame.setVisible(true);

        chooseButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    selectedFile = FileHandler.load(frame);
                    if (selectedFile != null) {
                        readBtn.setEnabled(true);
                        String fileName = selectedFile.getName();
                        filePathLabel.setText(fileName);
                    } else {
                        readBtn.setEnabled(false);
                        filePathLabel.setText("C:\\");
                    }
                } catch (Exception e) {
                    // throw new RuntimeException(e);
                    e.printStackTrace();
                }
            }
        });

        readBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                readCsv(selectedFile);
            }
        });

    }

    /** reads the content of the selected csv file and displays it **/


    public void readCsv(File file) {
        if (file != null) {
            String filePath = file.getAbsolutePath();
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(filePath));
                String line;
                StringBuilder content = new StringBuilder();


                while ((line = reader.readLine()) != null) {
                    String[] fileArray = line.split("\n");
                    for (String item : fileArray) {
                        String formattedLine = item.replace(",", "  \t");
                        content.append(formattedLine).append("<br>");
                    }
                    content.append("<br>");
                }

                contentLabel.setText("<html>" + content + "</html>");
                JScrollPane scrollPane = new JScrollPane(contentLabel);
                scrollPane.setPreferredSize(new Dimension(300, 300));
                contentPanel.add(scrollPane);
                frame.add(contentPanel, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



}
