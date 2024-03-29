/**
 * GraphDisplayView.java
 */
package edu.belmont.mth.visigraph.views.display;

import java.awt.*;
import java.awt.geom.*;
import edu.belmont.mth.visigraph.models.*;
import edu.belmont.mth.visigraph.settings.*;

/**
 * @author Cameron Behar
 */
public class GraphDisplayView
{
	public static Rectangle2D getBounds( Graph graph )
	{
		if( graph.vertices.size( ) <= 0 )
			return null;
		
		double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
		
		for( Vertex vertex : graph.vertices )
		{
			minX = Math.min( minX, vertex.x.get( ) );
			maxX = Math.max( maxX, vertex.x.get( ) );
			minY = Math.min( minY, vertex.y.get( ) );
			maxY = Math.max( maxY, vertex.y.get( ) );
		}
		
		for( Edge edge : graph.edges )
			if( !edge.isLinear( ) )
			{
				Rectangle2D rect = edge.getArc( ).getBounds2D( );
				minX = Math.min( minX, rect.getMinX( ) );
				maxX = Math.max( maxX, rect.getMaxX( ) );
				minY = Math.min( minY, rect.getMinY( ) );
				maxY = Math.max( maxY, rect.getMaxY( ) );
			}
		
		for( Caption caption : graph.captions )
		{
			minX = Math.min( minX, caption.x.get( ) );
			maxX = Math.max( maxX, caption.x.get( ) );
			minY = Math.min( minY, caption.y.get( ) );
			maxY = Math.max( maxY, caption.y.get( ) );
		}
		
		return new Rectangle2D.Double( minX, minY, maxX - minX, maxY - minY );
	}
	
	public static void paint( Graphics2D g2D, Graph graph, GraphSettings s )
	{
		// Draw all the edges first
		for( Edge edge : graph.edges )
			EdgeDisplayView.paintEdge( g2D, s, edge );
		
		// Then draw all the vertices
		for( Vertex vertex : graph.vertices )
			VertexDisplayView.paint( g2D, s, vertex );
		
		// Then draw all the captions
		if( s.showCaptions.get( ) )
			for( Caption caption : graph.captions )
				CaptionDisplayView.paint( g2D, s, caption );
	}
}
