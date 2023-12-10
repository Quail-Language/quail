package me.tapeline.quailj.runtime.std.qml.window;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends java.awt.Canvas {

    private BufferedImage scheduledImage;
    public BufferedImage getScheduledImage() {
        return scheduledImage;
    }

    public void setScheduledImage(BufferedImage scheduledImage) {
        this.scheduledImage = scheduledImage.getSubimage(0, 0,
                scheduledImage.getWidth(), scheduledImage.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(scheduledImage, 0, 0, null);
    }

}
