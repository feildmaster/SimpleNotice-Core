import java.lang.reflect.Field;
import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

/* As much as I'd love to make this class work without ever updating, the lack of an API makes this impossible
 *
 * To demonstrate, the only class I hook into at the moment in the core:
 *  - FontRender (nl): Their functions, their class, the variable in which it's stored in (minecraft.q)... All of those can change.
 *      This can't be helped though, since the obfuscation will undoubtedly change these
 *      This is, however, simple to update, compared to a lot of other things
 */
public final class NoticeGui {
    private final static NoticeGui instance = new NoticeGui();

    private final List<Notice> notices = new ArrayList<Notice>();
    private final double noticeTicks = 400D;
    private Minecraft minecraft;
    private boolean shade = true;
    private int noticeLimit = 5;
    private boolean attempted = false; // Flag for grabbing minecraft instance
    private boolean fade = true;

    private NoticeGui() {}

    /**
     * Add a new notice
     *
     * @param notice The notice to display
     */
    public void addNotice(String notice) {
        if (notice == null || notice.length() == 0) return;

        notices.add(new Notice(notice));
    }

    /**
     * Increase notice ticks
     */
    public void tick() {
        // Don't attempt if we can't find minecraft
        if (getMinecraft() == null) return;

        displayNotices();

        for (int i = 0; i < notices.size(); i++) {
            notices.get(i).ticks++;
        }

        // Newest notices take priority
        while (notices.size() > noticeLimit) {
            notices.remove(0);
        }
    }

    public void close() {
        notices.clear();
    }

    private void displayNotices() {
        // resolution, gamesettings, width, height
        //agd resolution = new agd(minecraft.A, minecraft.d, minecraft.e);

        // They have F3 open... change position!
        //boolean showDebug = getMinecraft().A.F;

        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.8F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        for (int i = 0; i < notices.size() && i < noticeLimit; i++) {
            // Get the notice
            int index = notices.size() - 1 - i;
            Notice notice = notices.get(index);

            // Length of message
            //int length = mod_notice.minecraft.q.b(null, i);
            // X Position
            int x = 3;
            // Y Position
            int y = i * 8 + 8; // line * font_size + font_size + 3 ?
            // The transparency of the text, 256 = no transparency
            int alpha = 256;

            if (this.fade) {
                double fade = (double) notice.ticks / noticeTicks;
                fade = 1.0D - fade;
                fade *= 10D;

                if (fade < 0.0D) {
                    fade = 0.0D;
                } else if (fade > 1.0D) {
                    fade = 1.0D;
                }

                fade *= fade;

                alpha = (int) (255D * fade);

                if (alpha <= (noticeTicks / 100)) {
                    continue;
                }
            }

            // Scale size
            if (shade) {
                // With Shadow
                getMinecraft().q.a(notice.message, x, y, 0xFFFFFF + (alpha << 24));
            } else {
                // Without Shadow
                getMinecraft().q.b(notice.message, x, y, 0xFFFFFF + (alpha << 24));
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public Minecraft getMinecraft() {
        if (minecraft == null && !attempted) {
            for (Field field : Minecraft.class.getDeclaredFields()) {
                if (Minecraft.class.isAssignableFrom(field.getType())) {
                    try {
                        minecraft = (Minecraft) field.get(null);
                    } catch (Exception ex) {
                        attempted = true; // Don't try again
                    }
                }
            }
        }
        return minecraft;
    }

    public static NoticeGui getInstance() {
        return instance;
    }

    public static NoticeGui getInstance(Minecraft minecraft) {
        if (minecraft != null && instance.minecraft == null) {
            instance.minecraft = minecraft;
        }
        return instance;
    }
}
