/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package downloadfile;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author MohamedGamal
 */
public class DownloadFile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(DownloadFile::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MediaFire Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JLabel instructions = new JLabel("Enter MediaFire link and click 'Download':", SwingConstants.CENTER);
        frame.add(instructions, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField linkField = new JTextField();
        panel.add(linkField, BorderLayout.CENTER);

        JButton downloadButton = new JButton("Download");
        panel.add(downloadButton, BorderLayout.EAST);

        frame.add(panel, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);

        downloadButton.addActionListener(e -> {
            String mediaFireUrl = linkField.getText().trim();
            if (mediaFireUrl.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a MediaFire link.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save File As");
            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                System.out.println(saveFile);
                
                MediaFireDownloader downloader = new MediaFireDownloader();
                // MediaFire link and file save path
                //String mediaFireUrl = "https://www.mediafire.com/file/your_file_link";
                //String saveFilePath = "C:/path/to/save/your_file.apk";
                String saveFilePath = saveFile.getAbsolutePath();

                // Call the downloader with a progress listener
                downloader.downloadMediaFireFile(mediaFireUrl, saveFilePath, percentage -> {
                    // Update the UI or print progress
                    statusLabel.setText("Download Progress: " + percentage + "%");
                    if(percentage == 100){
                        statusLabel.setText("Download File Completed.");
                    }
                });
            }
        });

        frame.setVisible(true);
    }

}
