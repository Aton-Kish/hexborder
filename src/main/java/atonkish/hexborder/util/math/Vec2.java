package atonkish.hexborder.util.math;

public class Vec2<T extends Number> implements Cloneable {
    public T x, y;

    public Vec2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vec2<T> clone() {
        try {
            return (Vec2<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }
}
