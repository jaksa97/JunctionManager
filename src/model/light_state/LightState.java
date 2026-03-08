package model.light_state;

import java.awt.Color;

public interface LightState {
	Color getColor();
	int getDuration();
	LightState nextState();
	boolean canPass();
}
