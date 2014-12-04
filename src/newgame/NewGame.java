package newgame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import sensor.ISensor;
import sensor.Navigation;
import sensor.SimpleRead;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author emmanuelsantana
 */
public class NewGame extends SimpleApplication implements ISensor {

    BitmapText helloText;
    Boolean isRunning = true;
    Geometry player;
    private Navigation sensorsState;
    private Spatial rectangleModel;
    private BitmapText hudText;

    @Override
    public void simpleInitApp() {

        flyCam.setMoveSpeed(15);
        loadTerrain();
        loadBoat();
        loadLights();
        setInitialCameraPosition();
        loadHUD();
        initKeys();
        loadSky();
        loadWater();
        SimpleRead serialRead = new SimpleRead(this);
    }

    @Override
    public void simpleUpdate(float tpf) {
        updateHUD();
        updateSensors();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void loadTerrain() {
        //Spatial terrain = assetManager.loadModel("Models/World.j3o");
        //rootNode.attachChild(terrain);
    }

    private void loadBoat() {
        rectangleModel = assetManager.loadModel("Models/ChrisCraft/ChrisCraft.j3o");
        rectangleModel.setLocalTranslation(0, -5, 0);
        rootNode.attachChild(rectangleModel);
    }

    private void loadLights() {
        /**
         * A white, directional light source
         */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    }

    private void loadHUD() {
        /**
         * Write text on the screen (HUD)
         */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);

    }

    private void updateHUD() {
        if (!isRunning) {
            System.out.println("Paused");
        } else {
            if (sensorsState != null) {
                helloText.setText("Roll: " + sensorsState.getRoll() + " Pitch: " + sensorsState.getPitch() + " Yaw: " + sensorsState.getYaw());
            }

        }


    }

    private void setInitialCameraPosition() {
        //Scam.getLocation().set(0f, 10f, 20f);
    }

    private void loadSky() {
        Texture west = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_west.png");
        Texture east = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_east.png");
        Texture north = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_north.png");
        Texture south = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_south.png");
        Texture up = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_up.png");
        Texture down = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.png");

        Spatial sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down, Vector3f.UNIT_XYZ);
        rootNode.attachChild(sky);
    }

    private void loadWater() {
        // we create a water processor

        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(rootNode);

// we set the water plane
        Vector3f waterLocation = new Vector3f(0, -6, 0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        viewPort.addProcessor(waterProcessor);

// we set wave properties
        waterProcessor.setWaterDepth(90);         // transparency of water
        waterProcessor.setDistortionScale(0.05f); // strength of waves
        waterProcessor.setWaveSpeed(0.05f);       // speed of waves

// we define the wave size by setting the size of the texture coordinates
        Quad quad = new Quad(400, 400);
        quad.scaleTextureCoordinates(new Vector2f(6f, 6f));

// we create the water geometry from the quad
        Geometry water = new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(-200, -6, 250);
        water.setShadowMode(ShadowMode.Cast);
        water.setMaterial(waterProcessor.getMaterial());
        rootNode.attachChild(water);
    }

    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        // Add the names to the action listener.
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(analogListener, "Left", "Right", "Rotate");

    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                isRunning = !isRunning;
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (isRunning) {
                if (name.equals("Rotate")) {
                    player.rotate(0, value * speed, 0);
                }
                if (name.equals("Right")) {
                    Vector3f v = player.getLocalTranslation();
                    player.setLocalTranslation(v.x + value * speed, v.y, v.z);
                }
                if (name.equals("Left")) {
                    Vector3f v = player.getLocalTranslation();
                    player.setLocalTranslation(v.x - value * speed, v.y, v.z);
                }
            } else {
                System.out.println("Press P to unpause.");
            }
        }
    };

    private void updateSensors() {
        if (isRunning) {


            if (sensorsState != null) {
                float r = sensorsState.getRoll();
                float p = sensorsState.getPitch();
                float y = sensorsState.getYaw();
                //hudText.setText("Navigation: Roll:" + r + " Pitch: " + p + " Yaw: " + y);
                
                    Quaternion roll = new Quaternion();
                    Quaternion pitch = new Quaternion();
                    Quaternion yaw = new Quaternion();
                    roll.fromAngleAxis(FastMath.PI * r / 180 * -1, new Vector3f(0, 0, 1));
                    pitch.fromAngleAxis(FastMath.PI * p / 180, new Vector3f(1, 0, 0));
                    yaw.fromAngleAxis(FastMath.PI * y / 180 * -1, new Vector3f(0, 1, 0));
                    //Quaternion rotation = roll.mult(pitch).mult(yaw);
                    Quaternion rotation = roll.mult(pitch);
                    //gameLevel.setLocalRotation(roll);
                    rectangleModel.setLocalRotation(rotation);
                    //gameLevel.setLocalRotation(yaw);
                

            }
        }
    }

    public void update(Navigation sensorsState) {
        this.sensorsState = sensorsState;
    }
}
