package uk.pixtle.application.plugins.policies;

import org.json.JSONObject;

public interface PluginSavePolicy extends PluginPolicy {

    public JSONObject save();
    public void load(JSONObject paramSavedJSON);

}
