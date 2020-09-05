package wendergalan.github.io.desafiosambatech.service;

import java.io.File;

public interface BucketService {
    public boolean deleteFile(String fileUrl);

    public String uploadFile(File file) throws Exception;
}
