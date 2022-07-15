package engine;
import java.awt.event.KeyEvent;
import java.nio.*;
import org.lwjgl.BufferUtils;

import renderer.*;

import static org.lwjgl.opengl.GL33.*;


public class LevelEditorScene extends Scene {
	
	private String vertexShaderSrc = 
			"#version 330 core\n" + 
			"\n" + 
			"layout (location = 0) in vec3 aPos;\n" + 
			"layout (location = 1) in vec4 aColor;\n" + 
			"\n" + 
			"out vec4 fColor;\n" + 
			"\n" + 
			"void main() \n" + 
			"{\n" + 
			"	fColor = aColor;\n" + 
			"	gl_Position = vec4(aPos,1.0);	\n" + 
			"}\n" + 
			"";
	private String fragmentShaderSrc =
			"#version 330 core\n" + 
			"\n" + 
			"in vec4 fColor;\n" + 
			"\n" + 
			"out vec4 color;\n" + 
			"\n" + 
			"void main() \n" + 
			"{\n" + 
			"	color = fColor;\n" + 
			"}";
	
	private int vertexID, fragmentID, shaderProgram;
	private Shader defaultShader;
	
	private float[] vertexArray = {
		// vertices					// color
		0.5f, -0.5f, 0.0f,			1.0f, 0.0f, 0.0f, 1.0f, // bottom right
		-0.5f, 0.5f, 0.0f,			0.0f, 1.0f, 0.0f, 1.0f, // top left
		0.5f, 0.5f, 0.0f,			0.0f, 0.0f, 1.0f, 1.0f, // top right
		-0.5f, -0.5f, 0.0f,			1.0f, 1.0f, 0.0f, 1.0f  // bottom left
	};
	
	// IMPORTANT: must be in counter-clockwise order
	private int[] elementArray = {
			/*
			 	x			x
			 	
			 	
			 	
			 	x			x
			 */
			2, 1, 0, // top right triangle
			0, 1, 3	 // bottom left triangle 
	};
	
	private int vaoID, vboID, eboID;
	
	public LevelEditorScene() {
		
	}
	
	@Override
	public void init() {
		
		defaultShader = new Shader("assets/shaders/default.glsl");
		defaultShader.compile();
		
	
		// generate VAO, VBO, and EAO buffer objects
		
		// create VAO
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		// create a float buffer of vertices
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();
		
		// create VBO
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER,vboID);
		glBufferData(GL_ARRAY_BUFFER,vertexBuffer, GL_STATIC_DRAW);
		
		// create the indices and upload
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();
		
		// create EBO
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
		
		// add vertex attribute pointers
		int positionSize = 3;
		int colorSize = 4;
		int floatSizeBytes = 4;
		int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
		glVertexAttribPointer(0,positionSize, GL_FLOAT, false, vertexSizeBytes,0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
		glEnableVertexAttribArray(1);
	}
	
	
	
	@Override
	public void update(float dt) {
		// bind shader program
		defaultShader.use();
		
		// bind the VAO
		glBindVertexArray(vaoID);
		
		// enable vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		defaultShader.detach();
		
		
	}
}
