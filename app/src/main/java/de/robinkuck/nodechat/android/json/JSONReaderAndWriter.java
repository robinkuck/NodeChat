package de.robinkuck.nodechat.android.json;

import android.os.AsyncTask;
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

    private class JSONWriter extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {
            FileUtils.writeToFile(fileName, jsonObjects[0].toString(), false);
            return null;
        }
    }
}
