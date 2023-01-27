package uk.pixtle.application.plugins.expansions;

import org.json.JSONObject;

public interface PluginSavableExpansion {

    public JSONObject save();
    public void load(JSONObject paramSavedJSON);

}
