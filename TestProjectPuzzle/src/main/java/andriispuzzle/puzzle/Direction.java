package andriispuzzle.puzzle;


public enum Direction {
    LEFT(0),
    RIGHT(1),
    TOP(2),
    BOTTOM(3);

    private final int intValue;

    Direction(int intValue) {
        this.intValue = intValue;
    }

    public Direction getOpposite() {
        switch (this) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case TOP:
                return BOTTOM;
            case BOTTOM:
                return TOP;
        }
        return null;
    }

    public int intValue() {
        return intValue;
    }

}
