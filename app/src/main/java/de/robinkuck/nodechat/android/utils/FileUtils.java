package de.robinkuck.nodechat.android.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtils {

    public static String readFromFile(final String fileName, final Context context) {
        String result = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receive = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receive = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receive);
                }
                inputStream.close();
                result = stringBuilder.toString();
                return result;
            }
        } catch (FileNotFoundException e) {
            Log.e("FileUtils", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("FileUtils", "Can not read file: " + e.toString());
        }
        return result;
    }

    public static void writeToFile(final String fileName, final String data, final Context context, final boolean append) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(fileName,
                            (append) ? Context.MODE_PRIVATE | Context.MODE_APPEND : Context.MODE_PRIVATE));
            System.out.println("[FileUtils] writing data: " + data);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("FileUtils", "File write failed: " + e.toString());
        }
    }

}
