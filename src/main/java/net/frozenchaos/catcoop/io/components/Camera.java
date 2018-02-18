package net.frozenchaos.catcoop.io.components;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Encoding;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Camera {
    private RPiCamera camera = null;

    public Camera() {
        try {
            camera = new RPiCamera();
            camera.setToDefaults();
            camera.setWidth(800);
            camera.setHeight(600);
            camera.setEncoding(Encoding.JPG);
        } catch(Exception ignored) {
        }
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
