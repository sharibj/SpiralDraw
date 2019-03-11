package draw.spiral;

import java.awt.Point;

/**
 * @author Sharib Jafari
 *
 */
public class Utils {
	private Utils() {
	}

	public static double getAngleForPoint(Point point, double radius) {
		double cosAngle = (point.x) / radius;
		double sinAngle = (point.y) / radius;
		double rad = Math.atan2(sinAngle, cosAngle);
		return toDegrees(rad);
	}

	public static Point getPointForAngle(double angle, double radius) {
		angle = toRadians(angle);
		int x = (int) (radius * Math.cos(angle));
		int y = (int) (radius * Math.sin(angle));
		return new Point(x, y);
	}

	public static double distance(Point start, Point end) {
		return Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y));
	}

	private static double toDegrees(double angle) {
		return angle * (180 / Math.PI);
	}

	private static double toRadians(double angle) {
		return angle * (Math.PI / 180);
	}
}
