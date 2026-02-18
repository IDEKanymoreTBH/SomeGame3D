/*Ideas For Game:
 * - Final Idea: Code Clicker: Player Messes With Code And Stuff
*/
package somegame3d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.SpotLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import com.simsilica.lemur.GuiGlobals;
import groovy.console.ui.Console;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import tonegod.gui.core.Screen;
import com.jme3.scene.control.BillboardControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.post.FilterPostProcessor;
public class App extends SimpleApplication implements ActionListener {
    public InteractionController intCont;
    //Global Values
    public int stamina = 100;
    public BulletAppState bulletAppState; //Physics Controller
    public BetterCharacterControl playerControl; //Controls Player's Physics
    public Node playerNode; // Node For Player
    public boolean moveForward = false; 
    public boolean moveBackwards = false;
    public boolean jump = false;
    public Spatial playerSpatial; //The actual Player object
    public boolean fpsShown = false;
    public boolean strafeLeft = false;
    public boolean strafeRight = false;
    public Vector3f walkDir = new Vector3f();
    public Vector3f desiredDir = new Vector3f();
    public float walkDirMult = 4f;
    public boolean isInMainMenu = true;
    public boolean isInSettings = false;
    public boolean isInPatchNotes = false;
    public boolean isInSettingsOptions = false;
    public boolean isInSettingsKeyBinds = false;
    public boolean isInSettingsDebugOptions = false;
    public boolean flyHacksEnabled = false;
    public int staminaDecreaseCounter = 0;
    public int staminaIncreaseCounter = 0;
    public boolean isInteractingWithShop1 = false;
    public boolean canMove = true;
    public boolean isInPauseMenu = false;
    public int developedProducts = 0;
    //All GUI Elements(Menus, UI, Etc)
    Picture mainMenu;
    Picture patchNotesGUI;
    Picture settingsGUI;
    Picture keybindsGUI;
    Picture optionsGUI;
    Picture debugOptionsGUI;
    Picture staminaBar = new Picture("StaminaBar");
    Picture devBar = new Picture("DevelopedBar");
    BitmapFont font;
    BitmapText text;
    BitmapText devText;
    SpotLight flashlight;
    Picture interactGui = new Picture("Interact_Gui");
    Picture shop1Gui = new Picture("Shop1GUI");
    Picture cursor;
    Picture pauseMenu = new Picture("Pause_Menu");
    //The Screen, Specifically Designed For ToneGodGUI
    public Geometry targetGeometry;
    private Screen screen;
    public static void main(String[] args) {
        App app = new App();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("SomeGame3D");
        settings.setFullscreen(true);
        settings.setResolution(1920, 1200);
        settings.setVSync(true);
        app.setSettings(settings);
        app.start();
    }
    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        screen = new Screen(this);
        guiNode.addControl(screen);
        // AlertBox albx = new AlertBox(screen, "alert", new Vector2f(15, 15)) {
        //     @Override
        //     public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
        //         System.out.println("Just Testing Some Random Thing");
        //         this.hide();
        //     }
        // };
        // albx.setMsg("Hello World");
        // albx.setButtonOkText("Yeh");
        // screen.addElement(albx);
        // albx.show();
        //Code To Set Custom Cursor
        cursor = new Picture("CursorTex");
        cursor.setWidth(32);
        cursor.setHeight(46);
        cursor.setImage(assetManager, "CursorTexture.png", true);
        cursor.setPosition(50, 50);
        cursor.setQueueBucket(RenderQueue.Bucket.Gui);
        cursor.setCullHint(Spatial.CullHint.Never);
        cursor.getMaterial().getAdditionalRenderState().setDepthTest(false);
        cursor.setLocalTranslation(50, 50, 10);
        guiNode.attachChild(cursor);
        //Interaction Stuff
        intCont = new InteractionController();
        //Other Stuff Like The NPC Test
        CharacterObj charObj = new CharacterObj(assetManager, 2, 8);
        charObj.render(rootNode);
        font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        text = new BitmapText(font);
        text.setSize(guiFont.getCharSet().getRenderedSize() + 10);
        text.setText(Integer.toString(stamina));
        text.setLocalTranslation(260, settings.getHeight() - 15, 0);
        text.setColor(ColorRGBA.Black);
        guiNode.attachChild(text);
        devText = new BitmapText(font);
        devText.setSize(guiFont.getCharSet().getRenderedSize() + 10);
        devText.setText(Integer.toString(developedProducts));
        devText.setLocalTranslation(100, 40, 0);
        devText.setColor(ColorRGBA.Black);
        guiNode.attachChild(devText);
        mainMenu = new Picture("Main_Menu");
        mainMenu.setImage(assetManager, "MainMenu.png", true);
        mainMenu.setWidth(settings.getWidth());
        mainMenu.setHeight(settings.getHeight());
        mainMenu.setPosition(0, 0);
        guiNode.attachChild(mainMenu);
        shop1Gui.setImage(assetManager, "Keypad.png", true);
        shop1Gui.setWidth(750);
        shop1Gui.setHeight(881);
        shop1Gui.setPosition(0, 0);
        staminaBar.setImage(assetManager, "StaminaBar.png", true);
        staminaBar.setWidth(1009);
        staminaBar.setHeight(791.015625f);
        staminaBar.setPosition(0, 410);
        guiNode.attachChild(staminaBar);
        devBar.setImage(assetManager, "Developed.png", true);
        devBar.setWidth(1009);
        devBar.setHeight(791.015625f);
        devBar.setPosition(0, 0);
        guiNode.attachChild(devBar);
        interactGui.setImage(assetManager, "InteractGUI.png", true);
        interactGui.setWidth(2000);
        interactGui.setHeight(1000);
        interactGui.setPosition(0, 0);
        pauseMenu.setImage(assetManager, "PauseMenu.png", true);
        pauseMenu.setWidth(settings.getWidth());
        pauseMenu.setHeight(settings.getHeight());
        pauseMenu.setPosition(0, 0);
        setDisplayStatView(false);
        setDisplayFps(fpsShown);
        fpsText.setColor(ColorRGBA.White);
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        //inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        //inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(this, "Click"/*, "Pause"*/);
        //Physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //Lighting
        flashlight = new SpotLight();
        
