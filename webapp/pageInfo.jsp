<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="ROLAP.*"%>
<%@ page import="java.util.*"%>
<%@ page import="xml.client.*"%>
<%@ page import="xml.meta.*"%>
<%
	ROLAPProcess proc = (ROLAPProcess) session.getServletContext().getAttribute("ROLAPProcess");
	Query query = proc.getQuery();
%>

<div>

	<div style="display: block; margin-left: auto; margin-right: auto">
		<span style="color: grey; border: 2px solid black; padding: 3px;">Slices:
		</span>
		<div style="padding: 8px;">
			<%
				for(xml.client.Dimension dimClient : query.getDimensions().getDimension())
				{
					xml.meta.Dimension dimMeta = proc.getMetaDimension(dimClient.getDimID());
					
					if(dimClient.getSlice() != null)
					{
						for(xml.client.Slice.SliceCollumns.Collumn col: dimClient.getSlice().getSliceCollumns().getCollumn())
						{
							xml.meta.Level lvl = proc.getLevel(dimMeta, col.getId());
							out.print(dimMeta.getDisplayName()+ "-"+ lvl.getDisplayName() + "=(");
							Iterator<String> values = col.getByValues().getValue().iterator();
							while(values.hasNext())
							{
								String value = values.next();
								
								if(values.hasNext())
									out.print(value+",");
								else
									out.print(value);
							}
							out.print(") ");
						}
					}
					
				}
			%>
		</div>
	</div>

	<div style="display: block; margin-left: auto; margin-right: auto">
		<span style="color: grey; border: 2px solid black; padding: 3px;">Levels:
		</span>
		<div style="padding: 8px;">
			<%
			for(xml.client.Dimension dimClient : query.getDimensions().getDimension())
			{
				xml.meta.Dimension dimMeta = proc.getMetaDimension(dimClient.getDimID());
				Iterator<String> levelIt = dimClient.getSelectedCollumns().getSelectedCollumn().iterator();
				while(levelIt.hasNext())
				{
					xml.meta.Level lvl = proc.getLevel(dimMeta, levelIt.next());
					if(levelIt.hasNext())
						out.print(dimMeta.getDisplayName()+"-"+lvl.getDisplayName()+",");
					else
						out.print(dimMeta.getDisplayName()+"-"+lvl.getDisplayName());
				}
			}
			%>
		</div>
	</div>

	<div style="display: block; margin-left: auto; margin-right: auto">
		<span style="color: grey; border: 2px solid black; padding: 3px;">Facts:
		</span>
		<div style="padding: 8px;">
			<%
				Iterator<SelectedFacts.Fact> factIt =  query.getCube().getSelectedFacts().getFact().iterator();
				while(factIt.hasNext())
				{
					SelectedFacts.Fact fact = factIt.next();
					xml.meta.Fact factMeta = proc.getMetaFact(query.getCube().getCubeID(), fact.getFactID());
					if(factIt.hasNext())
					{
						out.print(factMeta.getDisplayName() + " op: ("+fact.getAggrOperation()+")" + " before("+(fact.getFilterBefore()!=null ? fact.getFilterBefore().getOp() + " "+fact.getFilterBefore().getValue()+
							")" : ")") + " after("+ (fact.getFilterAfter()!= null ? fact.getFilterAfter().getOp()+ " "+fact.getFilterAfter().getValue()+")" : "), "));
					}
					else
					{
						out.print(factMeta.getDisplayName() + " op: ("+fact.getAggrOperation()+")" + " before("+(fact.getFilterBefore()!=null ? fact.getFilterBefore().getOp() + " "+fact.getFilterBefore().getValue()+
								")" : ")") + " after("+ (fact.getFilterAfter()!= null ? fact.getFilterAfter().getOp()+ " "+fact.getFilterAfter().getValue()+")" : ")"));
					}
				}
			%>
		</div>
	</div>

</div>