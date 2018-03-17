package de.robinkuck.nodechat.android.json;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import de.robinkuck.nodechat.android.utils.FileUtils;

import static de.robinkuck.nodechat.android.utils.FileUtils.writeToFile;

public class JSONReaderAndWriter {

    private String fileName;

    public JSONReaderAndWriter(final String fileName) {
        this.fileName = fileName;
    }

    public void writeJSONObject(final JSONObject jsonObject, final Context context) {
        System.out.println("[JSONReaderAndWriter] writing JSON Object: " + jsonObject.toString());
        FileUtils.writeToFile(fileName, jsonObject.toString(), context, false);
    }

    public JSONObject readJSONObject(final Context context) {
        try {
            return new JSONObject(FileUtils.readFromFile(fileName, context));
        } catch (JSONException e) {

        }
        return new JSONObject();
    }

}