        flashlight.setColor(ColorRGBA.White.mult(2f)); // brightness
        flashlight.setPosition(cam.getLocation());
        flashlight.setDirection(cam.getDirection());
        flashlight.setSpotRange(75f);                  // how far it shines
        flashlight.setSpotInnerAngle(8f * FastMath.DEG_TO_RAD);
        flashlight.setSpotOuterAngle(16f * FastMath.DEG_TO_RAD);
        rootNode.addLight(flashlight);
        //Shadow Renderer
        SpotLightShadowRenderer slsr = new SpotLightShadowRenderer(assetManager, 4096);
        slsr.setLight(flashlight);
        slsr.setShadowIntensity(0.9f);
        slsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        viewPort.addProcessor(slsr);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        SSAOFilter ssao = new SSAOFilter();
        fpp.addFilter(ssao);
        ssao.setIntensity(1.5f);
        ssao.setScale(0.5f);
        ssao.setBias(0.1f);
        viewPort.addProcessor(fpp);
        //Floor
        RoomGenerator gen = new RoomGenerator(assetManager, rootNode, bulletAppState);
        gen.generateStarterRoom();
        //Player(Not Gonna Have A Model Until Needed)
        playerSpatial = new Geometry("Box", new Box(0, 0, 0));
        Material playerMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        playerSpatial.setMaterial(playerMat);
        //Wrap in Node
        playerNode = new Node("PlayerNode");
        playerNode.attachChild(playerSpatial);

        //Scale down model
        float scale = 2.8f;
        playerNode.setLocalScale(scale);

        //Offset model so feet align with capsule
        float capsuleHeight = 1.8f * scale;
        playerSpatial.setLocalTranslation(0, capsuleHeight / 2, 0);

