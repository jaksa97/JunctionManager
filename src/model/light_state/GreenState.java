package model.light_state;

import java.awt.Color;

public class GreenState implements LightState {

	@Override
	public Color getColor() {
		return Color.GREEN;
	}

	@Override
	public int getDuration() {
		return 3000;
	}

	@Override
	public LightState nextState() {
		return new YellowState();
	}

	@Override
	public boolean canPass() {
		return true;
	}

}
