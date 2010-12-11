import java.awt.*;
import java.util.*;
import java.util.List;

import edu.belmont.mth.visigraph.models.*;
import edu.belmont.mth.visigraph.models.functions.*;

	public String evaluate( Graphics2D g2D, Graph g, Component owner )
	{
		HashMap vertexDegrees = new HashMap( );
		
		for ( Vertex v : g.vertexes )
			if ( v.isSelected.get( ) )
				vertexDegrees.put( v, 0 );
		
		if ( vertexDegrees.isEmpty( ) )
			for ( Vertex v : g.vertexes )
				vertexDegrees.put( v, 0 );
		
		for ( Edge e : g.edges )
		{
			if ( !e.isDirected && !e.isLoop )
			{
				Integer outDegrees = vertexDegrees.get( e.from );
				if ( outDegrees != null )
					vertexDegrees.put( e.from, outDegrees + 1 );
			}
			
			Integer inDegrees = vertexDegrees.get( e.to );
			if ( inDegrees != null )
				vertexDegrees.put( e.to  , inDegrees  + 1 );
		}
		
		int maxDegrees = 0;
		for ( Integer degrees : vertexDegrees.values( ) )
			if ( degrees > maxDegrees )
				maxDegrees = degrees;
		
		return Integer.toString( maxDegrees );
	}
	
	public boolean allowsDynamicEvaluation( ) { return true;                   }
	public boolean allowsOneTimeEvaluation( ) { return true;                   }
	public String  toString               ( ) { return "Get maximum indegree"; }

return (FunctionBase) this;
