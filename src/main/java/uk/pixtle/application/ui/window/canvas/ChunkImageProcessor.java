package uk.pixtle.application.ui.window.canvas;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ChunkImageProcessor extends Thread {

    @Getter
    @Setter
    Chunk chunk;

    public void run() {

        if(!chunk.isChanged()) {
            return;
        }

        if(chunk.getScale() != 1) {
            Image img = chunk.getActualImage().getScaledInstance((int) Math.ceil(chunk.getActualImage().getWidth() * chunk.getScale()), (int) Math.ceil(chunk.getActualImage().getHeight() * chunk.getScale()), Image.SCALE_FAST);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(chunk.getActualImage().getWidth() * chunk.getScale()), (int) Math.ceil(chunk.getActualImage().getHeight() * chunk.getScale()), BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(img, 0, 0, null);
            chunk.setLastRenderedImage(outputImage);
        }





        // STEP 2 check if scaling is needed and do it

        //chunk.setLastRenderedImage(newImage);

        // Put the old image into the new image

        /* OLD
        BufferedImage newImage;
        if(chunk.getLastHeightIn() == 0 && chunk.getLastHeightOut() == 0 && chunk.getLastWidthOut() == 0 && chunk.getLastWidthIn() == 0) {
            newImage = chunk.getActualImage();
        } else {
            int newImageWidth = (int) Math.ceil(chunk.getActualImage().getWidth() - (chunk.getLastWidthIn() + chunk.getLastWidthOut()));
            int newImageHeight = (int) Math.ceil(chunk.getActualImage().getHeight() - (chunk.getLastHeightIn() + chunk.getLastHeightOut()));
            System.out.println("a) " + newImageWidth + " " + newImageHeight);

            newImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
            int[] rgbBuffer = new int[newImageHeight * newImageWidth];

            chunk.getActualImage().getRGB((int) chunk.getLastWidthIn(), (int) chunk.getLastHeightIn(), newImageWidth, newImageHeight, rgbBuffer, 0, newImageWidth);
            newImage.setRGB(0, 0, newImageWidth, newImageHeight, rgbBuffer, 0, newImageWidth);
        }

        if(chunk.getLastScale() != 1) {

            Image img = newImage.getScaledInstance((int) Math.ceil(newImage.getWidth() * chunk.getLastScale()), (int) Math.ceil(newImage.getHeight() * chunk.getLastScale()), Image.SCALE_FAST);
            BufferedImage outputImage = new BufferedImage((int) Math.ceil(newImage.getWidth() * chunk.getLastScale()), (int) Math.ceil(newImage.getHeight() * chunk.getLastScale()), BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(img, 0, 0, null);
            newImage = outputImage;
        }

        System.out.println("b) " + newImage.getWidth() + " " + newImage.getHeight());

        chunk.setLastRenderedImage(newImage);
        chunk.setChanged(false);
         */
    }

    public ChunkImageProcessor(Chunk paramChunk) {
        this.setChunk(paramChunk);
    }
}
