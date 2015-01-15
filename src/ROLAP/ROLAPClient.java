package ROLAP;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBElement;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import web.WPWebServer;
import xml.client.Dimension;
import xml.client.Filter;
import xml.client.ObjectFactory;
import xml.client.Query;
import xml.client.SelectedFacts;
import xml.client.Query.Cube;
import xml.client.SelectedFacts.Fact;
import xml.client.Slice;
import xml.client.Slice.SliceCollumns.Collumn;
import xml.lov.ListOfValues;
import xml.meta.Fact.Aggregation;
import xml.meta.MetaModel;
import xml.server.Response;
import xml.server.Response.Headers.Head;


public class ROLAPClient implements ROLAPProcess {

	private static final long serialVersionUID = 1L;
	private static final String SERVER_URL = "10.22.104.125";
	private WPWebServer server;
	private int port;
	private String dataDir;
	private String webappDir;
	private Map<String,String> users;
	private ObjectFactory clientFactory;
	private MetaModel metaModel;
	private String serverIP;
	private String dimID;
	private String levelID;
	private String factID;
	private int factOccur;
	private String currentPage;
	private List<String> currentReports;

	private Query query;

	private Response current;

	public ROLAPClient(int port, String data, String webapp, String serverIP)
	{
		this.dataDir = data;
		this.port = port;
		this.webappDir = webapp;
		users = new HashMap<String,String>();
		clientFactory = new ObjectFactory();
		metaModel =  this.getMetaModel();
		dimID = "";
		levelID = "";
		this.resetQuery();
		this.serverIP = serverIP;
		this.current = null;
		factID="";
		factOccur=-1;
		currentPage="";
		currentReports = new LinkedList<String>();
	}

