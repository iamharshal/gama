package msi.gama.jogl.utils.Camera;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.swing.SwingUtilities;

import com.vividsolutions.jts.geom.Envelope;

import msi.gama.jogl.utils.JOGLAWTGLRenderer;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.ILocation;

public class CameraArcBall extends AbstractCamera {

	
	private double envWidth;
	private double envHeight;
	
	private double radius;
	
	private ILocation upVector;
	private double _orientation;
	
	public double _sensivity;
	
	public CameraArcBall(JOGLAWTGLRenderer joglawtglRenderer) {
		super(joglawtglRenderer);
		
		_orientation = 1.0;
    	_phi = 90.0;
        _theta = 360.00;
        _sensivity = 0.4;
		_keyboardSensivity= 4;

		setUpVector(new GamaPoint(0.0, 1.0,0.0));
		rotation();
	}
	
	public CameraArcBall(double xPos, double yPos, double zPos, double xLPos,
			double yLPos, double zLPos, JOGLAWTGLRenderer renderer) {
		super(xPos, yPos, zPos, xLPos, yLPos, zLPos, renderer);
		_position.x= xPos;
		_position.y= yPos;
		_position.z= zPos;
		
		_target.x= xLPos;
		_target.y= yLPos;
		_target.z= zLPos;
	}
	
	@Override
	public void UpdateCamera(GL gl, GLU glu, int width, int height) {

		if ( height == 0 ) {
			height = 1; // prevent divide by zero
		}
		float aspect = (float) width / height;
		// FIXME: need to see the influence of the different parameter.
		glu.gluPerspective(45.0f, aspect, 0.1f, getMaxDim() * 100);
		glu.gluLookAt(this._position.getX(), this._position.getY(), this._position.getZ(), 
					this._target.getX(), this._target.getY(),this._target.getZ(), 
					getUpVector().getX(), getUpVector().getY(), getUpVector().getZ());
		
		animate();
	}
	
	public void rotation()
	{
		if(_phi>180 && _phi <360) 
    	{
    		_orientation = -1;		
    		setUpVector(new GamaPoint(0.0, _orientation,0.0));
    	}
		else{
			_orientation = 1;		
    		setUpVector(new GamaPoint(0.0, _orientation,0.0));
		}	
		
		if( _theta > 360 ) _theta = 0;
	    if( _theta < 0 ) _theta = 360;

	    if( _phi > 360 ) _phi = 0;
	    if( _phi < 0 ) _phi = 360;
		
		double cosAngle = Math.cos(_theta*Math.PI/180.f);
		double sinAngle = Math.sin(_theta*Math.PI/180.f);
		
		_position.x = radius*sinAngle*Math.sin(_phi*Math.PI/180.f)+_target.x;
		_position.z = radius*cosAngle*Math.sin(_phi*Math.PI/180.f)+_target.z;
		_position.y = radius*Math.cos(_phi*Math.PI/180.f)+_target.y;	
	}
	
//	public void followAgent(IAgent a) {
//
//		ILocation l = a.getGeometry().getLocation();
//		Envelope env = a.getGeometry().getEnvelope();
//		
//		double xPos = l.getX() - myRenderer.displaySurface.getEnvWidth() / 2;
//		double yPos = -(l.getY() - myRenderer.displaySurface.getEnvHeight() / 2);
//		
//		double zPos = env.maxExtent() * 2 + l.getZ();
//		double zLPos = -(env.maxExtent() * 2);
//		
//		updatePosition(xPos, yPos, zPos);
//		lookPosition(xPos, yPos, zLPos);
//		
//	}
	
	@Override
	public void initializeCamera(double envWidth, double envHeight) {

		this.envWidth = envWidth;
		this.envHeight = envHeight;

		if ( envWidth > envHeight ) {
			setMaxDim(envWidth);
		} else {
			setMaxDim(envHeight);
		}

		radius = getMaxDim() * INIT_Z_FACTOR;
		_target.x = 0;
		_target.y = 0;
		_target.z = 0;
		_phi = 90.0;
        _theta = 360.00;
		rotation();	
	}
	
