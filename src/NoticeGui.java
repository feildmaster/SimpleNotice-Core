import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class NoticeGui extends vp {
    private final List<nt> notices = new ArrayList<nt>();
    private final double noticeTicks = 400D;
    private final Minecraft minecraft;
    private boolean shade = true;
    private int noticeLimit = 5;

    public NoticeGui(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void addNotice(String notice) {
        if (notice == null) return;

        notices.add(new nt("§f" + notice));
    }


    public void tick() {
        for (int i = 0; i < notices.size(); i++) {
            nt notice = notices.get(i);
            if (notice.b++ >= noticeTicks) {
                notices.remove(i--);
            }
        }

        displayNotices();
    }

    public void close() {
        notices.clear();
    }

    private void displayNotices() {
        // resolution, gamesettings, width, height
        //agd resolution = new agd(minecraft.A, minecraft.d, minecraft.e);

        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.8F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        for (int i = 0; i < notices.size() && i < noticeLimit; i++) {
            // Get the notice
            int pointer = notices.size() - 1 - i;
            nt notice = notices.get(pointer);

            // Length of message
            //int length = mod_notice.minecraft.q.b(null, i);
            // X Position
            int x = 3;
            // Y Position
            int y = i * 9 + 3;

//            double fade = (double) notice.b / noticeTicks;
//            fade = 1.0D - fade;
//            fade *= 10D;
//
//            if (fade < 0.0D) {
//                fade = 0.0D;
//            } else if (fade > 1.0D) {
//                fade = 1.0D;
//            }
//
//            //fade *= fade;
//
//            int alpha = (int) (255D * fade);

//            if (alpha < 2) {
//                break;
//            }

            // Scale size
            if (shade) {
                // With Shadow
                minecraft.q.a(notice.a, x, y, 0xffffff);
            } else {
                // Without Shadow
                minecraft.q.b(notice.a, x, y, 0xffffff);
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
