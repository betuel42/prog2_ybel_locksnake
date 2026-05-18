package de.ui.render;

import de.GameState;
import java.awt.Graphics2D;

public interface GameRenderer {
  void render(Graphics2D g2d, GameState state, int tileSize);
}
