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

package games.rednblack.h2d.common.plugins;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import games.rednblack.puremvc.Facade;
import net.mountainblade.modular.Module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by azakhary on 7/24/2015.
 */
public abstract class H2DPluginAdapter implements H2DPlugin, Module {

    public Facade facade;
    protected PluginAPI pluginAPI;
    protected String name;

    public H2DPluginAdapter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setAPI(PluginAPI pluginAPI) {
        this.pluginAPI = pluginAPI;
        facade = pluginAPI.getFacade();
    }

    /**
     * Triggered whenever a context menu is displayed
     *
     * @param selectedEntities list of entities that were selected when context menu was created about, if right clicked on empty space empty array is used
     * @param actionsSet list of current actions (notification id's) planned for this particular context menu, it can be modified by adding or removing elements.
     */
    @Override
    public void onDropDownOpen(Set<Integer> selectedEntities, Array<String> actionsSet) {

    }

    public PluginAPI getAPI() {
        return pluginAPI;
    }

    public Map<String, Object> getStorage() {
        return pluginAPI.getEditorConfig().pluginStorage.computeIfAbsent(name, k -> new HashMap<>());
    }

    /**
     * Loads a texture atlas bundled at the root of the plugin jar ({@code name.atlas} + {@code name.png}).
     * The jar is loaded by its own class loader, so libGDX cannot open it as a classpath file: both files
     * are copied into the editor cache dir first. Call it from {@link #initPlugin()} or later, once the API is set.
     *
     * @return the atlas, or null when the resources are missing or unreadable (already logged)
     */
    protected TextureAtlas loadPluginAtlas(String atlasName) {
        try {
            FileHandle atlasFile = extractPluginResource(atlasName + ".atlas");
            extractPluginResource(atlasName + ".png");
            return new TextureAtlas(atlasFile);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** @return drawable of a region in a plugin atlas, or null (e.g. for a text-only menu item) when unavailable */
    protected static Drawable atlasDrawable(TextureAtlas atlas, String region) {
        if (atlas == null) return null;
        TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion(region);
        return atlasRegion == null ? null : new TextureRegionDrawable(atlasRegion);
    }

    private FileHandle extractPluginResource(String fileName) throws IOException {
        File target = new File(pluginAPI.getCacheDir(), fileName);
        try (InputStream in = getClass().getResourceAsStream("/" + fileName)) {
            if (in == null) throw new IOException("Missing plugin resource: " + fileName);
            Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        target.deleteOnExit();
        return new FileHandle(target);
    }
}