	public void doit()
	{
		server = new WPWebServer(port, webappDir, this);
		try
		{
			server.start(); // inicia servidor web para fazer apresentacao da
			// informacao
		} catch (ServerException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void resetQuery()
	{
		query = clientFactory.createQuery();
		query.setDimensions(clientFactory.createQueryDimensions());
		Cube cube = clientFactory.createQueryCube();
		cube.setSelectedFacts(clientFactory.createSelectedFacts());
		query.setCube(cube);
	}

	private void resetDimensions()
	{
		query.setDimensions(clientFactory.createQueryDimensions());
	}

	private void addDimension(String id)
	{
		Dimension d = clientFactory.createDimension();
		d.setDimID(id);
		d.setSelectedCollumns(clientFactory.createDimensionSelectedCollumns());

		query.getDimensions().getDimension().add(d);
	}

	private Dimension getDimension(String id)
	{
		Dimension returned = null;
		for(Dimension d : query.getDimensions().getDimension())
		{
			if(d.getDimID().equals(id))
			{
				returned = d;
				break;
			}
		}

		return returned;
	}

	public void addSelectedCollumn(String dimID,String value)
	{
		Dimension toAdd = this.getDimension(dimID);
		if(toAdd == null)
		{
			this.addDimension(dimID);
			toAdd = this.getDimension(dimID);
		}
		toAdd.getSelectedCollumns().getSelectedCollumn().add(value);
	}

	public void addSliceOp(String dimID, String op)
	{
		Dimension dim = this.getDimension(dimID);
		dim.getSlice().setOperation(op);
	}

	public void addSliceCollumn(String dimID, String colID)
	{
		System.out.println("adding slice to dimension "+dimID + " and level : " +colID);
		Dimension toAdd = this.getDimension(dimID);
		if(toAdd.getSlice() == null)
		{
			Slice slice = clientFactory.createSlice();
			slice.setSliceCollumns(clientFactory.createSliceSliceCollumns());
			toAdd.setSlice(slice);
		}
		Collumn col = clientFactory.createSliceSliceCollumnsCollumn();
		col.setId(colID);
		col.setByValues(clientFactory.createSliceSliceCollumnsCollumnByValues());
		toAdd.getSlice().getSliceCollumns().getCollumn().add(col);
	}

	public void removeSliceCollumn(String dimID, String colID)
	{
		Dimension toAdd = this.getDimension(dimID);
		int index = 0;
		Slice.SliceCollumns slices = toAdd.getSlice().getSliceCollumns();
		if(slices.getCollumn().size() == 1)
		{
			toAdd.setSlice(null);
			return;
		}

		for(Collumn col : toAdd.getSlice().getSliceCollumns().getCollumn())
		{
			if(col.getId().equals(colID))
			{
				toAdd.getSlice().getSliceCollumns().getCollumn().remove(index);
				break;
			}

			index++;
		}
	}

	public List<String> upperHierarchy(String levelID, String dimID, int occur)
	{
		List<String> toRet = new LinkedList<String>();
		xml.meta.Dimension dim = this.getMetaDimension(dimID);
		int count = 0;
		for(xml.meta.Dimension.Hierarchies.Hierarchy hierarchy: dim.getHierarchies().getHierarchy())
		{
			if(count == occur && levelBelongs(levelID, hierarchy))
			{
				for(xml.meta.Dimension.Hierarchies.Hierarchy.Level lvl :hierarchy.getLevel())
				{
					if(levelID.equals(lvl.getLevelID()))
						break;
					else
					{
						xml.meta.Level theLevel = this.getLevel(dim, lvl.getLevelID());
						toRet.add(theLevel.getDisplayName() +"/" +theLevel.getLevelID());
					}
				}
				break;
			}
			else
				count++;
		}

		return toRet;
	}
	
	private boolean levelBelongs(String levelID, xml.meta.Dimension.Hierarchies.Hierarchy hierarchy)
	{
		boolean belongs = false;
		
		for(xml.meta.Dimension.Hierarchies.Hierarchy.Level lvl :hierarchy.getLevel())
		{
			if(levelID.equals(lvl.getLevelID()))
			{
				belongs = true;
				break;
			}
		}
		
		return belongs;
	}

	public List<String> lowerHierarchy(String levelID, String dimID, int occur)
	{
		List<String> toRet = new LinkedList<String>();
		xml.meta.Dimension dim = this.getMetaDimension(dimID);
		boolean lower = false;
		int count = 0;
		for(xml.meta.Dimension.Hierarchies.Hierarchy hierarchy: dim.getHierarchies().getHierarchy())
		{
			if(count == occur)
			{
				for(xml.meta.Dimension.Hierarchies.Hierarchy.Level lvl :hierarchy.getLevel())
				{
					if(lower)
					{	
						xml.meta.Level theLevel = this.getLevel(dim, lvl.getLevelID());
						toRet.add(theLevel.getDisplayName() +"/" +theLevel.getLevelID());
					}

					if(levelID.equals(lvl.getLevelID()))
						lower = true;
				}
				break;
			}
			else
				count++;
		}

		return toRet;
	}

	public int numHierarchies(String levelID, String dimID)
	{
		xml.meta.Dimension dim = this.getMetaDimension(dimID);

		return dim.getHierarchies().getHierarchy().size();
	}

	public void addSliceCollumnValue(String dimID, String colID, String value)
	{
		Collumn col = this.getCollumn(this.getDimension(dimID), colID);
		col.getByValues().getValue().add(value);
	}

	private Collumn getCollumn(Dimension d, String colID)
	{
		Collumn toRet = null;
		for(Collumn col: d.getSlice().getSliceCollumns().getCollumn())
		{
			if(col.getId().equals(colID))
			{
				toRet = col;
				break;
			}
		}

		return toRet;
	}

	private void addCube(String cubeID)
	{
		query.getCube().setCubeID(cubeID);
	}

	public void addFact(String factID, String op)
	{
		Fact fact = clientFactory.createSelectedFactsFact();
		fact.setAggrOperation(op);
		fact.setFactID(factID);
		query.getCube().getSelectedFacts().getFact().add(fact);
	}

	private Fact getFact(String factID)
	{
		Fact toRet = null;

		for(Fact fact : query.getCube().getSelectedFacts().getFact())
		{
			if(fact.getFactID().equals(factID))
			{
				toRet = fact;
				break;
			}
		}

		return toRet;
	}

	public void addFactFilterBefore(String factID, String op, String value)
	{
		Fact toAdd = this.getFact(factID);
		System.out.println("ADDING FILTER BEFORE: "+ toAdd.getFactID());
		Filter filter = clientFactory.createFilter();
		filter.setOp(op);
		filter.setValue(value);
		toAdd.setFilterBefore(filter);
	}

	public void removeFactFilterBefore(String factID)
	{
		Fact toRemove = this.getFact(factID);
		toRemove.setFilterBefore(null);
	}

	public void addFactFilterAfter(String factID, String op, String value) 
	{
		Fact toAdd = this.getFact(factID);
		System.out.println("ADDING FILTER ASFTER: "+ toAdd.getFactID());
		Filter filter = clientFactory.createFilter();
		filter.setOp(op);
		filter.setValue(value);
		toAdd.setFilterAfter(filter);
	}

	public void removeFactFilterAfter(String factID)
	{
		Fact toRemove = this.getFact(factID);
		toRemove.setFilterAfter(null);
	}

	private MetaModel getMetaModel()
	{
		MetaModel toRet = null;
		try {
			URI uri = new URI("http://"+SERVER_URL+":8080/DWss/rest/metamodel");
			URL url = uri.toURL();
			InputStream is = url.openStream();

			JAXBContext jc = JAXBContext.newInstance("xml.meta");
			Unmarshaller u = jc.createUnmarshaller();

			toRet = (MetaModel) ((JAXBElement)u.unmarshal(is)).getValue();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("failed to retrieve meta model from server");

			try {
				URI uri = new URI("file:///C:/Download/foodmart_meta.xml");
				URL url = uri.toURL();
				InputStream is = url.openStream();

				JAXBContext jc = JAXBContext.newInstance("xml.meta");
				Unmarshaller u = jc.createUnmarshaller();

				toRet = (MetaModel) ((JAXBElement)u.unmarshal(is)).getValue();

			} catch (Exception e1) {
				System.out.println("failed to retrieve meta model from server");


			}
		}
		return toRet;
	}

	public List<xml.meta.Dimension> getDimensions()
	{
		Iterator<String> ids = this.getDimensionIds();
		List<xml.meta.Dimension> toRet = new LinkedList<xml.meta.Dimension>();
		while(ids.hasNext())
		{
			String id = ids.next();
			for(xml.meta.Dimension dim : metaModel.getDimensions().getDimension())
			{
				if(id.equals(dim.getDimID()))
				{
					toRet.add(dim);
					break;
				}
			}
		}

		return toRet;
	}

	public List<xml.meta.Dimension> getTESTDims()
	{
		xml.meta.ObjectFactory fact = new xml.meta.ObjectFactory();
		List<xml.meta.Dimension> toRet = new LinkedList<xml.meta.Dimension>();
		for(int i = 0; i < 15; i++)
		{
			xml.meta.Dimension dim = fact.createDimension();
			dim.setDimID("STRING"+i+",");
			dim.setDisplayName("DIMENSION"+i);
			dim.setLevels(fact.createDimensionLevels());
			xml.meta.Level lvl = fact.createLevel();
			lvl.setLevelID("1");
			lvl.setDisplayName("LEVEL TWO @ DIMENSION "+dim.getDisplayName());
			lvl.setAttributes(fact.createLevelAttributes());
			xml.meta.LevelAttribute attr = fact.createLevelAttribute();
			attr.setAttr("ATTR/"+dim.getDimID()+"/1");
			lvl.getAttributes().getAttribute().add(attr);

			xml.meta.Level lvl2 = fact.createLevel();
			lvl2.setLevelID("2");
			lvl2.setDisplayName("LEVEL ONE @ DIMENSION "+dim.getDisplayName());
			lvl2.setAttributes(fact.createLevelAttributes());
			xml.meta.LevelAttribute attr2 = fact.createLevelAttribute();
			attr2.setAttr("ATTR/"+dim.getDimID()+"/2");

			lvl2.getAttributes().getAttribute().add(attr);

			dim.getLevels().getLevel().add(lvl);
			dim.getLevels().getLevel().add(lvl2);
			toRet.add(dim);
		}
		return toRet;
	}

	private Iterator<String> getDimensionIds()
	{
		Map<String,String> ids = new HashMap<String,String>();
		String cubeID = query.getCube().getCubeID();
		xml.meta.Cube toGet = this.getCube(cubeID);

		for(Fact fact : query.getCube().getSelectedFacts().getFact())
		{
			for(Object a : toGet.getFactTable().getFactOrDerivedFact())
			{
				if(a instanceof xml.meta.Fact)
				{
					xml.meta.Fact theFact = (xml.meta.Fact) a;
					if(theFact.getFactID().equals(fact.getFactID()))
						for(xml.meta.Fact.AdditionDimensions.Dimension dim : theFact.getAdditionDimensions().getDimension())
							if(!ids.containsKey(dim.getDimID()))
								ids.put(dim.getDimID(), dim.getDimID());
				}

				if(a instanceof xml.meta.Derived)
				{
					xml.meta.Derived theFact = (xml.meta.Derived) a;
					if(theFact.getFactID().equals(fact.getFactID()))
						for(xml.meta.Derived.AdditionDimensions.Dimension dim : theFact.getAdditionDimensions().getDimension())
							if(!ids.containsKey(dim.getDimID()))
								ids.put(dim.getDimID(), dim.getDimID());
				}
			}
		}

		return ids.values().iterator();
	}

	private xml.meta.Cube getCube(String cubeID)
	{
		xml.meta.Cube toRet = null;

		for(xml.meta.Cube cube : metaModel.getCubes().getCube())
		{
			if(cube.getFactTable().getTableID().equals(cubeID))
			{
				toRet = cube;
				break;
			}
		}

		return toRet;
	}

	public List<xml.meta.Cube> getCubes()
	{
		return metaModel.getCubes().getCube();
	}

	public void chooseCube(String cubeID)
	{
		this.addCube(cubeID);
		this.resetDimensions();
	}

	public List<Object> getMeasures()
	{	
		xml.meta.Cube toGet  = this.getCube(query.getCube().getCubeID());

		return toGet.getFactTable().getFactOrDerivedFact();

	}

	public Response getCurrent()
	{
		return this.current;
	}

	public void executeQuery()
	{
		System.out.println(this.returnXML());
				try
				{
					HttpClient client = buildHttpClient();
					HttpPost request = new HttpPost("http://"+SERVER_URL+":8080/DWss/rest/doQuery");
					request.addHeader("Content-Type", "text/xml");
					request.addHeader("Accept","text/xml");
					ByteArrayEntity postData = new ByteArrayEntity(this.returnXML().getBytes("UTF8"));
					request.setEntity(postData);
		
					HttpResponse response = client.execute(request);
		
					if (response == null)
						return;
		
		
					JAXBContext jc = JAXBContext.newInstance("xml.server");
		
					Unmarshaller u = jc.createUnmarshaller();
		
					this.current = (Response) u.unmarshal(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		
					for(Head head : current.getHeaders().getHead())
					{
						if(head.getType().equals("level"))
						{
							String id = head.getID();
							String[] idSplit = id.split("/");
							String dimID = idSplit[1];
							String levelID = idSplit[0];
		
							xml.meta.Dimension dimension = this.getMetaDimension(dimID);
							for(xml.meta.Level level : dimension.getLevels().getLevel())
							{
								if(level.getLevelID().equals(levelID))
								{
									head.setValue(level.getDisplayName());
									int type = this.getResponseType(dimID, level);
									
									if(type == 2)
										head.setType("number");
									else if(type == 1)
										head.setType("date");
									else
										head.setType("text");
								}
							}
						}
						else
						{
							String id = head.getID();
		
							xml.meta.Fact fact = this.getMetaFact(query.getCube().getCubeID(), id);
							
							head.setValue(fact.getDisplayName());
							head.setType("number");
						}
					}
		
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

	}

	private HttpClient buildHttpClient()
	{
		int TIMEOUT_MILLISEC = 30000; // = 30 seconds
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

		return new DefaultHttpClient(httpParams);
	}


	public void saveQuery(String name)
	{
		try
		{
			name = name.replace(" ", "%20");
			HttpClient client = buildHttpClient();
			HttpPost request = new HttpPost("http://"+SERVER_URL+":8080/DWss/rest/report?name="+name);
			request.addHeader("Content-Type", "text/xml");
			request.addHeader("Accept","text/xml");
			ByteArrayEntity postData = new ByteArrayEntity(this.returnXML().getBytes("UTF8"));
			request.setEntity(postData);	
			client.execute(request);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}


	public void loadReport(String name)
	{	
		try {
			URI uri = new URI("http://"+SERVER_URL+":8080/DWss/rest/report?name="+name);
			URL url = uri.toURL();

			InputStream is = url.openStream();

			JAXBContext jc = JAXBContext.newInstance("xml.client");
			Unmarshaller u = jc.createUnmarshaller();

			query = (Query) u.unmarshal(is);

		} catch (Exception e) {
			System.out.println("failed to retrieve the report");
		}


	}

	public void resetFacts()
	{
		this.query.getCube().setSelectedFacts(this.clientFactory.createSelectedFacts());
		this.query.setDimensions(this.clientFactory.createQueryDimensions());
	}

	public void resetSlice()
	{
		for(Dimension dim : query.getDimensions().getDimension())
			dim.setSlice(clientFactory.createSlice());
	}

	public void removeCollumn(String dimID, String value)
	{
		int i = 0;
		Dimension toRemove = this.getDimension(dimID);

		for(String col : toRemove.getSelectedCollumns().getSelectedCollumn())
		{
			if(col.equals(value))
			{
				toRemove.getSelectedCollumns().getSelectedCollumn().remove(i);
				break;
			}
			i++;
		}
	}

	@Override
	public String hello() {
		return "Hello World";
	}

	@Override
	public List<String> register(String user, String pw) {
		if(!users.containsKey(user))
			users.put(user, pw);

		List<String> pws = new LinkedList<String>();

		for(String a : users.values())
			pws.add(a);

		return pws;
	}

	private String returnXML()
	{
		String output = "";
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext jc = JAXBContext.newInstance("xml.client");
			jc.createMarshaller().marshal(query, baos);
			output = new String(baos.toString());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;

	}

	@Override
	public List<xml.meta.Fact> getTESTFacts() {
		xml.meta.ObjectFactory fact = new xml.meta.ObjectFactory();
		List<xml.meta.Fact> toRet = new LinkedList<xml.meta.Fact>();

		for(int i = 0; i < 3; i++)
		{
			xml.meta.Fact fac = fact.createFact();
			fac.setFactID(""+i);
			fac.setDisplayName("Fact "+i);
			Aggregation aggr = fact.createFactAggregation();

			aggr.getOperation().add("SUM");
			aggr.getOperation().add("AVG");
			aggr.getOperation().add("MAX");
			aggr.getOperation().add("MIN");
			fac.setAggregation(aggr);
			toRet.add(fac);
		}
		return toRet;
	}

	public List<xml.meta.Cube> getTESTCubes()
	{
		List<xml.meta.Cube> toRet = new LinkedList<xml.meta.Cube>();
		xml.meta.ObjectFactory factory = new xml.meta.ObjectFactory();

		xml.meta.Cube cube = factory.createCube();
		xml.meta.Cube.FactTable fact = factory.createCubeFactTable();
		fact.setDisplayName("Sales fact 1997");
		fact.setTableID("1");
		cube.setFactTable(fact);
		toRet.add(cube);

		xml.meta.Cube cube2 = factory.createCube();
		xml.meta.Cube.FactTable fact2 = factory.createCubeFactTable();
		fact2.setDisplayName("Sales fact 1998");
		fact2.setTableID("2");
		cube2.setFactTable(fact2);
		toRet.add(cube2);

		xml.meta.Cube cube3 = factory.createCube();
		xml.meta.Cube.FactTable fact3 = factory.createCubeFactTable();
		fact3.setDisplayName("Sales fact 1999");
		fact3.setTableID("3");
		cube3.setFactTable(fact3);	
		toRet.add(cube3);


		return toRet;
	}

	public xml.meta.Dimension getMetaDimension(String dimID)
	{
		for(xml.meta.Dimension dim : this.metaModel.getDimensions().getDimension())
		{
			if(dim.getDimID().equals(dimID))
				return dim;
		}

		return null;
	}

	public List<String> getTESTReports()
	{
		List<String> toRet = new LinkedList<String>();

		for(int i = 0; i <3; i++)
			toRet.add("Report "+i);

		return toRet;
	}

	private xml.meta.Table getTable(String tableID)
	{
		for(xml.meta.Table table: metaModel.getTables().getTable())
			if(table.getTableID().equals(tableID))
				return table;

		return null;
	}

	@Override
	public int getType(String dim, String attribute) {

		int type = -1;

		xml.meta.Dimension dimension = this.getMetaDimension(dim);
		String tableID = dimension.getTableID();
		xml.meta.Table table = this.getTable(tableID);
		for(xml.meta.TableAttribute attr : table.getAttributes().getAttribute())
		{
			if(attr.getAttrID().equals(attribute))
			{
				if(attr.getType().equals("number"))
					type = 2;
				if(attr.getType().equals("date"))
					type = 1;
				if(attr.getType().equals("text"))
					type = 0;
				if(attr.getType().equals("unique"))
					type = 4;
				if(attr.getType().equals("boolean"))
					type = 3;
			}
		}
		return type;

	}

	private int getForeignType(String tableID, String attribute)
	{
		xml.meta.Table table = this.getTable(tableID);
		int type = -1;
		for(xml.meta.TableAttribute attr : table.getAttributes().getAttribute())
		{
			if(attr.getAttrID().equals(attribute))
			{
				if(attr.getType().equals("number"))
					type = 2;
				if(attr.getType().equals("date"))
					type = 1;
				if(attr.getType().equals("text"))
					type = 0;
				if(attr.getType().equals("unique"))
					type = 4;
				if(attr.getType().equals("boolean"))
					type = 3;
			}
		}
		return type;
	}

	public List<String> DimensionListOfValues(String dimID, String levelID)
	{
		List<String> toRet = new LinkedList<String>();
		ListOfValues lov = null;
		try {
			URI uri = new URI("http://"+SERVER_URL+":8080/DWss/rest/lov?dim="+dimID+"&level="+levelID);
			URL url = uri.toURL();
			InputStream is = url.openStream();

			JAXBContext jc = JAXBContext.newInstance("xml.lov");
			Unmarshaller u = jc.createUnmarshaller();

			lov = (ListOfValues) (u.unmarshal(is));
			toRet = lov.getValue();

		} catch (Exception e) {
			System.out.println("failed to retrieve meta model");
			e.printStackTrace();
		}

		return toRet;
	}

	public void setDim(String dimID)
	{
		this.dimID = dimID;
	}

	public void setLevel(String levelID)
	{
		this.levelID = levelID;
	}

	public String getDim()
	{
		return this.dimID;
	}

	public String getLevel()
	{
		return this.levelID;
	}

	public List<ROLAP.Slice> getQueryDimensions()
	{
		List<ROLAP.Slice> slices = new LinkedList<ROLAP.Slice>();
		for(xml.client.Dimension dim : this.query.getDimensions().getDimension())
		{
			for(String levelID : dim.getSelectedCollumns().getSelectedCollumn())
			{
				xml.meta.Level lvl = this.getLevel(this.getMetaDimension(dim.getDimID()), levelID);
				int type = this.getResponseType(dim.getDimID(), lvl);
				if(type < 4)
				{
					ROLAP.Slice toAdd = new ROLAP.Slice(dim.getDimID(),levelID,type,lvl.getDisplayName());
					slices.add(toAdd);
				}
			}	
		}
		return slices;
	}

	private int getResponseType(String dimID, xml.meta.Level lvl)
	{
		int type = -1;
		for(xml.meta.LevelAttribute attr : lvl.getAttributes().getAttribute())
		{
			if(attr.getId().equals(lvl.getPrimary()))
			{
				if(attr.getAttr() == null)
				{
					xml.meta.Snowflaked snowflake = attr.getSnowflaked();
					while(snowflake.getSnowflaked() != null)
					{
						snowflake = snowflake.getSnowflaked();
					}
					type = this.getForeignType(snowflake.getFtable(), snowflake.getAttr());

				}
				else
				{
					type = this.getType(dimID, attr.getAttr());
				}
			}
		}
		return type;
	}

	public xml.meta.Fact getMetaFact(String cubeID, String factID)
	{
		xml.meta.Cube toGet = this.getCube(cubeID);
		xml.meta.Fact fact  = null;
		for(Object obj : toGet.getFactTable().getFactOrDerivedFact())
		{
			if(obj instanceof xml.meta.Fact)
			{
				xml.meta.Fact objFact = (xml.meta.Fact) obj;
				if(objFact.getFactID().equals(factID))
					fact = objFact;
			}

			if(obj instanceof xml.meta.Derived)
			{
				xml.meta.Derived objDerived = (xml.meta.Derived) obj;
				xml.meta.ObjectFactory factory = new xml.meta.ObjectFactory();

				if(objDerived.getFactID().equals(factID))
				{
					fact = factory.createFact();
					fact.setFactID(objDerived.getFactID());
					fact.setDisplayName(objDerived.getDisplayName());
				}

			}
		}
		return fact;

	}

	public List<ROLAP.Filter> getSelectedFacts()
	{
		Cube cube = this.query.getCube();
		List<ROLAP.Filter> toRet = new LinkedList<ROLAP.Filter>();
		for(SelectedFacts.Fact fact : cube.getSelectedFacts().getFact())
		{
			xml.meta.Fact f = this.getMetaFact(cube.getCubeID(), fact.getFactID());
			ROLAP.Filter filter = new ROLAP.Filter(f.getFactID(),f.getDisplayName());
			toRet.add(filter);
		}
		return toRet;
	}

	public xml.meta.Level getLevel(xml.meta.Dimension dim,String levelID)
	{
		for(xml.meta.Level lvl: dim.getLevels().getLevel())
		{
			if(lvl.getLevelID().equals(levelID))
				return lvl;
		}
		return null;
	}

	public String getFactID() {
		return factID;
	}

	public void setFactID(String factID) {
		this.factID = factID;
	}

	public int getFactOccur() {
		return factOccur;
	}
	
	public Query getQuery()
	{
		return query;
	}

	public void setFactOccur(int factOccur) {
		this.factOccur = factOccur;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public List<String> getMinMax(String factID) {
		List<String> toRet = new LinkedList<String>();
		ListOfValues lov = null;
		try {
			URI uri = new URI("http://"+SERVER_URL+":8080/DWss/rest/minmax?table="+query.getCube().getCubeID()+"&fact="+factID);
			URL url = uri.toURL();
			InputStream is = url.openStream();

			JAXBContext jc = JAXBContext.newInstance("xml.lov");
			Unmarshaller u = jc.createUnmarshaller();

			lov = (ListOfValues) (u.unmarshal(is));
			toRet = lov.getValue();

		} catch (Exception e) {
			toRet.add("0");
			toRet.add("3200");
		}

		return toRet;
	}

	public List<String> getReports()
	{
		ListOfValues lov = null;
		try {
			URI uri = new URI("http://"+SERVER_URL+":8080/DWss/rest/lor");
			URL url = uri.toURL();
			InputStream is = url.openStream();

			JAXBContext jc = JAXBContext.newInstance("xml.lov");
			Unmarshaller u = jc.createUnmarshaller();

			lov = (ListOfValues) (u.unmarshal(is));
			currentReports = lov.getValue();

		} catch (Exception e) {
			System.out.println("failed to retrieve meta model");
			e.printStackTrace();
		}

		return currentReports;
	}

	public List<String> getCurrentReports()
	{
		return currentReports;
	}

	public String getResponse()
	{
		String output = "";
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JAXBContext jc = JAXBContext.newInstance("xml.server");
			jc.createMarshaller().marshal(current, baos);
			output = new String(baos.toString());
			System.out.println(output);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return output;

	}
	
	public void doInfo()
	{
		query.getCube().getSelectedFacts();
	}


	public static void main(String[] args) throws InterruptedException
	{
		int port = 8080;
		String webappdir = "webapp";
		String datadir = "data";
		String serverIP = "";

		if(args.length>1)
			serverIP = args[0];
		else
			serverIP = "localhost";

		ROLAPClient proc = new ROLAPClient(port,datadir,webappdir,serverIP);
		proc.doit();

		//		for(;;){
		//
		//			System.out.println(proc.returnXML());
		//			Thread.sleep(15000);
		//		}
	}




}
