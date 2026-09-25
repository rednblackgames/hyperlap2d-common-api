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

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.editor.renderer.ecs.Engine;
import games.rednblack.h2d.common.IItemCommand;
import games.rednblack.h2d.common.factory.IFactory;
import games.rednblack.h2d.common.view.tools.Tool;
import com.kotcrab.vis.ui.widget.VisImageButton;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.h2d.common.vo.CursorData;
import games.rednblack.h2d.common.vo.EditorConfigVO;
import games.rednblack.h2d.common.vo.ProjectVO;
import games.rednblack.h2d.common.vo.SceneConfigVO;
import games.rednblack.puremvc.Facade;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Plugin API is a main interface of communication between plugin and HyperLap2D editor
 * Created by azakhary on 7/24/2015.
 */
public interface PluginAPI {
    /**
     * Get scene loader of the runtime to work with scene and items
     * @return SceneLoader
     */
    SceneLoader getSceneLoader();

    /**
     * Returns MVC facade, to send notifications or commands, and register mediators
     * @return Facade
     */
    Facade getFacade();

    /**
     * Returns Ashley engine of main scene where all entities are located
     * @return Engine
     */
    Engine getEngine();

    /**
     * Get simple libGDX Stage for UI part of editor, to add dialogs or other UI elements and widgets
     * @return
     */
    Stage getUIStage();

    /**
     * @return Path of plugin directory
     */
    String getPluginDir();

    /**
     * @return Path of cache directory
     */
    String getCacheDir();

    /**
     * @return Path of working project
     */
    String getProjectPath();

    /**
     * @return TextureAtlas of loaded project
     */
    TextureAtlas.AtlasRegion getProjectTextureRegion(String regionName);

    String getPackNameFromRegion(String regionName);

    /**
     * Adds new sub menu item to the top bar
     * @param menu unique identifier to global menu items provided in @Overlap2DMenuBar by three constants FILE_MENU, EDIT_MENU, WINDOWS_MENU
     * @param subMenuName pretty string to name new submenu item
     * @param notificationName unique notification id that will be fired when this menu item is clicked
     */
    void addMenuItem(String menu, String subMenuName, String notificationName);

    /**
     * Adds new sub menu item to the top bar, with an icon shown next to its text
     * @param menu unique identifier to global menu items provided in @Overlap2DMenuBar by three constants FILE_MENU, EDIT_MENU, WINDOWS_MENU
     * @param subMenuName pretty string to name new submenu item
     * @param notificationName unique notification id that will be fired when this menu item is clicked
     * @param icon drawable rendered before the item text (22x22 px expected), null for a text-only item
     */
    void addMenuItem(String menu, String subMenuName, String notificationName, Drawable icon);

    /**
     * Adds new tool to the tool bar
     * @param toolName pretty string to name new tool item
     * @param toolBtnStyle tool button style
     * @param addSeparator true, if should add menu separator
     * @param tool the tool object that is going to be added
     */
    void addTool(String toolName, VisImageButton.VisImageButtonStyle toolBtnStyle, boolean addSeparator, Tool tool);


    /**
     * hot-swaps a tool
     */
    void toolHotSwap(Tool tool);

    /**
     * hot-swaps a tool back
     */
    void toolHotSwapBack();

    /**
     * Creates new menu item for Contextual drop down menu, that is created when user right clicks on something in the editor.
     * This only creates a menu item, but it should be specifically added later to action set, at the moment context menu is summoned
     * @param action unique name of notification id that will be fired when this menu item is clicked
     * @param name pretty text to be written on this menu item
     */
    void setDropDownItemName(String action, String name);

    /**
     * Same as {@link #setDropDownItemName(String, String)}, but also sets an icon shown next to the item text.
     * @param action unique name of notification id that will be fired when this menu item is clicked
     * @param name pretty text to be written on this menu item
     * @param icon drawable rendered before the item text (22x22 px expected), null for a text-only item
     */
    void setDropDownItemName(String action, String name, Drawable icon);

    /**
     * re-loads current project entirely (used when changes were made that require to whole project to be reloaded)
     */
    void reLoadProject();

    /**
     * Saves current project
     */
    void saveProject();

    /**
     * Creates a revertable command that later can be undone or re-done by user with Ctrl+Z or similar.
     * @param command Object containing your command logic
     * @param body Additional data that can be send as parameters
     */
    void revertibleCommand(IItemCommand command, Object body);

    /**
     * Removes follower object (selection rectangle) from particular entity (usually makes sense when entity is deleted without proper action)
     * @param entity
     */
    void removeFollower(int entity);

    /**
     * Get a factory that draws assets to the scene at specific location
     *
     * @return IFactory interface
     */
    IFactory getItemFactory();

    /**
     * @return entities that are on scene
     */
    HashSet<Integer> getProjectEntities();

    /**
     * @param entity
     * @return if entity is on visible layer
     */
    boolean isEntityVisible(int entity);

    /**
     * shows drop down menu with specified actions set
     * @param actionsSet
     * @param observable item with right click on it
     */
    void showPopup(HashMap<String, String> actionsSet, Object observable);

    /**
     * shows drop down menu with specified actions set, with an icon next to the items that have one
     * @param actionsSet notification id -> item text
     * @param actionIcons notification id -> icon drawable (22x22 px expected); ids not in the map render text-only
     * @param observable item with right click on it
     */
    void showPopup(HashMap<String, String> actionsSet, HashMap<String, Drawable> actionIcons, Object observable);

    /**
     * sets cursor to new one with cursorData
     * @param cursorData
     * @param region for plugin unic textureRegion
     */
    void setCursor(CursorData cursorData, TextureRegion region);

    /**
     * returns current selected layer name
     */
    String getCurrentSelectedLayerName();

    /**
     * returns current editor configs
     */
    EditorConfigVO getEditorConfig();

    /**
     * returns current scene configs
     */
    SceneConfigVO getCurrentSceneConfigVO();

    /**
     * returns current project
     */
    ProjectVO getCurrentProjectVO();

    /**
     * returns current project info
     */
    ProjectInfoVO getCurrentProjectInfoVO();

    /**
     * Opens a separate application window, the way the live preview does. The window is driven by the same
     * loop as the editor: the listener's callbacks arrive on the render thread with that window current, and
     * anything it draws needs its own batch, since sharing a GL context does not share vertex array objects.
     *
     * @param onClose run when the window closes, however it closes
     * @return a handle on the window, or null when the backend has no windows to give
     */
    H2DWindow newWindow(ApplicationListener listener, String title, int width, int height, boolean resizable, Runnable onClose);
}
