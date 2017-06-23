package dom;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import adm.ADM;

public class Item extends DynamicObject{
	
	public Item(){ drawable = false; }
	public Item(int x, int y, int direction, int assetIndex, int frame){
		drawable = true;
		
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.assetIndex = assetIndex;
		this.frame = frame;
	}
	
	public BufferedImage getImage(){
		return ADM.getInstance().getItemAsset(assetIndex, direction*DynamicObject.MAX_FRAME + frame);
	}
}
