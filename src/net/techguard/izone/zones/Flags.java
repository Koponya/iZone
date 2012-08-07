package net.techguard.izone.zones;

public enum Flags
{
  PROTECTION, 
  MONSTER, 
  ANIMAL, 
  WELCOME, 
  FAREWELL, 
  HEAL, 
  HURT, 
  PVP, 
  CREEPER, 
  TNT, 
  EXPLOSION, 
  FIRE, 
  RESTRICTION, 
  JAIL, 
  LIGHTNING, 
  DEATHDROP, 
  SAFEDEATH, 
  DROP, 
  INTERACT, 
  ENDERMAN, 
  GOD, 
  GAMEMODE, 
  TAKEITEM_IN, 
  TAKEITEM_OUT, 
  GIVEITEM_IN, 
  GIVEITEM_OUT, 
  TEXTUREPACK, 
  MELT;

  private static int ids;
  private int id;

  static { ids = 0;

    for (Flags flag : values())
      flag.id = (ids++);
  }

  public String getName()
  {
    if (this == PROTECTION) return "Protection";
    if (this == MONSTER) return "No Monsters";
    if (this == ANIMAL) return "No Animals";
    if (this == WELCOME) return "Welcome";
    if (this == FAREWELL) return "Farewell";
    if (this == HEAL) return "Healing";
    if (this == HURT) return "Hurting";
    if (this == PVP) return "PvP";
    if (this == CREEPER) return "No Creeper";
    if (this == TNT) return "No TNT";
    if (this == EXPLOSION) return "No Explosions";
    if (this == FIRE) return "No Fire";
    if (this == RESTRICTION) return "Restriction";
    if (this == LIGHTNING) return "No Lightning";
    if (this == JAIL) return "Jail";
    if (this == DEATHDROP) return "No Drops on Death";
    if (this == SAFEDEATH) return "Keep Items/Experience on Death";
    if (this == DROP) return "No Drops";
    if (this == INTERACT) return "Interact";
    if (this == ENDERMAN) return "No Enderman Interaction";
    if (this == GOD) return "Godmode";
    if (this == GAMEMODE) return "Gamemode";
    if (this == TAKEITEM_IN) return "Take Item(In)";
    if (this == TAKEITEM_OUT) return "Take Item(Out)";
    if (this == GIVEITEM_IN) return "Give Item(In)";
    if (this == GIVEITEM_OUT) return "Give Item(Out)";
    if (this == TEXTUREPACK) return "TexturePack";
    if (this == MELT) return "Melt";
    return "unknown flag";
  }

  public String toString() {
    return super.toString().toLowerCase();
  }

  public Integer getId() {
    return Integer.valueOf(this.id);
  }
}