package games.rednblack.h2d.common.command;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.puremvc.Facade;

public class ReplaceSpriteAnimationCommandBuilder {

    private Object[] payload;

    public void begin(int forEntity) {
        payload = new Object[3];
        payload[0] = forEntity;
    }

    public void setAnimationName(String name) {
        payload[1] = name;
    }

    public void setRegion(Array<TextureAtlas.AtlasRegion> regions) {
        payload[2] = regions;
    }

    public void execute(Facade facade) {
        facade.sendNotification(MsgAPI.ACTION_REPLACE_SPRITE_ANIMATION_DATA, payload);
    }
}
