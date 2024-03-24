package GK_lab3;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * A panel that displays a two-dimensional animation that is constructed
 * using a scene graph to implement hierarchical modeling.  There is a
 * checkbox that turns the animation on and off.
 */
public class SceneGraph extends JPanel {

	public static void main(String[] args) {
		JFrame window = new JFrame("Scene Graph 2D");
		window.setContentPane( new SceneGraph() );
		window.pack();
		window.setLocation(100,60);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	//-------------------------- Create the world and implement the animation --------------

	private final static int WIDTH = 800;   // The preferred size for the drawing area.
	private final static int HEIGHT = 600;

	private final static double X_LEFT = -4;    // The xy limits for the coordinate system.
	private final static double X_RIGHT = 4;
	private final static double Y_BOTTOM = -3;
	private final static double Y_TOP = 3;

	private final static Color BACKGROUND = Color.WHITE; // Initial background color for drawing.

	private float pixelSize;  // The size of a pixel in drawing coordinates.

	private int frameNumber = 0;  // Current frame number, goes up by one in each frame.

	private static ArrayList<TransformedObject> siedmiobokLines = new ArrayList<>();

	private CompoundObject world; // SceneGraphNode representing the entire scene.

	// TODO: Define global variables to represent animated objects in the scene.
	private TransformedObject rotatingRect;  // (DELETE THIS EXAMPLE)

	/**
	 *  Builds the data structure that represents the entire picture. 
	 */
	private void createWorld() {

		world = new CompoundObject();  // Root node for the scene graph.

		// TODO: Create objects and add them to the scene graph.

		double[] triangXY = new double[]{-1.75,0,-1.25,0,-1.5,1,1.75,0,1.25,0,1.5,1,-0.5,-3,0.5,-3,0,-1};
		double[] linesXY = new double[]{-2,1.25,-1,0.75,1,1.25,2,0.75,-1.25,-0.75,1.25,-1.25};
		double[] centres = new double[]{-2,1.25,-1,0.75,1,1.25,2,0.75,-1.25,-0.75,1.25,-1.25};
		Color[] cMap = new Color[]{Color.magenta,Color.green,Color.blue,Color.red,Color.black};


		//generate triangles
		ArrayList<SceneGraphNode> triangles = new ArrayList<>();
		ArrayList<SceneGraphNode> redLines = new ArrayList<>();


		for (int i=0; i<3;i++){
			int finalI = i;
			triangles.add(new SceneGraphNode() {
				@Override
				void doDraw(Graphics2D g) {
					Path2D path = new Path2D.Double();
					path.moveTo(triangXY[6* finalI],triangXY[6* finalI+1]);
					path.lineTo(triangXY[6* finalI+2],triangXY[6* finalI+3]);
					path.lineTo(triangXY[6* finalI+4],triangXY[6* finalI+5]);
					path.closePath();
					g.fill(path);
				}
			});
			redLines.add(new SceneGraphNode() {
				@Override
				void doDraw(Graphics2D g) {
					g.setStroke(new BasicStroke(0.1f));
					g.draw( new Line2D.Double( linesXY[4* finalI],linesXY[4* finalI+1],
							linesXY[4* finalI+2],linesXY[4* finalI+3]) );
				}
			});
		}

		for (int i=0; i<centres.length/2;i++) {
			int finalI = i;
			siedmiobokLines.add(new TransformedObject(new SceneGraphNode() {
				@Override
				void doDraw(Graphics2D g) {
					int n= 8;
					double[] x = new double[n];
					double[] y = new double[n];

					double theta = Math.PI - 2.24285714286;
					g.setColor(color);
					g.setStroke(new BasicStroke(0.02f));

					for (int i=0;i<8;i++){
						x[i] = centres[2*finalI] + 0.3 * Math.cos(i*theta+frameNumber*0.25);
						y[i] = centres[2*finalI+1] + 0.3 * Math.sin(i*theta+frameNumber*0.25);
						if (i!=0){
							g.draw( new Line2D.Double( x[i-1],y[i-1],x[i],y[i]) );
							g.draw( new Line2D.Double( x[i],y[i],centres[2*finalI],centres[2*finalI+1]) );
						}
					}

				}
			}));
		}

		for (SceneGraphNode siedmiobokLine : siedmiobokLines) {
			siedmiobokLine.setColor(cMap[4]);
			world.add(siedmiobokLine);
		}

		for (SceneGraphNode line : redLines) {
			line.setColor(cMap[3]);
			world.add(line);
		}


		for (SceneGraphNode triangle : triangles) {
			triangle.setColor(cMap[triangles.indexOf(triangle)]);
			world.add(triangle);
		}


	} // end createWorld()


	/**
	 * This method is called just before each frame is drawn.  It updates the modeling
	 * transformations of the objects in the scene that are animated.
	 */
	public void updateFrame() {
		frameNumber++;

		// TODO: Update state in preparation for drawing the next frame.

	}



	//------------------- A Simple Scene Object-Oriented Scene Graph API ----------------

	private static abstract class SceneGraphNode {
		Color color;  // If not null, the default color for this node and its children.
		              // If null, the default color is inherited.

		SceneGraphNode setColor(Color c) {
			this.color = c;
			return this;
		}

		final void draw(Graphics2D g) {
			Color saveColor = null;
			if (color != null) {
				saveColor = g.getColor();
				g.setColor(color);
			}
			doDraw(g);
			if (saveColor != null) {
				g.setColor(saveColor);
			}
		}
		abstract void doDraw(Graphics2D g);
	}
	
	/**
	 *  Defines a subclass, CompoundObject, of SceneGraphNode to represent
	 *  an object that is made up of sub-objects.  Initially, there are no
	 *  sub-objects.  Objects are added with the add() method.
	 */
	private static class CompoundObject extends SceneGraphNode {
		ArrayList<SceneGraphNode> subobjects = new ArrayList<SceneGraphNode>();
		CompoundObject add(SceneGraphNode node) {
			subobjects.add(node);
			return this;
		}
		void doDraw(Graphics2D g) {
			for (SceneGraphNode node : subobjects)
				node.draw(g);
		}
	}

	/**
	 *  TransformedObject is a subclass of SceneGraphNode that
	 *  represents an object along with a modeling transformation to
	 *  be applied to that object.  The object must be specified in
	 *  the constructor.  The transformation is specified by calling
	 *  the setScale(), setRotate() and setTranslate() methods. Note that
	 *  each of these methods returns a reference to the TransformedObject
	 *  as its return value, to allow for chaining of method calls.
	 *  The modeling transformations are always applied to the object
	 *  in the order scale, then rotate, then translate.
	 */
	private static class TransformedObject extends SceneGraphNode {
		SceneGraphNode object;
		double rotationInDegrees = 0;
		double scaleX = 1, scaleY = 1;
		double translateX = 0, translateY = 0;
		TransformedObject(SceneGraphNode object) {
			this.object = object;
		}
		TransformedObject setRotation(double degrees) {
			rotationInDegrees = degrees;
			return this;
		}
		TransformedObject setTranslation(double dx, double dy) {
			translateX = dx;
			translateY = dy;
			return this;
		}
		TransformedObject setScale(double sx, double sy) {
			scaleX = sx;
			scaleY = sy;
			return this;
		}
		void doDraw(Graphics2D g) {
			AffineTransform savedTransform = g.getTransform();
			if (translateX != 0 || translateY != 0)
				g.translate(translateX,translateY);
			if (rotationInDegrees != 0)
				g.rotate( rotationInDegrees/180.0 * Math.PI);
			if (scaleX != 1 || scaleY != 1)
				g.scale(scaleX,scaleY);
			object.draw(g);
			g.setTransform(savedTransform);
		}
	}
	
	       // Create some basic objects as custom SceneGraphNodes.

	private static SceneGraphNode line = new SceneGraphNode() { 
		void doDraw(Graphics2D g) {g.draw( new Line2D.Double( -0.5,0, 0.5,0) ); }
	};

	private static SceneGraphNode rect = new SceneGraphNode() {
		void doDraw(Graphics2D g) {  g.draw(new Rectangle2D.Double(-0.5,-0.5,1,1)); }
	};

	private static SceneGraphNode filledRect = new SceneGraphNode() {
		void doDraw(Graphics2D g) {  g.fill(new Rectangle2D.Double(-0.5,-0.5,1,1)); }
	};

	private static SceneGraphNode circle = new SceneGraphNode() {
		void doDraw(Graphics2D g) {  g.draw(new Ellipse2D.Double(-0.5,-0.5,1,1)); }
	};

	private static SceneGraphNode filledCircle = new SceneGraphNode() {
		void doDraw(Graphics2D g) {  g.fill(new Ellipse2D.Double(-0.5,-0.5,1,1)); }
	};

	private static SceneGraphNode filledTriangle = new SceneGraphNode() {
		void doDraw(Graphics2D g) {  // width = 1, height = 1, center of base is at (0,0);
			Path2D path = new Path2D.Double();
			path.moveTo(-0.5,0);
			path.lineTo(0.5,0);
			path.lineTo(0,1);
			path.closePath();
			g.fill(path);
		}
	};





	//--------------------------------- Implementation ------------------------------------

	private JPanel display;  // The JPanel in which the scene is drawn.

	/**
	 * Constructor creates the scene graph data structure that represents the
	 * scene that is to be drawn in this panel, by calling createWorld().
	 * It also sets the preferred size of the panel to the constants WIDTH and HEIGHT.
	 * And it creates a timer to drive the animation.
	 */
	public SceneGraph() {
		display = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D)g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				applyLimits(g2, X_LEFT, X_RIGHT, Y_TOP, Y_BOTTOM, false);
				g2.setStroke( new BasicStroke(pixelSize) ); // set default line width to one pixel.
				world.draw(g2);
			}
		};
		display.setPreferredSize( new Dimension(WIDTH,HEIGHT));
		display.setBackground( BACKGROUND );
		final Timer timer = new Timer(17,new ActionListener() { // about 60 frames per second
			public void actionPerformed(ActionEvent evt) {
				updateFrame();
				repaint();
			}
		});
		final JCheckBox animationCheck = new JCheckBox("Run Animation");
		animationCheck.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (animationCheck.isSelected()) {
					if ( ! timer.isRunning() )
						timer.start();
				}
				else {
					if ( timer.isRunning() )
						timer.stop();
				}
			}
		});
		JPanel top = new JPanel();
		top.add(animationCheck);
		setLayout(new BorderLayout(5,5));
		setBackground(Color.DARK_GRAY);
		setBorder( BorderFactory.createLineBorder(Color.DARK_GRAY,4) );
		add(top,BorderLayout.NORTH);
		add(display,BorderLayout.CENTER);
		createWorld();
	}



	/**
	 * Applies a coordinate transform to a Graphics2D graphics context.  The upper left corner of 
	 * the viewport where the graphics context draws is assumed to be (0,0).  The coordinate
	 * transform will make a requested rectangle visible in the drawing area.  The requested
	 * limits might be adjusted to preserve the aspect ratio.  (This method sets the global variable 
	 * pixelSize to be equal to the size of one pixel in the transformed coordinate system.)
	 * @param g2 The drawing context whose transform will be set.
	 * @param xleft requested x-value at left of drawing area.
	 * @param xright requested x-value at right of drawing area.
	 * @param ytop requested y-value at top of drawing area.
	 * @param ybottom requested y-value at bottom of drawing area; can be less than ytop, which will
	 *     reverse the orientation of the y-axis to make the positive direction point upwards.
	 * @param preserveAspect if preserveAspect is false, then the requested rectangle will exactly fill
	 * the viewport; if it is true, then the limits will be expanded in one direction, horizontally or
	 * vertically, to make the aspect ratio of the displayed rectangle match the aspect ratio of the
	 * viewport.  Note that when preserveAspect is false, the units of measure in the horizontal and
	 * vertical directions will be different.
	 */
	private void applyLimits(Graphics2D g2, double xleft, double xright, 
			double ytop, double ybottom, boolean preserveAspect) {
		int width = display.getWidth();   // The width of the drawing area, in pixels.
		int height = display.getHeight(); // The height of the drawing area, in pixels.
		if (preserveAspect) {
			// Adjust the limits to match the aspect ratio of the drawing area.
			double displayAspect = Math.abs((double)height / width);
			double requestedAspect = Math.abs(( ybottom-ytop ) / ( xright-xleft ));
			if (displayAspect > requestedAspect) {
				double excess = (ybottom-ytop) * (displayAspect/requestedAspect - 1);
				ybottom += excess/2;
				ytop -= excess/2;
			}
			else if (displayAspect < requestedAspect) {
				double excess = (xright-xleft) * (requestedAspect/displayAspect - 1);
				xright += excess/2;
				xleft -= excess/2;
			}
		}
		double pixelWidth = Math.abs(( xright - xleft ) / width);
		double pixelHeight = Math.abs(( ybottom - ytop ) / height);
		pixelSize = (float)Math.min(pixelWidth,pixelHeight);
		g2.scale( width / (xright-xleft), height / (ybottom-ytop) );
		g2.translate( -xleft, -ytop );
	}

}