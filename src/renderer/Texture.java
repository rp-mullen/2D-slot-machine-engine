package renderer;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture {

	private String filepath;
	private int texID;
	
	public Texture(String filepath) {
		this.filepath = filepath;
		
		// generate texture on GPU
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D,texID);
		
		// set texture parameters
		// repeat image in both directions
		glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		// pixelate when stretching
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = STBImage.stbi_load(filepath,width, height, channels, 0);
		
		if (image != null) {
			if (channels.get(0) == 3) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,width.get(0), height.get(0),
					0, GL_RGB, GL_UNSIGNED_BYTE, image);
			} else if (channels.get(0) == 4) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,width.get(0), height.get(0),
					0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			} else {
				assert false : "Error: (Texture) Unknown number of channels";
			}
		}
		else {
			assert false : "Error: could not load image texture: " + filepath;
		}
		
		STBImage.stbi_image_free(image);
		
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texID);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
