package atonkish.hexborder.util.polygon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.Vec3d;

import atonkish.hexborder.util.math.Vec2;

public class Hexagon implements Polygon {
    private static final double SQRT3 = Math.sqrt(3.0D);

    private final int n = 6;
    private final Vec3d pos;
    private final Vec3d offset;
    private final int side;

    public Hexagon(Vec3d pos, Vec3d offset, int side) {
        this.pos = pos;
        this.offset = offset;
        this.side = side;
    }

    @Override
    public int getVerticesNumber() {
        return this.n;
    }

    @Override
    public Vec2<Integer> getMainOriginIndex() {
        Vec2<Double> approximateOriginIndex = this.getApproximateOriginIndex();

        int m1 = (int) Math.floor(approximateOriginIndex.x);
        int m2 = (int) Math.ceil(approximateOriginIndex.x);

        int n1 = (int) Math.floor(approximateOriginIndex.y);
        int n2 = (int) Math.ceil(approximateOriginIndex.y);

        Vec2<Integer> index1 = new Vec2<Integer>(m1, (m1 - n1) % 2 == 0 ? n1 : n2);
        Vec2<Integer> index2 = new Vec2<Integer>(m2, (m1 - n1) % 2 == 0 ? n2 : n1);

        Vec3d origin1 = this.getOriginPos(index1);
        Vec3d origin2 = this.getOriginPos(index2);

        return origin1.distanceTo(this.pos) < origin2.distanceTo(this.pos) ? index1 : index2;
    }

    private Vec2<Double> getApproximateOriginIndex() {
        double x = (this.pos.x - this.offset.x) / (3.0D * this.side / 2.0D);
        double z = (this.pos.z - this.offset.z) / Math.round(SQRT3 * this.side / 2.0D);
        return new Vec2<Double>(x, z);
    }

    @Override
    public Vec2<Integer> getNeighborOriginIndex(int i) {
        Vec2<Integer> index = this.getMainOriginIndex();

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

    @Override
    public Vec3d getOriginPos(Vec2<Integer> index) {
        double x = index.x * (3.0D * this.side / 2.0D);
        double z = index.y * Math.round(SQRT3 * this.side / 2.0D);
        return new Vec3d(x + this.offset.x, 0, z + this.offset.z);
    }

    @Override
    public Vec3d getVertexPos(int i, Vec2<Integer> index) {
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

        return this.getOriginPos(index).add(delta);
    }

    @Override
    public List<Vec3d> getPosList(Vec2<Integer> index) {
        Vec3d origin = this.getOriginPos(index);
        Vec3d neighbor = this.getOriginPos(this.getNeighborOriginIndex(0));

        List<Vec3d> deltaListFragment = new ArrayList<Vec3d>();
        for (int z = 0; z < Math.round(SQRT3 * side / 2.0D); z++) {
            for (int x = 0; x < side; x++) {
                Vec3d delta = new Vec3d(x, 0, z);

                if (x >= side / 2.0D) {
                    Vec3d pos = origin.add(delta).add(0.5, 0, 0.5);
                    if (origin.distanceTo(pos) >= neighbor.distanceTo(pos)) {
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
}
