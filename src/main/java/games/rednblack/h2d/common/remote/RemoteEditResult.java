package games.rednblack.h2d.common.remote;

import java.util.ArrayList;
import java.util.List;

/** Result of a remote edit. {@code ok} is false if rejected (validation errors or not allowed). */
public class RemoteEditResult {
    public boolean ok;
    public String error;
    public List<String> validationErrors = new ArrayList<>();
}