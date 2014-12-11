package bynull.realty.components;

import com.google.common.collect.ImmutableList;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author dionis on 05/12/14.
 */
@Component
public class ImageComponentImpl implements ImageComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageComponentImpl.class);

    public static final int WIDTH = 268;
    public static final int HEIGHT = 268;
    @Resource
    AmazonS3Component amazonS3Component;

    @Override
    public String createJpeg(String imageId, byte[] imageContent) {
        String objectName = toObjectName(imageId);
        amazonS3Component.storeObjectAccessibleForRead(imageContent, objectName, imageId, "image/jpeg");
        return amazonS3Component.getObjectUrlForPublicFile(objectName, imageId);
    }

    private String toObjectName(String imageId) {
        return imageId + ".jpg";
    }

    private String getOrientationValue(byte[] imageContent) {
        try {
            IImageMetadata metadata = Sanselan.getMetadata(imageContent);
            if (metadata == null) {
                LOGGER.info("No meta-data for image available. Skipping");
                return null;
            }
            @SuppressWarnings("unchecked") List<TiffImageMetadata.Item> items = metadata.getItems();
            return (String) items
                    .stream()
                    .filter(e -> "Orientation".equalsIgnoreCase(e.getKeyword()))
                    .findFirst()
                    .map(ImageMetadata.Item::getText)
                    .orElseGet(() -> null);
        } catch (Exception e) {
            LOGGER.warn("Exception occurred while trying to get orientation value. Returning null", e);
            return null;
        }

    }

    /**
     * Get needed list of rotation's that should be applied to image
     *
     * @param imageContent
     * @return list of rotation operation's or empty collection if not needed
     */
    private List<Scalr.Rotation> getRotations(byte[] imageContent) {
        String orientationValue = getOrientationValue(imageContent);
        if (orientationValue == null) {
            LOGGER.warn("No orientation value present. Can not rotate");
            return Collections.emptyList();
        } else {
            switch (orientationValue) {
                case "1":
                    return ImmutableList.of();
                case "2":
                    return ImmutableList.of(Scalr.Rotation.FLIP_HORZ);
                case "3":
                    return ImmutableList.of(Scalr.Rotation.CW_180);
                case "4":
                    return ImmutableList.of(Scalr.Rotation.FLIP_VERT);
                case "5":
                    return ImmutableList.of(Scalr.Rotation.CW_90, Scalr.Rotation.FLIP_HORZ);
                case "6":
                    return ImmutableList.of(Scalr.Rotation.CW_90);
                case "7":
                    return ImmutableList.of(Scalr.Rotation.CW_270, Scalr.Rotation.FLIP_HORZ);
                case "8":
                    return ImmutableList.of(Scalr.Rotation.CW_270);
                default:
                    LOGGER.warn("Not supported image orientation value passed [{}]", orientationValue);
                    return Collections.emptyList();
            }
        }
    }

    @Override
    public void deleteJpeg(String imageId) {
        String objectName = toObjectName(imageId);
        amazonS3Component.deleteObject(objectName, imageId);
    }

    @Override
    public void deleteJpegSilently(String imageId) {
        try {
            deleteJpeg(imageId);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while trying to delete image with id [" + imageId + "] from Amazon S3", e);
        }
    }

    /**
     * NB!!! Original image content should be passed because it has EXIF metadata in it, which contains 'Orientation' in it.
     *
     * @param imageContent
     * @return
     */
    @Override
    public byte[] createSmallThumbnail(byte[] imageContent) {
        return createThumbnailWithSize(imageContent, HEIGHT, WIDTH);
    }

    @Override
    public byte[] createMediumThumbnail(byte[] imageContent) {
        return createThumbnailWithSize(imageContent, 640, 640);
    }

    @Override
    public byte[] createThumbnailWithSize(byte[] imageContent, int height, int width) {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(imageContent));

            //rotate original image, not the resized one.
            List<Scalr.Rotation> rotations = getRotations(imageContent);
            for (Scalr.Rotation rotation : rotations) {
                LOGGER.info("Rotating image [{}]", rotation);
                original = Scalr.rotate(original, rotation);
            }

            final Scalr.Mode resizeMode;
            if (original.getHeight() > original.getWidth()) {
                resizeMode = Scalr.Mode.FIT_TO_WIDTH;
            } else if (original.getHeight() < original.getWidth()) {
                resizeMode = Scalr.Mode.FIT_TO_HEIGHT;
            } else {
                resizeMode = Scalr.Mode.AUTOMATIC;
            }
            BufferedImage resizedImage = Scalr.resize(original, Scalr.Method.AUTOMATIC, resizeMode, width, height);
            int offsetX = (resizedImage.getWidth() - width) / 2;
            int offsetY = (resizedImage.getHeight() - height) / 2;
            resizedImage = Scalr.crop(resizedImage, offsetX, offsetY, width, height);

            ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", resultOutputStream);
            return resultOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
