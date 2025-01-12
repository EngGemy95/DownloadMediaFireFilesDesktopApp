package downloadfile;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MohamedGamal
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaFireDownloader {

    // Progress Listener Interface
    public interface ProgressListener {
        void onProgressUpdate(int percentage);
    }

    // Method to download a MediaFire file
    public void downloadMediaFireFile(String mediaFireUrl, String saveFilePath, ProgressListener listener) {
        new Thread(() -> {
            try {
                // Step 1: Extract the direct download link
                String downloadUrl = getDirectDownloadLink(mediaFireUrl);
                if (downloadUrl == null) {
                    System.out.println("Failed to retrieve the download URL.");
                    return;
                }

                // Step 2: Download the file and track progress
                downloadFile(downloadUrl, saveFilePath, listener);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // Extract the direct download link from a MediaFire URL
    private String getDirectDownloadLink(String mediaFireUrl) throws IOException {
        // Fetch and parse the HTML of the MediaFire page
        Document doc = Jsoup.connect(mediaFireUrl).userAgent("Mozilla/5.0").get();

        // Locate the download button or link
        Element downloadButton = doc.selectFirst("a#downloadButton");
        if (downloadButton != null) {
            return downloadButton.attr("href"); // Extract the direct download link
        }
        return null; // Return null if no download link is found
    }

    // Download the file with progress tracking
    private void downloadFile(String fileUrl, String saveFilePath, ProgressListener listener) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(fileUrl).openConnection();
        connection.connect();

        if (connection.getResponseCode() / 100 == 2) { // Check if response code is 2xx
            int fileSize = connection.getContentLength(); // Total file size
            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(saveFilePath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                int totalBytesRead = 0;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    // Calculate and update progress
                    if (fileSize > 0 && listener != null) {
                        int progress = (int) (((double) totalBytesRead / fileSize) * 100);
                        listener.onProgressUpdate(progress);
                    }
                }
            }
        } else {
            throw new IOException("Failed to download file, HTTP response code: " + connection.getResponseCode());
        }
        connection.disconnect();
    }
      
}
