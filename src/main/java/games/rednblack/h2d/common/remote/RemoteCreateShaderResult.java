package games.rednblack.h2d.common.remote;

/** Result of create_shader: the shader name (ok), or an error (e.g. already exists / compile failed). */
public class RemoteCreateShaderResult {
    public boolean ok;
    public String error;
    public String name;
}