package uk.pixtle.application.plugins.policies;

import org.json.JSONObject;

public interface PluginSavePolicy extends PluginPolicy {

    public JSONObject save() throws Exception;
    public void load(JSONObject paramSavedJSON) throws Exception; // TO-DO exceptions

}
