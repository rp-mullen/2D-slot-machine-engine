package editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import components.Component;
import components.Spritesheet;
import engine.KeyListener;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;


public class SlotMachine extends Component {
	
	private List<Slot> slots;
	private List<Float> times;
	
	private static HashMap<Integer,String> valNames = new HashMap<>();
	
	private float t = 0.0f;
	private float finalTime;
	
	private boolean running = false;
	
	
	public SlotMachine(Spritesheet sprites) {
		
		// map slot object indices to their sprite names
		valNames.put(0, "Cherry");
		valNames.put(1, "Strawberry");
		valNames.put(2, "Lemon");
		valNames.put(3, "Orange");
		valNames.put(4, "Date");
		valNames.put(5, "Grapes");
		valNames.put(6,"Bell");
		valNames.put(7, "Watermelon");
		valNames.put(8,"Seven");
		
		slots = new ArrayList<>();
		times = new ArrayList<>();
		
		for (int i = 0; i < 5; i++) {
			slots.add(new Slot(sprites, i));
			times.add(slots.get(i).getTime());
			}
		
		// let it run just a little longer than the longest running slot
		for (int j = 1; j < times.size(); j++) {
			finalTime = Math.max(times.get(j), times.get(j-1));
		}
		finalTime++;
	}
	
	@Override
	public void update(float dt) {
		
		// SPACE to start the slots
		if (KeyListener.isKeyPressed(GLFW_KEY_SPACE) && !this.running) {
			this.running = true;
		}
		
		// run each slot
		if (t <= finalTime && this.running) {
			t += dt;
			for (Slot slot : slots) {
				slot.update(dt);
			}
		} 
		// calculate and print results
		else if (t > finalTime && this.running) {
			System.out.println("Done!\n");
			System.out.println("     Results     ");
			System.out.println("==================");
			
			for (Slot slot : this.slots) {
				String name = SlotMachine.getName(slot.getwinningObjectID());
				System.out.println(" * " + name);
			}
			
			System.out.println("");
			
			if (isJackpot()) {
				System.out.println("Jackpot!");
			} else {
				System.out.println("No Jackpot -- Better luck next time.");
			}
			
			this.running = false;
		}
	}
	
	public static String getName(int x) {
		return valNames.get(x);
	}
	
	public boolean isJackpot() {
		int ID = slots.get(0).getwinningObjectID();
		for (int i = 1; i < slots.size(); i++) {
			if (slots.get(i).getwinningObjectID() != ID) {
				return false;
			}
		}
		return true;
	}
}
