package wendergalan.github.io.desafiosambatech.service;

import java.io.File;

public interface BucketService {
    public boolean deleteFile(String fileUrl);

    String uploadFile(File file, String fileName) throws Exception;

    public String uploadFile(File file) throws Exception;
}
