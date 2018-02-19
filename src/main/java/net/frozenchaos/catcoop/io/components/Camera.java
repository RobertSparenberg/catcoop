package net.frozenchaos.catcoop.io.components;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Encoding;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Camera extends IoComponent {
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
            camera.setWidth(800);
            camera.setHeight(600);
            camera.turnOffPreview();
            camera.setTimeout(1);
            camera.setEncoding(Encoding.JPG);
        } catch(Exception e) {
            logger.error("Error initializing Camera", e);
        }
        logger.debug("Initialized camera successfully");
    }

    @Override
    public void destroy() {
    }

    public void takeSnapshot() {
        if(camera != null) {
            try {
                camera.takeStill("test.jpg");
                logger.trace("Camera took a snapshot");
            } catch(InterruptedException|IOException e) {
                logger.error("Camera couldn't take a snapshot", e);
            }
        } else {
            logger.trace("Camera did not take a snapshot, it was not initialized");
        }
    }
}
