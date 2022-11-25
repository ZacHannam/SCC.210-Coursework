package uk.pixtle.util;

import org.json.JSONObject;

public interface JSONImport {

    public void loadObjectFromJSON(JSONObject json);
    public JSONObject toJSON();

}
