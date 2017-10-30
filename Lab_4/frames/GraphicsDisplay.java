package frames;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.Math;
import javax.swing.JPanel;

public class GraphicsDisplay extends JPanel {

    /**
     * Coordinates of points on the graph
     */
    private Double[][][] graphicsData;
    private static final int NUMBER_OF_GRAPHICS = 2;

    /**
     * Define rules for displaying graph
     */
    private boolean showAxis = true;
    private boolean showMarkers = false;
    private boolean showRotate = false;
    private boolean showTwoGraphs = false;

    /**
     * Boundaries of space that will appear
     */
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    /**
     * Display scale of graph
     */
    private double scale;

    /**
     * Styles of lines
     */
    private BasicStroke[] graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;

    /**
     * Label's font
     */
    private Font axisFont;


    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke[NUMBER_OF_GRAPHICS];
        graphicsStroke[0] = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f,
                                            new float[] {15, 5, 15, 5, 15, 5, 15, 5, 5, 5, 5, 5, 5, 5}, 0.0f);
        graphicsStroke[1] = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[] {10, 5, 10, 5}, 0.0f);
        axisStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f);
        axisFont = new Font("Serif", Font.BOLD, 24);

        graphicsData = new Double[NUMBER_OF_GRAPHICS][][];
    }

    public void showGraphics(Double[][] graphicsData, int graphicsNo) {
        this.graphicsData[graphicsNo] = graphicsData;
        repaint();
        if (graphicsNo != 0) {
            showTwoGraphs = true;
        }
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void setShowRotate(boolean showRotate)
    {
        this.showRotate = showRotate;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graphicsData[0] == null || graphicsData[0].length == 0) {
            return;
        }

        minX = graphicsData[0][0][0];
        maxX = graphicsData[0][graphicsData[0].length - 1][0];
        minY = graphicsData[0][0][1];
        maxY = minY;
        for (int i = 1; i < graphicsData[0].length; ++i) {
            if (graphicsData[0][i][1] < minY) {
                minY = graphicsData[0][i][1];
            } else if (graphicsData[0][i][1] > maxY) {
                maxY = graphicsData[0][i][1];
            }
        }

        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX, scaleY);
        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        } else if (scale == scaleY) {
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }

        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (showRotate) {
            AffineTransform transform = AffineTransform.getRotateInstance(- Math.PI / 2, getSize().getWidth() / 2,
                                                                          getSize().getHeight() / 2);
            transform.concatenate(new AffineTransform(getSize().getHeight() / getSize().getWidth(), 0.0, 0.0,
                                                      getSize().getWidth() / getSize().getHeight(),
                                                      (getSize().getWidth() - getSize().getHeight()) / 2,
                                                      (getSize().getHeight() - getSize().getWidth()) / 2));
            canvas.setTransform(transform);
        }

        paintGraphics(canvas, 0);
        if (showTwoGraphs) {
            paintGraphics(canvas, 1);
        }

        if (showAxis) {
            paintAxis(canvas);
        }
        if (showMarkers) {
            paintMarkers(canvas);
        }

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    protected void paintGraphics(Graphics2D canvas, int graphicsNo) {

        canvas.setStroke(graphicsStroke[graphicsNo]);
        if (graphicsNo == 0) {
            canvas.setColor(Color.BLUE);
        } else {
            canvas.setColor(Color.GREEN);
        }

        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData[graphicsNo].length; ++i) {
            Point2D.Double point = xyToPoint(graphicsData[graphicsNo][i][0], graphicsData[graphicsNo][i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }

    protected void paintMarkers(Graphics2D canvas) {

        canvas.setStroke(markerStroke);
        canvas.setColor(Color.BLUE);
        canvas.setPaint(Color.BLUE);

        for (Double[] point: graphicsData[0])  {
            if (specialPoint(point)) {
                canvas.setColor(Color.RED);
            }

            Ellipse2D.Double marker = new Ellipse2D.Double();
            Point2D.Double center = xyToPoint(point[0], point[1]);
            Point2D.Double corner = shiftPoint(center, 5.5, 5.5);
            Point2D.Double currentPoint = shiftPoint(center, 0.0, 5.5);
            Point2D.Double nextPoint = shiftPoint(center, 0.0, -5.5);
            Line2D.Double marker1 = new Line2D.Double(currentPoint, nextPoint);
            currentPoint = shiftPoint(center, 5.5, 0.0);
            nextPoint = shiftPoint(center, -5.5, 0.0);
            Line2D.Double marker2 = new Line2D.Double(currentPoint, nextPoint);
            marker.setFrameFromCenter(center, corner);
            canvas.draw(marker);
            canvas.draw(marker1);
            canvas.draw(marker2);

            canvas.setColor(Color.BLUE);
        }
    }

    private boolean specialPoint(Double[] point) {
        int number = Math.abs(point[1].intValue());
        int sum = 0;

        while (number > 0 && sum < 10) {
            sum += number % 10;
            number /= 10;
        }

        if (sum < 10) {
            return true;
        } else {
            return false;
        }
    }

    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();

        if (minX <= 0.0 && maxX >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
        }

        if (minY <= 0.0 && maxY >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("x", (float)(labelPos.getX() - bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));
        }
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}