	public void initialize3DCamera(double envWidth, double envHeight) 
	{	
//		System.out.println("Camera 3D");
		this.envWidth = envWidth;
		this.envHeight = envHeight;

		if ( envWidth > envHeight ) {
			setMaxDim(envWidth);
		} else {
			setMaxDim(envHeight);
		}

		radius = getMaxDim() * INIT_Z_FACTOR;
		_target.x = 0;
		_target.y = 0;
		_target.z = 0;
		_phi = 135.0;
        _theta = 360.00;
		rotation();
	}
	
	public double getRadius()
	{
		return this.radius;
	}
	
	public void setRadius(double r)
	{
		this.radius = r;
	}
	
	@Override
	public ILocation getUpVector() {
		return upVector;
	}
	@Override
	public void setUpVector(ILocation upVector) {
		this.upVector = upVector;
	}
	
	// Move in the XY plan by changing camera pos and look pos.
	@Override
	public void moveXYPlan2(double diffx, double diffy, double z, double w, double h) {

		double translationValue = 0;

		if ( Math.abs(diffx) > Math.abs(diffy) ) {// Move X

			translationValue = Math.abs(diffx) * ((z + 1) / w);

			if ( diffx > 0 ) {// move right
				updatePosition(_position.getX() - translationValue, _position.getY(), _position.getZ());
				lookPosition(_target.getX() - translationValue, _target.getY(), _target.getZ());
			} else {// move left
				updatePosition(_position.getX() + translationValue, _position.getY(), _position.getZ());
				lookPosition(_target.getX() + translationValue, _target.getY(), _target.getZ());
			}
		} else if ( Math.abs(diffx) < Math.abs(diffy) ) { // Move Y

			translationValue = Math.abs(diffy) * ((z + 1) / h);

			if ( diffy > 0 ) {// move down
				updatePosition(_position.getX(), _position.getY()+ translationValue, _position.getZ());
				this.lookPosition(_target.getX(), _target.getY() + translationValue, _target.getZ());
			} else {// move up
				updatePosition(_position.getX(), _position.getY()- translationValue, _position.getZ());
				lookPosition(_target.getX(), _target.getY() - translationValue, _target.getZ());
			}
		}
	}
	
