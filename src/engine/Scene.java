package engine;
import java.util.*;

import renderer.Renderer;
public abstract class Scene {
	
	protected Renderer renderer = new Renderer();
	protected Camera camera;
	private boolean isRunning = false;
	protected List<GameObject> gameObjects = new ArrayList<>();
	
	public Scene() {
		
	}
	
	public void init() {
		
	}
	
	public void start() {
		for (GameObject go : gameObjects) {
			go.start();
			this.renderer.add(go);
		}
	}
	
	public void addGameObjectToScene(GameObject go) {
		if (!isRunning) {
			gameObjects.add(go);
		} else {
			gameObjects.add(go);
			go.start();
			this.renderer.add(go);
		}
	}
	
	public Camera camera() {
		return this.camera;
	}
	
	public abstract void update(float dt);
}
