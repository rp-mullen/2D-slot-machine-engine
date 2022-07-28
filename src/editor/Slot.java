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
	private boolean aligned = false;
	
	private boolean done = false;
	
	private List<GameObject> fruits;
	
	private int winningObject;
	
	private transient GameObject testObj;
	public Slot(Spritesheet sprites, int n) {
		
		fruits = new ArrayList<>();
		
		this.t += n*Math.random() + n;
		
		this.offsetX = n * slotDistance;
		
		System.out.println("sprites size: " + sprites.size());
		
		for (int i = 0; i < sprites.size(); i++) {
			GameObject obj = Prefabs.generateSpriteObject(sprites.getSprite(i), 100, 100);
		
			obj.transform.position.x = slotX + offsetX + paddingX;
			obj.transform.position.y = slotBottomY + paddingY + fruitDistY*i;
		
			obj.transform.zIndex = 50;
		
			this.fruits.add(obj);
			
			Window.getScene().addGameObjectToScene(obj);
		}
		
		
		
		//this.width = 1334.0f;
		//this.height = 786.0f;
				
	
		
		//Window.getScene().addGameObjectToScene(slotObject);
	}
	
	@Override
	public void update(float dt) {
		if (this.slotRunning) {
			t += dt;
			for (int i = 0; i < fruits.size(); i++) {
				
				//System.out.println("time: " + t);
				
				GameObject obj = fruits.get(i);
				obj.transform.position.y += 1000*dt;
			
				if (t >= finalTime) {
					if (this.fruits.get(i).transform.position.y - (slotMiddleY + fruitDistY) <= 30) {
						//float dY = this.fruits.get(i).transform.position.y - slotMiddleY;
						//this.aligned = true;
						this.winningObject = i;
						
						
						//System.out.println(SlotMachine.getName(winningObject));
						this.done = true;
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
		} else {
			
		}
		//System.out.println("winning object: " + this.winningObject);
	}
	
	private void alignToCenter(float dt) {
		
		
	}
		
	
	
	public int getWinningObject() {
		return this.winningObject;
	}
	
	public boolean doneRunning() {
		return this.done;
	}
	
	public float getTime() {
		return this.finalTime;
	}
}
