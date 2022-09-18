package atonkish.hexborder.util.polygon;

import java.util.List;

import net.minecraft.util.math.Vec3d;

import atonkish.hexborder.util.math.Vec2;

public interface Polygon {
    public int getVerticesNumber();

    public Polygon getNeighbor(int i);

    public Vec2<Integer> getOriginIndex();

    public Vec3d getOriginPos();

    public Vec3d getVertexPos(int i);

    public List<Vec3d> getAreaPointList();
}
