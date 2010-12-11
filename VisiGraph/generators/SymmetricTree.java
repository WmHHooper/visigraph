import java.awt.*;
import java.util.regex.*;

import edu.belmont.mth.visigraph.models.*;
import edu.belmont.mth.visigraph.settings.*;
import edu.belmont.mth.visigraph.models.generators.*;

	public Graph generate( String params, boolean areLoopsAllowed, boolean areDirectedEdgesAllowed, boolean areMultipleEdgesAllowed, boolean areCyclesAllowed, Component owner )
	{
		Graph graph = new Graph( UserSettings.instance.defaultGraphName.get( ) + " " + toString( ), areLoopsAllowed, areDirectedEdgesAllowed, areMultipleEdgesAllowed, areCyclesAllowed );
		Pattern pattern = Pattern.compile( getParametersValidatingExpression( ) );
		Matcher matcher = pattern.matcher( params ); matcher.find( );
		
		int levelCount = Integer.parseInt( matcher.group( 1 ) );
		int fanOut = Integer.parseInt( matcher.group( 2 ) );
		
		buildTree( graph, levelCount, fanOut, 0.0, 0.0 );
		
		return graph;
	}
	
	public Vertex buildTree( Graph g, int level, int fanOut, double x, double y )
	{
		Vertex root = new Vertex( x, y );
		g.vertexes.add( root );
		
		if ( level > 0 )
		{
			double radiansPerBranch = ( 2 * Math.PI ) / fanOut;
			
			for ( int i = 0; i < fanOut; ++i )
			{
				Vertex branch = buildTree( g, level - 1, fanOut, x + 100.0 * Math.pow( level, 1.3 ) * Math.cos( radiansPerBranch * i ), y + 100.0 * Math.pow( level, 1.3 ) * Math.sin( radiansPerBranch * i ) );
				g.edges.add( new Edge( false, root, branch ) );
			}
		}
		
		return root;
	}

    public String toString                          ( ) { return "Symmetric tree";             }    
    public String getParametersDescription          ( ) { return "[recursions] [fan-out]";     }
    public String getParametersValidatingExpression ( ) { return "^\\s*(\\d+)\\s*(\\d+)\\s*$"; }
	
    public GeneratorBase.BooleanRule areLoopsAllowed         ( ) { return GeneratorBase.BooleanRule.ForcedFalse; }
    public GeneratorBase.BooleanRule areDirectedEdgesAllowed ( ) { return GeneratorBase.BooleanRule.ForcedFalse; }
    public GeneratorBase.BooleanRule areMultipleEdgesAllowed ( ) { return GeneratorBase.BooleanRule.ForcedFalse; }
    public GeneratorBase.BooleanRule areCyclesAllowed        ( ) { return GeneratorBase.BooleanRule.ForcedFalse; }
    public GeneratorBase.BooleanRule areParametersAllowed    ( ) { return GeneratorBase.BooleanRule.ForcedTrue;  }
        
return (GeneratorBase) this;
        
