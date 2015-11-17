package com.mj.tcs.data.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Entity
@Table(name = "tcs_model_location", uniqueConstraints =
@UniqueConstraint(columnNames = {"name", "scene"})
)
public class DummyLocation extends BaseLocation implements Cloneable {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private Scene scene;

    @OneToOne
    @JoinColumn(name = "point", nullable = false)
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
