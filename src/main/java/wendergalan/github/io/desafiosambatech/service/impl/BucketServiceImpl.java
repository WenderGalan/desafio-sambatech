package wendergalan.github.io.desafiosambatech.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wendergalan.github.io.desafiosambatech.service.BucketService;
import wendergalan.github.io.desafiosambatech.service.MessageByLocaleService;

import java.io.File;

import static wendergalan.github.io.desafiosambatech.utility.Utility.generateFileName;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketServiceImpl implements BucketService {

    private final AmazonS3 s3Client;
    private final MessageByLocaleService message;

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${aws.endpointUrl}")
    private String endpointUrl;

    /**
     * Faz o upload do arquivo para o bucket
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public String uploadFile(File file) throws Exception {
        try {
            String fileName = generateFileName(file);
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
            return endpointUrl + "/" + bucketName + "/" + fileName;
        } catch (AmazonServiceException ase) {
            log.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            log.info("Error Message:    " + ase.getMessage());
            log.info("HTTP Status Code: " + ase.getStatusCode());
            log.info("AWS Error Code:   " + ase.getErrorCode());
            log.info("Error Type:       " + ase.getErrorType());
            log.info("Request ID:       " + ase.getRequestId());
            throw new Exception(message.getMessage("aws.upload.file.error"));
        } catch (AmazonClientException ace) {
            log.info("Caught an AmazonClientException: ");
            log.info("Error Message: " + ace.getMessage());
            throw new Exception(message.getMessage("aws.upload.file.error"));
        }
    }

    /**
     * Deleta o arquivo do bucket
     *
     * @param fileUrl
     * @return
     */
    public boolean deleteFile(String fileUrl) {
        try {
            String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
