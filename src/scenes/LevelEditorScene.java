package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.EditorCamera;
import components.GizmoSystem;
import components.GridLines;
import components.MouseControls;
import components.RigidBody;
import components.ScaleGizmo;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import components.TranslateGizmo;
import editor.GameBackground;
import editor.Slot;
import editor.SlotMachine;
import engine.Camera;
import engine.GameObject;
import engine.MouseListener;
import engine.Prefabs;
import engine.Transform;
import engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import renderer.DebugDraw;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;
    private Sprite slotSprite;
    
    GameObject levelEditorStuff = this.createGameObject("LevelEditor");
    
    public LevelEditorScene() {

    }

    
    
    @Override
    public void init() {
    	
    	loadResources();
    	sprites = AssetPool.getSpritesheet("assets/images/spritesheets/slotMachineSPrites.png");
    	Spritesheet gizmos = AssetPool.getSpritesheet("assets/images/spritesheets/gizmos.png");
    	
    	Sprite BGSprite = new Sprite();
    	BGSprite.setTexture(AssetPool.getTexture("assets/images/casinoGameBg.jpg"));
    	
    	this.camera = new Camera(new Vector2f(0, 0));
    	levelEditorStuff.addComponent(new MouseControls());
    	levelEditorStuff.addComponent(new GridLines());
    	levelEditorStuff.addComponent(new EditorCamera(this.camera));
    	levelEditorStuff.addComponent(new GameBackground(BGSprite));
    	levelEditorStuff.addComponent(new SlotMachine(sprites));
    	levelEditorStuff.addComponent(new GizmoSystem(gizmos));
    	
    	levelEditorStuff.start();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheets/gizmos.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/gizmos.png"),
                        24, 48, 3, 0));
        
        AssetPool.getTexture("assets/images/casinoGameBg.jpg");
        
        AssetPool.addSpritesheet("assets/images/spritesheets/slotMachineSPrites.png",
        		new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/slotMachineSprites.png"),
        				200,200,9,0));
        
        for (GameObject g : gameObjects) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
        }
    }

    
    float x = 0.0f;
    float y = 0.0f;
    
    float t = 0.0f;
    float angle = 0.0f;
    
    @Override
    public void update(float dt) {
    	levelEditorStuff.update(dt);
    	this.camera.adjustProjection();
   
    	for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        
    }

    
    @Override
    public void render() {
    	this.renderer.render();
    }
    
    @Override
    public void imgui() {
    	ImGui.begin("Level editor stuff");
    	levelEditorStuff.imgui();
    	ImGui.end();
    	
        ImGui.begin("Test window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i=0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                // attach to mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}