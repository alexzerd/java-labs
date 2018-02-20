
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.JPanel;

@SuppressWarnings({ "serial" })
public class GraphicsDisplay extends JPanel {

    class GraphPoint {
        double xd;
        double yd;
        int x;
        int y;
        int n;
    }

    class Zone {
        double maxX;
        double minX;
    	double maxY;
    	double minY;
    	double tmp;
    	boolean use;
    }
	
    public class MouseMotionHandler implements MouseMotionListener, MouseListener {	    
	
    	private double comparePoint(Point p1, Point p2) {
    	    return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    	}
	
    	private GraphPoint find(int x, int y) {
    	    if (graphicsData == null) {
    	        return null;
    	    }
    	    GraphPoint smp = new GraphPoint();
    	    GraphPoint smp2 = new GraphPoint();
    	    double r, r2 = 1000;
    	    for (int i = 0; i < graphicsData.length; ++i) {
        		Point p = new Point();
        		p.x = x;
        		p.y = y;
        		Point p2 = new Point();
        		p2.x = graphicsDataI[i][0];
        		p2.y = graphicsDataI[i][1];
        		r = comparePoint(p, p2);
        		if (r < 7.0) {
        		    smp.x = graphicsDataI[i][0];
        		    smp.y = graphicsDataI[i][1];
        		    smp.xd = graphicsData[i][0];
        		    smp.yd = graphicsData[i][1];
        		    smp.n = i;
        		    if (r < r2) {
        		        r2 = r;
        		        smp2 = smp;
        		    }
        		    return smp2;
        		}
    	    }
    	    return null;
    	}

    	public void mouseMoved(MouseEvent ev) {
    	    GraphPoint smp;
    	    smp = find(ev.getX(), ev.getY());
    	    if (smp != null) {
    	        setCursor(Cursor.getPredefinedCursor(8));
    	        SMP = smp;
    	    } else {
    	        setCursor(Cursor.getPredefinedCursor(0));
    	        SMP = null;
    	    }
    	    repaint();
    	}
    	
    	public void mouseDragged(MouseEvent e) {
    	    if (selMode) {
        		if (!isRotaited) {
        		    rect.setFrame(mausePX, mausePY, e.getX() - rect.getX(), e.getY() - rect.getY());
        		} else {
        		    rect.setFrame(- mausePY + getHeight(), mausePX, - e.getY() + mausePY, e.getX() - mausePX);
        		}
        		repaint();
    	    }
    	    if (dragMode) {
        		if(pointToXY(e.getX(), e.getY()).y < maxY && pointToXY(e.getX(), e.getY()).y > minY) {
        		    graphicsData[SMP.n][1] = pointToXY(e.getX(), e.getY()).y;
                    SMP.yd = pointToXY(e.getX(), e.getY()).y;
                    SMP.y = e.getY();
                }
        		repaint();
    	    }
    	}
	
    	public void mouseClicked(MouseEvent e) {
    	    if (e.getButton() != 3) {
    	        return;
    	    }
    	    
    	    try {
    	        zone = stack.pop(); 
    	    } catch (EmptyStackException ex) {
    	        ex.printStackTrace();
    	    }
    	    
    	    if(stack.empty()) {
    	        zoom = false;
    	    }
    	    repaint();
    	}
    	
    	public void mouseEntered(MouseEvent arg0) {    
    	}
    	
    	public void mouseExited(MouseEvent arg0) {    
    	}
    
    	public void mousePressed(MouseEvent e) {
    	    if (e.getButton() != 1) {
    	        return;
    	    }
    	    
    	    if (SMP != null) {
        		selMode = false;
        		dragMode = true;
    	    } else {
        		dragMode = false;
        		selMode = true;
        		mausePX = e.getX();
        		mausePY = e.getY();
        		if (!isRotaited) {
        		    rect.setFrame(e.getX(), e.getY(), 0, 0);
        		} else {
        		    rect.setFrame(e.getX(), e.getY(), 0, 0);
        		}
    	    }
    	}
    	
