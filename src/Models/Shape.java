package Models;

import javafx.scene.canvas.GraphicsContext;

import java.util.Map;

public abstract class Shape{

    public abstract void setProperties(Map<String,Integer> properties);

    public abstract Map<String,Integer> getProperties();

    public abstract void draw(GraphicsContext gc);

    public abstract Shape clone();
}
