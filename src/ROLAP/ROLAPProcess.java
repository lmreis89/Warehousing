package ROLAP;


import java.io.Serializable;
import java.util.List;

import xml.client.Dimension;
import xml.client.Query;
import xml.client.SelectedFacts.Fact;
import xml.server.Response;

public interface ROLAPProcess extends Serializable{

	public String hello();
	
	public List<String> register(String user, String pw);
	
	
	//It starts here
	public void addSelectedCollumn(String dimID,String value);
	public void addSliceOp(String dimID, String op);
	public void addSliceCollumn(String dimID, String colID);
	public void addSliceCollumnValue(String dimID, String colID, String value);
	public void addFact(String factID, String op);
	public void addFactFilterBefore(String factID, String op, String value);
	public void addFactFilterAfter(String factID, String op, String value);
	public List<xml.meta.Dimension> getDimensions();
	public List<xml.meta.Cube> getCubes();
	public void chooseCube(String cubeID);
	public List<Object> getMeasures();
	public void executeQuery();
	public void saveQuery(String name);
	public void loadReport(String name);
	public Response getCurrent();
	public void removeCollumn(String dimID, String value);
	public void resetQuery();
	public void resetSlice();
	public int getType(String dim,String attribute);
	public List<String> DimensionListOfValues(String dimID, String levelID);
	public void removeSliceCollumn(String dimID, String colID);
	public void setDim(String dimID);
	public void setLevel(String levelID);
	public String getDim();
	public String getLevel();
	public List<Slice> getQueryDimensions();
	public List<Filter> getSelectedFacts();
	public String getFactID();
	public void setFactID(String factID);
	public int getFactOccur();
	public void setFactOccur(int factOccur);
	public String getCurrentPage();
	public void setCurrentPage(String currentPage);
	public void removeFactFilterBefore(String factID);
	public void removeFactFilterAfter(String factID);
	public List<String> getMinMax(String factID);
	public List<String> getReports();
	public void resetFacts();
	public List<String> getCurrentReports();
	
	public List<String> upperHierarchy(String levelID, String dimID, int occur);
	public List<String> lowerHierarchy(String levelID, String dimID, int occur);
	public int numHierarchies(String levelID, String dimID);
	
	public String getResponse();
	public Query getQuery();
	public xml.meta.Dimension getMetaDimension(String dimID);
	public xml.meta.Fact getMetaFact(String cubeID, String factID);
	public xml.meta.Level getLevel(xml.meta.Dimension dim,String levelID);
	
	
	public List<xml.meta.Cube> getTESTCubes();
	public List<xml.meta.Dimension> getTESTDims();
	public List<xml.meta.Fact> getTESTFacts();
	public List<String> getTESTReports();
	
}