    	public void mouseReleased(MouseEvent e) {
    	    rect.setFrame(0, 0, 0, 0);
    	    if (e.getButton() != 1) {
        		repaint();
        		return;
    	    }
    	    if (selMode && !isRotaited) {
        		if (e.getX() <= mausePX || e.getY() <= mausePY) {
        		    return;
        		}
        		int eY = e.getY();
        		int eX = e.getX();
        		if (eY > getHeight()) {
        		    eY = getHeight();
    		    }
        		if (eX > getWidth()) {
        		    eX = getWidth();
        		}
        		double maxX = pointToXY(eX, 0).x;
        		double minX = pointToXY(mausePX, 0).x;
        		double maxY = pointToXY(0, mausePY).y;
        		double minY = pointToXY(0, eY).y;
        		stack.push(zone);
        		zone = new Zone();
        		zone.use = true;
        		zone.maxX = maxX;
        		zone.minX = minX;
        		zone.minY = minY;
        		zone.maxY = maxY;
        		selMode = false;
        		zoom=true;
    	    } else if (selMode) {
        		if (pointToXY(mausePX, 0).y <= pointToXY(e.getX(), 0).y || pointToXY(0, e.getY()).x <= pointToXY(0, mausePY).x) {
        		    return;
        		}
        		int eY = e.getY();
        		int eX = e.getX();
        		if (eY < 0) {
        		    eY = 0;
        		}
        		if (eX > getWidth()) {
        		    eX = getWidth();
        		}
        		stack.push(zone);
        		zone = new Zone();
        		zone.use = true;
        		zone.maxY = pointToXY(mausePX, 0).y;
        		zone.maxX = pointToXY(0, eY).x;
        		zone.minX = pointToXY(0, mausePY).x;
        		zone.minY = pointToXY(eX, 0).y;
        		selMode = false;
        		zoom = true;
    	    }
    	    repaint();
        }
    }
    
    private Zone zone = new Zone();
    private Double[][] graphicsData;
    private int[][] graphicsDataI;
    
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double scale;
    private double scaleX;
    private double scaleY;
    
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean isRotaited = false;
    private boolean showGrid = true;
    private boolean PPP = false;
    private boolean zoom=false;
    private boolean selMode = false;
    private boolean dragMode = false;
    
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
    private BasicStroke graphicsStroke;
    private BasicStroke gridStroke;
    private BasicStroke hatchStroke;
    private BasicStroke axisStroke;
    private BasicStroke selStroke;
    private BasicStroke markerStroke;
    private Font axisFont;
    private Font captionFont;
    
