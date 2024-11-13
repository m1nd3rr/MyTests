package com.example.mytest;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryManager {

    private static Cloudinary cloudinary;

    public static void initCloudinary() {
        if (cloudinary == null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dkomf6xhr");
            config.put("api_key", "321419335523648");
            config.put("api_secret", "4-0b3gnQjADcJAlQDDzaWBHMlnI");

            cloudinary = new Cloudinary(config);
        }
    }

    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            initCloudinary();
        }
        return cloudinary;
    }
}
