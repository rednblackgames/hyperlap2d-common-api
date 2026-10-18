package games.rednblack.h2d.common.remote;

import java.util.ArrayList;
import java.util.List;

/**
 * Request to make or edit a widget in ways no properties panel field covers: converting items into
 * one, setting a state override on one of its parts, or showing it in a state. Sent via
 * {@code MsgAPI.ACTION_REMOTE_WIDGET}; fulfilled on the render thread by RemoteOpsMediator.
 */
public class RemoteWidgetRequest {
    public enum Op { CONVERT, SET_OVERRIDE, SHOW_STATE }
    public Op op;

    /** CONVERT: the items to make the widget from, by uniqueId. */
    public List<String> entityIds = new ArrayList<>();
    /** CONVERT: the widget type. */
    public String widgetType;

    /** SET_OVERRIDE: the part; SHOW_STATE: the widget. By uniqueId. */
    public String entityId;
    /** SET_OVERRIDE / SHOW_STATE: the state. */
    public String state;
    /** SET_OVERRIDE: the property overridden, and its value in the state. */
    public String key;
    public String value;
    /** SET_OVERRIDE: takes the override away, with its transition and sequence. */
    public boolean remove;

    /** SET_OVERRIDE: whether the transition is touched at all; a null duration then takes it away. */
    public boolean setTransition;
    public Float transitionDuration;
    public String transitionInterpolation;

    /** SET_OVERRIDE: whether the sequence is touched at all; both empty then take it away. */
    public boolean setSequence;
    public String sequenceEnter;
    public String sequenceExit;

    public RemoteHandle<RemoteWidgetResult> handle;
}
