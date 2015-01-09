package bynull.realty.components;

/**
 * @author dionis on 05/12/14.
 */
public interface AmazonS3Component {
    void storeObjectAccessibleForRead(byte[] object, String objectName, String objectId, String metaType);

    byte[] getObject(String objectName, String objectId);

    void deleteObject(String objectName, String objectId);

    String getObjectUrlForPublicFile(String objectName, String imageId);
}
