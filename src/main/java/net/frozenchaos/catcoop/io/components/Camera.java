package net.frozenchaos.catcoop.io.components;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Encoding;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Camera extends IoComponent {
    private static final Encoding IMAGE_ENCODING = Encoding.JPG;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RPiCamera camera = null;

    public Camera(IoManager ioManager) {
        super(ioManager);
    }

    @Override
    public void init() {
        logger.debug("Initializing Camera");
        try {
            camera = new RPiCamera();
            camera.setToDefaults();
            camera.turnOffPreview();
            camera.setTimeout(1);
            camera.setVerticalFlipOn();
            camera.setEncoding(IMAGE_ENCODING);
        } catch(Exception e) {
            logger.error("Error initializing Camera", e);
        }
        logger.debug("Initialized camera successfully");
    }

    @Override
    public void destroy() {
    }

    public byte[] takeSnapshot(int width, int height) {
        if(camera != null) {
            try {
                BufferedImage bufferedImage = camera.takeBufferedStill(width, height);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, IMAGE_ENCODING.name(), outputStream);
                logger.trace("Camera took a snapshot");
                return outputStream.toByteArray();
            } catch(InterruptedException|IOException e) {
                logger.error("Camera couldn't take a snapshot", e);
            }
        } else {
            logger.trace("Camera did not take a snapshot, it was not initialized");
        }
        return new byte[0];
    }
}
