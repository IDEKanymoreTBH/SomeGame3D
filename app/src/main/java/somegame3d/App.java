/*Ideas For Game:
 * - Final Idea: JBattle: JME Krunker With Blocks To Place And Orbs To Throw
 * Class Ideas:
 * Traveler: Nothing But Fists. Twice As Fast As Everyone Else.
 * The Hyperborean Henchman: Uses Ice Spells To Destroy Opponents. Strongly Affected By The Boiling Befuddlement And RandEvents.Solar_Flare.
 * Schwarzschild's Miscreant: Uses Black Holes And Wormholes To Defeat Opponents. Is Susceptible To His Own Attacks And Imaginary Numbers.
 * The Torvald Turing Machine: Uses Linux Kernals And Git To Erase Enemies. Strongly Affected By RandEvents.Solar_Flare And Certain Technology.
 * The Boiling Befuddlement: Uses Very Hot Things To Destroy Opponents. Strongly Affected By The Hyperborean Henchman And RandEvents.Snow.
*/
package somegame3d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.light.AmbientLight;
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
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import com.simsilica.lemur.GuiGlobals;
import somegame3d.IATFileInterpreter.IATFileInterpretMode;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.core.Screen;
import com.jme3.scene.control.BillboardControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.post.FilterPostProcessor;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
/**The Main App That Does Everything */
public class App extends SimpleApplication implements ActionListener {
    /**Controls The Interactions A Player Can Do */
    public InteractionController intCont;
    //Global Values
    /**The Physics Controller For The Entire App Basically */
    public BulletAppState bulletAppState; //Physics Controller
    /**The Player*/
    public static PlayerObject player;
    /**The Node The Player Is On */
    public Node playerNode; // Node For Player
    /**Self Explanatory */
    public boolean moveForward = false; 
    /**Self Explanatory */
    public boolean moveBackwards = false;
    /**Self Explanatory */
    public boolean jump = false;
    /**Toggles Whether The FPS Is Shown */
    public boolean fpsShown = false;
    /**Self Explanatory */
    public boolean strafeLeft = false;
    /**Self Explanatory */
    public boolean strafeRight = false;
    /**The Walking Direction For The Player */
    public Vector3f walkDir = new Vector3f();
    /**The Desired Direction For The Player */
    public Vector3f desiredDir = new Vector3f();
    /**The Walking Direction Multilpier. Increases When Sprinting */
    public float walkDirMult = 4f;
    /**Self Explanatory */
    public boolean isInMainMenu = true;
    /**Self Explanatory */
    public boolean isInSettings = false;
    /**Self Explanatory */
    public boolean isInPatchNotes = false;
    /**Self Explanatory */
    public boolean isInSettingsOptions = false;
    /**Self Explanatory */
    public boolean isInSettingsKeyBinds = false;
    /**Self Explanatory */
    public boolean isInSettingsDebugOptions = false;
    /**Self Explanatory */
    public boolean flyHacksEnabled = false;
    /**Counts Up Until A Certain Amount, Then Decreases The Stamina.*/
    public int staminaDecreaseCounter = 0;
    /**Counts Up To A Certain Amount, Then Increases The Stamina */
    public int staminaIncreaseCounter = 0;
    /**Self Explanatory */
    public boolean isInteractingWithShop1 = false;
    /**Self Explanatory */
    public boolean canMove = true;
    /**Self Explanatory */
    public boolean isInPauseMenu = false;
    /**An Old Stat That Used To Be Used*/
    public int developedProducts = 0;
    /**The Moving Platforms List */
    public static ArrayList<Geometry> movingPlatforms = new ArrayList<>();
    public static boolean isInStoryMode = false;
    public static boolean isCircleActive = false;
    public static boolean isAltCircleActive = false;
    public static int primarySelected = 1;
    public static int secondarySelected = 1;
    public static float fpsTimer = 0f;
    public static int frameCount;
    //All GUI Elements(Menus, UI, Etc)
    Picture mainMenu;
    Picture settingsGUI;
    Picture keybindsGUI;
    Picture optionsGUI;
    Picture debugOptionsGUI;
    Picture staminaBar = new Picture("StaminaBar");
    BitmapFont font;
    BitmapText text;
    BitmapText FPSText;
    SpotLight flashlight;
    AmbientLight amblight;
    Picture crosshair = new Picture("CrossHair");
    Picture interactGui = new Picture("Interact_Gui");
    Picture shop1Gui = new Picture("Shop1GUI");
    Picture cursor;
    Picture pauseMenu = new Picture("Pause_Menu");
    Picture testGui = new Picture("FPSHand");
    Picture attackCircleGUI = new Picture("AttackCircle");
    Picture altAttackCircleGUI = new Picture("AltAttackCircle");
    RoomGenerator gen;
    //The Screen, Specifically Designed For ToneGodGUI
    public Geometry targetGeometry;
    private Screen screen;
    public static void main(String[] args) {
        App app = new App();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("JBattle");
        settings.setFullscreen(true);
        settings.setResolution(1920, 1200);
        settings.setVSync(true);
        app.setSettings(settings);
        app.start();
    }
    @Override
    public void simpleInitApp() {
        player = new PlayerObject(new Geometry("Box", new Box(0, 0, 0)), new BetterCharacterControl(0.5f * 2.8f, 1.8f * 2.8f, 80f), new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"), CompSciPlayerClass.class);
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
        //Interaction Stuff
        intCont = new InteractionController();
        //All Billboards That Help With Tutorials.
        CharacterObj charObj = new CharacterObj(assetManager, 2, 8, 0f, -1f, -20f, "Textures/WalkLook.png");
        CharacterObj charObj2 = new CharacterObj(assetManager, 2, 8, 0f, -1f, -30f, "Textures/SprintCrouch.png");
        charObj.render(rootNode);
        charObj2.render(rootNode);
        font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        text = new BitmapText(font);
        text.setSize(guiFont.getCharSet().getRenderedSize() + 10);
        text.setText(Integer.toString(player.getStamina()));
        text.setLocalTranslation(260, settings.getHeight() - 15, 0);
        text.setColor(ColorRGBA.Black);
        FPSText = new BitmapText(font);
        FPSText.setSize(guiFont.getCharSet().getRenderedSize() + 10);
        FPSText.setText(fpsText.getText());
        FPSText.setLocalTranslation(600, settings.getHeight() - 30, 0);
        FPSText.setColor(ColorRGBA.White);
        guiNode.attachChild(text);
        mainMenu = new Picture("Main_Menu");
        mainMenu.setImage(assetManager, "Textures/MainMenu.png", true);
        mainMenu.setWidth(settings.getWidth());
        mainMenu.setHeight(settings.getHeight());
        mainMenu.setPosition(0, 0);
        guiNode.attachChild(mainMenu);
        shop1Gui.setImage(assetManager, "Textures/Keypad.png", true);
        shop1Gui.setWidth(750);
        shop1Gui.setHeight(881);
        shop1Gui.setPosition(0, 0);
        staminaBar.setImage(assetManager, "Textures/StaminaBar.png", true);
        staminaBar.setWidth(1009);
        staminaBar.setHeight(791.015625f);
        staminaBar.setPosition(0, 410);
        guiNode.attachChild(staminaBar);
        interactGui.setImage(assetManager, "Textures/InteractGUI.png", true);
        interactGui.setWidth(2000);
        interactGui.setHeight(1000);
        interactGui.setPosition(0, 0);
        pauseMenu.setImage(assetManager, "Textures/PauseMenu.png", true);
        pauseMenu.setWidth(settings.getWidth());
        pauseMenu.setHeight(settings.getHeight());
        pauseMenu.setPosition(0, 0);
        crosshair.setImage(assetManager, "Textures/Crosshair.png", true);
        crosshair.setWidth(20);
        crosshair.setHeight(20);
        crosshair.setPosition((settings.getWidth()/2)-10, (settings.getHeight()/2)-10);
        attackCircleGUI.setImage(assetManager, "Textures/AttackCircle.png", true);
        attackCircleGUI.setWidth(settings.getWidth());
        attackCircleGUI.setHeight(settings.getHeight());
        attackCircleGUI.setPosition(0, 0);
        altAttackCircleGUI.setImage(assetManager, "Textures/AltAttackCircle.png", true);
        altAttackCircleGUI.setWidth(settings.getWidth());
        altAttackCircleGUI.setHeight(settings.getHeight());
        altAttackCircleGUI.setPosition(0, 0);
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
        viewPort.setBackgroundColor(ColorRGBA.Black);
        flashlight.setColor(ColorRGBA.White.mult(2.5f)); // brightness
        flashlight.setPosition(cam.getLocation());
        flashlight.setDirection(cam.getDirection());
        //flashlight.setPosition(new Vector3f(10, 10, 0));
        //flashlight.setDirection(new Vector3f(0, -1, 0));
        flashlight.setSpotRange(75f);                  // how far it shines
        flashlight.setSpotInnerAngle(FastMath.PI/2-1.35f);
        flashlight.setSpotOuterAngle(FastMath.PI/2-1.25f);
        rootNode.addLight(flashlight);
        amblight = new AmbientLight();
        amblight.setColor(ColorRGBA.White.mult(0.035f));
        rootNode.addLight(amblight);
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
        System.out.println("PlayerClass Name: " + player.getPlayerClass().getName());
        //Test Thing
        String texturePath = switch(player.getPlayerClass().getName()) {
            case "somegame3d.TravelerClass" -> new TravelerClass().getTexturePath();
            case "somegame3d.ColdPlayerClass" -> new ColdPlayerClass().getTexturePath();
            case "somegame3d.PhysicsPlayerClass" -> new PhysicsPlayerClass().getTexturePath();
            case "somegame3d.CompSciPlayerClass" -> new CompSciPlayerClass().getTexturePath();
            case "somegame3d.HotPlayerClass" -> new HotPlayerClass().getTexturePath();
            default -> new TravelerClass().getTexturePath();
        };
        testGui.setImage(assetManager, texturePath, true);
        testGui.setWidth(settings.getWidth());
        testGui.setHeight(settings.getHeight());
        testGui.setPosition(0, 0);
        //Floor
        gen = new RoomGenerator(assetManager, rootNode, bulletAppState);
        //gen.generateStarterRoom();
        //Wrap in Node
        playerNode = new Node("PlayerNode");
        playerNode.attachChild(player.getSpatial());
        //scale
        playerNode.setLocalScale(2.8f);
        //Offset model so feet align with capsule
        float capsuleHeight = 1.8f * 2.8f;
        player.getSpatial().setLocalTranslation(0, capsuleHeight / 2, 0);
        player.getControl().setJumpForce(new Vector3f(0, 500, 0));
        playerNode.addControl(player.getControl());
        bulletAppState.getPhysicsSpace().add(player.getControl());

        //Position Player above floor
        playerNode.setLocalTranslation(0, capsuleHeight / 2 + 0.01f, 0);
        playerNode.setLocalTranslation(0, 0.3f + capsuleHeight / 2 + 0.01f, 0);
        rootNode.attachChild(playerNode);
        //Input
        inputManager.addMapping("MoveForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("MoveBackwards", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("SnapshotCoords", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addMapping("Crouch", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addMapping("Set_FlyHack", new KeyTrigger(KeyInput.KEY_SEMICOLON));
        inputManager.addMapping("Interact", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Toggle_Flashlight", new KeyTrigger(KeyInput.KEY_RSHIFT));
        inputManager.addMapping("Activate_Circle", new KeyTrigger(KeyInput.KEY_LBRACKET));
        inputManager.addMapping("Activate_Alt_Circle", new KeyTrigger(KeyInput.KEY_RBRACKET));
        inputManager.addMapping("DoOneAction", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("DoTwoAction", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("DoThreeAction", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("DoFourAction", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addListener(this, "MoveForward", "Jump", "ToggleFPS", "MoveBackwards", "StrafeLeft", "StrafeRight", "Sprint", "SnapshotCoords", "Crouch", "Set_FlyHack", "Interact", "Pause", "Toggle_Flashlight", "Activate_Circle", "Activate_Alt_Circle", "DoOneAction", "DoTwoAction", "DoThreeAction", "DoFourAction");
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
        IATFileInterpreter iatfi = new IATFileInterpreter(IATFileInterpretMode.IATFILE_V1, rootNode, assetManager, bulletAppState);
        iatfi.interpret("MapFiles/TestMap.iat");
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        //Set Camera Fly Speed
        flyCam.setMoveSpeed(20);
    }
    @Override
    public void simpleUpdate(float tpf) {
        //Makes The Game Not Run Until The Menus Are Done
        if(isInMainMenu || isInSettings || isInSettingsKeyBinds || isInSettingsOptions || isInSettingsDebugOptions) return;
        fpsTimer += tpf;
        frameCount++;
        if(fpsTimer >= 1.0f) {
            FPSText.setText("FPS: " + Integer.toString(frameCount));
            frameCount = 0;
            fpsTimer = 0;
        }
        guiNode.attachChild(FPSText);
        String texturePath = switch(player.getPlayerClass().getName()) {
            case "somegame3d.TravelerClass" -> new TravelerClass().getTexturePath();
            case "somegame3d.ColdPlayerClass" -> new ColdPlayerClass().getTexturePath();
            case "somegame3d.PhysicsPlayerClass" -> new PhysicsPlayerClass().getTexturePath();
            case "somegame3d.CompSciPlayerClass" -> new CompSciPlayerClass().getTexturePath();
            case "somegame3d.HotPlayerClass" -> new HotPlayerClass().getTexturePath();
            default -> new TravelerClass().getTexturePath();
        };
        testGui.setImage(assetManager, texturePath, true);
        if(isCircleActive) {
            guiNode.attachChild(attackCircleGUI);
            guiNode.detachChild(altAttackCircleGUI);
            isAltCircleActive = false;
        } else {
            guiNode.detachChildNamed("AttackCircle");
        }
        if(isAltCircleActive) {
            guiNode.attachChild(altAttackCircleGUI);
            guiNode.detachChild(attackCircleGUI);
            isCircleActive = false;
        } else {
            guiNode.detachChild(altAttackCircleGUI);
        }
        flashlight.setPosition(cam.getLocation());
        flashlight.setDirection(cam.getDirection().normalize());
        guiNode.attachChild(staminaBar);
        guiNode.attachChild(testGui);
        guiNode.attachChild(crosshair);
        if(walkDirMult == 10f && (moveForward || moveBackwards || strafeLeft || strafeRight)) {
            if(staminaDecreaseCounter == 7) {
                staminaDecreaseCounter = 0;
                player.setStamina(player.getStamina() - 1);
            } else {
                staminaDecreaseCounter++;
            }
        } else {
            if(staminaIncreaseCounter == 20) {
                staminaIncreaseCounter = 0;
                if(player.getStamina() < 100) {
                    player.setStamina(player.getStamina() + 1);
                }
            } else {
                staminaIncreaseCounter++;
            }
        }
        if(player.getStamina() <= 0) {
            walkDirMult = 4f;
        }
        text.setText(Integer.toString(player.getStamina()));
        guiNode.attachChild(text);
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

            } else if((results.getClosestCollision().getGeometry() == gen.blockGeom) && (cam.getLocation().distanceSquared(results.getClosestCollision().getGeometry().getWorldTranslation()) <= 25)) {
                    guiNode.attachChild(interactGui);
                    intCont.updateValue("Block.test", true);
                } else {
                    guiNode.detachChildNamed("Interact_Gui");
                    intCont.updateValue("Shop1.obj", false);
                    intCont.updateValue("Block.test", false);
            }
        } else {
            guiNode.detachChildNamed("Interact_Gui");
            intCont.updateValue("Shop1.obj", false);
            intCont.updateValue("Block.test", false);
        }
        float accel = player.getControl().isOnGround() ? 6f : 1.5f;
        float friction = player.getControl().isOnGround() ? 4f : 0f;
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
        player.getControl().setWalkDirection(new Vector3f((walkDir.x * 1.5f) * walkDirMult, 0, (walkDir.z * 1.5f) * walkDirMult));
        player.getControl().setViewDirection(cam.getDirection());
        if(!flyHacksEnabled) {
            if(!player.getControl().isDucked()) {
                cam.setLocation(player.getSpatial().getWorldTranslation().add(new Vector3f(0f, -2.8f, 0f)));
            } else {
                cam.setLocation(player.getSpatial().getWorldTranslation().add(new Vector3f(0f, -4.2f, 0f)));
            }
        }
        //Below Controls How Far The Camera Can Tilt Up And Down
        float[] angles = cam.getRotation().toAngles(null);
        angles[0] = FastMath.clamp(angles[0], -FastMath.HALF_PI + 0.1f, FastMath.HALF_PI - 0.1f);
        cam.setRotation(new Quaternion().fromAngles(angles[0], angles[1], angles[2]));

        for (Geometry g : movingPlatforms) {
            g.setLocalTranslation(g.getLocalTranslation().add(new Vector3f(0, 0, 0f)));
            g.updateGeometricState();
            g.updateModelBound();
        }
    }
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("MoveForward") && !isInMainMenu && !isInSettings && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions && canMove) {
            moveForward = isPressed;
        } else if (name.equals("Jump") && isPressed) {
            if(canMove) {
                if (player.getControl().isOnGround()) {
                    player.getControl().jump();
                }
            } else if(!canMove && isInteractingWithShop1) {
                if(developedProducts >= 15) {
                    developedProducts -= 15;
                    System.out.println("Purchase Successful");
                } else {
                    throw new InvalidPurchaseException("Purchase Cannot Be Made. Insufficient Funds");
                }
            }
        } else if (name.equals("ToggleFPS") && !isInMainMenu && !isInSettings && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions) {
            if(!fpsShown) {
                fpsShown = true;
                setDisplayFps(fpsShown);
            }
            else {
                fpsShown = false;
                setDisplayFps(fpsShown);
            }
        } else if (name.equals("MoveBackwards") && !isInMainMenu && !isInSettings && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions && canMove) {
            moveBackwards = isPressed;
        } else if (name.equals("StrafeLeft") && !isInMainMenu && !isInSettings && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions && canMove) {
            strafeLeft = isPressed;
        } else if (name.equals("StrafeRight") && !isInMainMenu && !isInSettings && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions && canMove) {
            strafeRight = isPressed;
        } else if (name.equals("Sprint") && !isInMainMenu && !isInSettings && !isInSettingsKeyBinds && !isInSettingsOptions && !isInSettingsDebugOptions) {
            walkDirMult = (isPressed && player.getStamina() > 0) ? 10f : 4f;
        } else if (name.equals("Click") && isPressed) {
            if(isInStoryMode) {
                
            }
            if(inputManager.getCursorPosition().x >= 777.0f && inputManager.getCursorPosition().x <= 1142.0f && inputManager.getCursorPosition().y <= 901.0f && inputManager.getCursorPosition().y >= 782.0f && isInMainMenu) {
                //Story Mode
                inputManager.setCursorVisible(false);
                flyCam.setEnabled(true);
                isInMainMenu = false;
                isInStoryMode = true;
                canMove = true;
                guiNode.detachAllChildren();
            }
            if(inputManager.getCursorPosition().x >= 777.0f && inputManager.getCursorPosition().x <= 1142.0f && inputManager.getCursorPosition().y <= 662.0f && inputManager.getCursorPosition().y >= 537.0f && isInMainMenu) {
                //Multiplayer Button
                System.out.println("ScreenSize: " + Float.toString(screen.getWidth()) + ", " + Float.toString(screen.getHeight()));
                int albxW = 377;
                int albxH = 500;
                AlertBox albx = new AlertBox(screen, "alert", new Vector2f((screen.getWidth() / 2) - (albxW/2), (screen.getHeight() / 2 - albxH/2)), new Vector2f(albxW, albxH)) {
                    @Override
                    public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                        this.hide();
                        screen.removeElement(this);
                    }
                };
                albx.setMsg("Multiplayer Is Not Yet Added. Come Back Soon!");
                albx.setTextPadding(new Vector4f(0,0,0,0));
                albx.setButtonOkText("OK");
                screen.addElement(albx);
                albx.setLocalTranslation(albx.getX(), albx.getY(), 1000);
                albx.show();
            }
            if(inputManager.getCursorPosition().x >= 777.0f && inputManager.getCursorPosition().x <= 1142.0f && inputManager.getCursorPosition().y <= 419.0f && inputManager.getCursorPosition().y >= 299.0f && isInMainMenu) {
                //Settings
                isInSettings = true;
                isInMainMenu = false;
                guiNode.detachChildNamed("Main_Menu");
                settingsGUI = new Picture("Settings_Menu"); //AppSettings already is named settings :/
                settingsGUI.setImage(assetManager, "Textures/Settings.png", true);
                settingsGUI.setWidth(settings.getWidth());
                settingsGUI.setHeight(settings.getHeight());
                settingsGUI.setPosition(0, 0);
                guiNode.attachChild(settingsGUI);
            }
            if(inputManager.getCursorPosition().x >= 777.0f && inputManager.getCursorPosition().x <= 1142.0f && inputManager.getCursorPosition().y <= 187.0f && inputManager.getCursorPosition().y >= 68.0f && isInMainMenu) {
                //Quit
                System.exit(0);
            }
            if(inputManager.getCursorPosition().x >= 1820.0f && inputManager.getCursorPosition().x <= 1903.0f && inputManager.getCursorPosition().y <= 1183.0f && inputManager.getCursorPosition().y >= 1101.0f && isInSettings) {
                //Exit Button
                isInSettings = false;
                isInMainMenu = true;
                guiNode.attachChild(mainMenu);
                inputManager.setCursorVisible(true);
            }
            if(inputManager.getCursorPosition().x >= 1820.0f && inputManager.getCursorPosition().x <= 1903.0f && inputManager.getCursorPosition().y <= 1183.0f && inputManager.getCursorPosition().y >= 1101.0f && (isInSettingsKeyBinds || isInSettingsOptions || isInSettingsDebugOptions)) {
                //Exit Button 2
                isInSettingsKeyBinds = false;
                isInSettingsOptions = false;
                isInSettingsDebugOptions = false;
                isInSettings = true;
                guiNode.detachAllChildren();
                guiNode.attachChild(settingsGUI);
                System.out.println("IDEK3");
            }
            if(inputManager.getCursorPosition().x >= 753.0f && inputManager.getCursorPosition().x <= 1168.0f && inputManager.getCursorPosition().y <= 1106.0f && inputManager.getCursorPosition().y >= 1021.0f && isInSettings) {
                //Options
                isInSettings = false;
                isInSettingsOptions = true;
                guiNode.detachChildNamed("Settings_Menu");
                optionsGUI = new Picture("Options_Menu");
                optionsGUI.setImage(assetManager, "Textures/Options.png", true);
                optionsGUI.setWidth(settings.getWidth());
                optionsGUI.setHeight(settings.getHeight());
                optionsGUI.setPosition(0, 0);
                guiNode.attachChild(optionsGUI);
            }
            if(inputManager.getCursorPosition().x >= 753.0f && inputManager.getCursorPosition().x <= 1168.0f && inputManager.getCursorPosition().y <= 1194.0f && inputManager.getCursorPosition().y >= 1113.0f && isInSettings) {
                //Keybinding
                isInSettings = false;
                isInSettingsKeyBinds = true;
                guiNode.detachChildNamed("Settings_Menu");
                keybindsGUI = new Picture("Keybind_Menu");
                keybindsGUI.setImage(assetManager, "Textures/Keybinds.png", true);
                keybindsGUI.setWidth(settings.getWidth());
                keybindsGUI.setHeight(settings.getHeight());
                keybindsGUI.setPosition(0, 0);
                guiNode.attachChild(keybindsGUI);
            }
            if(inputManager.getCursorPosition().x >= 776.0f && inputManager.getCursorPosition().x <= 1146.0f && inputManager.getCursorPosition().y <= 1149.0f && inputManager.getCursorPosition().y >= 1070.0f) {
                //Changes W Key To Another
            }
            if(isInPauseMenu && inputManager.getCursorPosition().x >= 13.0f && inputManager.getCursorPosition().x <= 291.0f && inputManager.getCursorPosition().y <= 1018.0f && inputManager.getCursorPosition().y >= 977.0f) {
                //Resume Button In Start Menu
                if(!isInteractingWithShop1) {
                    flyCam.setEnabled(true);
                    inputManager.setCursorVisible(false);
                    canMove = true;
                }
                guiNode.detachChildNamed("Pause_Menu");
                isInPauseMenu = false;
            }
            if(isInPauseMenu && inputManager.getCursorPosition().x >= 13.0f && inputManager.getCursorPosition().x <= 291.0f && inputManager.getCursorPosition().y <= 837.0f && inputManager.getCursorPosition().y >= 789.0f) {
                //Quit Button Inside Pause Menu
                flyCam.setEnabled(false);
                isInStoryMode = false;
                isInMainMenu = true;
                guiNode.attachChild(mainMenu);
                inputManager.setCursorVisible(true);
            }
            if(inputManager.getCursorPosition().x >= 13.0f && inputManager.getCursorPosition().x <= 661.0f && inputManager.getCursorPosition().y <= 932.0f && inputManager.getCursorPosition().y >= 881.0f) {}
            System.out.println(Float.toString(inputManager.getCursorPosition().x).concat(", ").concat(Float.toString(inputManager.getCursorPosition().y)));
        } else if (name.equals("SnapshotCoords")) {
            developedProducts++;
            System.out.println("Cam Direction: " + cam.getDirection().toString());
            //System.out.println(Float.toString(playerSpatial.getWorldTranslation().x).concat(", ").concat(Float.toString(playerSpatial.getWorldTranslation().y)).concat(", ").concat(Float.toString(playerSpatial.getWorldTranslation().z)));
        } else if (name.equals("Pause") && isPressed) {
            if(!isInPauseMenu) {
                flyCam.setEnabled(false);
                inputManager.setCursorVisible(true);
                guiNode.attachChild(pauseMenu);
                canMove = false;
                isInPauseMenu = true;
            } else {
                if(!isInteractingWithShop1) {
                    flyCam.setEnabled(true);
                    inputManager.setCursorVisible(false);
                    canMove = true;
                }
                guiNode.detachChildNamed("Pause_Menu");
                isInPauseMenu = false;
            }
        } else if (name.equals("Crouch") && isPressed) {
            player.getControl().setDucked(!player.getControl().isDucked());
        } else if (name.equals("Set_FlyHack") && isPressed) {
            flyHacksEnabled = !flyHacksEnabled;
            //Also will test cursor positioning
            inputManager.setMouseCursor(null);
        } else if (name.equals("Interact") && isPressed) {
            if (intCont.getValue("Shop1.obj") && !isInPauseMenu) {
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
            } else if(intCont.getValue("Block.test")) {
                rootNode.detachChildNamed("BlockTest");
                bulletAppState.getPhysicsSpace().remove(gen.blockGeom);
            }
        } else if(name.equals("Toggle_Flashlight") && isPressed) {
            flashlight.setEnabled(!flashlight.isEnabled());
        } else if(name.equals("Activate_Circle")) {
            isCircleActive = isPressed;
        } else if(name.equals("Activate_Alt_Circle")) {
            isAltCircleActive = isPressed;
        } else if(name.equals("DoOneAction") && isPressed) {
            if(App.isCircleActive) {
                App.primarySelected = 1;
            } else if(App.isAltCircleActive) {
                App.secondarySelected = 1;
            } else {
                App.player.setPlayerClass(TravelerClass.class);
            }
        } else if(name.equals("DoTwoAction") && isPressed) {
            if(App.isCircleActive) {
                App.primarySelected = 2;
            } else if(App.isAltCircleActive) {
                App.secondarySelected = 2;
            } else {
                App.player.setPlayerClass(ColdPlayerClass.class);
            }
        } else if(name.equals("DoThreeAction") && isPressed) {
            if(App.isCircleActive) {
                App.primarySelected = 3;
            } else if(App.isAltCircleActive) {
                App.secondarySelected = 3;
            } else {
                App.player.setPlayerClass(PhysicsPlayerClass.class);
            }
        } else if(name.equals("DoFourAction") && isPressed) {
            if(App.isCircleActive) {
                App.primarySelected = 4;
            } else if(App.isAltCircleActive) {
                App.secondarySelected = 4;
            } else {
                App.player.setPlayerClass(CompSciPlayerClass.class);
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
    public Geometry blockGeom;
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
        Geometry floorGeom2 = new Geometry("Floor2", new Box(10, 1, 10));
        floorGeom3 = new Geometry("Floor3", new Box(10, 1, 10));
        Geometry floorGeom4 = new Geometry("Floor4", new Box(10, 1, 10));
        Geometry floorGeom5 = new Geometry("Floor5", new Box(10, 1, 10));
        Geometry starterWallGeometry = new Geometry("Wall1", new Box(1, 10, 9));
        Geometry starterWallGeometry2 = new Geometry("Wall2", new Box(9, 10, 1));
        //Translates The Objects
        starterWallGeometry.setLocalTranslation(10, 8, 0);
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
        wallMat.setTexture("DiffuseMap", this.assetManager.loadTexture("Textures/WallTexture.png"));
        //Attaches The Material To It's Geometry
        floorGeom2.setMaterial(floorMat);
        floorGeom3.setMaterial(floorMat);
        floorGeom4.setMaterial(floorMat);
        floorGeom5.setMaterial(floorMat);
        starterWallGeometry.setMaterial(wallMat);
        starterWallGeometry2.setMaterial(wallMat);
        //Attach The Geometry Object To The RootNode
        this.rootNode.attachChild(floorGeom2);
        this.rootNode.attachChild(floorGeom3);
        this.rootNode.attachChild(floorGeom4);
        this.rootNode.attachChild(floorGeom5);
        this.rootNode.attachChild(starterWallGeometry);
        this.rootNode.attachChild(starterWallGeometry2);
        //Creates The RigidBodyControl For The Geometry
        RigidBodyControl floorPhysics2 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f);
        floorPhysics3 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f);
        RigidBodyControl floorPhysics4 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f);
        RigidBodyControl floorPhysics5 = new RigidBodyControl(new BoxCollisionShape(new Vector3f(10, 1, 10)), 0f);
        RigidBodyControl starterWallPhysics = new RigidBodyControl(new BoxCollisionShape(new Vector3f(((Box) starterWallGeometry.getMesh()).getXExtent(), 10, ((Box) starterWallGeometry.getMesh()).getZExtent())), 0f);
        RigidBodyControl starterWall2Physics = new RigidBodyControl(new BoxCollisionShape(new Vector3f(((Box) starterWallGeometry2.getMesh()).getXExtent(), 10, ((Box) starterWallGeometry2.getMesh()).getZExtent())), 0f);
        //Attaches The Control To Their Objects
        floorGeom2.addControl(floorPhysics2);
        floorGeom3.addControl(floorPhysics3);
        floorGeom4.addControl(floorPhysics4);
        floorGeom5.addControl(floorPhysics5);
        starterWallGeometry.addControl(starterWallPhysics);
        starterWallGeometry2.addControl(starterWall2Physics);
        //Adds The Physics Control To BulletAppState
        this.bulletAppState.getPhysicsSpace().add(floorPhysics2);
        this.bulletAppState.getPhysicsSpace().add(floorPhysics3);
        this.bulletAppState.getPhysicsSpace().add(floorPhysics4);
        this.bulletAppState.getPhysicsSpace().add(floorPhysics5);
        this.bulletAppState.getPhysicsSpace().add(starterWallPhysics);
        this.bulletAppState.getPhysicsSpace().add(starterWall2Physics);
    }
    /**
     * Creates A Test Block For Right Now. IDEK.
     * @param x The X To Place It
     * @param y The Y To Place It
     * @param z The Z To Place It
     * @param width The Total Width Of The Block
     * @param height The Total Height Of The Block
     * @param depth The Total Depth/Length Of The Block
     * @author IDEKAnymoreTBH on Github
     */
    public void generateTestBlock(int x, int y, int z, int width, int height, int depth) {
        blockGeom = new Geometry("BlockTest", new Box(width/2, height/2, depth/2));
        blockGeom.setLocalTranslation(x, y, z);
        blockGeom.updateModelBound();
        blockGeom.updateGeometricState();
        Material blockMat = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        blockGeom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        blockMat.setBoolean("UseMaterialColors", true);
        blockMat.setColor("Diffuse", ColorRGBA.Gray);
        Texture tex = this.assetManager.loadTexture("Textures/BlockTexture.png");
        tex.setMagFilter(Texture.MagFilter.Nearest);
        tex.setMinFilter(Texture.MinFilter.Trilinear);
        blockMat.setTexture("DiffuseMap", tex);
        blockGeom.setMaterial(blockMat);
        this.rootNode.attachChild(blockGeom);
        RigidBodyControl blockPhysics = new RigidBodyControl(new BoxCollisionShape(new Vector3f(width/2, height/2, depth/2)), 0f);
        blockGeom.addControl(blockPhysics);
        this.bulletAppState.getPhysicsSpace().add(blockPhysics);
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
    private float x;
    private float y;
    private float z;
    private String texturePath;
    /**
     * Creates A Character Object. This Character Will Follow The Player With Its Direction.
     * @param assetManager The Main Application AssetManager.
     * @param npcWidth The Width Of The Character
     * @param npcHeight The Height Of The Character
     */
    public CharacterObj(AssetManager assetManager, float npcWidth, float npcHeight, float x, float y, float z, String texturePath) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.texturePath = texturePath;
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
        this.mat.setTexture("DiffuseMap", this.assetManager.loadTexture(this.texturePath));
        this.mat.setBoolean("UseMaterialColors", true);
        this.mat.setColor("Diffuse", ColorRGBA.Cyan);
        this.mat.setColor("Ambient", ColorRGBA.fromRGBA255(0, 100, 100, 1));
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
        this.billBoardNode.setLocalTranslation(this.x, this.y, this.z);
        globalCharCount++;
    }
    /**
     * Renders The BillBoard Character
     * @param rNode The RootNode To Attach It To. Must Be Same As Actual RootNode From App
     */
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
    private Map<String, Boolean> interactionMap;
    /**Constructs An Interaction Controller To Control Interactions */
    public InteractionController() {
        interactionMap = new HashMap<>();
        interactionMap.put("Shop1.obj", false);
        interactionMap.put("Block.test", false);
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
    /**
     * Gets A Value From The Interaction List
     * @param name The Key To Get A Value From
     * @return The Value, Or Null
     */
    public boolean getValue(String name) {
        return interactionMap.get(name);
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
/**This Class Has A Bunch Of Utilities That Are Used. 
 * @apiNote You Should Not Instantiate This Class. Doing So Throws An Exception. If You Want To Tho, See The See Also.
 * @see #Utils
*/
class Utils {
    private static final String PROJECT_URL = "https://mnxrzxedpwbfzkcunqtp.supabase.co/rest/v1/Servers";
    private static final String API_KEY = "sb_publishable_BSGqs8kQhXzDisKHDdWr_w_oQoIwuEN";
    /**An Enumerator For Every Block ID. */
    enum BlockID {
        /**A Test Block */
        TEST("Textures/BlockTexture.png"),
        /**Another Test Block */
        TEST2("Textures/SomeTexture.png");

        /**PNG/Texture Associated With An ID */
        private final String idTex;
        /**
         * Constructs An Entry For The BlockID Enumerator
         * @param val The Value To Set
         */
        private BlockID(String val) {
            this.idTex = val;
        }
        /**
         * Gets The Value Associated With An Entry
         * @return The Texture Associated With An ID
         */
        public String getValue() {
            return idTex;
        }
        /**
         * Returns The Entry In BlockID With The Specified Value, Or Null If None Exist.
         * @param val The Value To Check For
         * @return The Entry, Or Null
         */
        public static BlockID getForValue(String val) {
            for(BlockID bid : values()) {
                if(bid.idTex.equals(val)) {
                    return bid;
                }
            }
            return null;
        }
    }
    /**
     * Constructs A New Utils Object. You Shouldn't Do This
     * @throws UnsupportedOperationException Because This Operation Is NOT Supported
     * @author IDEKAnymoreTBH On Github
     */
    public Utils() {
        throw new UnsupportedOperationException("This Is A Utils Class. You Cannot Initialize It");
    }
    /**
     * Places A Block.
     * @param x The X To Place It At
     * @param y The Y To Place It At
     * @param z The Z To Place It At
     * @param width The Total Width To Give The Block
     * @param length The Total Length To Give The Block
     * @param height The Total Height To Give The Block
     * @param bid The BlockID To Give The Block (See BlockID Enum)
     * @see BlockID
     */
    public void placeBlock(int x, int y, int z, int width, int length, int height, BlockID bid) {
        //Geometry blockGeom = new Geometry();
    }
    public void createMap(String toCreate) {
        if(toCreate.equals("StoryMap")) {
            //Load Story Maps
        } else if(toCreate.equals("MultiplayerMap")) {
            //Load Some Random Map IDK
        }
    }
    /**
     * A Method I Intend To Use At Some Point...
     * @param keyVal The Value To Put Into The Database
     * @return The Http Response
     * @throws Exception Yeah A Lot Of Things Can Throw An Exception
     */
    public String insert(String keyVal) throws Exception {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(PROJECT_URL);
            request.setHeader("apikey", API_KEY);
            request.setHeader("Authorization", "Bearer " + API_KEY);
            request.setHeader("Content-Type", "application/json");
            String json = String.format("{\"Server_Test\": %s}", keyVal);
            request.setEntity(new StringEntity(json));
            return client.execute(request, response -> EntityUtils.toString(response.getEntity()));
        }
    }
    /**
     * Another Method I Intend To Use Someday...
     * @return The Response
     * @throws Exception If Something Goes Wrong
     */
    public String getAll() throws Exception {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(PROJECT_URL);
            request.setHeader("apikey", API_KEY);
            request.setHeader("Authorization", "Bearer " + API_KEY);
            return client.execute(request, response -> EntityUtils.toString(response.getEntity())); 
        }
    }
}
class IATFileInterpreter {
    /**The Interpret Succeeded */
    public int SUCCESS = 0;
    /**The Interpret Failed */
    public int FAILURE = 1;
    public String ifim;
    private Node rNode;
    private AssetManager assetManager;
    private BulletAppState bulletAppState;
    private static Texture textureToUse;
    /**All Different Interpret Modes For IATFileInterpreter */
    enum IATFileInterpretMode {
        /**The First Version Of IATFile.*/
        IATFILE_V1,
        /**The Second Version Of IATFile. (Shouldn't Be Used) */
        IATFILE_V2
    }
    /**
     * Constructs An IATFileInterpreter.
     * @param ifim The Interpret Mode
     * @param rNode The Root Node, For Placing Objects
     */
    public IATFileInterpreter(IATFileInterpretMode ifim, Node rNode, AssetManager assetManager, BulletAppState bulletAppState) {
        this.rNode = rNode;
        this.assetManager = assetManager;
        this.bulletAppState = bulletAppState;
        textureToUse = null;
        if(ifim == IATFileInterpretMode.IATFILE_V1) {
            this.ifim = "IAT_V1";
        } else if(ifim == IATFileInterpretMode.IATFILE_V2) {
            this.ifim = "IAT_V2";
        } else {
            throw new IllegalArgumentException("Error: IFIM Does Not Match Any Version");
        }
    }
    /**
     * Interprets An IAT File
     * @param fileName The Name Of The File To Interpret
     * @return An Int, Representing The Status. 0 = Success. 1 = Failure.
     * @author IDEKanymoreTBH On Github
     */
    public int interpret(String fileName) {
        //Checks If File Type Is IAT
        if(!fileName.endsWith(".iat")) {
            System.out.println("Error: Document '" + fileName + "' Is Not Of Type: IAT");
            return FAILURE;
        }
        //Path path = Paths.get(fileName);
        Path path;
        try {
            path = Path.of(getClass().getClassLoader().getResource("MapFiles/TestMap.iat").toURI());
        } catch(URISyntaxException err) {
            err.printStackTrace();
            System.out.println("Error: Path To File Is Invalid");
            return FAILURE;
        }
        System.out.println("Path: " + path.toString());
        Object[] t;
        try {
            t = Files.readAllLines(path, StandardCharsets.UTF_8).toArray();
        } catch(IOException err) {
            err.printStackTrace();
            System.out.println("Error: Malformed Or Unreadable Bytes Found In: " + fileName);
            return FAILURE;
        }
        Object[] objs;
        ArrayList<String> temp = new ArrayList<>();
        for(Object o : t) {
            if(!(((String)o).isBlank() || ((String)o).isEmpty())) {
                temp.add(o.toString());
            }
        }
        objs = temp.toArray();
        //Checks If File Is Not IAT
        System.out.println(objs[0]);
        if(!objs[0].equals("<DocType DocType.IAT>")) {
            System.out.println("Error: No Document Type Declaration Found");
            return FAILURE;
        }
        //Checks For IATVersion Mismatch
        if((objs[1].equals("<IATVersion 1.0.0>") && this.ifim.equals("IAT_V2")) || (objs[1].equals("<IATVersion 2.0.0>") && this.ifim.equals("IAT_V1"))) {
            System.out.println("Error: Internal Version Is: " + this.ifim + " But IAT File Was Written In Type: " + objs[1]);
            return FAILURE;
        }
        //Checks For IATVersion 2
        if(objs[1].equals("<IATVersion 2.0.0>")) {
            System.out.println("Error: IAT Versions 2.0.0+ Are Not Supported. Please Use IAT Version 1.0.0");
            return FAILURE;
        }
        //Checks If File Has Start And End Of Doc
        if(!(objs[2].equals("#StartDoc") && objs[objs.length - 1].equals("#EndDoc"))) {
            System.out.println("Error: IATFile Must Have Both A Start And An End To The Document. Check If Your File Has '#StartDoc' And '#EndDoc'");
            return FAILURE;
        }
        Object[] temp2 = Arrays.copyOfRange(objs, 2, objs.length - 1);
        for(int i = 1; i < temp2.length; i++) {
            if(temp2[i].toString().startsWith("ObjCreator.create('") && temp2[i].toString().endsWith(");")) {
                if(IATFileInterpreter.CMDGenerateObject(temp2[i].toString(), this.assetManager, this.bulletAppState, this.rNode) == "Error") {
                    System.out.println("Error: CMDGenerateObject.java Tried To Create An Object Of Non-Existant Type");
                    return FAILURE;
                }
            } else if(temp2[i].toString().startsWith("IATPrintStream.printf('") && temp2[i].toString().endsWith("');")) {
                IATFileInterpreter.CMDPrint(temp2[i].toString().substring(temp2[i].toString().indexOf("printf('") + 8, temp2[i].toString().indexOf("');")));
            } else if(temp2[i].toString().startsWith("IATSystemManager.crash('") && temp2[i].toString().endsWith("');")) {
                IATFileInterpreter.CMDCrash(temp2[i].toString().substring(temp2[i].toString().indexOf("crash('") + 7, temp2[i].toString().indexOf("');")));
            } else if(temp2[i].toString().startsWith("ObjCreator.setTexture('") && temp2[i].toString().endsWith("');")) {
                if(IATFileInterpreter.CMDChangeTexture(temp2[i].toString(), this.assetManager) == null) {
                    System.out.println("Error: SetTexture Call Did Not Succeed Because The Texture Path Was Invalid");
                    return FAILURE;
                } else {
                    textureToUse = IATFileInterpreter.CMDChangeTexture(temp2[i].toString(), this.assetManager);
                }
            } //else if(temp2[i].toString().startsWith("ObjCreator.createMoveable('") && temp2[i].toString().endsWith(");")) {
                //if(IATFileInterpreter.CMDGenerateMovingObj(temp2[i].toString(), this.assetManager, this.bulletAppState, this.rNode).equals("Error")) {
                    //System.out.println("An Error Occured Making A Moving Platform");
                //}
            /*}*/ else {
                //Syntax Error
                System.out.println(String.format("Error: Line %d From: '%s' Is Not Any Recognized Command, Or Has A Syntax Error.", i + 2, fileName));
                return FAILURE;
            }
        }
        return SUCCESS;
    }
    /**
     * Prints From The Commands Inside An IATFile
     * @param text The Text To Print
     */
    private static void CMDPrint(String text) {
        System.out.println("Output From [IATFile]: " + text);
    }
    /**
     * Generates An Object Based On The IATFile Stuff.
     */
    private static Object CMDGenerateObject(String interpreterObjThing, AssetManager am, BulletAppState bas, Node rNode) {
        System.out.println(interpreterObjThing);
        int startI = interpreterObjThing.indexOf("'") + 1;
        int endI = interpreterObjThing.lastIndexOf("'");
        String temp = interpreterObjThing.substring(startI, endI);
        int[] actualPositions = new int[6];
        System.out.println(temp);
        if(temp.equals("Geometry")) {
            ArrayList<Character> chars = new ArrayList<>();
            for (int i = 0; i < interpreterObjThing.toCharArray().length; i++) {
                if(i > 29 && (((Character)interpreterObjThing.charAt(i)).equals('-') || ((Character)interpreterObjThing.charAt(i)).equals(',') || Character.isDigit(interpreterObjThing.charAt(i)))) {
                    chars.add(interpreterObjThing.charAt(i));
                }
            }
            for (int j = 0; j < actualPositions.length; j++) {
                System.out.println("Chars: " + chars.toString());
                String temp2 = "";
                for (int i = 0; i < chars.size(); i++) {
                    System.out.println(chars.get(i));
                    if(chars.get(i).equals('-') || Character.isDigit(chars.get(i))) {
                        temp2 = temp2.concat(chars.get(i).toString());
                    } else {
                        break;
                    }
                }
                System.out.println(temp2);
                for (int i = 0; i < temp2.length() + 1; i++) {
                    if(chars.size() == 1) {
                        break;
                    }
                    chars.remove(0);
                }
                System.out.println("Characters Final: " + chars.toString());
                actualPositions[j] = Integer.parseInt(temp2);
            }
            System.out.println(Arrays.toString(actualPositions));
            Geometry geo = new Geometry("Geometry At X: " + Integer.toString(actualPositions[0]) + ", Y: " + Integer.toString(actualPositions[1]) + ", Z: " + Integer.toString(actualPositions[2]), new Box(actualPositions[3]/2, actualPositions[4]/2, actualPositions[5]/2));
            geo.setLocalTranslation(actualPositions[0], actualPositions[1], actualPositions[2]);
            Material mat = new Material(am, "Common/MatDefs/Light/Lighting.j3md");
            geo.setShadowMode(ShadowMode.CastAndReceive);
            mat.setBoolean("UseMaterialColors", true);
            mat.setColor("Diffuse", ColorRGBA.LightGray);
            mat.setColor("Ambient", ColorRGBA.Gray);
            Texture tex = IATFileInterpreter.textureToUse;
            tex.setMagFilter(Texture.MagFilter.Nearest);
            tex.setMinFilter(Texture.MinFilter.Trilinear);
            mat.setTexture("DiffuseMap", tex);
            geo.setMaterial(mat);
            rNode.attachChild(geo);
            RigidBodyControl physics;
            physics = new RigidBodyControl(new BoxCollisionShape(new Vector3f(actualPositions[3]/2, actualPositions[4]/2, actualPositions[5]/2)), 0f);
            physics.setKinematic(false);
            geo.addControl(physics);
            bas.getPhysicsSpace().add(physics);
            return geo;
        } else {
            System.out.println("Error: Tried To Create An Object Of Type 'Non-Existant'");
            return "Error";
        }
    }
    private static Texture CMDChangeTexture(String textToInterpret, AssetManager am) {
        String temp = textToInterpret.substring(textToInterpret.indexOf("ObjCreator.setTexture('") + 23, textToInterpret.indexOf("');"));
        try {
            Texture tex = am.loadTexture(temp);
            tex.setMagFilter(Texture.MagFilter.Nearest);
            tex.setMinFilter(Texture.MinFilter.Trilinear);
            return tex;
        } catch(AssetNotFoundException err) {
            System.out.println("Error: Texture " + temp + " Is Not A Valid Texture.");
            return null;
        }
    }
    // private static String CMDGenerateMovingObj(String interpretText, AssetManager am, BulletAppState bas, Node rootNode) {
    //     int startI = interpretText.indexOf("'") + 1;
    //     int endI = interpretText.lastIndexOf("'");
    //     String temp = interpretText.substring(startI, endI);
    //     int[] actualPositions = new int[6];
    //     if(temp.equals("Geometry")) {
    //         ArrayList<Character> chars = new ArrayList<>();
    //         for (int i = 0; i < interpretText.toCharArray().length; i++) {
    //             if(i > 37 && (((Character)interpretText.charAt(i)).equals('-') || ((Character)interpretText.charAt(i)).equals(',') || Character.isDigit(interpretText.charAt(i)))) {
    //                 chars.add(interpretText.charAt(i));
    //             }
    //         }
    //         for (int j = 0; j < actualPositions.length; j++) {
    //             String temp2 = "";
    //             System.out.println("Chars: [Moving OBJ]: " + chars);
    //             for (int i = 0; i < chars.size(); i++) {
    //                 if(chars.get(i).equals('-') || Character.isDigit(chars.get(i))) {
    //                     temp2 = temp2.concat(chars.get(i).toString());
    //                 } else {
    //                     break;
    //                 }
    //             }
    //             for(int i = 0; i < temp2.length() + 1; i++) {
    //                 if(chars.size() == 1) {
    //                     break;
    //                 }
    //                 chars.remove(0);
    //             }
    //             System.out.println("Temporary String 2 [Moving Object Wise]: " + temp2);
    //             actualPositions[j] = Integer.parseInt(temp2);
    //         }
    //         System.out.println("Moving Platform's Positions: " + Arrays.toString(actualPositions));
    //         Geometry geo = new Geometry("Geometry At X: " + Integer.toString(actualPositions[0]) + ", Y: " + Integer.toString(actualPositions[1]) + ", Z: " + Integer.toString(actualPositions[2]), new Box(actualPositions[3]/2, actualPositions[4]/2, actualPositions[5]/2));
    //         geo.setLocalTranslation(actualPositions[0], actualPositions[1], actualPositions[2]);
    //         Material mat = new Material(am, "Common/MatDefs/Light/Lighting.j3md");
    //         geo.setShadowMode(ShadowMode.CastAndReceive);
    //         mat.setBoolean("UseMaterialColors", true);
    //         mat.setColor("Diffuse", ColorRGBA.LightGray);
    //         mat.setColor("Ambient", ColorRGBA.Gray);
    //         Texture tex = IATFileInterpreter.textureToUse;
    //         tex.setMagFilter(Texture.MagFilter.Nearest);
    //         tex.setMinFilter(Texture.MinFilter.Trilinear);
    //         mat.setTexture("DiffuseMap", tex);
    //         geo.setMaterial(mat);
    //         rootNode.attachChild(geo);
    //         RigidBodyControl physics;
    //         physics = new RigidBodyControl(new BoxCollisionShape(new Vector3f(actualPositions[3]/2, actualPositions[4]/2, actualPositions[5]/2)), 1f);
    //         physics.setKinematic(true);
    //         geo.addControl(physics);
    //         bas.getPhysicsSpace().add(physics);
    //         App.movingPlatforms.add(geo);
    //         return "YAY";
    //     } else {
    //         System.out.println("Error From MovObj: Non-Existant Type Tried.");
    //         return "Error";
    //     }
    // }
    /**
     * You Only Use This Method To Crash The Game. Not Very Good...
     * @param actualText The Text For The RuntimeException
     * @throws RuntimeException Thrown Everytime
     */
    private static void CMDCrash(String actualText) throws RuntimeException{
        throw new RuntimeException("Error Thrown From [IATFile]: " + actualText);
    }
}
/**This Interface Is Used For Every Class That A Player Can Select.*/
interface IPlayerClass {
    public String getDescription();
    public String getTexturePath();
    public void doPrimaryAttack();
    public void doSecondaryAttack();
}
class TravelerClass implements IPlayerClass {
    public String getDescription() {
        return "This Guy Just Punches People. Probably Not Very Effective...";
    }
    public String getTexturePath() {
        return "Textures/Hands.png";
    }
    public void doPrimaryAttack() {
        switch(App.primarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
    public void doSecondaryAttack() {
        switch(App.secondarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
}
class ColdPlayerClass implements IPlayerClass {
    public String getDescription() {
        return "Almost 0 Kelvin. Use This To Your Advantage. Watch For Sources Of Heat BTW.";
    }
    public String getTexturePath() {
        return "Textures/ColdHands.png";
    }
    public void doPrimaryAttack() {
        switch(App.primarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
    public void doSecondaryAttack() {
        switch(App.secondarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
}
class PhysicsPlayerClass implements IPlayerClass {
    public String getDescription() {
        return "Uses Physics (Such As Wormholes Or Black Hole Spawning Radii) To His Advantage. Not A Big Fan Of Imaginary Numbers.";
    }
    public String getTexturePath() {
        return "Textures/PhysicsHands.png";
    }
    public void doPrimaryAttack() {
        switch(App.primarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
    public void doSecondaryAttack() {
        switch(App.secondarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
}
class CompSciPlayerClass implements IPlayerClass {
    public String getDescription() {
        return "Uses His Git Versions And Linux Kernals To Destroy The Competition. Big Fan Of Imaginary Numbers, And Despiser Of Solar Flares.";
    }
    public String getTexturePath() {
        return "Textures/CompSciHands.png";
    }
    public void doPrimaryAttack() {
        switch(App.primarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
    public void doSecondaryAttack() {
        switch(App.secondarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
}
class HotPlayerClass implements IPlayerClass {
    public String getDescription() {
        return "Contains The Heat Of An Exploding Star Inside Of Him. Not A Fan Of The Cold.";
    }
    public String getTexturePath() {
        return "Textures/HotHands.png";
    }
    public void doPrimaryAttack() {
        switch(App.primarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
    public void doSecondaryAttack() {
        switch(App.secondarySelected) {
            case 1:
                //Do First Attack
                break;
            case 2:
                //Do Second Attack
                break;
            case 3:
                //Do Third Attack
                break;
            case 4:
                //Do Fourth Attack
                break;
            default:
                throw new UnknownError("Somehow, Someway, The Primary Attack Selected Was Outside The Range Of Possible Attacks.");
        }
    }
}
class PlayerObject {
    private Spatial playerSpatial;
    private int stamina;
    private BetterCharacterControl playerControl;
    private Material playerMat;
    private Class<? extends IPlayerClass> pClass;
    public PlayerObject(Spatial playerSpatial, BetterCharacterControl playerControl, Material playerMat, Class<? extends IPlayerClass> pClass) {
        this.playerSpatial = playerSpatial;
        this.stamina = 100;
        this.playerControl = playerControl;
        this.playerMat = playerMat;
        this.playerSpatial.setMaterial(this.playerMat);
        this.pClass = pClass;
    }
    public int getStamina() {
        return this.stamina;
    }
    public void setStamina(int newStamina) {
        this.stamina = newStamina;
    }
    public Spatial getSpatial() {
        return this.playerSpatial;
    }
    public BetterCharacterControl getControl() {
        return this.playerControl;
    }
    public Class<? extends IPlayerClass> getPlayerClass() {
        return this.pClass;
    }
    public void setPlayerClass(Class<? extends IPlayerClass> newClass) {
        this.pClass = newClass;
    }
}
//To Compile This, First Get It Into A Fat JAR Using ShadowJar, Then Do:
//jpackage --name GameName --input build/libs --main-jar GameName-1.0.jar --main-class packagePath --type exe / dmg / deb --icon resources/icons/game.(ico/icns) (Dont Put --icon For Linux)
//If Needed, Add --app-version, --vendor, --description