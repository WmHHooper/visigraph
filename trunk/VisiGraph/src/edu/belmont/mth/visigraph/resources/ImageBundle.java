/**
 * ImageBundle.java
 */
package edu.belmont.mth.visigraph.resources;

import java.awt.*;
import java.net.*;
import java.util.*;

/**
 * @author Cameron Behar
 * 
 */
public class ImageBundle extends ResourceBundle
{
	private String							fileSuffix;
	private static HashMap<String, Image>	map	= new HashMap<String, Image>();
	
	public ImageBundle()
	{
		this("");
	}
	
	private ImageBundle(String suffix)
	{
		fileSuffix = suffix;
	}
	
	@Override
	public Enumeration<String> getKeys()
	{
		return (new Vector<String>(map.keySet())).elements();
	}
	
	@Override
	protected final Object handleGetObject(String key)
	{
		return loadImage(key, ".png");
	}
	
	private Image loadImage(String filename, String extension)
	{
		String imageName = filename + fileSuffix + extension;
		
		Image image = map.get(imageName);
		
		if (image != null)
			return image;
		
		URL url = getClass().getResource("images/" + imageName);
		
		if (url == null)
			return null;
		
		image = Toolkit.getDefaultToolkit().createImage(url);
		map.put(imageName, image);
		
		return image;
	}
}