    private int mausePX = 0;
    private int mausePY = 0;
    private GraphPoint SMP;
    double xMax;
    private Rectangle2D.Double rect;
    private Stack<Zone> stack = new Stack<Zone>();
    
    
    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f,
                new float[] {15, 5, 15, 5, 15, 5, 15, 5, 5, 5, 5, 5, 5, 5}, 0.0f);
        axisStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f);
        axisFont = new Font("Serif", Font.BOLD, 24);
        gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
    	hatchStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
    	selStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 8, 8 }, 0.0f);
    	captionFont = new Font("Serif", Font.BOLD, 10);
		
    	MouseMotionHandler mouseMotionHandler = new MouseMotionHandler();
    	addMouseMotionListener(mouseMotionHandler);
    	addMouseListener(mouseMotionHandler);
    	rect = new Rectangle2D.Double();
    	zone.use = false;
    }

    public void showGraphics(Double[][] graphicsData) {
    	this.graphicsData = graphicsData;
    	graphicsDataI = new int[graphicsData.length][2];
    	repaint();
    }
    
    public void setShowAxis(boolean showAxis) {
    	this.showAxis = showAxis;
    	repaint();
    }

    public void setShowRotate(boolean transform) {
    	this.isRotaited = transform;
    	repaint();
    }

    public void setShowGrid(boolean showGrid) {
    	this.showGrid = showGrid;
    	repaint();
    }

    public int getDataLenght() {
        return graphicsData.length;
    }

    public double getValue(int i, int j) {
        return graphicsData[i][j];
    }

    public void setShowMarkers(boolean showMarkers) {
    	this.showMarkers = showMarkers;
    	repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	if (graphicsData == null || graphicsData.length == 0) {
    	    return;
    	}
    	
    	minX = graphicsData[0][0];
    	maxX = graphicsData[graphicsData.length - 1][0];
    	minY = graphicsData[0][1];
    	maxY = minY;
    	for (int i = 1; i < graphicsData.length; ++i) {
    	    if (graphicsData[i][1] < minY) {
    	        minY = graphicsData[i][1];
    	    } else if (graphicsData[i][1] > maxY) {
    	        maxY = graphicsData[i][1];
    	    }
    	}
            
    	if (zone.use) {// && zone.MINX>minX){
    	    minX = zone.minX;
    	}
    	if (zone.use) {// && zone.MAXX<maxX){
    	    maxX = zone.maxX;
    	}
    	if (zone.use) {// && zone.MINY>minY){
    	    minY = zone.minY;
    	}
    	if (zone.use) {// && zone.MAXY<maxY){
    	    maxY = zone.maxY;
    	}
    	
    	scaleX = 1.0 / (maxX - minX);
    	scaleY = 1.0 / (maxY - minY);
    	if (!isRotaited) {
    	    scaleX *= getSize().getWidth();
    	} else {
    	    scaleX *= getSize().getHeight();
    	}
    	if (!isRotaited) {
    	    scaleY *= getSize().getHeight();
    	} else {
    	    scaleY *= getSize().getWidth();
    	}
    	if (isRotaited) {
    	    ((Graphics2D) g).rotate(- Math.PI / 2);
    	    ((Graphics2D) g).translate(-getHeight(), 0);
    	}
    	scale = Math.min(scaleX, scaleY);
    	if(!zoom) {
    	    if (scale == scaleX) {
        		double yIncrement = 0;
        		if (!isRotaited) {
        		    yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
        		} else {
        		    yIncrement = (getSize().getWidth() / scale - (maxY - minY)) / 2;
        		    maxY += yIncrement;
        		    minY -= yIncrement;
        		}
    	    }
    	    if (scale == scaleY) {
        		double xIncrement = 0;
        		if (!isRotaited) {
        		    xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
        		    maxX += xIncrement;
        		    minX -= xIncrement;
        		} else {
        		    xIncrement = (getSize().getHeight() / scale - (maxX - minX)) / 2;
        		    maxX += xIncrement;
        		    minX -= xIncrement;
        		}
    	    }
    	}
    	Graphics2D canvas = (Graphics2D) g;
    	Stroke oldStroke = canvas.getStroke();
    	Color oldColor = canvas.getColor();
    	Paint oldPaint = canvas.getPaint();
    	Font oldFont = canvas.getFont();
    	if (showGrid) {
    	    paintGrid(canvas);
    	}
    	if (showAxis) {
    	    paintAxis(canvas);
    	}
    	if (showMarkers) {
    	    paintMarkers(canvas);
    	}
    	if (SMP != null) {
    	    paintHint(canvas);
    	}
    	if (selMode) {
    	    canvas.setColor(Color.BLACK);
    	    canvas.setStroke(selStroke);
    	    canvas.draw(rect);
    	}
    	//canvas.drawString ("maxY", (int)xyToPoint(0, xMax).x+5, (int)xyToPoint(0, xMax).y+5);
    	canvas.setFont(oldFont);
    	canvas.setPaint(oldPaint);
    	canvas.setColor(oldColor);
    	canvas.setStroke(oldStroke);
    
    	paintGraphics(canvas);
    }
    
    protected void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.BLUE);
	
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; ++i) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
    	    graphicsDataI[i][0] = (int) point.getX();
    	    graphicsDataI[i][1] = (int) point.getY();
    	    if (isRotaited) {
    	            graphicsDataI[i][0] = (int) point.getY();
    	            graphicsDataI[i][1] = getHeight() - (int) point.getX();
    	    }
	    
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

        for (Double[] point: graphicsData)  {
            if (isSpecialPoint(point)) {
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
    
    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    private boolean isSpecialPoint(Double[] point) {
        int number = Math.abs(point[1].intValue());
        int sum = 0;

        while(number > 0 && sum < 10) {
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

    protected Point2D.Double pointToXY(int x, int y) {
        Point2D.Double p = new Point2D.Double();
        if (!isRotaited) {
            p.x = x / scale + minX;
            int q = (int) xyToPoint(0, 0).y;
            p.y = maxY - maxY * ((double) y / (double) q);
        } else {
            if(!zoom){
                p.y = -x / scale + (maxY);
                p.x = -y / scale + maxX;
            }else{
                p.y = -x / scaleY + (maxY);
                p.x = -y / scaleX + maxX;
            }
        }
        return p;
    }
	
	protected void paintHatch(Graphics2D canvas, double x1, double y1, double step) {
		Color oldColor = canvas.getColor();
		Stroke oldStroke = canvas.getStroke();
		canvas.setColor(Color.GRAY);
		canvas.setStroke(hatchStroke);
		GeneralPath graphics = new GeneralPath();
		int uu = 0;
		int y = (int) xyToPoint(0, 0).getY();
		int x;
		int d = 0;
		for (double i = x1 + step; i < maxX; i += step) {
			uu++;
			if (uu == 5) {
				uu = -5;
				d = 5;
			} else
				d = 0;
			x = (int) xyToPoint(i, 0).getX();
			if (!isRotaited) {
				if (x > getWidth() - 22)
					break;
			} else {
				if (x > getHeight() - 22)
					break;
			}
			graphics.moveTo(x, y - 5 - d);
			graphics.lineTo(x, y + 5 + d);
		}
		uu = 0;
		for (double i = x1 - step; i > minX; i -= step) {
			uu++;
			if (uu == 5) {
				uu = -5;
				d = 5;
			} else
				d = 0;
			x = (int) xyToPoint(i, 0).getX();
			graphics.moveTo(x, y - 5 - d);
			graphics.lineTo(x, y + 5 + d);
		}
		x = (int) xyToPoint(0, 0).getX();
		uu = 0;
		for (double i = y1 + step; i < maxY; i += step) {
			uu++;
			if (uu == 5) {
				uu = -5;
				d = 5;
			} else
				d = 0;
			y = (int) xyToPoint(0, i).getY();
			if (y < 20)
				break;
			graphics.moveTo(x - 5 - d, y);
			graphics.lineTo(x + 5 + d, y);
		}
		uu = 0;
		for (double i = y1 - step; i > minY; i -= step) {
			uu++;
			if (uu == 5) {
				uu = -5;
				d = 5;
			} else
				d = 0;
			y = (int) xyToPoint(0, i).getY();
			graphics.moveTo(x - 5 - d, y);
			graphics.lineTo(x + 5 + d, y);
		}
		canvas.draw(graphics);
		canvas.setStroke(oldStroke);
		canvas.setColor(oldColor);
	}
	
	protected void paintHint(Graphics2D canvas) {
	    Color oldColor = canvas.getColor();
        canvas.setColor(Color.MAGENTA);
        StringBuffer label = new StringBuffer();
        label.append("X = ");
        label.append(formatter.format((SMP.xd)));
        label.append(", Y = ");
        label.append(formatter.format((SMP.yd)));
        FontRenderContext context = canvas.getFontRenderContext();
        Rectangle2D bounds = captionFont.getStringBounds(label.toString(),context);
        if (!isRotaited) {
            int dy = -10;
            int dx = +7;
            if (SMP.y < bounds.getHeight()) {
                dy = +13;
            }
            if (getWidth() < bounds.getWidth() + SMP.x + 20) {
                dx = -(int) bounds.getWidth() - 15;
            }
            canvas.drawString (label.toString(), SMP.x + dx, SMP.y + dy);
        } else {
            int dy = 10;
            int dx = -7;
            if (SMP.x < 10) {
                dx = +13;
            }
            if (SMP.y < bounds.getWidth() + 20) {
                dy = -(int) bounds.getWidth() - 15;
            }
            canvas.drawString (label.toString(), getHeight() - SMP.y + dy, SMP.x + dx);
        }
        canvas.setColor(oldColor);
    }

	private double fix0MAX(final double m) {
		double mm = m;
		int o = 1;
		while (mm < 1.0d) {
			mm = mm * 10;
			o *= 10;
		}
		int i = (int) mm + 1;
		return (double) i / o;
	}

	private double fix1MAX(final double m) {
		double mm = m;
		int o = 1;
		while (mm > 1.0d) {
			mm = mm / 10;
			o *= 10;
		}
		mm *= 10;
		int i = (int) mm + 1;
		o /= 10;
		return (double) i * o;
	}

	protected void paintCaptions(Graphics2D canvas, double step) {
		formatter.setMaximumFractionDigits(5);
		formatter.setGroupingUsed(false);
		DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
		dottedDouble.setDecimalSeparator('.');
		formatter.setDecimalFormatSymbols(dottedDouble);
		Color oldColor = canvas.getColor();
		Stroke oldStroke = canvas.getStroke();
		Font oldFont = canvas.getFont();
		canvas.setColor(Color.BLACK);
		canvas.setFont(captionFont);
		int xp = (int) xyToPoint(0, 0).x;
		int yp;
		FontRenderContext context = canvas.getFontRenderContext();
		double y = step;
		while (y <= maxY) {
			yp = (int) xyToPoint(0, y).y;
			if (yp < 30)
				break;
			String xs = formatter.format(y);
			Rectangle2D bounds = captionFont.getStringBounds(xs, context);
			canvas.drawString (xs, (int) (xp - 5 - bounds.getWidth()), yp);
			y += step;
		}
		y = -step;

		while (y >= minY) {
			yp = (int) xyToPoint(0, y).y;
			String xs = formatter.format(y);
			Rectangle2D bounds = captionFont.getStringBounds(xs, context);
			canvas.drawString (xs, (int) (xp - 5 - bounds.getWidth()), yp);
			y -= step;
		}

		double x = 0.0d + step;
		yp = (int) xyToPoint(0, 0).y;
		while (x <= maxX) {

			xp = (int) xyToPoint(x, 0).x;
			String xs = formatter.format(x);
			Rectangle2D bounds = captionFont.getStringBounds(xs, context);
			if (!isRotaited) {
				if (xp + (int) (bounds.getWidth() / 2) > getWidth())
					break;
			} else {
				if (xp + bounds.getWidth() > getHeight())
					break;
			}
			canvas.drawString (xs, xp - (int) (bounds.getWidth() / 2), yp + 20);
			x += step;
		}
		x = -step;
		while (x >= minX) {
			xp = (int) xyToPoint(x, 0).x;
			String xs = formatter.format(x);
			Rectangle2D bounds3 = captionFont.getStringBounds(xs, context);
			if (xp - (int) (bounds3.getWidth() / 2) < 0)
				break;
			canvas.drawString (xs, xp - (int) (bounds3.getWidth() / 2), yp + 20);
			x -= step;
		}
		canvas.drawString ("0", (int) xyToPoint(0, 0).getX() + 5,
				(int) xyToPoint(0, 0).getY() + 20);
		canvas.setColor(oldColor);
		canvas.setStroke(oldStroke);
		canvas.setFont(oldFont);
	}

	protected void paintGrid(Graphics2D canvas) {
		GeneralPath graphics = new GeneralPath();
		double MAX = Math.max(Math.abs(maxX - minX), Math.abs(maxY - minY));
		double MAX20 = MAX / 20;
		double step = 0.0f;
		if (MAX20 < 1)
			step = fix0MAX(MAX20);
		else
			step = fix1MAX(MAX20);
		if (PPP) {
			int YY = Math.min(getWidth(), getHeight());
			if (YY < 200)
				step *= 3;
			else if (YY < 400)
				step *= 2;
		}
		Color oldColor = canvas.getColor();
		Stroke oldStroke = canvas.getStroke();
		canvas.setStroke(gridStroke);
		canvas.setColor(Color.BLUE);
		int xp = 0;
		double x = 0.0d;
		int gH = getHeight();
		int gW = getWidth();
		if (isRotaited) {
			gH = getWidth();
			gW = getHeight();
		}
		xp = (int) xyToPoint(0, 0).x;
		while (xp > 0) {
			graphics.moveTo(xp, 0);
			graphics.lineTo(xp, gH);
			xp = (int) xyToPoint(x, 0).x;
			x -= step;
		}
		xp = (int) xyToPoint(0, 0).x;

		while (xp < gW) {
			graphics.moveTo(xp, 0);
			graphics.lineTo(xp, gH);
			xp = (int) xyToPoint(x, 0).x;
			x += step;
		}
		int yp = (int) xyToPoint(0, 0).y;
		double y = 0.0f;
		while (yp < gH) {
			yp = (int) xyToPoint(0, y).y;
			graphics.moveTo(0, yp);
			graphics.lineTo(gW, yp);
			y -= step;
		}
		yp = (int) xyToPoint(0, 0).y;
		while (yp > 0) {
			yp = (int) xyToPoint(0, y).y;
			graphics.moveTo(0, yp);
			graphics.lineTo(gW, yp);
			y += step;
		}
		canvas.draw(graphics);
		paintHatch(canvas, 0, 0, step / 10);
		paintCaptions(canvas, step);
		canvas.setColor(oldColor);
		canvas.setStroke(oldStroke);
	}
}
