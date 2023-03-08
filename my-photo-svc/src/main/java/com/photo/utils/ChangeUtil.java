package com.photo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Base64;

@Slf4j
public class ChangeUtil {
    /**
     * 图片转base64
     * @param imagePath 图片路径
     * @return 返回base64字符串
     */
    public static String ImageToBase64Stream(String imagePath) throws IOException {
        String base64Output = null;
        FileInputStream fileInputStream = null;
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            fileInputStream = new FileInputStream(imagePath);
            int available = fileInputStream.available();
            byte[] bytes = new byte[available];
            fileInputStream.read(bytes);
            base64Output = encoder.encodeToString(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileInputStream.close();
        }
        return base64Output;
    }
}
