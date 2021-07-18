package games.rednblack.h2d.common.filters;

public abstract class IAbstractResourceFilter {
    public final String name, id;

    private boolean active = false;

    public IAbstractResourceFilter(String name, String id) {
        this.id = id;
        this.name = name;
    }

    public abstract boolean filterResource(String resName, int entityType);

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