        //BetterCharacterControl
        float radius = 0.5f * scale;
        float mass = 80f;
        playerControl = new BetterCharacterControl(radius, capsuleHeight, mass);
        playerControl.setJumpForce(new Vector3f(0, 450, 0));
        playerNode.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);

        //Position Player above floor
        playerNode.setLocalTranslation(0, capsuleHeight / 2 + 0.01f, 0);
        playerNode.setLocalTranslation(0, 0.3f + capsuleHeight / 2 + 0.01f, 0);
        rootNode.attachChild(playerNode);
        //Input
        inputManager.addMapping("MoveForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("ToggleFPS", new KeyTrigger(KeyInput.KEY_TAB));
        inputManager.addMapping("MoveBackwards", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("SnapshotCoords", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addMapping("Crouch", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addMapping("Set_FlyHack", new KeyTrigger(KeyInput.KEY_SEMICOLON));
        inputManager.addMapping("Interact", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(this, "MoveForward", "Jump", "ToggleFPS", "MoveBackwards", "StrafeLeft", "StrafeRight", "Sprint", "SnapshotCoords", "Crouch", "Set_FlyHack", "Interact", "Pause");
        //(Test) Object To Test Interactables
        targetGeometry = new Geometry("Test_Interactable", new Box(0.1f, 0.5f, 0.4f));
        targetGeometry.setLocalTranslation(9, 4, 5);
        Material test_Mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        targetGeometry.setShadowMode(RenderQueue.ShadowMode.Receive);
        test_Mat.setBoolean("UseMaterialColors", true);
        test_Mat.setColor("Diffuse", ColorRGBA.Cyan);
        targetGeometry.setMaterial(test_Mat);
        rootNode.attachChild(targetGeometry);
        RigidBodyControl testObjControl = new RigidBodyControl(new BoxCollisionShape(new Vector3f(((Box) targetGeometry.getMesh()).getXExtent(), ((Box) targetGeometry.getMesh()).getYExtent(), ((Box) targetGeometry.getMesh()).getZExtent())), 0f);
        targetGeometry.addControl(testObjControl);
        bulletAppState.getPhysicsSpace().add(testObjControl);
        //Set Camera Fly Speed
        flyCam.setMoveSpeed(20);
    }
    @Override
    public void simpleUpdate(float tpf) {
        fpsText.setColor(ColorRGBA.White);
        cursor.setPosition(inputManager.getCursorPosition().x - 5, inputManager.getCursorPosition().y - 40);
        cursor.setLocalTranslation(cursor.getLocalTranslation().x, cursor.getLocalTranslation().y, 10);
        //Makes The Game Not Run Until The Menus Are Done
        if(isInMainMenu || isInSettings || isInPatchNotes || isInSettingsKeyBinds || isInSettingsOptions || isInSettingsDebugOptions) return;
        flashlight.setPosition(cam.getLocation());
        flashlight.setDirection(cam.getDirection().normalize());
        guiNode.attachChild(staminaBar);
        guiNode.attachChild(devBar);
        if(walkDirMult == 10f && (moveForward || moveBackwards || strafeLeft || strafeRight)) {
            if(staminaDecreaseCounter == 7) {
                staminaDecreaseCounter = 0;
                stamina -= 1;
            } else {
                staminaDecreaseCounter++;
            }
        } else {
            if(staminaIncreaseCounter == 20) {
                staminaIncreaseCounter = 0;
                if(stamina < 100) {
                    stamina += 1;
                }
            } else {
                staminaIncreaseCounter++;
            }
        }
        if(stamina <= 0) {
            walkDirMult = 4f;
        }
        text.setText(Integer.toString(stamina));
        guiNode.attachChild(text);
        devText.setText(Integer.toString(developedProducts));
        guiNode.attachChild(devText);
        RoomGenerator.floorPhysics3.setPhysicsLocation(RoomGenerator.floorPhysics3.getPhysicsLocation().add(new Vector3f(0, tpf * 0.1f, 0)));
        RoomGenerator.floorGeom3.setLocalTranslation(RoomGenerator.floorPhysics3.getPhysicsLocation().add(new Vector3f(0, tpf * 0.1f, 0)));
        desiredDir.set(0, 0, 0);
        if (moveForward) {
            desiredDir.addLocal(new Vector3f(cam.getDirection().x, 0, cam.getDirection().z));
        }
        if (moveBackwards) {
            desiredDir.subtractLocal(new Vector3f(cam.getDirection().x, 0, cam.getDirection().z));
        }
        if (strafeLeft) {
            desiredDir.addLocal(new Vector3f(cam.getLeft().x, 0, cam.getLeft().z));
        }
        if (strafeRight) {
            desiredDir.subtractLocal(new Vector3f(cam.getLeft().x, 0, cam.getLeft().z));
        }
        //Tell If Player Is Looking Somewhere
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();
        rootNode.collideWith(ray, results);
        if(results.size() > 0) {
            if((results.getClosestCollision().getGeometry() == targetGeometry) && (cam.getLocation().distanceSquared(results.getClosestCollision().getGeometry().getWorldTranslation()) <= 25)) {
                guiNode.attachChild(interactGui);
                intCont.updateValue("Shop1.obj", true);

            } else {
                guiNode.detachChildNamed("Interact_Gui");
                intCont.updateValue("Shop1.obj", false);
            }
        }
        float accel = playerControl.isOnGround() ? 6f : 1.5f;
        float friction = playerControl.isOnGround() ? 4f : 0f;
        if(desiredDir.lengthSquared() > 0) {
            desiredDir.normalizeLocal();
            walkDir.interpolateLocal(desiredDir, accel * tpf);
        } else {
            if(walkDir.length() <= (friction * tpf)) {
                walkDir.set(0, 0, 0);
            }
            else {
                walkDir.subtractLocal(walkDir.normalize().mult(friction * tpf));
            }
        }
        playerControl.setWalkDirection(new Vector3f(walkDir.x * walkDirMult, 0, walkDir.z * walkDirMult));
        playerControl.setViewDirection(cam.getDirection());
        if(!flyHacksEnabled) {
            if(!playerControl.isDucked()) {
                cam.setLocation(playerSpatial.getWorldTranslation().add(new Vector3f(0f, -2.8f, 0f)));
            } else {
                cam.setLocation(playerSpatial.getWorldTranslation().add(new Vector3f(0f, -4.2f, 0f)));
            }
        }
        //Below Controls How Far The Camera Can Tilt Up And Down
        float[] angles = cam.getRotation().toAngles(null);
        angles[0] = FastMath.clamp(angles[0], -FastMath.HALF_PI + 0.1f, FastMath.HALF_PI - 0.1f);
        cam.setRotation(new Quaternion().fromAngles(angles[0], angles[1], angles[2]));
    }
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("MoveForward") && !isInMainMenu && !isInSettings && !isInPatchNotes && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions && canMove) {
            moveForward = isPressed;
        } else if (name.equals("Jump") && isPressed) {
            if(canMove) {
                if (playerControl.isOnGround()) {
                    playerControl.jump();
                }
            } else if(!canMove && isInteractingWithShop1) {
                if(developedProducts >= 15) {
                    developedProducts -= 15;
                    System.out.println("Purchase Successful");
                } else {
                    throw new InvalidPurchaseException("Purchase Cannot Be Made. Insufficient Funds");
                }
            }
        } else if (name.equals("ToggleFPS") && !isInMainMenu && !isInSettings && !isInPatchNotes && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions) {
            if(!fpsShown) {
                fpsShown = true;
                setDisplayFps(fpsShown);
            }
            else {
                fpsShown = false;
                setDisplayFps(fpsShown);
            }
        } else if (name.equals("MoveBackwards") && !isInMainMenu && !isInSettings && !isInPatchNotes && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions && canMove) {
            moveBackwards = isPressed;
        } else if (name.equals("StrafeLeft") && !isInMainMenu && !isInSettings && !isInPatchNotes && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions && canMove) {
            strafeLeft = isPressed;
        } else if (name.equals("StrafeRight") && !isInMainMenu && !isInSettings && !isInPatchNotes && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions && canMove) {
            strafeRight = isPressed;
        } else if (name.equals("Sprint") && !isInMainMenu && !isInSettings && !isInPatchNotes && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions) {
            walkDirMult = (isPressed && stamina > 0) ? 10f : 4f;
        } else if (name.equals("Click") && isPressed) {
            if(inputManager.getCursorPosition().x >= 777.0f && inputManager.getCursorPosition().x <= 1142.0f && inputManager.getCursorPosition().y <= 901.0f && inputManager.getCursorPosition().y >= 782.0f && isInMainMenu) {
                inputManager.setCursorVisible(false);
                flyCam.setEnabled(true);
                isInMainMenu = false;
                guiNode.detachAllChildren();
            }
            if(inputManager.getCursorPosition().x >= 777.0f && inputManager.getCursorPosition().x <= 1142.0f && inputManager.getCursorPosition().y <= 662.0f && inputManager.getCursorPosition().y >= 537.0f && isInMainMenu) {
                isInSettings = true;
                isInMainMenu = false;
                guiNode.detachChildNamed("Main_Menu");
                settingsGUI = new Picture("Settings_Menu"); //AppSettings already is named settings :/
                settingsGUI.setImage(assetManager, "Settings.png", true);
                settingsGUI.setWidth(settings.getWidth());
                settingsGUI.setHeight(settings.getHeight());
                settingsGUI.setPosition(0, 0);
                guiNode.attachChild(settingsGUI);
            }
            if(inputManager.getCursorPosition().x >= 777.0f && inputManager.getCursorPosition().x <= 1142.0f && inputManager.getCursorPosition().y <= 422.0f && inputManager.getCursorPosition().y >= 298.0f && isInMainMenu) {
                isInMainMenu = false;
                isInPatchNotes = true;
                guiNode.detachChildNamed("Main_Menu");
                patchNotesGUI = new Picture("Patch_Notes");
                patchNotesGUI.setImage(assetManager, "Patch_Notes.png", true);
                patchNotesGUI.setWidth(settings.getWidth());
                patchNotesGUI.setHeight(settings.getHeight());
                patchNotesGUI.setPosition(0, 0);
                guiNode.attachChild(patchNotesGUI);
            }
            if(inputManager.getCursorPosition().x >= 777.0f && inputManager.getCursorPosition().x <= 1142.0f && inputManager.getCursorPosition().y <= 187.0f && inputManager.getCursorPosition().y >= 68.0f && isInMainMenu) {
                System.exit(0);
            }
            if(inputManager.getCursorPosition().x >= 1820.0f && inputManager.getCursorPosition().x <= 1903.0f && inputManager.getCursorPosition().y <= 1183.0f && inputManager.getCursorPosition().y >= 1101.0f && (isInSettings || isInPatchNotes)) {
                isInSettings = false;
                isInPatchNotes = false;
                isInMainMenu = true;
                guiNode.attachChild(mainMenu);
            }
            if(inputManager.getCursorPosition().x >= 1820.0f && inputManager.getCursorPosition().x <= 1903.0f && inputManager.getCursorPosition().y <= 1183.0f && inputManager.getCursorPosition().y >= 1101.0f && (isInSettingsKeyBinds || isInSettingsOptions || isInSettingsDebugOptions)) {
                isInSettingsKeyBinds = false;
                isInSettingsOptions = false;
                isInSettingsDebugOptions = false;
                isInSettings = true;
                guiNode.detachAllChildren();
                guiNode.attachChild(settingsGUI);
            }
            if(inputManager.getCursorPosition().x >= 753.0f && inputManager.getCursorPosition().x <= 1168.0f && inputManager.getCursorPosition().y <= 1106.0f && inputManager.getCursorPosition().y >= 1021.0f && isInSettings) {
                isInSettings = false;
                isInSettingsOptions = true;
                guiNode.detachChildNamed("Settings_Menu");
                optionsGUI = new Picture("Options_Menu");
                optionsGUI.setImage(assetManager, "Options.png", true);
                optionsGUI.setWidth(settings.getWidth());
                optionsGUI.setHeight(settings.getHeight());
                optionsGUI.setPosition(0, 0);
                guiNode.attachChild(optionsGUI);
            }
            if(inputManager.getCursorPosition().x >= 753.0f && inputManager.getCursorPosition().x <= 1168.0f && inputManager.getCursorPosition().y <= 1194.0f && inputManager.getCursorPosition().y >= 1113.0f && isInSettings) {
                isInSettings = false;
                isInSettingsKeyBinds = true;
                guiNode.detachChildNamed("Settings_Menu");
                keybindsGUI = new Picture("Keybind_Menu");
                keybindsGUI.setImage(assetManager, "Keybinds.png", true);
                keybindsGUI.setWidth(settings.getWidth());
                keybindsGUI.setHeight(settings.getHeight());
                keybindsGUI.setPosition(0, 0);
                guiNode.attachChild(keybindsGUI);
            }
            if(inputManager.getCursorPosition().x >= 753.0f && inputManager.getCursorPosition().x <= 1168.0f && inputManager.getCursorPosition().y <= 1015.0f && inputManager.getCursorPosition().y >= 931.0f && isInSettings) {
                isInSettings = false;
                isInSettingsDebugOptions = true;
                guiNode.detachChildNamed("Settings_Menu");
                debugOptionsGUI = new Picture("Debug_Menu");
                debugOptionsGUI.setImage(assetManager, "Debug_Options.png", true);
                debugOptionsGUI.setWidth(settings.getWidth());
                debugOptionsGUI.setHeight(settings.getHeight());
                debugOptionsGUI.setPosition(0, 0);
                guiNode.attachChild(debugOptionsGUI);
            }
            if(inputManager.getCursorPosition().x >= 776.0f && inputManager.getCursorPosition().x <= 1146.0f && inputManager.getCursorPosition().y <= 1149.0f && inputManager.getCursorPosition().y >= 1070.0f) {
                //Changes W Key To Another
            }
            if(inputManager.getCursorPosition().x >= 13.0f && inputManager.getCursorPosition().x <= 223.0f && inputManager.getCursorPosition().y <= 1018.0f && inputManager.getCursorPosition().y >= 977.0f) {
                //Basically What Happens When You Click Enter Inside Pause Menu
                flyCam.setEnabled(true);
                inputManager.setCursorVisible(false);
                guiNode.detachChildNamed("Pause_Menu");
                isInPauseMenu = false;
                canMove = true;
            }
            if(inputManager.getCursorPosition().x >= 13.0f && inputManager.getCursorPosition().x <= 661.0f && inputManager.getCursorPosition().y <= 932.0f && inputManager.getCursorPosition().y >= 881.0f) {}
            System.out.println(Float.toString(inputManager.getCursorPosition().x).concat(", ").concat(Float.toString(inputManager.getCursorPosition().y)));
        } else if (name.equals("SnapshotCoords")) {
            developedProducts++;
            //System.out.println(Float.toString(playerSpatial.getWorldTranslation().x).concat(", ").concat(Float.toString(playerSpatial.getWorldTranslation().y)).concat(", ").concat(Float.toString(playerSpatial.getWorldTranslation().z)));
        } else if (name.equals("Pause") && isPressed) {
            if(!isInPauseMenu) {
                flyCam.setEnabled(false);
                inputManager.setCursorVisible(true);
                guiNode.attachChild(pauseMenu);
                canMove = false;
                isInPauseMenu = true;
            } else {
                flyCam.setEnabled(true);
                inputManager.setCursorVisible(false);
                guiNode.detachChildNamed("Pause_Menu");
                isInPauseMenu = false;
                canMove = true;
            }
        } else if (name.equals("Crouch") && isPressed) {
            playerControl.setDucked(!playerControl.isDucked());
        } else if (name.equals("Set_FlyHack") && isPressed) {
            flyHacksEnabled = !flyHacksEnabled;
            //Also will test cursor positioning
            inputManager.setMouseCursor(null);
        } else if (name.equals("Interact") && isPressed) {
            if (intCont.interactionMap.get("Shop1.obj")) {
                if(!isInteractingWithShop1) {
                    flyCam.setEnabled(false);
                    inputManager.setCursorVisible(true);
                    guiNode.attachChild(shop1Gui);
                    isInteractingWithShop1 = true;
                    canMove = false;
                } else {
                    flyCam.setEnabled(true);
                    inputManager.setCursorVisible(false);
                    guiNode.detachChildNamed("Shop1GUI");
                    isInteractingWithShop1 = false;
                    canMove = true;
                }
            }
        }
        
        /*else if (name.equals("Play_Sound")) {
            AudioNode audioPlayer = new AudioNode(assetManager, "pop-402324.wav", DataType.Buffer);
            audioPlayer.setPositional(false);
            audioPlayer.setLooping(false);
            audioPlayer.setVolume(2);
            this.rootNode.attachChild(audioPlayer);
            audioPlayer.playInstance();
        }*/
    }
}
/**
 * This Class Is Responsible For Generating Rooms For Everything. Basically Just Abstracting Creating A Room
 * @apiNote The Reason It Is Called RoomGenerator Is Because I Originally Wanted A 3D Rouge-Like, But Decided Against It
 * @version 1.5.2 Beta
 */
class RoomGenerator{
    public AssetManager assetManager;
    public Node rootNode;
    public BulletAppState bulletAppState;
    public ArrayList<Geometry> roomNodeList;
    public static Geometry floorGeom3;
    public static RigidBodyControl floorPhysics3;
    /**
     * Creates A RoomGenerator To Spawn Stuff
     * @param assetManager Main App AssetManager, To Attach Things Correctly
     * @param rootNode Main App RootNode, To Attach Things Correctly
     * @param bulletAppState Main App BulletAppState, To Attach Things Correctly
     * @version 1.2.7 Beta
     * @author IDEKAnymoreTBH On Github
     */
    public RoomGenerator(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
        this.roomNodeList = new ArrayList<>();
    }
    /**
     * Generates A Room. This Is Basically The Only Room For Now.
     * @version 1.3.5 Beta
     * @author IDEKAnymoreTBH On Github
     */
    public void generateStarterRoom() {
        //Create Geometry Objects
        Geometry floorGeom = new Geometry("Floor", new Box(10, 1, 10));
        Geometry floorGeom2 = new Geometry("Floor2", new Box(10, 1, 10));
        floorGeom3 = new Geometry("Floor3", new Box(10, 1, 10));
        Geometry floorGeom4 = new Geometry("Floor4", new Box(10, 1, 10));
        Geometry floorGeom5 = new Geometry("Floor5", new Box(10, 1, 10));
        Geometry starterWallGeometry = new Geometry("Wall1", new Box(1, 10, 9));
        Geometry starterWallGeometry2 = new Geometry("Wall2", new Box(9, 10, 1));
        //Translates The Objects
        starterWallGeometry.setLocalTranslation(10, 8, 0);
        floorGeom.setLocalTranslation(0, -1, 0);
        floorGeom2.setLocalTranslation(10, -1, 0);
        floorGeom3.setLocalTranslation(-10, -1f, 0);
        floorGeom4.setLocalTranslation(0, -1, 10);
        floorGeom5.setLocalTranslation(0, -1, -10);
        starterWallGeometry2.setLocalTranslation(0, 8, 10);
        //Create Material For Geometry Objects
        //Material floorMat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //Material wallMat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material floorMat = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Material wallMat = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //Add Shadow Stuff
        floorGeom.setShadowMode(RenderQueue.ShadowMode.Receive);
        floorGeom2.setShadowMode(RenderQueue.ShadowMode.Receive);
        floorGeom3.setShadowMode(RenderQueue.ShadowMode.Receive);
        floorGeom4.setShadowMode(RenderQueue.ShadowMode.Receive);
        floorGeom5.setShadowMode(RenderQueue.ShadowMode.Receive);
        starterWallGeometry.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        starterWallGeometry2.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        //Sets The Color Of The Materials
        //floorMat.setColor("Color", ColorRGBA.Gray);
        //wallMat.setColor("Color", ColorRGBA.Gray);
        floorMat.setBoolean("UseMaterialColors", true);
        wallMat.setBoolean("UseMaterialColors", true);
        floorMat.setColor("Diffuse", ColorRGBA.Gray);
        wallMat.setColor("Diffuse", ColorRGBA.Gray);
        //Textures The Materials
        wallMat.setTexture("DiffuseMap", assetManager.loadTexture("WallTexture.png"));
        //Attaches The Material To It's Geometry
        floorGeom.setMaterial(floorMat);
        floorGeom2.setMaterial(floorMat);
        floorGeom3.setMaterial(floorMat);
        floorGeom4.setMaterial(floorMat);
        floorGeom5.setMaterial(floorMat);
        starterWallGeometry.setMaterial(wallMat);
        starterWallGeometry2.setMaterial(wallMat);
        //Attach The Geometry Object To The RootNode
        this.rootNode.attachChild(floorGeom);
        this.rootNode.attachChild(floorGeom2);
        this.rootNode.attachChild(floorGeom3);
        this.rootNode.attachChild(floorGeom4);
        this.rootNode.attachChild(floorGeom5);
        this.rootNode.attachChild(starterWallGeometry);
        this.rootNode.attachChild(starterWallGeometry2);
        //Creates The RigidBodyControl For The Geometry
        RigidBodyControl floorPhysics = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f);
        RigidBodyControl floorPhysics2 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f);
        floorPhysics3 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f); floorPhysics3.setKinematic(true);
        RigidBodyControl floorPhysics4 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f);
        RigidBodyControl floorPhysics5 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f);
        RigidBodyControl starterWallPhysics = new RigidBodyControl(new BoxCollisionShape(new Vector3f(((Box) starterWallGeometry.getMesh()).getXExtent(), 10, ((Box) starterWallGeometry.getMesh()).getZExtent())), 0f);
        RigidBodyControl starterWall2Physics = new RigidBodyControl(new BoxCollisionShape(new Vector3f(((Box) starterWallGeometry2.getMesh()).getXExtent(), 10, ((Box) starterWallGeometry2.getMesh()).getZExtent())), 0f);
        //Attaches The Control To Their Objects
        floorGeom.addControl(floorPhysics);
        floorGeom2.addControl(floorPhysics2);
        floorGeom3.addControl(floorPhysics3);
        floorGeom4.addControl(floorPhysics4);
        floorGeom5.addControl(floorPhysics5);
        starterWallGeometry.addControl(starterWallPhysics);
        starterWallGeometry2.addControl(starterWall2Physics);
        //Adds The Physics Control To BulletAppState
        this.bulletAppState.getPhysicsSpace().add(floorPhysics);
        this.bulletAppState.getPhysicsSpace().add(floorPhysics2);
        this.bulletAppState.getPhysicsSpace().add(floorPhysics3);
        this.bulletAppState.getPhysicsSpace().add(floorPhysics4);
        this.bulletAppState.getPhysicsSpace().add(floorPhysics5);
        this.bulletAppState.getPhysicsSpace().add(starterWallPhysics);
        this.bulletAppState.getPhysicsSpace().add(starterWall2Physics);
    }
}
/**
 * This Class Summons A Billboard Type Player, That Follows The Player.
 * @apiNote This Class May Become Deprecated Depending On If I Actually Use 3D Models
 * @version 0.1.4 Beta
 * @author IDEKAnymoreTBH On Github
 */
