package games.rednblack.h2d.common.remote;

import java.util.HashMap;
import java.util.Map;

/**
 * Request to add/remove a component or set component field values on an entity, validated
 * through the editor's properties panels. Sent via {@code MsgAPI.ACTION_REMOTE_EDIT};
 * fulfilled on the render thread by RemoteOpsMediator, which drives the same UI*Properties
 * panels the user edits (so widget validation is never bypassed).
 */
public class RemoteEditRequest {
    public enum Op { ADD_COMPONENT, REMOVE_COMPONENT, SET_FIELDS }

    public Op op;
    /** Target entity, by its uniqueId string. */
    public String entityId;
    /** Addable-component key for ADD/REMOVE (e.g. "Physics"); panel key for SET_FIELDS (e.g. "basic"). */
    public String componentKey;
    /** For SET_FIELDS: field name -> value (Number for numerics, Boolean for flags, String for text). */
    public Map<String, Object> fields = new HashMap<>();

    public RemoteHandle<RemoteEditResult> handle;
}