/**
 * EdgeSvgView.java
 */
package edu.belmont.mth.visigraph.views.svg;

import java.awt.geom.*;
import edu.belmont.mth.visigraph.models.*;
import edu.belmont.mth.visigraph.settings.*;
import edu.belmont.mth.visigraph.utilities.*;
import static edu.belmont.mth.visigraph.utilities.GeometryUtilities.*;

/**
 * @author Cameron Behar
 *
 */
public class EdgeSvgView
{	
	public static String format(Edge e, Palette p, GraphSettings s)
	{
		StringBuilder sb = new StringBuilder();
		Point2D apparentHandleLocation = e.isLinear() ? GeometryUtilities.midpoint(e.from, e.to) : e.getHandlePoint2D();
		double handleRadius = e.thickness.get() * GlobalSettings.defaultEdgeHandleRadiusRatio;
		
		if(e.isLinear())
		{
			sb.append("<line ");
			sb.append("x1=\"" + e.from.x.get() + "\" ");
			sb.append("y1=\"" + e.from.y.get() + "\" ");
			sb.append("x2=\"" + e.to.x.get() + "\" ");
			sb.append("y2=\"" + e.to.y.get() + "\" ");
			sb.append("style=\"stroke:" + SvgUtilities.formatColor(e.isSelected.get() ? p.selectedEdgeLine.get() : p.getElementColor(e.color.get())) + ";stroke-width:" + e.thickness.get() + "\"/>\r\n");
		}
		else
		{
			double fromAngle   = angle(-(e.from.y.get()  - e.getCenter().getY()), e.from.x.get()  - e.getCenter().getX());
			double handleAngle = angle(-(e.handleY.get() - e.getCenter().getY()), e.handleX.get() - e.getCenter().getX());
			double toAngle     = angle(-(e.to.y.get()    - e.getCenter().getY()), e.to.x.get()    - e.getCenter().getX());
			boolean isClockwise = angleBetween(fromAngle, handleAngle) < angleBetween(fromAngle, toAngle);
			
			sb.append("<path ");
			
			if (isClockwise) sb.append("d=\"M " + e.from.x.get() + "," + e.from.y.get() + " ");
			else			 sb.append("d=\"M " + e.to.x.get()   + "," + e.to.y.get()   + " ");
			
			double radius = e.getCenter().distance(e.handleX.get(), e.handleY.get());
			sb.append("A " + radius + "," + radius + " 0 0,0 ");
			
			if (isClockwise) sb.append(e.to.x.get()   + "," + e.to.y.get()   + "\" ");
			else			 sb.append(e.from.x.get() + "," + e.from.y.get() + "\" ");
			
			sb.append("style=\"fill:none;stroke:" + SvgUtilities.formatColor(e.isSelected.get() ? p.selectedEdgeLine.get() : p.getElementColor(e.color.get())) + ";stroke-width:" + e.thickness.get() + "\"/>\r\n");
		}
		
		if(s.showEdgeHandles.get())
		{
			sb.append("<circle ");
			sb.append("cx=\"" + apparentHandleLocation.getX() + "\" ");
			sb.append("cy=\"" + apparentHandleLocation.getY() + "\" ");
			sb.append("r=\"" + handleRadius + "\" ");
			sb.append("stroke=\"" + SvgUtilities.formatColor(e.isSelected.get() ? p.selectedEdgeLine.get() : p.getElementColor(e.color.get())) + "\" ");
			sb.append("stroke-width=\"1\" ");
			sb.append("style=\"fill:" + SvgUtilities.formatColor(e.isSelected.get() ? p.selectedEdgeLine.get() : p.getElementColor(e.color.get())) + ";\"/>\r\n");
		}
		
		// Draw arrow head for directed edges
		if(e.isDirected)
		{
			Point2D.Double[] arrowPoint = new Point2D.Double[3];
			double tangentAngle;
			
			if(e.isLinear())
				tangentAngle = Math.atan2(e.to.y.get() - e.from.y.get(), e.to.x.get() - e.from.x.get());
			else
			{
				tangentAngle = Math.atan2(e.handleY.get() - e.getCenter().getY(), e.handleX.get() - e.getCenter().getX());
				
				// We need to calculate these angles so that we know in which order whether to flip the angle by 180 degrees
				double fromAngle = Math.atan2(e.from.y.get() - e.getCenter().getY(), e.from.x.get() - e.getCenter().getX());
				double toAngle = Math.atan2(e.to.y.get() - e.getCenter().getY(), e.to.x.get() - e.getCenter().getX());
				
				if (GeometryUtilities.angleBetween(fromAngle, tangentAngle) >= GeometryUtilities.angleBetween(fromAngle, toAngle))
					tangentAngle += Math.PI;
				
				tangentAngle += Math.PI / 2;
			}
			
			for(int i = 0; i < 3; ++i)
			{
				double theta = tangentAngle + i * 2.0 * Math.PI / 3.0;
				arrowPoint[i] = new Point2D.Double(e.thickness.get() * GlobalSettings.defaultDirectedEdgeArrowRatio * Math.cos(theta) + apparentHandleLocation.getX(), 
												   e.thickness.get() * GlobalSettings.defaultDirectedEdgeArrowRatio * Math.sin(theta) + apparentHandleLocation.getY());
			}
			
			sb.append("<path d=\"");
			sb.append("M " + arrowPoint[0].x + "," + arrowPoint[0].y + " ");
			sb.append("L " + arrowPoint[1].x + "," + arrowPoint[1].y + " ");
			sb.append("L " + arrowPoint[2].x + "," + arrowPoint[2].y + " ");
			sb.append("L " + arrowPoint[0].x + "," + arrowPoint[0].y + "\" ");
			sb.append("style=\"fill:" + SvgUtilities.formatColor(e.isSelected.get() ? p.selectedEdgeLine.get() : p.getElementColor(e.color.get())) + ";\"/>\r\n");
		}
		
		if(s.showEdgeLabels.get())
		{
			sb.append("<text ");
			sb.append("x=\"" + (e.handleX.get() + handleRadius + 1) + "\" ");
			sb.append("y=\"" + (e.handleY.get() - handleRadius - 1) + "\" ");
			sb.append("fill=\"" + SvgUtilities.formatColor(e.isSelected.get() ? p.selectedEdgeLine.get() : p.getElementColor(e.color.get())) + "\">" + SvgUtilities.formatString(e.label.get()) + "</text>\r\n");
		}
		
		if(s.showEdgeWeights.get())
		{
			sb.append("<text ");
			sb.append("x=\"" + (e.handleX.get() - handleRadius - 12) + "\" ");
			sb.append("y=\"" + (e.handleY.get() + handleRadius + 12) + "\" ");
			sb.append("fill=\"" + SvgUtilities.formatColor(e.isSelected.get() ? p.selectedEdgeLine.get() : p.getElementColor(e.color.get())) + "\">" + SvgUtilities.formatString(e.weight.get().toString()) + "</text>\r\n");
		}
		
		return sb.toString();
	}
}