class CharacterObj {
    private Mesh npcMesh;
    private float npcWidth;
    private float npcHeight;
    public Geometry geo;
    private static int globalCharCount = 0;
    private Material mat;
    private AssetManager assetManager;
    public BillboardControl bbControl;
    private Node billBoardNode;
    /**
     * Creates A Character Object. This Character Will Follow The Player With Its Direction.
     * @param assetManager The Main Application AssetManager.
     * @param npcWidth The Width Of The Character
     * @param npcHeight The Height Of The Character
     */
    public CharacterObj(AssetManager assetManager, float npcWidth, float npcHeight) {
        this.assetManager = assetManager;
        this.npcWidth = npcWidth;
        this.npcHeight = npcHeight;
        this.npcMesh = new Mesh();
        this.npcMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{ //Change npcWidth and npcHeight to change dimensions
            -this.npcWidth/2, 0, 0,
            this.npcWidth/2, 0, 0,
            -this.npcWidth/2, this.npcHeight, 0,
            this.npcWidth/2, this.npcHeight, 0
        });
        this.npcMesh.setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{
            0,0, 1,0, 0,1, 1,1
        });
        this.npcMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0,1,2,2,1,3});
        this.npcMesh.setBuffer(VertexBuffer.Type.Normal, 3, new float[] {
            0,0,1,
            0,0,1,
            0,0,1,
            0,0,1
        });
        this.npcMesh.updateBound();
        this.npcMesh.updateCounts();
        this.geo = new Geometry("Character".concat(Integer.toString(globalCharCount + 1)), this.npcMesh);
        this.mat = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        this.mat.setTexture("DiffuseMap", this.assetManager.loadTexture("TestPNG.png"));
        this.mat.setBoolean("UseMaterialColors", true);
        this.mat.setColor("Diffuse", ColorRGBA.Cyan);
        this.geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        this.mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        this.geo.setMaterial(this.mat);
        this.geo.setQueueBucket(RenderQueue.Bucket.Transparent);
        bbControl = new BillboardControl();
        bbControl.setAlignment(BillboardControl.Alignment.AxialY);
        this.billBoardNode = new Node("BillBoardNode");
        this.billBoardNode.attachChild(this.geo);
        this.billBoardNode.addControl(bbControl);
        globalCharCount++;
    }
    public void render(Node rNode) {
        rNode.attachChild(this.billBoardNode);
        System.out.println("Mesh identity: " + System.identityHashCode(this.npcMesh));
        System.out.println("Geometry mesh identity: " + System.identityHashCode(this.geo.getMesh()));    
        rNode.updateGeometricState();
    }
}
/**
 * This Class Controls All Interactions. Upon Starting The Game, It Will List All Keys That Can Be Interacted
 * With. These Keys Can Be Updated Using The Aptly Named 'updateValue.'
 * @apiNote Seeing As This Game Is Not Yet Finished In Development, The Constructor Of InteractionController 
 * Can Only Put One Thing Inside The Interactions To Launch. That Being The Shop1. Further Interaction Objects Should Be Put Here
 * @version 1.0.4
 * @author IDEKAnymoreTBH On Github
 */
