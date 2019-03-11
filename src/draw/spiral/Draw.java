package draw.spiral;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Sharib Jafari
 *
 */
public class Draw {
	private static final int MAX_X = 3000;
	private static final int MAX_Y = 2000;
	// 1 to 360
	private static final int FOLDS = 24;
	// Thickness of stroke
	private static final int STROKE = 10;
	private static final Color COLOR = Color.RED;
	private static final int MID_X = MAX_X / 2;
	private static final int MID_Y = MAX_Y / 2;
	private static final String TITLE = "Draw Spiral";
	// Escape key = 27
	private static final int CLEAR_KEY_CODE = 27;
	private static final boolean MIRROR = true;

	private List<List<Point>> points = null;

	public void launch() {
		reset();
		Point center = new Point(MID_X, MID_Y);
		JFrame f = new JFrame(TITLE);
		f.setSize(MAX_X, MAX_Y);
		f.setLocation(0, 0);
		f.setResizable(false);
		JPanel p = new JPanel() {
			private static final long serialVersionUID = 1L;
			Point point = null;
			{
				// Mouse Release Listener
				// Add null to the points list, to indicate line break
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						for (int counter = 0; counter < FOLDS; counter++) {
							points.get(counter).add(null);
						}

						if (MIRROR) {
							for (int counter = FOLDS; counter < FOLDS * 2; counter++) {
								points.get(counter).add(null);
							}
						}
					}
				});

				// Mouse Dragged Listener
				// Record coordinates of each point to trace the line
				addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseDragged(MouseEvent e) {
						point = e.getPoint();
						double radius = Utils.distance(center, point);
						double angle = Utils.getAngleForPoint(new Point(point.x - MID_X, point.y - MID_Y), radius);
						for (int counter = 0; counter < FOLDS; counter++) {
							int angleIncrement = (360 / FOLDS) * counter;
							Point calculatedPoint = Utils.getPointForAngle(angle + angleIncrement, radius);
							int x = calculatedPoint.x + MID_X;
							int y = calculatedPoint.y + MID_Y;
							points.get(counter).add(new Point(x, y));
							if (MIRROR) {
								points.get(counter + FOLDS).add(new Point(Math.abs(calculatedPoint.x - MID_X), y));
							}
						}
						repaint();
					}

				});
			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				int count = FOLDS;
				if (MIRROR) {
					count *= 2;
				}
				for (int counter = 0; counter < count; counter++) {
					paintList(g2, points.get(counter));
				}
			}
		};
		f.add(p);
		f.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				int keyCode = event.getKeyCode();
				if (keyCode == CLEAR_KEY_CODE) {
					reset();
					p.repaint();
				}
			}
		});
		f.setVisible(true);
	}

	private void paintList(Graphics2D g, List<Point> points) {
		Point prevPoint = null;
		if (points != null && !points.isEmpty()) {
			g.setStroke(new BasicStroke(STROKE));
			g.setColor(COLOR);
			for (Point point : points) {
				if (point != null && prevPoint != null && point != prevPoint) {
					g.drawLine(prevPoint.x, prevPoint.y, point.x, point.y);
				}
				prevPoint = point;
			}
		}
	}

	private void reset() {
		points = new ArrayList<>();
		for (int i = 0; i < FOLDS; i++) {
			points.add(new ArrayList<Point>());
		}
		if (MIRROR) {
			for (int i = 0; i < FOLDS; i++) {
				points.add(new ArrayList<Point>());
			}
		}
	}

	public static void main(String[] args) {
		new Draw().launch();
	}
}
