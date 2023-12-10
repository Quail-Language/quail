class Entity {
    object<World> getWorld(this) {}

    list getLocation(this) {}
    void setRotation(this, num yaw, num pitch)
    bool teleport(this, list location) {}

    list getVelocity(this) {}
    void setVelocity(this, list velocity) {}

    num getHeight(this) {}
    num getWidth(this) {}

    bool isOnGround(this) {}
    bool isInWater(this) {}

    list<Entity> getNearbyEntities(this, num x, num y, num z) {}

    num getEntityId(this) {}
    string getUUID(this) {}

    void remove(this) {}

    bool isDead(this) {}
    bool isValid(this) {}

    object<Server> getServer(this) {}

    bool isPersistent(this) {}
    void setPersistent(this, isPersistent) {}

    list<Entity> getPassengers(this) {}
    void addPassenger(this, object<Entity> passenger) {}
    void removePassenger(this, object<Entity> passenger) {}
    bool isNoPassengers(this) {}
    bool ejectPassengers(this) {}

    num getFallDistance(this) {}
    void setFallDistance(this, num distance) {}

    void playEffect(this, string type) {}

    string getType(this) {}

    bool isInsideVehicle() {}
    bool leaveVehicle() {}

    bool isCustomNameVisible() {}
    void setCustomNameVisible(bool isCustomNameVisible) {}

    bool isGlowing() {}
    void setGlowing(bool isGlowing) {}

    bool isInvulnerable() {}

    bool isSilent() {}
    void setSilent(bool isSilent) {}
}