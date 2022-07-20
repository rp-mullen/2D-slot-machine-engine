package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

import org.lwjgl.opengl.GL;

import util.Time;

public class Window {
	
	private static Window instance = null;
	private long windowID; 
	private ImGuiLayer imGuiLayer;
	
	private int width, height;
	private String title; 
	
	public float r, g, b , a;
	
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
		
	};
	
	public static void changeScene(int newScene) {
		switch (newScene) {
		case 0:
			currentScene = new LevelEditorScene();
			currentScene.init();
			currentScene.start();
			break;
		
		case 1:
			currentScene = new LevelScene();
			currentScene.init();
			currentScene.start();
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
	
	
	public static Scene getScene() {
		return get().currentScene;
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
		glfwSetWindowSizeCallback(windowID, (w, newWidth, newHeight) -> {
			Window.setWidth(newWidth);
			Window.setHeight(newHeight);
		});
		
		glfwMakeContextCurrent(windowID);
		
		// enable v-sync
		glfwSwapInterval(1);
		glfwShowWindow(windowID);
		
		
		GL.createCapabilities();
		
		this.imGuiLayer = new ImGuiLayer(windowID);
		this.imGuiLayer.initImGui();
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		
		Window.changeScene(0);
		}
	
	private void loop() {
		float beginTime = (float)glfwGetTime();
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
			
			this.imGuiLayer.update(dt);
			
			glfwSwapBuffers(windowID);
			
			endTime = (float)glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
			
		}
	}
	
	public static int getWidth() {
		return get().width;
	}
	
	public static int getHeight() {
		return get().height;
	}
	
	public static void setWidth(int newWidth) {
		get().width = newWidth;
	}
	
	public static void setHeight(int newHeight) {
		get().height = newHeight;
	}
}
