package model.light_state;

import java.awt.Color;

public class RedState implements LightState {

	@Override
	public Color getColor() {
		return Color.RED;
	}

	@Override
	public int getDuration() {
		return 3000;
	}

	@Override
	public LightState nextState() {
		return new GreenState();
	}

	@Override
	public boolean canPass() {
		return false;
	}

}
