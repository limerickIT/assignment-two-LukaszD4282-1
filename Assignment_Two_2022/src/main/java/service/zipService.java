/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.File;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ldebi
 */
public class zipService {

    public ZipFile downloadZipFileWithoutPassword() throws ZipException {

        String filePath = (System.getProperty("user.dir") + "\\src\\main\\resources\\static\\assets\\images");

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(false);
        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
        ZipFile zipFile = new ZipFile("BeerImages.zip");
        zipFile.addFolder(new File(filePath), zipParameters);

        return zipFile;
    }
}
