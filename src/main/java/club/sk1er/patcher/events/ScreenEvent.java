package club.sk1er.patcher.events;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class ScreenEvent {
    public final GuiScreen screen;

    public ScreenEvent(GuiScreen screen) {
        this.screen = screen;
    }

    public static class Init extends ScreenEvent {
        public final List<GuiButton> buttonList;
        public Init(GuiScreen screen, List<GuiButton> buttonList) {
            super(screen);
            this.buttonList = buttonList;
        }

        public static class Pre extends Init {
            public Pre(GuiScreen screen, List<GuiButton> buttonList) {
                super(screen, buttonList);
            }
        }

        public static class Post extends Init {
            public Post(GuiScreen screen, List<GuiButton> buttonList) {
                super(screen, buttonList);
            }
        }
    }

    public static class Action extends ScreenEvent {
        public final GuiButton button;
        public Action(GuiScreen screen, GuiButton button) {
            super(screen);
            this.button = button;
        }

        public static class Pre extends Action {
            public boolean isCancelled = false;
            public Pre(GuiScreen screen, GuiButton button) {
                super(screen, button);
            }
        }

        public static class Post extends Action {
            public Post(GuiScreen screen, GuiButton button) {
                super(screen, button);
            }
        }
    }

    public static class Draw extends ScreenEvent {
        public final int mouseX;
        public final int mouseY;
        public final float deltaTick;

        public Draw(GuiScreen screen, int mouseX, int mouseY, float deltaTick) {
            super(screen);
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.deltaTick = deltaTick;
        }

        public static class Pre extends Draw {
            public Pre(GuiScreen screen, int mouseX, int mouseY, float deltaTick) {
                super(screen, mouseX, mouseY, deltaTick);
            }
        }

        public static class Post extends Draw {
            public Post(GuiScreen screen, int mouseX, int mouseY, float deltaTick) {
                super(screen, mouseX, mouseY, deltaTick);
            }
        }
    }

    public static class KeyEvent extends ScreenEvent {
        public KeyEvent(GuiScreen screen) {
            super(screen);
        }

        public static class Pre extends KeyEvent {
            public boolean isCancelled = false;
            public Pre(GuiScreen screen) {
                super(screen);
            }
        }

        public static class Post extends KeyEvent {
            public Post(GuiScreen screen) {
                super(screen);
            }
        }
    }
}
