package net.frozenchaos.catcoop.io.components;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Encoding;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoManager;

import java.io.IOException;

public class Camera extends IoComponent {
    private RPiCamera camera = null;

    public Camera(IoManager ioManager) {
        super(ioManager);
    }

    @Override
    public void init() {
        System.out.println("Initializing camera");
        try {
            camera = new RPiCamera();
            camera.setToDefaults();
            camera.setWidth(800);
            camera.setHeight(600);
            camera.setEncoding(Encoding.JPG);
        } catch(Exception ignored) {
        }
        System.out.println("Initialized camera successfully");
    }

    @Override
    public void destroy() {
    }

    public void takeSnapshot() {
        if(camera != null) {
            try {
                camera.takeStill("test.jpg");
            } catch(InterruptedException|IOException e) {
                e.printStackTrace();
            }
        }
    }
}
