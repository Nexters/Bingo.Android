package com.thanksbingo.icondownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class IconDownloader {

//    public interface AfterDownloadingIcon {
//
//        public void afterDownloadingIcon();
//    }

    public class NoIconUrlException extends Exception {
        public NoIconUrlException(String message) {
            super(message);
        }
    }

    public class UndecidedIconsDirPath extends Exception {
        public UndecidedIconsDirPath(String message) {
            super(message);
        }
    }

    private class BitmapAndFileName {
        public Bitmap bitmap;
        public String url;
    }

    //private Context context;
    private ArrayList<String> iconURLs;
    private String dirPath;         // Format "/~~~/~~~/.../~~~/"

//    private AfterDownloadingIcon callback;

    public IconDownloader(ArrayList<String> _iconURLs, String _dirPath) {

        iconURLs = _iconURLs;
        dirPath = _dirPath;
    }

    public IconDownloader(String _dirPath) {

        iconURLs = null;
        dirPath = _dirPath;
    }

//    public void setAfterDownloadingIconCallback(AfterDownloadingIcon _cb) {
//        callback = _cb;
//    }

    public void setIconURLs(ArrayList<String> _iconURLs) {

        iconURLs = _iconURLs;
    }

    public void setDirPath(String _dirPath) {

        dirPath = _dirPath;
    }

    public String downloadIconImage(String iconUrl) throws NoIconUrlException, UndecidedIconsDirPath {

        if (iconUrl == null) {
            throw new NoIconUrlException("There is no target to download.");
        }
        if (dirPath == null || dirPath.compareTo("") == 0) {
            throw new UndecidedIconsDirPath("Undecided path of directory where store icon image file.");
        }

//        ArrayList<String> t_iconURLs = new ArrayList<String>();
//        t_iconURLs.add(iconUrl);
//        new ImageDownloader().execute(t_iconURLs);

        BitmapAndFileName data = new BitmapAndFileName();
        data.url = iconUrl;
        data.bitmap = downloadBitmap(data.url);

        String fileName = generateComplicateFileName(data.url);

        String iconsStoragePath = Environment.getExternalStorageDirectory() + dirPath;
        File sdIconStorageDir = new File(iconsStoragePath);
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + "/" + fileName;
            File f = new File(filePath);
            if (f.exists())
                return fileName;
            else {

                FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                data.bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);

                bos.flush();
                bos.close();
            }

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
        }

        return fileName;
    }

    private Bitmap downloadBitmap(String url) {

        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        try {

            HttpResponse response = client.execute(getRequest);

            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" + " retrieving bitmap from " + url + e.toString());
        }

        return null;
    }













    public ArrayList<String> downloadIconImages() throws NoIconUrlException, UndecidedIconsDirPath {

        if (iconURLs == null || iconURLs.size() == 0) {
            throw new NoIconUrlException("There is no target to download.");
        }
        if (dirPath == null || dirPath.compareTo("") == 0) {
            throw new UndecidedIconsDirPath("Undecided path of directory where store icon image file.");
        }

        new ImageDownloader().execute(iconURLs);

        ArrayList<String> fileNameList = new ArrayList<String>();
        for (int i = 0; i < iconURLs.size(); i++) {
            fileNameList.add(generateComplicateFileName(iconURLs.get(i)));
        }

        return fileNameList;
    }

    private class ImageDownloader extends AsyncTask<ArrayList<String>, Void, ArrayList<BitmapAndFileName>> {

        @Override
        protected ArrayList<BitmapAndFileName> doInBackground(ArrayList<String>... urlList) {

            ArrayList<BitmapAndFileName> dataToGoOnPostExecute = new ArrayList<BitmapAndFileName>();

            int urlCount = urlList[0].size();
            for (int i = 0; i < urlCount; i++) {
                BitmapAndFileName data = new BitmapAndFileName();
                data.url = urlList[0].get(i);
                data.bitmap = downloadBitmap(data.url);
                dataToGoOnPostExecute.add(data);
            }

            return dataToGoOnPostExecute;
        }

        @Override
        protected void onPostExecute(ArrayList<BitmapAndFileName> iconData) {

            // save icon images on external storage
            int dataLength = iconData.size();
            for (int i = 0; i < dataLength; i++) {

                Bitmap iconBitmap = iconData.get(i).bitmap;
                String fileName = generateComplicateFileName(iconData.get(i).url);

                String iconsStoragePath = Environment.getExternalStorageDirectory() + dirPath;
                File sdIconStorageDir = new File(iconsStoragePath);
                sdIconStorageDir.mkdirs();

                try {
                    String filePath = sdIconStorageDir.toString() + "/" + fileName;
                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                    BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                    iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);

                    bos.flush();
                    bos.close();

                } catch (FileNotFoundException e) {
                    Log.w("TAG", "Error saving image file: " + e.getMessage());
                } catch (IOException e) {
                    Log.w("TAG", "Error saving image file: " + e.getMessage());
                }
            }
        }

        private Bitmap downloadBitmap(String url) {

            final DefaultHttpClient client = new DefaultHttpClient();
            final HttpGet getRequest = new HttpGet(url);

            try {

                HttpResponse response = client.execute(getRequest);

                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" + " retrieving bitmap from " + url + e.toString());
            }

            return null;
        }
    }

    private static String getExtOfFile(String fileName) {

        String[] temp = fileName.split("\\.");
        String ext = temp[temp.length-1];

        return ext;
    }

    private static String generateComplicateFileName(String image_url) {

        String fileName = image_url;
        //String ext = getExtOfFile(fileName);
//        String ext = "png";
//        fileName = generateSHA256(fileName) + "." + ext;
        fileName = generateSHA256(fileName);

        return fileName;
    }

    private static String generateSHA256(String key){
        String SHA = "";
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(key.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }
}
