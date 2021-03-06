package dom;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import adm.ADM;
import logger.Logger;
import ui.UI;


public class DynamicObject {
	static public class DIRECTION{
		static public final int DOWN=0, LEFT=1, RIGHT=2, UP=3;
	}
	
	static public final int MAX_FRAME = 4;
	static public final int FRAME_UPDATE_TIME = 300;
	static public final int DRAWING_EXTRA_RANGE = 50;

	protected boolean drawable;
	
	protected int x, y; //position
	protected int lastX, lastY; //last drawing position
	protected int direction; //face direction   pictureID
	protected int lastDirection; //last drawing position
	protected int assetIndex;
	protected int frame;
	protected long lastUpdateTime;
	
	public DynamicObject(){
		
	}
	public DynamicObject(int x, int y, int direction, int assetIndex, int frame){
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.assetIndex = assetIndex;
		this.frame = frame;
		lastUpdateTime = System.currentTimeMillis();
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(0,0,0);
		return img;
	}
	
	public boolean getDrawable(){
		return drawable;
	}
	public void drawImage(Graphics g){
		if(!drawable)
			return;
		BufferedImage img = getImage();
		int playerX = DOM.getInstance().getPlayerX(), playerY = DOM.getInstance().getPlayerY();
		int canvasWidth = UI.getInstance().getCanvasWidth(), canvasHeight = UI.getInstance().getCanvasHeight();
		if( x+img.getWidth()/2+DynamicObject.DRAWING_EXTRA_RANGE < playerX - canvasWidth/2
		    || x-img.getWidth()/2-DynamicObject.DRAWING_EXTRA_RANGE > playerX + canvasWidth/2
		    || y+img.getHeight()/2+DynamicObject.DRAWING_EXTRA_RANGE < playerY - canvasHeight/2
		    || y+img.getHeight()/2-DynamicObject.DRAWING_EXTRA_RANGE > playerY + canvasHeight/2)
			return;
		g.drawImage(img, x-playerX -img.getWidth()/2 +canvasWidth/2 , y-playerY -img.getHeight()/2 +canvasHeight/2, null);
		//logger.Logger.log("Drawing with frame : "+frame);
	}
	
