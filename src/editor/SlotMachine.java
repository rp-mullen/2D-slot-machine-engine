package editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import components.Component;
import components.Spritesheet;

public class SlotMachine extends Component {
	
	private List<Slot> slots;
	private List<Integer> results;
	
	private static HashMap<Integer,String> valNames = new HashMap<>();
	
	private float t = 0.0f;
	private float totalTime = 0.0f;
	private boolean running = true;
	
	private boolean reportResult = false;
	private int resultCount = 0;
	
	public SlotMachine(Spritesheet sprites) {
		
		valNames.put(0, "Cherry");
		valNames.put(1, "Strawberry");
		valNames.put(2, "Lemon");
		valNames.put(3, "Orange");
		valNames.put(4, "Date");
		valNames.put(5, "Grapes");
		valNames.put(6,"Bell");
		valNames.put(7, "Watermelon");
		valNames.put(8,"Seven");
		
		results = new ArrayList<>();
		slots = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			slots.add(new Slot(sprites, i));
			this.totalTime += slots.get(i).getTime();
		}
	}
	
	@Override
	public void update(float dt) {
		t += dt;
		if (t < slots.get(slots.size()-1).getTime()) {
			for (Slot slot : slots) {
				slot.update(dt);
			}
		} else {
			this.reportResult = true;
		}
		
		if (this.reportResult && this.resultCount < 1) {
			for (Slot slot : this.slots) {
				System.out.println(SlotMachine.getName(slot.getWinningObject()));
			}
			this.reportResult = false;
			this.resultCount++;
		}
	}
	
	public static String getName(int x) {
		return valNames.get(x);
	}
}
