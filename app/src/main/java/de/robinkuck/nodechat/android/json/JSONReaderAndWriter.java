package de.robinkuck.nodechat.android.json;

import org.json.JSONException;
import org.json.JSONObject;
import de.robinkuck.nodechat.android.utils.FileUtils;

public class JSONReaderAndWriter {

    private String fileName;

    public JSONReaderAndWriter(final String fileName) {
        this.fileName = fileName;
    }

    public void writeJSONObject(final JSONObject jsonObject) {
        System.out.println("[JSONReaderAndWriter] writing JSON Object: " + jsonObject.toString());
        FileUtils.writeToFile(fileName, jsonObject.toString(), false);
    }

    public JSONObject readJSONObject() {
        try {
            return new JSONObject(FileUtils.readFromFile(fileName));
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

}
