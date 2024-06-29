/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package games.rednblack.h2d.common.vo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import games.rednblack.editor.renderer.utils.HyperJson;

import java.util.HashMap;
import java.util.Map;

public class EditorConfigVO {
    private static final Json json = HyperJson.getJson();

    public static final String EDITOR_CONFIG_FILE = "config.pit";
    public String lastOpenedSystemPath = "";
    public String lastImportedSystemPath = "";
    public String keyBindingLayout = "default";

    public long totalSpentTime = 0;

    public boolean disableAmbientComposite = true;
    public boolean showBoundingBoxes = false;
    public float scrollVelocity = 30f;
    public boolean autoSave = false;
    public boolean enablePlugins = true;
    public Color backgroundColor = new Color(0.15f, 0.15f, 0.15f, 1.0f);

    public float uiScaleDensity = 1f;

    public int msaaSamples = 4;
    public int fpsLimit = 60;
    public boolean useANGLEGLES2 = true;
    public boolean failSafeException = false;

    //Map to store plugin storage
    public Map<String, Map<String, Object>> pluginStorage = new HashMap<>();
    public HashMap<String, Boolean> enabledFilters = new HashMap<>();

    public String constructJsonString() {
        String str = "";
        json.setOutputType(JsonWriter.OutputType.json);
        str = json.toJson(this);
        return str;
    }
}
