package com.example.dobble.release.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import static com.example.dobble.release.Cfg.BASE_STATIC_URL;

public final class Helpers {

    private static final int dirDepth = 2;
    private static final int symbolsPerSubDir = 3;
    /**
     * /root/size/dir1/dir2/.../dirN/someImg.jpg - Path pattern
     * @param id - img id
     * @returns {string} of evenly distributed directories
     */
    private static String buildImgSubDirPath(String id) {
        byte[] bytes = new byte[id.length()];
        int idInt = Integer.parseInt(id);
        for (int i = id.length() -1 ; i >= 0; i--) {
            bytes[i] = (byte) (idInt % 10);
            idInt /= 10;
        }
        String imgMd5Hash = new String(Hex.encodeHex(DigestUtils.md5(bytes)));

        StringBuilder subDirPath = new StringBuilder();
        for (int i = 0; i < dirDepth; i++) {
            subDirPath.append(imgMd5Hash.substring(i * symbolsPerSubDir, i * symbolsPerSubDir
                + symbolsPerSubDir));
            subDirPath.append("/");
        }
        // Remove tailing slash
        subDirPath = new StringBuilder(subDirPath.substring(0, subDirPath.length() - 1));
        return subDirPath.toString();
    }
    public static String buildPathByImgId(long id, String resolution) {
        String subDirPath = buildImgSubDirPath(String.valueOf(id));
        return BASE_STATIC_URL + resolution + "/" + subDirPath +
            "/" + id + ".jpg";
    }
}
