package games.rednblack.h2d.common.remote;

/**
 * Request to create a shader resource. Either pass a template (0=Default Array, 1=Distance Field,
 * 2=Screen Reading) or custom vertex/fragment GLSL source. The shader is written to the project's
 * shaders dir and registered.
 */
public class RemoteCreateShaderRequest {
    public String name;
    /** 0=Default Array, 1=Distance Field, 2=Screen Reading. Ignored if vertex/fragment are provided. */
    public int templateType = 0;
    /** Custom vertex GLSL (optional; if set with fragment, overrides the template). */
    public String vertex;
    /** Custom fragment GLSL (optional). */
    public String fragment;

    public RemoteHandle<RemoteCreateShaderResult> handle;
}