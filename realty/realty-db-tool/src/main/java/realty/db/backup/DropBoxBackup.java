package realty.db.backup;

import com.dropbox.core.*;

import java.io.*;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dionis on 2/25/15.
 */
public class DropBoxBackup {

    private static final String CLIENT_IDENTIFIER = "DropBoxBackup Client/v1.0";
    private static final String APP_KEY = "xr3p3orgoqjnx76";
    private static final String APP_SECRET = "esf8x5tuhxwur3s";
    public static final String ACCESS_TOKEN = "mdy6p1sXnS4AAAAAAAAAD0Hr34gRsw_RXZcBw_c7txsgIzNZqnqSQvaVwYIHVxoA";
    public static class ConsoleProgress implements ProgressAwareIoUtil.Progress {

        private final AtomicInteger chunkCounter = new AtomicInteger();

        @Override
        public void onProgressStart(int chunkSize, long chunkSizeSoFar, long totalSizeBytes) {
            chunkCounter.incrementAndGet();
            String formattedString = String.format("\rProcessing chunk [%d] [%d / %d] (%.2f)%%", chunkSize, chunkSizeSoFar, totalSizeBytes, 100.0 * chunkSizeSoFar / totalSizeBytes);
            System.out.printf("%-100s", formattedString);
        }

        @Override
        public void onProgressEnd(int chunkSize, long chunkSizeSoFar, long totalSizeBytes) {
            System.out.printf(" [%s]", "Done");
            if (chunkCounter.get() % 100 == 0) {
                System.out.println();
            }
        }

        @Override
        public void onUploadEnd() {
            System.out.println();
            System.out.println("Upload ended");
        }
    };

    public void backup(String file, String targetDirectory) throws Exception {
        DbxClient client = getClient(ACCESS_TOKEN);

        File inputFile = new File(file);
        if(!inputFile.exists()) {
            throw new DbToolException("Specified input file '"+file+"' doesn't exist.");
        }
        if(!inputFile.canRead()) {
            throw new DbToolException("Specified input file '"+file+"' isn't readable.");
        }

        String targetDirectoryRemotePath = targetDirectoryRemotePath(targetDirectory);
        DbxEntry targetDirectoryMetaData = client.getMetadata(targetDirectoryRemotePath);
        final DbxEntry.Folder folder;
        if(targetDirectoryMetaData == null) {
            folder = client.createFolder(targetDirectoryRemotePath);
        } else if(targetDirectoryMetaData.isFolder()) {
            folder = targetDirectoryMetaData.asFolder();
        } else {
            throw new DbToolException("Specified folder doesn't exist");
        }


        try(FileInputStream inputStream = new FileInputStream(inputFile)) {
            String remotePath = remoteFilePath(inputFile, folder);
            DbxEntry fileMetaData = client.getMetadata(remotePath);

            if (fileMetaData == null) {
                final long length = inputFile.length();
                System.out.println("No file found. Starting file upload");
                DbxStreamWriter e = new DbxStreamWriter() {
                    @Override
                    public void write(NoThrowOutputStream out) throws Throwable {
                        ProgressAwareIoUtil.copyStreamToStream(inputStream, out, length, new ConsoleProgress());
                    }
                };
                DbxEntry.File uploadedFile = client.uploadFile(remotePath,
                        DbxWriteMode.add(), length, e);
                System.out.println("Uploaded: " + uploadedFile.toString());
            } else {
                throw new DbToolException("File already exists");
            }
        }
    }

    private DbxClient getClient(String accessToken) {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig(
                CLIENT_IDENTIFIER, Locale.getDefault().toString());
        return new DbxClient(config, accessToken);
    }

    private String remoteFilePath(File inputFile, DbxEntry.Folder folder) {
        return folder.path + "/" + inputFile.getName();
    }

    private String targetDirectoryRemotePath(String targetDirectory) {
        return "/" + targetDirectory;
    }

    public void showAccountInfo() throws DbxException {
        DbxClient client = getClient(ACCESS_TOKEN);
        showAccountInfo(client);
    }

    private void showAccountInfo(DbxClient client) throws DbxException {
        if (client != null && client.getAccountInfo() != null) {
            System.out.println("Linked account: " + client.getAccountInfo().displayName);
            System.out.println("User id: " + client.getAccountInfo().userId);
            System.out.println("Country: " + client.getAccountInfo().country);
            System.out.println("Quota: " + client.getAccountInfo().quota);
        } else {
            System.err.println("Couldn't acquire account information");
        }
    }
}
