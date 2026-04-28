package com.literature.russian_literature.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {
    private static final Logger LOG = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file, String subfolder) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "russian-literature/" + subfolder,
                        "public_id", UUID.randomUUID().toString(),
                        "resource_type", "auto",
                        "overwrite", true
                ));
        return uploadResult.get("secure_url").toString();
    }

    public void deleteFile(String publicId, String resourceType) throws IOException {
        if (publicId == null || publicId.isBlank()) {
            LOG.warn("Attempted to delete file with empty publicId, skipping");
            return;
        }
        LOG.info("Deleting file from Cloudinary: {} (type: {})", publicId, resourceType);
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", resourceType));
    }

    public static String extractPublicIdFromUrl(String url) {
        String[] parts = url.split("/upload/");
        if (parts.length < 2) return null;
        String path = parts[1];

        if (path.startsWith("v")) {
            int slashIndex = path.indexOf('/');
            if (slashIndex != -1) path = path.substring(slashIndex + 1);
        }

        int dotIndex = path.lastIndexOf('.');
        if (dotIndex != -1) path = path.substring(0, dotIndex);
        return path;
    }
}
