package atonkish.hexborder.util.polygon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.Vec3d;

import atonkish.hexborder.util.math.Vec2;

public class Hexagon implements Polygon {
    private static final double SQRT3 = Math.sqrt(3.0D);

    private final int n = 6;

    private final Vec2<Integer> index;
    private final Vec3d offset;
    private final int side;

    public Hexagon(Vec2<Integer> index, Vec3d offset, int side) {
        this.index = index;
        this.offset = offset;
        this.side = side;
    }

    public Hexagon(Vec3d pos, Vec3d offset, int side) {
        this(Hexagon.calcOriginIndex(pos, offset, side), offset, side);
    }

    @Override
    public int getVerticesNumber() {
        return this.n;
    }

    @Override
    public Polygon getNeighbor(int i) {
        return new Hexagon(this.getNeighborOriginIndex(i), this.offset, this.side);
    }

    @Override
    public Vec2<Integer> getOriginIndex() {
        return this.index;
    }

    @Override
    public Vec3d getOriginPos() {
        double x = this.index.x * (3.0D * this.side / 2.0D);
        double z = this.index.y * Math.round(SQRT3 * this.side / 2.0D);
        return new Vec3d(x + this.offset.x, 0, z + this.offset.z);
    }

    @Override
    public Vec3d getVertexPos(int i) {
        Vec3d delta = Vec3d.ZERO;

        switch (i % this.getVerticesNumber()) {
            case 0:
                delta = new Vec3d(this.side, 0, 0);
                break;
            case 1:
                delta = new Vec3d(this.side / 2.0D, 0, Math.round(SQRT3 * this.side / 2.0D));
                break;
            case 2:
                delta = new Vec3d(-this.side / 2.0D, 0, Math.round(SQRT3 * this.side / 2.0D));
                break;
            case 3:
                delta = new Vec3d(-this.side, 0, 0);
                break;
            case 4:
                delta = new Vec3d(-this.side / 2.0D, 0, -Math.round(SQRT3 * this.side / 2.0D));
                break;
            case 5:
                delta = new Vec3d(this.side / 2.0D, 0, -Math.round(SQRT3 * this.side / 2.0D));
                break;
        }

        return this.getOriginPos().add(delta);
    }

    @Override
    public List<Vec3d> getAreaPointList() {
        Vec3d origin = this.getOriginPos();
        Vec3d neighbor = this.getNeighbor(0).getOriginPos();

        List<Vec3d> deltaListFragment = new ArrayList<Vec3d>();
        for (int z = 0; z < Math.round(SQRT3 * side / 2.0D); z++) {
            for (int x = 0; x < side; x++) {
                Vec3d delta = new Vec3d(x, 0, z);

                if (x >= side / 2.0D) {
                    Vec3d pos = origin.add(delta).add(0.5, 0, 0.5);
                    if (origin.distanceTo(pos) > neighbor.distanceTo(pos)) {
                        continue;
                    }
                }

                deltaListFragment.add(delta);
            }
        }

        List<Vec3d> posList = new ArrayList<>();
        deltaListFragment.forEach(delta -> posList.add(origin.add(delta.x, 0, delta.z)));
        deltaListFragment.forEach(delta -> posList.add(origin.add(-delta.x - 1, 0, delta.z)));
        deltaListFragment.forEach(delta -> posList.add(origin.add(-delta.x - 1, 0, -delta.z - 1)));
        deltaListFragment.forEach(delta -> posList.add(origin.add(delta.x, 0, -delta.z - 1)));

        return posList;
    }

    private Vec2<Integer> getNeighborOriginIndex(int i) {
        Vec2<Integer> index = this.getOriginIndex().clone();

        switch (i % this.getVerticesNumber()) {
            case 0:
                index.x += 1;
                index.y += 1;
                break;
            case 1:
                index.y += 2;
                break;
            case 2:
                index.x -= 1;
                index.y += 1;
                break;
            case 3:
                index.x -= 1;
                index.y -= 1;
                break;
            case 4:
                index.y -= 2;
                break;
            case 5:
                index.x += 1;
                index.y -= 1;
                break;
        }

        return index;
    }

    private static Vec2<Integer> calcOriginIndex(Vec3d pos, Vec3d offset, int side) {
        Vec2<Double> approximateOriginIndex = Hexagon.calcApproximateOriginIndex(pos, offset, side);

        int m1 = (int) Math.floor(approximateOriginIndex.x);
        int m2 = (int) Math.ceil(approximateOriginIndex.x);

        int n1 = (int) Math.floor(approximateOriginIndex.y);
        int n2 = (int) Math.ceil(approximateOriginIndex.y);

        Vec2<Integer> index1 = new Vec2<Integer>(m1, (m1 - n1) % 2 == 0 ? n1 : n2);
        Vec2<Integer> index2 = new Vec2<Integer>(m2, (m1 - n1) % 2 == 0 ? n2 : n1);

        Vec3d origin1 = new Hexagon(index1, offset, side).getOriginPos();
        Vec3d origin2 = new Hexagon(index2, offset, side).getOriginPos();

        return origin1.distanceTo(pos) < origin2.distanceTo(pos) ? index1 : index2;
    }

    private static Vec2<Double> calcApproximateOriginIndex(Vec3d pos, Vec3d offset, int side) {
        double x = (pos.x - offset.x) / (3.0D * side / 2.0D);
        double z = (pos.z - offset.z) / Math.round(SQRT3 * side / 2.0D);
        return new Vec2<Double>(x, z);
    }
}
