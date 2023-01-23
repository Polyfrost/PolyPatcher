package club.sk1er.patcher.util.world.sound.audioswitcher;

import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.Notifications;
import cc.polyfrost.patcher.events.ScreenEvent;
import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenOptionsSounds;

import java.util.ArrayList;
import java.util.List;

public class AudioSwitcher {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final ALCHelper alcHelper = new ALCHelper();
    private List<String> devices = new ArrayList<>();

    private boolean previousWasOptionsSounds;
    private boolean changedDevice;
    private int buttonYPosition;

    @Subscribe
    public void initGui(ScreenEvent.Init event) {
        GuiScreen gui = event.screen;
        List<GuiButton> buttonList = event.buttonList;

        if (gui instanceof GuiScreenOptionsSounds) {
            this.previousWasOptionsSounds = true;
            this.devices = this.alcHelper.getAvailableDevices(true);

            String selectedAudioDevice = PatcherConfig.selectedAudioDevice;
            if (selectedAudioDevice == null || selectedAudioDevice.isEmpty()) {
                selectedAudioDevice = this.devices.isEmpty() ? "Default Sound Device" : this.devices.get(0);
            }

            for (GuiButton button : buttonList) {
                if (button.id == 200) {
                    String buttonText = selectedAudioDevice;
                    int stringWidth = this.mc.fontRendererObj.getStringWidth(buttonText);
                    if (stringWidth >= 175) {
                        buttonText = this.mc.fontRendererObj.trimStringToWidth(buttonText, 170) + "...";
                    }

                    //#if MC==10809
                    this.buttonYPosition = button.yPosition - 44;
                    //#else
                    //$$ this.buttonYPosition = button.y + 60;
                    //#endif
                    buttonList.add(new GuiButton(38732, (gui.width / 2) - 100, this.buttonYPosition, buttonText));
                    break;
                }
            }
        } else if (previousWasOptionsSounds) {
            this.previousWasOptionsSounds = false;
            if (this.changedDevice) {
                Patcher.instance.forceSaveConfig();

                try {
                    this.mc.getSoundHandler().onResourceManagerReload(this.mc.getResourceManager());
                } catch (Exception e) {
                    Notifications.INSTANCE.send("Patcher", "Failed to reinitialize OpenAL.");
                    Patcher.instance.getLogger().error("Failed to reinitialize OpenAL.", e);
                }

                this.changedDevice = false;
            }
        }
    }

    @Subscribe
    public void drawScreen(ScreenEvent.Draw.Post event) {
        GuiScreen gui = event.screen;

        if (gui instanceof GuiScreenOptionsSounds) {
            gui.drawCenteredString(this.mc.fontRendererObj, "Sound Device (Click to Change)", gui.width / 2, this.buttonYPosition - 12, -1);
        }
    }

    @Subscribe
    public void actionPerformed(ScreenEvent.Action.Pre event) {
        GuiScreen gui = event.screen;
        GuiButton button = event.button;
        int buttonId = button.id;

        if (gui instanceof GuiScreenOptionsSounds && buttonId == 38732) {
            this.fetchAvailableDevicesUncached();
            if (this.devices.isEmpty()) return;

            String selectedAudioDevice = PatcherConfig.selectedAudioDevice;
            if (selectedAudioDevice != null && !selectedAudioDevice.isEmpty()) {
                int index = this.devices.indexOf(selectedAudioDevice);
                if (index + 1 >= this.devices.size()) {
                    selectedAudioDevice = this.devices.get(0);
                } else {
                    selectedAudioDevice = this.devices.get(index + 1);
                }
            } else {
                selectedAudioDevice = this.devices.get(0);
            }

            String buttonText = selectedAudioDevice;
            int stringWidth = this.mc.fontRendererObj.getStringWidth(buttonText);
            if (stringWidth >= 175) {
                buttonText = this.mc.fontRendererObj.trimStringToWidth(buttonText, 170) + "...";
            }

            PatcherConfig.selectedAudioDevice = selectedAudioDevice;
            button.displayString = buttonText;
            if (!this.changedDevice) this.changedDevice = true;
            event.isCancelled = true;
        }
    }

    public void fetchAvailableDevicesUncached() {
        this.devices = this.alcHelper.getAvailableDevices(false);
    }

    public List<String> getDevices() {
        return devices;
    }
}
