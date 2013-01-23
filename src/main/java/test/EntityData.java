package test;


import de.kumpelblase2.remoteentities.api.RemoteEntityType;

public class EntityData {
    public int id;
    public RemoteEntityType type;
    public String name;
    public RemoteLocation location;
    public boolean isStationary = false;
    public boolean isPushable = true;
    public float speed;
    
    public EntityData(int id, RemoteEntityType type, String name,
            RemoteLocation location,boolean isStationary,
            boolean isPushable, float speed) {
        super();
        this.id = id;
        this.type = type;
        this.name = name;
        this.location = location;
        this.isStationary = isStationary;
        this.isPushable = isPushable;
        this.speed = speed;
    }
}
