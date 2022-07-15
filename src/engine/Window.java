package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

import org.lwjgl.opengl.GL;

import util.Time;

public class Window {
	
	private static Window instance = null;
	private long windowID; 
	
	private int width, height;
	private String title; 
	
	public float r, g, b , a;
	private boolean fadeOut;
	
	private static Scene currentScene;
	
	// note: singleton pattern!
	private Window() {
		this.width = 800;
		this.height = 600;
		this.title = "Window";
		
		r= 1;
		g = 1;
		b = 1;
		a = 1;
		
		fadeOut = false;
	};
	
	public static void changeScene(int newScene) {
		switch (newScene) {
		case 0:
			currentScene = new LevelEditorScene();
			currentScene.init();
			break;
		
		case 1:
			currentScene = new LevelScene();
			currentScene.init();
			break;
		default:
			assert false : "Unknown scene '" + newScene + "'";
		}
	}
	
	public static Window get() {
		if (instance == null) {
			instance = new Window();
		}
		
		return instance;
	}
	
	public void run() {
		init();
		loop();
		
		// free memory
		glfwDestroyWindow(windowID);
		glfwTerminate();
	}
	
	private void init() {
		glfwInit();
		
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		// v. 3.3
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		
		// generate window ID & focus context
		windowID = glfwCreateWindow(this.width, this.height, this.title, 0, 0);
		
		glfwSetCursorPosCallback(windowID,MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(windowID,MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(windowID,MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(windowID,KeyListener::keyCallback);
		
		glfwMakeContextCurrent(windowID);
		
		// enable v-sync
		glfwSwapInterval(1);
		
		GL.createCapabilities();
		
		Window.changeScene(0);
		glfwShowWindow(windowID);
	}
	
	private void loop() {
		float beginTime = Time.getTime();
		float endTime;
		
		float dt = -1.0f;
		while (!glfwWindowShouldClose(windowID)) {
			
			
			glfwPollEvents();
			
			glClearColor(r,g,b,a);
			glClear(GL_COLOR_BUFFER_BIT);
			
			if (dt >= 0) {
				// does events in update -> fade out
				currentScene.update(dt);
			}
			
			
			glfwSwapBuffers(windowID);
			
			endTime = Time.getTime();
			dt = endTime - beginTime;
			beginTime = endTime;
			
		}
	}
	
}
