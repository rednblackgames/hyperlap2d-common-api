package games.rednblack.h2d.common.remote;

/** Request the authoritative entityType -> display-name map (core + extensions). */
public class RemoteTypeNamesRequest {
    public RemoteHandle<RemoteTypeNamesResult> handle;
}