package club.sk1er.container;

import gg.essential.universal.UDesktop;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Desktop;
import java.net.URI;

public class ContainerMessage {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String message = "Patcher is a Forge mod, not an application. Please put this in your mods folder, located inside your Minecraft folder."
            + "\nIf you don't know how to install a Forge mod, search 'Forge Mod Installation Tutorials' online."
            + "\nIf you're still lost, contact the support Discord at https://polyfrost.org/discord.";
        String title = "Patcher - This is not the proper installation method.";

        if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            int option = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] { "Join Discord", "Close" }, JOptionPane.YES_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                UDesktop.browse(URI.create("https://polyfrost.org/discord"));
            }
        } else {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }

        System.exit(0);
    }
}
