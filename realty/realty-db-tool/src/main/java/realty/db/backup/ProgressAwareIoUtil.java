package realty.db.backup;

import com.dropbox.core.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author dionis on 2/25/15.
 */
public class ProgressAwareIoUtil {
    public static final int DefaultCopyBufferSize = IOUtil.DefaultCopyBufferSize;

    public static interface Progress {
        void onProgressStart(int chunkSize, long chunkSizeSoFar, long totalSizeBytes);
        void onProgressEnd(int chunkSize, long chunkSizeSoFar, long totalSizeBytes);
        void onUploadEnd();
    }

    public static void copyStreamToStream(InputStream in, OutputStream out, long totalSize, Progress progress)
            throws IOUtil.ReadException, IOUtil.WriteException
    {
        copyStreamToStream(in, out, DefaultCopyBufferSize, totalSize, progress);
    }

    public static void copyStreamToStream(InputStream in, OutputStream out, byte[] copyBuffer, long totalSize, Progress progress)
            throws IOUtil.ReadException, IOUtil.WriteException
    {
        long chunkSizeSoFar = 0;
        while (true) {
            int count;
            try {
                count = in.read(copyBuffer);
                chunkSizeSoFar += count;
                if (progress != null) {
                    progress.onProgressStart(count, chunkSizeSoFar, totalSize);
                }
            }
            catch (IOException ex) {
                throw new IOUtil.ReadException(ex);
            }

            if (count == -1) break;

            try {
                out.write(copyBuffer, 0, count);
                if (progress != null) {
                    progress.onProgressEnd(count, chunkSizeSoFar, totalSize);
                }
            }
            catch (IOException ex) {
                throw new IOUtil.WriteException(ex);
            }
        }
        if (progress != null) {
            progress.onUploadEnd();
        }
    }

    public static void copyStreamToStream(InputStream in, OutputStream out, int copyBufferSize, long totalSize, Progress progress)
            throws IOUtil.ReadException, IOUtil.WriteException
    {
        copyStreamToStream(in, out, new byte[copyBufferSize], totalSize, progress);
    }
}
