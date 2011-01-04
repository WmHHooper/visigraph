import java.awt.*;
import java.util.*;
import edu.belmont.mth.visigraph.models.*;
import edu.belmont.mth.visigraph.settings.*;
import edu.belmont.mth.visigraph.utilities.*;
import edu.belmont.mth.visigraph.models.functions.*;

	public String evaluate( Graphics2D g2D, Graph g, Component owner )
	{
		Collection components = GraphUtilities.findWeaklyConnectedComponents( g );
		
		g.suspendNotifications( true );
		
		int color = 0;
		for( Collection component : components )
		{
			for( Vertex vertex : component )
				vertex.color.set( color );
			++color;
		}
		
		for( Edge edge : g.edges )
			edge.color.set( edge.from.color.get( ) );
		
		g.suspendNotifications( false );
		
		return Integer.toString( components.size( ) );
	}
	
	public Object getAttribute( Function.Attribute attribute )
	{
		switch( attribute )
		{
			case Function.Attribute.AUTHOR:
				return "Cameron Behar";
			case Function.Attribute.VERSION:
				return "20110101";
			case Function.Attribute.DESCRIPTION:
				return "Colors the vertices and edges of a given graph according to their membership to connected components and returns the total number of connected components found.</p><p>" + GlobalSettings.applicationName + "'s built-in implementation of a union-find algorithm is used with union-by-rank and path-flattening optimizations to perform the test in <code><i>O</i>(|<i>V</i>|\u03B1(|<i>E</i>|))</code> time where <code>\u03B1(<i>n</i>)</code> is the inverse Ackermann function.";
			case Function.Attribute.SIDE_EFFECTS:
				return "The color of each vertex and edge will be set to the color incrementally-chosen for its connected component.";
			case Function.Attribute.OUTPUT:
				return "The total number of connected components found.";
			case Function.Attribute.ALLOWS_DYNAMIC_EVALUATION:
				return false;
			case Function.Attribute.ALLOWS_ONE_TIME_EVALUATION:
				return true;
			case Function.Attribute.RELATED_GENERATORS:
				return new String[ ] { "Condensation of (another graph)" };
			case Function.Attribute.RELATED_FUNCTIONS:
				return new String[ ] { "Color strongly connected components", "Color weakly connected components", "Connect completely", "Count connected components", "Count strongly connected components", "Count weakly connected components" };
			case Function.Attribute.TAGS:
				return new String[ ] { "Connectivity", "Graph evaluator", "Graph modifier", "Substructures" };
			default:
				return null;
		}
	}
	
	public boolean isApplicable( boolean areLoopsAllowed, boolean areDirectedEdgesAllowed, boolean areMultipleEdgesAllowed, boolean areCyclesAllowed )
	{
		return !areDirectedEdgesAllowed;
	}
	
	public String toString( )
	{
		return "Color connected components";
	}
	
return (Function) this;
