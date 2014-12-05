package bynull.realty.components;

import bynull.realty.config.Config;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author dionis on 05/12/14.
 */
@Component
public class AmazonS3ComponentImpl implements AmazonS3Component, InitializingBean, DisposableBean {
    private static final String         accessKey   = "AKIAJ7GFF3NP7EHZHYTA";
    private static final String         sercetKey   = "o0YklIRxXHrgwtZuJ6WCLQ2sY5L5CQLIF0s628Dq";
    private static final String         bucketName  = "rent4.me";
    private static final Logger LOGGER      = LoggerFactory.getLogger(AmazonS3ComponentImpl.class);

    //https://hellomobil.ee.s3.amazonaws.com/dev%2Fimages%2FPlaces%2F6ce12f13-aabf-4aaa-9178-c08e028d7951%2F6ce12f13-aabf-4aaa-9178-c08e028d7951.jpg?Expires=1376317551&
    // AWSAccessKeyId=AKIAIPBXWLN46TR423DA&Signature=M6cgeW6LZfch4P5LhMlKw53YGM4%3D

    @Resource
    private Config config ;
    private AmazonS3Client s3Client;

    @Override
    public void afterPropertiesSet() throws Exception {
        s3Client = new AmazonS3Client( new BasicAWSCredentials(accessKey, sercetKey));
    }

    @Override
    public void destroy() throws Exception {
        s3Client.shutdown();
    }

    public void storeObject( byte[] object, String objectName, String objectId){
        storeObjectAccessibleForRead( object, objectName, objectId, "image/jpeg");
    }

    private String getPath(String objectName, String objectId) {
        return getFolder() + objectId + "/" + objectName;
    }

    @Override
    public void storeObjectAccessibleForRead( byte[] object, String objectName, String objectId, String contentType){
        ObjectMetadata metadata = new ObjectMetadata();
        Assert.notNull(object);
        InputStream input = new ByteArrayInputStream(object) ;
        metadata.setContentLength(object.length);
        metadata.setContentType(contentType);


        try{

            PutObjectRequest por = new PutObjectRequest( bucketName, getPath(objectName, objectId), input , metadata );
            por.setCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(por);
        }catch(AmazonServiceException ase){
            LOGGER.error("Error Message: " + ase.getMessage());

            throw new RuntimeException(ase);
        } finally {
            IOUtils.closeQuietly(input);

        }
    }

    private String getFolder() {
        return config.getS3Folder() + "/images/";
    }

    @Override
    public byte[] getObject(String objectName, String objectId){

        // Process the objectData stream.
        InputStream objectData = null;
        byte[] bytes = null;
        try {
            S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, getPath(objectName, objectId)));
            objectData = object.getObjectContent();
            try {
                bytes = IOUtils.toByteArray(objectData);
            } catch (IOException e) {
                LOGGER.error("Error Message: " + e.getMessage());
            }


        } catch (AmazonServiceException ace) {
            LOGGER.error("Error Message: " + ace.getMessage());
            throw new RuntimeException(ace);
        }
        finally {
            IOUtils.closeQuietly(objectData);
        }


        return bytes;
    }

    @Override
    public void deleteObject(String objectName, String objectId){
        try{
            s3Client.deleteObject(bucketName, getPath(objectName, objectId));
        }catch(AmazonServiceException ace){
            LOGGER.error("Error Message: " + ace.getMessage());
            throw new RuntimeException(ace);
        }

    }

    @Override
    public String getObjectUrlForPublicFile(String objectName, String imageId) {
        return getBaseUrl() + "/" + getPath(objectName, imageId);
    }

    private String getBaseUrl() {
        return "http://"+bucketName+".s3.amazonaws.com";
    }
}
