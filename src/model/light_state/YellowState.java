package model.light_state;

import java.awt.Color;

public class YellowState implements LightState {

	@Override
	public Color getColor() {
		return Color.YELLOW;
	}

	@Override
	public int getDuration() {
		return 1000;
	}

	@Override
	public LightState nextState() {
		return new RedState();
	}

	@Override
	public boolean canPass() {
		return false;
	}

}