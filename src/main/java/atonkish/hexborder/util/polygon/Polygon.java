package atonkish.hexborder.util.polygon;

import java.util.List;

import net.minecraft.util.math.Vec3d;

import atonkish.hexborder.util.math.Vec2;

public interface Polygon {
    public int getVerticesNumber();

    public Vec2<Integer> getMainOriginIndex();

    public Vec2<Integer> getNeighborOriginIndex(int i);

    public Vec3d getOriginPos(Vec2<Integer> index);

    public Vec3d getVertexPos(int i, Vec2<Integer> index);

    public List<Vec3d> getPosList(Vec2<Integer> index);
}