	@Override
	public void animate()
	{
		double translationValue = 2*(_position.getZ() + 1) / myRenderer.getHeight();
   	 	if (this.forward) 
	 	{
	 		if(shiftKeyDown)
	 		{				
	 			_phi -= -_keyboardSensivity*_sensivity; 	 			
	 			rotation();
	 		}
	 		else
	 		{
	 			updatePosition(_position.getX(), _position.getY()- translationValue, _position.getZ());
   	 			lookPosition(_target.getX(), _target.getY() - translationValue, _target.getZ());
	 			
	 		}
	 	}
	    if (this.backward)
	    {
	    	if(shiftKeyDown)
	 		{				
	 			_phi -= _keyboardSensivity*_sensivity;   	 			
	 			rotation();
	 		}
	 		else
	 		{
	 			updatePosition(_position.getX(), _position.getY()+ translationValue, _position.getZ());
	 			this.lookPosition(_target.getX(), _target.getY() + translationValue, _target.getZ());
	    	}
	    }
	    if (this.strafeLeft)
	    {   
	    	if(shiftKeyDown)
	 		{
	 			_theta -= -_keyboardSensivity*_sensivity;					
	 			rotation();
	 		}
	 		else
	 		{
	 			updatePosition(_position.getX() + translationValue, _position.getY(), _position.getZ());
	 			lookPosition(_target.getX() + translationValue, _target.getY(), _target.getZ());
	 		}
	    }
	    if (this.strafeRight)
	    {
	    	if(shiftKeyDown)
	 		{
	 			_theta -= _keyboardSensivity*_sensivity;					
	 			rotation();
	 		}
	 		else
	 		{
	 			updatePosition(_position.getX() - translationValue, _position.getY(), _position.getZ());
	 			lookPosition(_target.getX() - translationValue, _target.getY(), _target.getZ());
	 		}
	    }
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {	
		float incrementalZoomStep;
		// Check if Z is not equal to 0 (avoid being block on z=0)
		if ( this.radius != 0 ) {
			incrementalZoomStep = (float)radius / 10;
		} else 
		{
			incrementalZoomStep = 0.1f;
		}
		if ( arg0.getWheelRotation() < 0 ) 
		{// Move Down

			radius -= incrementalZoomStep;
			rotation();
		} else {// Move Up
			radius += incrementalZoomStep;
			rotation();
		}
		myRenderer.displaySurface.setZoomLevel(getMaxDim() * AbstractCamera.INIT_Z_FACTOR / radius);
//		PrintParam();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
		if ( isArcBallOn(arg0) && isModelCentered || myRenderer.displaySurface.selectRectangle ) 
		{
			//check the difference between the current x and the last x position
			int horizMovement = arg0.getX()- lastxPressed;
			// check the difference between the current y and the last y position
			int vertMovement = arg0.getY()  - lastyPressed; 
			
			// set lastx to the current x position
			lastxPressed = arg0.getX() ; 
			// set lastyPressed to the current y position
			lastyPressed = arg0.getY();
			
			_theta -= horizMovement*_sensivity;					
			_phi -= vertMovement*_sensivity;
			
			rotation();
		}
		else
		{
			// check the difference between the current x and the last x position
			int diffx = arg0.getX() - lastxPressed;
			// check the difference between the current y and the last y position
			int diffy = arg0.getY() - lastyPressed;
			// set lastx to the current x position
			lastxPressed = arg0.getX();
			// set lastyPressed to the current y position
			lastyPressed = arg0.getY();

			double speed = 0.035;

			// Decrease the speed of the translation if z is negative.
			if ( _position.getZ() < 0 ) {
				speed = speed / Math.abs(_position.getZ()) * 2;
			} else {
				speed = speed * Math.abs(_position.getZ()) / 4;
			}

			moveXYPlan2(diffx, diffy, _position.getZ(), this.myRenderer.getWidth(),
				this.myRenderer.getHeight());
		}
//		PrintParam();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {
//		PrintParam();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if ( isArcBallOn(arg0) && isModelCentered ) {
			if ( SwingUtilities.isRightMouseButton(arg0) ) {
				myRenderer.reset();
			}
		} else {
			// myCamera.PrintParam();
			// System.out.println( "x:" + mouseEvent.getX() + " y:" + mouseEvent.getY());
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {
		lastxPressed = arg0.getX();
		lastyPressed = arg0.getY();
		
		//Picking mode
		if(myRenderer.displaySurface.picking)
		{
			//Activate Picking when press and right click and if in Picking mode
			if(SwingUtilities.isRightMouseButton(arg0))
				isPickedPressed = true;	
			
			mousePosition.x = arg0.getX();
			mousePosition.y = arg0.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if ( myRenderer.displaySurface.selectRectangle ) {
			enableROIDrawing = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case VK_LEFT: 
			strafeLeft = true;
			shiftKeyDown = checkShiftKeyDown(arg0);
			break;
		case VK_RIGHT: 
			strafeRight = true;
			shiftKeyDown = checkShiftKeyDown(arg0);
			break;
		case VK_UP:
			forward = true;
			shiftKeyDown = checkShiftKeyDown(arg0);
			break;
		case VK_DOWN:
			backward = true;
			shiftKeyDown = checkShiftKeyDown(arg0);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case VK_LEFT: // player turns left (scene rotates right)
			strafeLeft = false;
			break;
		case VK_RIGHT: // player turns right (scene rotates left)
			strafeRight = false;
			break;
		case VK_UP:
			forward = false;
			break;
		case VK_DOWN:
			backward = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	public double getMaxDim() {
		return maxDim;}
}