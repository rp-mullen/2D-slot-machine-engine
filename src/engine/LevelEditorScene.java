package engine;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import util.AssetPool;
import static org.lwjgl.glfw.GLFW.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LevelEditorScene extends Scene {
	
	private GameObject obj1;
    private Spritesheet sprites;
    SpriteRenderer obj1Sprite;
    
	public LevelEditorScene() {
		
	}
	
	@Override
	public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        if (levelLoaded) {
            return;
        }

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)), 2);
        obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
        obj1.addComponent(obj1Sprite);
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);
    }
	
	private void loadResources() {
		AssetPool.getShader("assets/shaders/default.glsl");
		AssetPool.addSpritesheet("assets/images/spritesheet.png", 
				new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
						16, 16, 26, 0));
	}
	
	
	private int spriteIndex = 0;
	private float spriteFlipTime = 0.2f;
	private float spriteFlipTimeLeft = 0.0f;
	@Override
	public void update(float dt) {
		
		/*
		spriteFlipTimeLeft -= dt;
		if (spriteFlipTimeLeft <= 0) {
			spriteFlipTimeLeft = spriteFlipTime;
			spriteIndex++;
			if (spriteIndex > 4) {
				spriteIndex = 0;
			}
			obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
		}
		
		*/
		
		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}
		
		this.renderer.render();
	}
	
	@Override
	public void imgui() {
		ImGui.begin("Test Window");
		ImGui.text("Some random text");
		ImGui.end();
	}
}
