package com.mj.tcs.data.model;

import java.util.Objects;

/**
 * @author Wang Zhen
 */
public class DummyLocation extends BaseLocation implements Cloneable {

    private Scene scene;

    private Point point;

    public DummyLocation(){}

    public DummyLocation(Point point) {
        this.point = Objects.requireNonNull(point, "point is null");
        setScene(Objects.requireNonNull(this.point.getScene(), "scene is null"));
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public DummyLocation clone() {
        DummyLocation clone = (DummyLocation) super.clone();
        clone.setScene(scene);
        clone.setPoint(point);

        return clone;
    }
}
