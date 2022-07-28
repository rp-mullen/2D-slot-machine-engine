package editor;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import components.Component;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import engine.GameObject;
import engine.Prefabs;
import engine.Window;
import renderer.PickingTexture;
import util.AssetPool;
import util.Settings;

public class Slot extends Component{
	private float t = 0;
	private float finalTime = 5;
	
	private float paddingX = 15;
	private float paddingY = 15;
	
	private float slotX = (Window.getWidth() / 2)*(612.0f / 2560.0f)*(Window.getWidth()/Window.getHeight());
	
	private float offsetX;
	private float slotDistance = (Window.getWidth() / 2)*(254.0f/2560.0f)*(Window.getWidth()/Window.getHeight());
	
	private float slotTopY = (Window.getHeight() / 2)*((324.0f + 617.0f) / 1440.0f)*(Window.getWidth()/Window.getHeight());;
	private float slotBottomY = (Window.getHeight() / 2)*(324.0f / 1440.0f)*(Window.getWidth()/Window.getHeight());
	
	private float slotMiddleY = (slotTopY - slotBottomY) / 2;
	
	private float fruitDistY = 90.0f;
	
	private float maxY = slotTopY + 6* fruitDistY;
	
	private boolean outOfBounds = false;
	private boolean slotRunning = true;
	
	private boolean done = false;
	
	private int slotNum;
	
	private transient List<GameObject> fruits;
	
	private int winningObjectID;
	
	private Spritesheet sprites;
	
	public Slot(Spritesheet sprites, int n) {
		
		fruits = new ArrayList<>();
		this.sprites = sprites;
		this.slotNum = n;
		
		init();
		
	}
	
	public void init() {
		this.finalTime += slotNum*Math.random() + slotNum;
		
		this.offsetX = slotNum * slotDistance;
		
		
		for (int i = 0; i < sprites.size(); i++) {
			GameObject obj = Prefabs.generateSpriteObject(this.sprites.getSprite(i), 100, 100);
		
			obj.transform.position.x = slotX + offsetX + paddingX;
			obj.transform.position.y = slotBottomY + paddingY + fruitDistY*i;
			obj.setNoSerialize();
			obj.transform.zIndex = 50;
		
			if (obj.transform.position.y >= slotTopY && obj.transform.position.y < maxY) {
				obj.getComponent(SpriteRenderer.class).setColor(new Vector4f(0,0,0,0));
			}
			
			this.fruits.add(obj);
			
			Window.getScene().addGameObjectToScene(obj);
		}
	}
	
	@Override
	public void update(float dt) {
		if (this.slotRunning) {
			t += dt;
			for (int i = 0; i < fruits.size(); i++) {
				
				GameObject obj = fruits.get(i);
				obj.transform.position.y += 900*dt;
			
				if (t >= finalTime) {
					if (this.fruits.get(i).transform.position.y - (slotMiddleY + fruitDistY) <= 30) {						
						this.winningObjectID = i;
						
						// erase other sprites
						for (int j = 0; j < this.fruits.size(); j++) {
							if (j != winningObjectID) {
								GameObject go = this.fruits.get(j);
								go.getComponent(SpriteRenderer.class).setColor(new Vector4f(0,0,0,0));
							}
						}
						
						// center winning sprite
						this.fruits.get(winningObjectID).transform.position.y = slotMiddleY + 1.5f*fruitDistY;
						
						//System.out.println(SlotMachine.getName(winningObjectID));
						for (int k = 0; k <= 1; k++) {
							GameObject dup = Prefabs.generateSpriteObject(this.sprites.getSprite(winningObjectID), 100, 100);
							
							if (k == 0) {
								dup.setNoSerialize();
								dup.transform.position.y = this.fruits.get(winningObjectID).transform.position.y + 1.45f*fruitDistY;
								dup.transform.position.x = this.fruits.get(winningObjectID).transform.position.x;
							}
							if (k == 1) {
								dup.setNoSerialize();
								dup.transform.position.y = this.fruits.get(winningObjectID).transform.position.y - 1.2f*fruitDistY;
								dup.transform.position.x = this.fruits.get(winningObjectID).transform.position.x;
							}
							
							Window.getScene().addGameObjectToScene(dup);
						}
						
						this.fruits.get(winningObjectID).transform.position.y += 10;
						
						this.slotRunning = false;
						break;
					}
					
				}	
				
				if (obj.transform.position.y >= slotTopY && obj.transform.position.y < maxY) {
					this.outOfBounds = true;
				} else if (obj.transform.position.y >= maxY) {
					obj.transform.position.y = slotBottomY + paddingY;
					obj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1,1,1,1));
				}
		
				if (outOfBounds) {
					obj.getComponent(SpriteRenderer.class).setColor(new Vector4f(0,0,0,0));
					this.outOfBounds = false;
				}
			}
		} 
	}
	
	private void alignToCenter(float dt) {
		
		
	}
		
	
	
	public int getwinningObjectID() {
		return this.winningObjectID;
	}
	
	public boolean doneRunning() {
		return !this.slotRunning;
	}
	
	public float getTime() {
		return this.finalTime;
	}
}