class InteractionController {
    public Map<String, Boolean> interactionMap;
    public InteractionController() {
        interactionMap = new HashMap<>();
        interactionMap.put("Shop1.obj", false);
        System.out.println("Your Choice Of Interactions: ".concat(Arrays.toString(interactionMap.keySet().toArray())));
    }
    /**
     * Updates The Value Of An Interaction Status.
     * @param name The Value You Want To Replace. All Possible Keys Are Listed When Starting A New Session
     * @param newVal The Value To Put For Said Key
     * @author IDEKAnymoreTBH On Github
     * @see InteractionController
     */
    public void updateValue(String name, boolean newVal) {
        interactionMap.put(name, newVal);
    }
}
/**
 * The {@code InvalidPurchaseException} Class Is Thrown By The Main Application Whenever A Purchase Is Made That Would Send The Player's Script Count Negative.
 * This Class Extends {@code RuntimeException} To Be Thrown Whenever.
 * @see RuntimeException
 * @author IDEKAnymoreTBH On Github
 * @version 1.0.2 Beta
 */
class InvalidPurchaseException extends RuntimeException {
    /**
     * Creates A New {@code InvalidPurchaseException}. These Tell If The Player Has Sufficient Amount To Buy Something.
     * @author IDEKAnymoreTBH On Github
     * @param m The Message. It Is Of Type Object, Meaning Anything Can Go There And Still Be Valid.
     * @see InvalidPurchaseException
     */
    public InvalidPurchaseException(Object m) {
        super(m.toString());
    }
}