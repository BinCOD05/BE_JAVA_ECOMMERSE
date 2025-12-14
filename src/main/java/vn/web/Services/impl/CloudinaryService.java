package vn.web.Services.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary ;

    public CloudinaryService(){
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dqkfttwu4",
                "api_key", "151963421813818",
                "api_secret", "P-sZYm_X3nVP_C7jZXi0TxwYSYE"
        )) ;
    }

    public String upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return (String) data.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload ảnh rồi ông giáo ạ!", e);
        }
    }
}
