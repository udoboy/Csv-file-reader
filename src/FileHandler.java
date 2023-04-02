import javax.swing.*;
import java.io.*;

public class FileHandler {
    /** this function shows the file chooser through which user can select a file from the system memory
     * it returns the selected file **/
    public static File load(JFrame window) throws IOException, ClassNotFoundException {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.showDialog(window, "Choose a file");
        File selectedFile = jFileChooser.getSelectedFile();
        if (selectedFile != null){
            String filePath = selectedFile.getAbsolutePath();
            if (filePath.endsWith(".csv")){
                //the user should only be able to choose csv files
                File file = selectedFile;
                return file;
            }
            else{
                JOptionPane jOptionPane = new JOptionPane();
                jOptionPane.setMessage(JOptionPane.ERROR_MESSAGE);
                jOptionPane.setMessage("File selected was not a csv file please try again");
                JDialog dialog = jOptionPane.createDialog(window, "Error");
                dialog.setVisible(true);
                return null;
            }

        }
        else {
            return null;
        }


    }
}
