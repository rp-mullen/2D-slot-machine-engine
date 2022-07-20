package engine;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import util.AssetPool;
import static org.lwjgl.glfw.GLFW.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {
	
	private GameObject obj1;
	private GameObject obj2;

	private Spritesheet sprites;
	
	public LevelEditorScene() {
		
	}
	
	@Override
	public void init() {
		
		loadResources();
		
		this.camera = new Camera(new Vector2f(-250,0));
		
		sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");
		
		/*
		obj1 = new GameObject("Object 1", new Transform(new Vector2f(100,100), new Vector2f(256,256)));
		obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
		this.addGameObjectToScene(obj1);
		
		obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,100), new Vector2f(256,256)));
		obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
		this.addGameObjectToScene(obj2);
		*/
		
		System.out.println("Still running");
		
		obj1 = new GameObject("Object 1", new Transform(new Vector2f(200,100), new Vector2f(256,256)), 2);
		obj1.addComponent(new SpriteRenderer(new Sprite(
				AssetPool.getTexture("assets/images/blendImage1.png"))));
		this.addGameObjectToScene(obj1);
		
		obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,100), new Vector2f(256,256)), 2);
		obj2.addComponent(new SpriteRenderer(new Sprite(
				AssetPool.getTexture("assets/images/blendImage2.png"))));
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
}
