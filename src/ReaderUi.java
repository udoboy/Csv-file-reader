import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

public class ReaderUi implements DropTargetListener {
    JLabel filePathLabel;
    File selectedFile;
    Color backgroundColor;

    JLabel contentLabel;
    public JFrame frame = new JFrame("Group 6 " +
            " reader");
    public JPanel contentPanel = new JPanel();
    TableModel tableModel;
    JTable table;

    JScrollPane scrollPane;

    Thread readerThread;
    Runnable readerRunnable;
    JButton readBtn;

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

        readBtn = new JButton("Read");
        readBtn.setEnabled(false);
        readBtn.setFocusable(false);
        detailsPanel.add(readBtn);

        JLabel label = new JLabel("Or drop a csv file here");
        JPanel dropPanel = new JPanel();
        dropPanel.setLayout(new GridBagLayout());
        dropPanel.add(label);
        dropPanel.setBackground(Color.decode("#bcc2be"));
        dropPanel.setPreferredSize(new Dimension(100,50));

        new DropTarget(dropPanel, this);

        mainPanel.add(titleLabel);
        mainPanel.add(chooseButton);
        mainPanel.add(dropPanel);
        mainPanel.add(detailsPanel);

        scrollPane = new JScrollPane();

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
               load("choose", null);
            }
        });

        readBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //readCsv(selectedFile);
                readIntoTable(selectedFile);
            }
        });

    }

    /** checks the file selected by the user either through drag and drop
     * or by file chooser and sets the selected file to the current file so that once the read button is clicked
     * the selected file is read and displayed**/
    public void load(String method, File file){
        if (method.equals("drop")){
            if (file.getAbsolutePath().endsWith(".csv")){
                selectedFile = file;
                if (selectedFile != null){
                    readBtn.setEnabled(true);
                    String fileName = selectedFile.getName();
                    filePathLabel.setText(fileName);
                }
                else{
                    readBtn.setEnabled(false);
                    filePathLabel.setText("C:\\");
                }
            }
            else{
                JOptionPane jOptionPane = new JOptionPane();
                jOptionPane.setMessage(JOptionPane.ERROR_MESSAGE);
                jOptionPane.setMessage("File selected was not a csv file please try again");
                JDialog dialog = jOptionPane.createDialog(frame, "Error");
                dialog.setVisible(true);
            }
        }
        else{
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
    }

    /** reading the contents of the csv file into a table using a worker thread in order to prevent ui freeze
     * for large csv files**/
    public void readIntoTable(File file){
        readerRunnable  = new Runnable() {
            @Override
            public void run() {
            if (file != null) {
                String filePath = file.getAbsolutePath();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(filePath));
                    String line;
                    List<String>  columns = new ArrayList<>();
                    List<String[]> values = new ArrayList<String[]>();
                    int index =0;

                    while ((line = reader.readLine()) != null) {
                        String[] fileArray = line.split("\n");
                        //iterating through items in the file array after they have been split
                        //these items will be of the form {one,two,three,four} , {five,seven}
                        for (String item : fileArray) {
                            //splitting the items based on the commas and loading them into a list
                                String[] wordArray = item.split(",");
                                String[] arr = new String[wordArray.length];
                                System.out.println(wordArray.length);
                                int wordIndex = 0;
                                for (String word : wordArray){
                                    //loading the column names into a list
                                    if (index ==0){
                                        columns.add(word);
                                    }
                                    else{
                                        //loading the values into a list of arrays
                                        arr[wordIndex] = word;
                                        wordIndex ++;

                                    }
                                }

                                if (index != 0){
                                    values.add(arr);
                                }
                                index ++;
                            }


                        }

                    System.out.println(Arrays.toString(values.get(0)));
                    tableModel = new DefaultTableModel(values.toArray(new Object[][]{}), columns.toArray());

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            table = new JTable(tableModel);
                            scrollPane.setViewportView(table);
                            contentPanel.add(scrollPane);
                            frame.add(contentPanel, BorderLayout.CENTER);
                            frame.revalidate();
                            frame.repaint();
                        }
                    });


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
        };


        readerThread = new Thread(readerRunnable);
        readerThread.start();
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        // Accept copy drops
        dtde.acceptDrop(DnDConstants.ACTION_COPY);
        // Get the transfer which can provide the dropped item data
        Transferable transferable = dtde.getTransferable();
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        for (DataFlavor flavor: flavors){
            try {
                List<File>  files = (List<File>) transferable.getTransferData(flavor);

                load("drop", files.get(0));

            } catch (UnsupportedFlavorException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }


}
