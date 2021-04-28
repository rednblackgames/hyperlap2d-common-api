package games.rednblack.h2d.common.command;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonJson;
import games.rednblack.h2d.common.MsgAPI;
import org.puremvc.java.interfaces.IFacade;

public class ReplaceSpineCommandBuilder {

    private Object[] payload;

    public void begin(Entity forEntity) {
        payload = new Object[4];
        payload[0] = forEntity;
    }

    public void setAnimationName(String name) {
        payload[1] = name;
    }

    public void setSkeletonJson(SkeletonJson skeletonJson) {
        payload[2] = skeletonJson;
    }

    public void setSkeleton(Skeleton skeleton) {
        payload[3] = skeleton;
    }

    public void execute(IFacade facade) {
        facade.sendNotification(MsgAPI.ACTION_REPLACE_SPINE_ANIMATION_DATA, payload);
    }
}
