import net.minecraft.client.Minecraft;

public class mod_notice extends BaseMod {
    private NoticeGui nm = new NoticeGui(ModLoader.getMinecraftInstance());
    public static mod_notice instance;
    private final String channel = "SimpleNotice";

    @Override
    public String getVersion() {
        return "SimpleNotice v1.0 ALPHA";
    }

    @Override
    public void load() {
        instance = this;
        // Register channel
        ModLoader.registerPacketChannel(this, channel);
        // Listen to ticks
        ModLoader.setInGameHook(this, true, false);
    }

    @Override
    public boolean onTickInGame(float tick, Minecraft game) {
        nm.tick();
        return true;
    }

    @Override
    public void receiveCustomPacket(ee packet) {
        nm.addNotice(new String(packet.c));
    }

    @Override
    public void serverDisconnect() {
        nm.close();
    }


}