	public void update(int nextX, int nextY, int nextDirection, int assetIndex){
		drawable = true;
		long currTime = System.currentTimeMillis();
		if(direction != nextDirection)  // turn
		{
			direction = nextDirection;
			//frame = 0;
			//lastUpdateTime = currTime;
		}
		
		if(currTime-lastUpdateTime >= FRAME_UPDATE_TIME)
		{
			if(nextX!=lastX || nextY!=lastY)  // move
			{
				frame += 1;
				frame %= MAX_FRAME;
				lastUpdateTime = currTime;
				lastX = nextX;
				lastY = nextY;
				lastDirection = nextDirection;
			}
			else if(nextDirection == lastDirection) // stay
			{
				frame = 0;
				lastUpdateTime = currTime;
			}
		}
		x = nextX;
		y = nextY;
		this.assetIndex = assetIndex;
	}
	public void update(int nextX, int nextY, int nextDirection){
		drawable = true;
		long currTime = System.currentTimeMillis();
		if(direction != nextDirection)  // turn
		{
			direction = nextDirection;
			//frame = 0;
			//lastUpdateTime = currTime;
		}
		
		if(currTime-lastUpdateTime >= FRAME_UPDATE_TIME)
		{
			if(nextX!=lastX || nextY!=lastY)  // move
			{
				frame += 1;
				frame %= MAX_FRAME;
				lastUpdateTime = currTime;
				lastX = nextX;
				lastY = nextY;
				lastDirection = nextDirection;
			}
			else if(nextDirection == lastDirection) // stay
			{
				frame = 0;
				lastUpdateTime = currTime;
			}
		}
		x = nextX;
		y = nextY;
	}
	public void updateByDirection(int directionX, int directionY, int assetIndex){
		drawable = true;
		long currTime = System.currentTimeMillis();
		int nextDirection = setDirection(directionX, directionY);
		int nextX = x+directionX;
		int nextY = y+directionY;
		if(direction != nextDirection)  // turn
		{
			direction = nextDirection;
			//frame = 0;
			//lastUpdateTime = currTime;
		}
		
		if(currTime-lastUpdateTime >= FRAME_UPDATE_TIME)
		{
			if(nextX!=lastX || nextY!=lastY)  // move
			{
				frame += 1;
				frame %= MAX_FRAME;
				lastUpdateTime = currTime;
				lastX = nextX;
				lastY = nextY;
				lastDirection = nextDirection;
			}
			else if(nextDirection == lastDirection) // stay
			{
				frame = 0;
				lastUpdateTime = currTime;
			}
		}
		x = nextX;
		y = nextY;
		this.assetIndex = assetIndex;
	}
	public void updateByDirection(int directionX, int directionY){
		drawable = true;
		long currTime = System.currentTimeMillis();
		int nextDirection = setDirection(directionX, directionY);
		int nextX = x+directionX;
		int nextY = y+directionY;
		if(direction != nextDirection)  // turn
		{
			direction = nextDirection;
			//frame = 0;
			//lastUpdateTime = currTime;
		}
		
		if(currTime-lastUpdateTime >= FRAME_UPDATE_TIME)
		{
			if(nextX!=lastX || nextY!=lastY)  // move
			{
				frame += 1;
				frame %= MAX_FRAME;
				lastUpdateTime = currTime;
				lastX = nextX;
				lastY = nextY;
				lastDirection = nextDirection;
			}
			else if(nextDirection == lastDirection) // stay
			{
				frame = 0;
				lastUpdateTime = currTime;
			}
		}
		x = nextX;
		y = nextY;
	}
	public void updateByDirection(int nextX, int nextY, int directionX, int directionY, int assetIndex){
		drawable = true;
		long currTime = System.currentTimeMillis();
		int nextDirection = setDirection(directionX, directionY);
		if(direction != nextDirection)  // turn
		{
			direction = nextDirection;
			//frame = 0;
			//lastUpdateTime = currTime;
		}
		
		if(currTime-lastUpdateTime >= FRAME_UPDATE_TIME)
		{
			if(nextX!=lastX || nextY!=lastY)  // move
			{
				frame += 1;
				frame %= MAX_FRAME;
				lastUpdateTime = currTime;
				lastX = nextX;
				lastY = nextY;
				lastDirection = nextDirection;
			}
			else if(nextDirection == lastDirection) // stay
			{
				frame = 0;
				lastUpdateTime = currTime;
			}
		}
		x = nextX;
		y = nextY;
		this.assetIndex = assetIndex;
	}
	public void updateByDirection(int nextX, int nextY, int directionX, int directionY){
		drawable = true;
		long currTime = System.currentTimeMillis();
		int nextDirection = setDirection(directionX, directionY);
		if(direction != nextDirection)  // turn
		{
			direction = nextDirection;
			//frame = 0;
			//lastUpdateTime = currTime;
		}
		
		if(currTime-lastUpdateTime >= FRAME_UPDATE_TIME)
		{
			if(nextX!=lastX || nextY!=lastY)  // move
			{
				frame += 1;
				frame %= MAX_FRAME;
				lastUpdateTime = currTime;
				lastX = nextX;
				lastY = nextY;
				lastDirection = nextDirection;
			}
			else if(nextDirection == lastDirection) // stay
			{
				frame = 0;
				lastUpdateTime = currTime;
			}
		}
		x = nextX;
		y = nextY;
	}
	public void updateWithoutDirection(int nextX, int nextY, int assetIndex){
		drawable = true;
		x = nextX;
		y = nextY;
		this.assetIndex = assetIndex;
	}
	public void updateWithoutDirection(int nextX, int nextY){
		drawable = true;
		x = nextX;
		y = nextY;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public void updateXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void updateDirection(int direction){
		this.direction = direction;
	}
	public void updateAssetIndex(int assetIndex){
		this.assetIndex = assetIndex;
	}
	
	public void updateFrame(int frame){
		this.frame = frame;
	}
	
	
	protected int setDirection(int directionX, int directionY) {
		int direction = this.direction;
		int absX = Math.abs(directionX), absY = Math.abs(directionY);
		if(directionX > 0 && absX >= absY) {
			direction = DIRECTION.RIGHT;
		}else if( directionX < 0 && absX >= absY){
			direction = DIRECTION.LEFT;
		}else if( directionY > 0 && absX <= absY){
			direction = DIRECTION.UP;
		}else if( directionY < 0 && absX <= absY){
			direction = DIRECTION.DOWN;
		}
		return direction;
	}
}
