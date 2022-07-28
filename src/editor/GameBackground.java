package editor;

import components.Component;
import components.NonPickable;
import components.Sprite;
import engine.GameObject;
import engine.Prefabs;
import engine.Window;
import util.AssetPool;
import util.Settings;

public class GameBackground extends Component {
	
	private transient float width = Window.getWidth() / 2;
	private transient float height = Window.getHeight() / 2;
	
	private transient GameObject backgroundObject;
	private transient Sprite bgSprite = new Sprite();
	
	public GameBackground(Sprite BGSprite) {
    	bgSprite = BGSprite;
    	
		this.backgroundObject = Prefabs.generateSpriteObject(bgSprite, width, height);
		this.backgroundObject.addComponent(new NonPickable());
		this.backgroundObject.transform.zIndex = -100;
		
		Window.getScene().addGameObjectToScene(backgroundObject);
	}
	
	
}
