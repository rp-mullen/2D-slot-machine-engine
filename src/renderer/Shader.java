package renderer;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Shader {
	
	private int shaderProgramID;
	
	private String vertexSource;
	private String fragmentSource;
	private String filepath;
	
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
		
		System.out.println(vertexSource);
		System.out.println(fragmentSource);
		
		
		
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
		glUseProgram(shaderProgramID);
	}
	
	public void detach() {
		glUseProgram(0);
	}
}
