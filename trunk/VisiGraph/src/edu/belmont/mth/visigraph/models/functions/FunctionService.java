/**
 * FunctionService.java
 */
package edu.belmont.mth.visigraph.models.functions;

import java.io.*;
import edu.belmont.mth.visigraph.models.*;
import bsh.Interpreter;

/**
 * @author Cameron Behar
 *
 */
public class FunctionService
{	
	public final static FunctionService instance = new FunctionService();
	
	public final ObservableList<FunctionBase> functions;
	
	private FunctionService ( )
	{
		functions = new ObservableList<FunctionBase>("functions");
		
		functions.add(new CountCrossings());
		
		File folder = new File("functions");
		if(folder.exists())
			for(String filename : folder.list( new FilenameFilter() { public boolean accept(File dir, String name) { return name.endsWith(".function"); } } ))
				try { functions.add((FunctionBase)new Interpreter().source("functions/" + filename)); }
				catch (Exception e) { e.printStackTrace(); }
	}
}