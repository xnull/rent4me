package bynull.realty.components;

/**
 * @author dionis on 05/12/14.
 */
public interface ImageComponent {
    /**
     * Create image
     *
     * @param imageId
     * @param imageContent
     * @return url of image.
     */
    String createJpeg(String imageId, byte[] imageContent);

    void deleteJpeg(String imageId);

    void deleteJpegSilently(String imageId);

    /**
     * Resize
     *
     * @param imageContent
     * @return
     */
    byte[] createSmallThumbnail(byte[] imageContent);

    byte[] createMediumThumbnail(byte[] imageContent);

    byte[] createThumbnailWithSize(byte[] imageContent, int height, int width);
}
