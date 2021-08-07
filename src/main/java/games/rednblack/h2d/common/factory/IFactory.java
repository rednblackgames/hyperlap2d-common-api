package games.rednblack.h2d.common.factory;

import com.badlogic.gdx.math.Vector2;

public interface IFactory {
    int getCreatedEntity();

    boolean createSimpleImage(String regionName, Vector2 position);
    boolean createSpriteAnimation(String animationName, Vector2 position);
    boolean createSpineAnimation(String animationName, Vector2 position);
    boolean createItemFromLibrary(String libraryName, Vector2 position);
}
