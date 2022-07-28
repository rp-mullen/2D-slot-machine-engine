package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.*;

import org.lwjgl.opengl.GL;

import renderer.DebugDraw;
import renderer.Framebuffer;
import renderer.PickingTexture;
import renderer.Renderer;
import renderer.Shader;
import scenes.LevelEditorScene;
import scenes.LevelScene;
import scenes.Scene;
import util.AssetPool;
import util.Settings;
import util.Time;

public class Window {
	
	private static Window instance = null;
	private long windowID; 
	private ImGuiLayer imGuiLayer;
	
	private int width, height;
	private String title; 
	
	private Framebuffer framebuffer;
	private PickingTexture pickingTexture;
	
	
	public float r, g, b , a;
	
	private static Scene currentScene;
	
	// note: singleton pattern!
	private Window() {
		this.width = Settings.SCREEN_NATIVE_RESOLUTION_X;
		this.height = Settings.SCREEN_NATIVE_RESOLUTION_Y;
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
			currentScene.load();
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
		
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		
		this.framebuffer = new Framebuffer(Settings.SCREEN_NATIVE_RESOLUTION_X, Settings.SCREEN_NATIVE_RESOLUTION_Y);
		this.pickingTexture = new PickingTexture(Settings.SCREEN_NATIVE_RESOLUTION_X,Settings.SCREEN_NATIVE_RESOLUTION_Y);
		glViewport(0,0,Settings.SCREEN_NATIVE_RESOLUTION_X,Settings.SCREEN_NATIVE_RESOLUTION_Y);
		
		this.imGuiLayer = new ImGuiLayer(windowID,pickingTexture);
		this.imGuiLayer.initImGui();
		
		
		Window.changeScene(0);
		}
	
	private void loop() {
		float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(windowID)) {
            // Poll events
            glfwPollEvents();

            // Render pass 1. Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, Settings.SCREEN_NATIVE_RESOLUTION_X, Settings.SCREEN_NATIVE_RESOLUTION_Y);
            
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();


            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            // Render pass 2. Render actual game
            DebugDraw.beginFrame();

            this.framebuffer.bind();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                DebugDraw.draw();
                Renderer.bindShader(defaultShader);
                currentScene.update(dt);
                currentScene.render();
            }
            this.framebuffer.unbind();

            this.imGuiLayer.update(dt, currentScene);
            glfwSwapBuffers(windowID);
            MouseListener.endFrame();
            
            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        currentScene.saveExit();
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
	
	public static Framebuffer getFramebuffer() {
		return get().framebuffer;
	}
	
	public static float getTargetAspectRatio() {
		return 16.0f / 10.0f;
	}
	
	public static ImGuiLayer getImguiLayer() {
		return get().imGuiLayer;
	}
}
