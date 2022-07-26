package renderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL33.*;


public class PickingTexture {
	private int pickingTextureId;
	private int fbo;
	private int depthTexture;
	
	public PickingTexture(int width, int height) {
		if (!init(width,height)) {
			assert false : "Error initializing picking texture";
		}
	}
	
	public boolean init(int width, int height) {
		// generate framebuffer object
				fbo = glGenFramebuffers();
				glBindFramebuffer(GL_FRAMEBUFFER,fbo);
				
				pickingTextureId = glGenTextures();
				
				// create texture to render data to, attach to framebuffer
				glBindTexture(GL_TEXTURE_2D, pickingTextureId);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexImage2D(GL_TEXTURE_2D,0,GL_RGB32F, width, height, 0, GL_RGB, GL_FLOAT, 0);
				
				glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,
						GL_TEXTURE_2D,this.pickingTextureId, 0);
				
				
				// create texture object for the depth buffer
				glEnable(GL_DEPTH_TEST);
				depthTexture = glGenTextures();
				glBindTexture(GL_TEXTURE_2D,depthTexture);
				glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0,
						GL_DEPTH_COMPONENT, GL_FLOAT, 0);
				glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D,
						depthTexture, 0);
				
				// disable reading
				glReadBuffer(GL_NONE);
				glDrawBuffer(GL_COLOR_ATTACHMENT0);
				
				if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
					assert false : "Error: Framebuffer is not complete";
					return false;
				}
				
				// unbind texture and framebuffer
				glBindFramebuffer(GL_FRAMEBUFFER,0);
				glBindFramebuffer(GL_FRAMEBUFFER,0);
				glDisable(GL_DEPTH_TEST);
				return true;
	}
	
	public void enableWriting() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER,fbo);
	}
	
	public void disableWriting() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER,0);
	}
	
	public int readPixel(int x, int y) {
		glBindFramebuffer(GL_READ_FRAMEBUFFER,fbo);
		glReadBuffer(GL_COLOR_ATTACHMENT0);
		
		float pixels[] = new float[3];
		glReadPixels(x,y,1,1,GL_RGB,GL_FLOAT,pixels);
		
		return (int)pixels[0];
		
		
	}
}
