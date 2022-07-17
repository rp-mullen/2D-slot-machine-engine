package renderer;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {
	
	private int shaderProgramID;
	
	private String vertexSource;
	private String fragmentSource;
	private String filepath;
	
	
	private boolean beingUsed = false;
	
	public Shader(String filepath) {
		this.filepath = filepath;
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String[] splitString = source.split("(#type)( )+([a-zA-Z])+");
			
			int index = source.indexOf("#type") + 6;
			int eol = source.indexOf("\n", index);
			
			String firstPattern = source.substring(index,eol).trim();
			
			index = source.indexOf("#type",eol) + 6;
			eol = source.indexOf("\n", index);
			String secondPattern = source.substring(index,eol).trim();
			
			
			if (firstPattern.contentEquals("vertex")) {
				vertexSource = splitString[1];
			} else if (firstPattern.contentEquals("fragment")) {
				fragmentSource = splitString[1];
			} else {
				throw new IOException("Unexpected token");
			}
			
			if (secondPattern.contentEquals("vertex")) {
				vertexSource = splitString[2];
			} else if (secondPattern.contentEquals("fragment")) {
				fragmentSource = splitString[2];
			} else {
				throw new IOException("Unexpected token");
			}
			
		} catch(IOException e) {
			e.printStackTrace();
			assert false : "Error: Could not open file for shader " + filepath + "\n";
		}
		
		
	}
	
	public void compile() {
		
		int vertexID, fragmentID, shaderProgram;
		
		//1. load and compile vertex shader
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexSource);
		glCompileShader(vertexID);
				
				// check for errors in compilation
		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: " + filepath + "''\n Veretx shader compilation failed.");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false : "";
		}
				
				//1. load and compile vertex shader
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentSource);
		glCompileShader(fragmentID);
						
		// check for errors in compilation
		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: " + filepath + "\n Fragment shader compilation failed.");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}
		
		// link shaders and check for errors
		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexID);
		glAttachShader(shaderProgramID, fragmentID);
		glLinkProgram(shaderProgramID);
				
		success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("Error: 'defaultShader.glsl'\n Linking compilation failed.");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false : "";
		}
	}
	
	public void use() {
		if (!beingUsed) {
			glUseProgram(shaderProgramID);
			beingUsed = true;
		}
	}
	
	public void detach() {
		glUseProgram(0);
		beingUsed = false;
	}
	
	// uniforms
	
	
	// matrices
	public void uploadMat4f(String varName, Matrix4f mat4) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer); // create a buffer to put flattened matrix in
		glUniformMatrix4fv(varLocation, false, matBuffer);
	}
	
	public void uploadMat3f(String varName, Matrix3f mat3) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer); // create a buffer to put flattened matrix in
		glUniformMatrix3fv(varLocation, false, matBuffer);
	}
	
	// vectors
	public void uploadVec4f(String varName, Vector4f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
	}
	
	public void uploadVec3f(String varName, Vector3f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform3f(varLocation, vec.x, vec.y, vec.z);
	}
	
	public void uploadVec2f(String varName, Vector2f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform2f(varLocation, vec.x, vec.y);
	}
	
	// scalars
	public void uploadFloat(String varName, float val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1f(varLocation, val);
	}
	
	public void uploadInt(String varName, int val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, val);
	}
	
	public void uploadTexture(String varName, int slot) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, slot);
	}
	
	public void uploadIntArray(String varName, int[] array) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1iv(varLocation, array);
	}
}
