package games.rednblack.h2d.common.command;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import games.rednblack.h2d.common.MsgAPI;
import org.puremvc.java.interfaces.IFacade;

public class ReplaceRegionCommandBuilder {

    private Object[] payload;

    public void begin(int forEntity) {
        payload = new Object[3];
        payload[0] = forEntity;
    }

    public void setRegionName(String regionName) {
        payload[1] = regionName;
    }

    public void setRegion(TextureRegion region) {
        payload[2] = region;
    }

    public void execute(IFacade facade) {
        facade.sendNotification(MsgAPI.ACTION_REPLACE_REGION_DATA, payload);
    }
}
