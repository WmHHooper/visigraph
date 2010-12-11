import java.awt.*;
import java.util.*;

import edu.belmont.mth.visigraph.models.*;
import edu.belmont.mth.visigraph.models.functions.*;
import edu.belmont.mth.visigraph.utilities.GeometryUtilities;

	public String evaluate( Graphics2D g2D, Graph g, Component owner )
	{
		if ( g.vertexes.isEmpty( ) )
			return "0";
		
		double[ ][ ] distances = new double[g.vertexes.size( )][g.vertexes.size( )];
		
		for ( int i = 0; i < g.vertexes.size( ); ++i )
			for ( int j = 0; j < g.vertexes.size( ); ++j )
				distances[i][j] = ( i == j ? 0.0 : Double.POSITIVE_INFINITY );
		
		HashMap indices = new HashMap( );
		for ( int i = 0; i < g.vertexes.size( ); ++i )
			indices.put( g.vertexes.get( i ), i );
		
		for ( Edge e : g.edges )
			if ( !e.isLoop )
			{
				int from = indices.get( e.from ), to = indices.get( e.to );
				
				if ( e.weight.get( ) < distances[from][to] )
					distances[from][to] = 1.0;
				
				if ( !e.isDirected && e.weight.get( ) < distances[to][from] )
					distances[to][from] = 1.0;
			}
		
		for ( int k = 0; k < g.vertexes.size( ); ++k )
			for ( int i = 0; i < g.vertexes.size( ); ++i )
				for ( int j = 0; j < g.vertexes.size( ); ++j )
					distances[i][j] = Math.min( distances[i][j], distances[i][k] + distances[k][j] );
		
		double minEccentricity = Double.POSITIVE_INFINITY;
		for ( int i = 0; i < g.vertexes.size( ); ++i )
		{
			double eccentricity = Double.NEGATIVE_INFINITY;
			
			for ( int j = 0; j < g.vertexes.size( ); ++j )
				eccentricity = Math.max( eccentricity, distances[i][j] );
			
			minEccentricity = Math.min( minEccentricity, eccentricity );
		}
		
		for ( int i = 0; i < g.vertexes.size( ); ++i )
		{
			double eccentricity = Double.NEGATIVE_INFINITY;
			
			for ( int j = 0; j < g.vertexes.size( ); ++j )
				eccentricity = Math.max( eccentricity, distances[j][i] );
			
			minEccentricity = Math.min( minEccentricity, eccentricity );
		}
		
		return ( minEccentricity == Double.POSITIVE_INFINITY ? "\u221E" : Long.toString( Math.round( minEccentricity ) ) );
	}
	
	public boolean allowsDynamicEvaluation( ) { return false;                           }
	public boolean allowsOneTimeEvaluation( ) { return true;                            }
	public String  toString               ( ) { return "Calculate radius (unweighted)"; }
	
return (FunctionBase) this;
