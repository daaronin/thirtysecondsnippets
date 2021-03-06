/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;
import com.badlogic.gdx.files.FileHandle;

/**
 *
 * @author George
 */
public class Installation {
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id() {
        if (sID == null) {  
            String loc = null;
            if (Gdx.files.isLocalStorageAvailable()){
               loc = Gdx.files.getLocalStoragePath();
               FileHandle file = Gdx.files.getFileHandle(loc, Files.FileType.Internal);
                File installation = new File(file.path()+"/"+INSTALLATION);
            
                //FileHandle file = Gdx.files.internal(INSTALLATION);
                //File installation = new File(file.path());
                try {
                    if (!installation.exists())
                        writeInstallationFile(installation);
                    sID = readInstallationFile(installation);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}
