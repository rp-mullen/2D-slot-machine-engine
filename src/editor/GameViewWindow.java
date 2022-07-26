package editor;

import engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewWindow {

	public static void imgui() {
		ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
		
		ImVec2 windowSize = getLargestSizeForViewport();
		ImVec2 windowPos = getCenteredPositionForViewport();
		
		ImGui.setCursorPos(windowPos.x, windowPos.y);
		int textureId = Window.getFramebuffer().getTextureId();
		ImGui.image(textureId,  windowSize.x, windowSize.y, 0, 1, 1, 0);
	}
}
