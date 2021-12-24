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
import games.rednblack.editor.renderer.utils.HyperJson;

import java.util.ArrayList;

public class ProjectVO {

    public String projectName = "";

    public String projectVersion = null;

    public String projectMainExportPath = "";

    public String lastOpenScene = "";
    public String lastOpenResolution = "";
    public boolean lockLines = false;
    public float gridSize = 1;
    public Color backgroundColor = new Color(0,0,0,1);
    public boolean box2dDebugRender = false;

    public TexturePackerVO texturePackerVO = new TexturePackerVO();

    public ArrayList<SceneConfigVO> sceneConfigs = new ArrayList<>();

    public String constructJsonString() {
        String str = "";
        Json json = HyperJson.getJson();
        str = json.toJson(this);
        return str;
    }
}