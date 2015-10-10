import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class BackendFileUpload {
    public static void main(String[] args) {
        String server = "www.truellprojects.com";
        int port = 21;
        String user = "truell20";
        String pass = "Google2!";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File[] localFiles = new File("../../Backend/").listFiles();

            for(int a = 0; a < localFiles.length; a++) {
                String remoteFile = "ApPosition/"+localFiles[a].getName();
                InputStream inputStream = new FileInputStream(localFiles[a]);

                System.out.println("Start uploading file " + a);
                boolean done = ftpClient.storeFile(remoteFile, inputStream);
                inputStream.close();
                if (done) {
                    System.out.println("File " + a + "is uploaded successfully.");
                }
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}