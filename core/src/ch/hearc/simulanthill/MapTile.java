package ch.hearc.simulanthill;

import java.awt.color.*;

public abstract class MapTile extends Element 
{

	protected int color;

	public int getColor() 
	{
		return color;
	}

	public void setColot(int _color) 
	{
		color = _color;
	}

}
