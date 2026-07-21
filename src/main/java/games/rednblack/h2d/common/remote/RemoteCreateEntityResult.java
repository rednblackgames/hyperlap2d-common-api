package games.rednblack.h2d.common.remote;

/** Result of create_entity: the new entity's uniqueId (ok), or an error. */
public class RemoteCreateEntityResult {
    public boolean ok;
    public String error;
    public String uniqueId;
}