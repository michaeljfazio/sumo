/*
 *
 */
package org.geoimage.viewer.core.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.geoimage.analysis.BlackBorderAnalysis;
import org.geoimage.analysis.MaskGeometries;
import org.geoimage.analysis.VDSAnalysis;
import org.geoimage.def.GeoTransform;
import org.geoimage.def.SarImageReader;
import org.geoimage.impl.cosmo.AbstractCosmoSkymedImage;
import org.geoimage.impl.s1.Sentinel1;
import org.geoimage.viewer.core.GeometryImage;
import org.geoimage.viewer.core.analysisproc.AnalysisProcess;
import org.geoimage.viewer.core.analysisproc.VDSAnalysisProcessListener;
import org.geoimage.viewer.core.api.ilayer.ILayer;
import org.geoimage.viewer.core.api.ilayer.IMask;
import org.geoimage.viewer.core.io.GenericCSVIO;
import org.geoimage.viewer.core.io.SimpleShapefile;
import org.geoimage.viewer.core.io.SumoXMLWriter;
import org.geoimage.viewer.core.layers.visualization.vectors.ComplexEditVDSVectorLayer;
import org.jrc.sumo.configuration.PlatformConfiguration;
import org.jrc.sumo.util.Constant;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;


class AnalysisParams {
	//HH HV VH VV
	public float[] thresholdArrayValues={0,0,0,0};
	public String pathImg[]=null;
	public String shapeFile="";

	public String iceShapeFile="";
	/*public String iceRepoPath="";
	public String icePatternName="";
	public boolean icePatternRemote=false;*/

	public String outputFolder="";
	public String xmlOutputFolder="";
	public float enl=0;
	public int buffer=0;
	public String epsg="EPSG:4326";
	public Date startDate;
	public int maxDetections=0;
}

public abstract class AbstractBatchAnalysis implements VDSAnalysisProcessListener{
	private static org.slf4j.Logger logger=LoggerFactory.getLogger(AbstractBatchAnalysis.class);
	protected AnalysisParams params=null;
	//private  List<ComplexEditVDSVectorLayer>layerResults=null;
	private VDSAnalysis analysis;
	private int realTileSizeX=0;
	private int realTileSizeY=0;

	private int runVersionNumber=1;
	private String runVersion="BATCH";

	public AbstractBatchAnalysis(AnalysisParams analysisParams){
		params= analysisParams;
	}


	protected abstract void runAnalysis();
	/*protected GeoImageReader getCurrentReader(){
		return currentReader;
	}*/

	/**
	 *
	 */
	protected void runProcess(){
		runAnalysis();
	}


	public int getRunVersionNumber() {
		return runVersionNumber;
	}


	public void setRunVersionNumber(int runVersionNumber) {
		this.runVersionNumber = runVersionNumber;
	}


	public String getRunVersion() {
		return runVersion;
	}


	public void setRunVersion(String runVersion) {
		this.runVersion = runVersion;
	}


	/**
	 *
	 * @param reader
	 * @return
	 */
	protected GeometryImage readShapeFile(String shpFilePath,Polygon imgBox,GeoTransform gt){
		GeometryImage gl=null;
	    try {
            gl = SimpleShapefile.createIntersectedLayer(new File(shpFilePath),imgBox,gt);
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
        }
        return gl;
	}


	 /**
     *
     * @param ENL
     * @param analysis
     * @param bufferedMask
     * @param thresholds
     * @return
     */
    public AnalysisProcess prepareBatchAnalysis(SarImageReader reader,IMask mask,IMask iceMask,AnalysisParams params){
    	int tileSize = (int)(Constant.TILESIZE / reader.getPixelsize()[0]);
        if(tileSize < Constant.TILESIZEPIXELS) tileSize = Constant.TILESIZEPIXELS;

        int horizontalTilesImage = reader.getWidth() / tileSize;
        int verticalTilesImage= reader.getHeight()/ tileSize;

     // the real size of tiles
        realTileSizeX = reader.getWidth() / horizontalTilesImage;
        realTileSizeY = reader.getHeight() / verticalTilesImage;


        java.util.HashMap<String,Float> thresholdsMap = new java.util.HashMap<>();
        thresholdsMap.put("HH", params.thresholdArrayValues[0]);
        thresholdsMap.put("HV", params.thresholdArrayValues[1]);
        thresholdsMap.put("VH", params.thresholdArrayValues[2]);
        thresholdsMap.put("VV", params.thresholdArrayValues[3]);

        MaskGeometries mg=null;
        if(mask!=null)
        	mg=new MaskGeometries(mask.getName(),mask.getGeometries(),mask.getName());

        MaskGeometries icemg=null;
        if(iceMask!=null)
        	icemg=new MaskGeometries(iceMask.getName(),iceMask.getGeometries(),iceMask.getName());


        analysis = new VDSAnalysis(reader,
        		mg,
        		icemg,
        		params.enl,
        		thresholdsMap,realTileSizeX,realTileSizeY,
        		horizontalTilesImage,verticalTilesImage
        		);



    	BlackBorderAnalysis blackBorderAnalysis=null;
        if(reader instanceof Sentinel1){
       	 	if(analysis.getCoastMask()!=null)
       	 		blackBorderAnalysis= new BlackBorderAnalysis(reader,realTileSizeX,
       	 			realTileSizeY,analysis.getCoastMask().getMaskGeometries());
       	 	else
       		    blackBorderAnalysis= new BlackBorderAnalysis(reader,realTileSizeX,
       		    		realTileSizeY,null);
        }

    	return new AnalysisProcess(reader,params.enl,analysis, params.buffer,0,blackBorderAnalysis);

    }



    /**
     *
     * @param imageName
     * @param reader
     */
	protected void saveResults(String imageName,SarImageReader reader,List<ComplexEditVDSVectorLayer>layerResults){
		if(layerResults!=null){
			String outfolder=new StringBuilder(params.outputFolder)
   					.append(File.separator)
   					.append(imageName).toString();

		   //create folder if not exist
		   File folder=new File(outfolder);
		   if(!folder.exists())
			   folder.mkdirs();

		   File xmlOutFolder=null;

		    xmlOutFolder=new File(params.xmlOutputFolder);
		    if(!xmlOutFolder.exists())
		    	xmlOutFolder.mkdirs();

    	   for(ComplexEditVDSVectorLayer l:layerResults){
    		   StringBuilder outfileName=new StringBuilder(outfolder).append(File.separator)
    				            .append(reader.getImId()).append("_");
    		   if(l.getBand().equalsIgnoreCase("Merged")){
    			   outfileName.append(l.getBand());
    		   }else{
    			   outfileName.append(reader.getBandName(Integer.valueOf(l.getBand())));
    		   }



				if(reader.isContainsMultipleImage() && reader instanceof AbstractCosmoSkymedImage){
					outfileName=outfileName.append("_").append(((AbstractCosmoSkymedImage)reader).getGroup());
				}

    		   l.save(outfileName.toString()+"_OLD",ComplexEditVDSVectorLayer.OPT_EXPORT_XML_SUMO_OLD,params.epsg,reader);


    		   File outFile=new File(outfileName.toString()+".xml");
    		   SumoXMLWriter.saveNewXML(outFile,l,
    				   params.epsg,reader,params.thresholdArrayValues,
    				   params.buffer,params.enl,params.shapeFile,runVersion,runVersionNumber);

    		   //copy the xml in the folder for ingestion => copy the merged for multiple band or the single xml
    		   try{
    			   if(params.xmlOutputFolder==null||params.xmlOutputFolder.equals("")){
    				   params.xmlOutputFolder=folder.getParent();
    			   }
	    		   if(layerResults.size()==1||l.getBand().equals("Merged")){
	    			   FileUtils.copyFile(outFile,new File( params.xmlOutputFolder+File.separator+reader.getImId()+".xml"));
	    		   }
    	   	   }catch(Exception e){
    	   		   logger.error("File xml not saved in the xmloutputfolder:",e);
    	   	   }
/* moved out of the loop
    		   //save the bound box as shape file
    		   try{
    			   String bbox=outfolder+File.separator+"bbox.shp";
    			   List<Geometry> ggBbox=new ArrayList<Geometry>();

    			   try {
    	    		  ggBbox.add(reader.getBbox(PlatformConfiguration.getConfigurationInstance().getLandMaskMargin(0)));
    				  SimpleShapefile.exportGeometriesToShapeFile(ggBbox, new File(bbox),"Polygon",reader.getGeoTransform(),null);
    			   } catch (Exception e) {
    				  logger.error("Problem exporting the bounding box:"+e.getLocalizedMessage(),e);
    			   }

    			   String bboxcsv=params.outputFolder+File.separator+"bbox.csv";
    			   GenericCSVIO.geomCsv(new File(bboxcsv),ggBbox,null,imageName,true);
    		   }catch(Exception e ){
    			   logger.error("Problem saving bbox in csv:"+imageName,e);
    		   }*/

    		   //save targets as shape file
    		   try{
    			   String targets=outfileName.append(".shp").toString();
    			  // l.save(targets, ISave.OPT_EXPORT_SHP, "EPSG:4326");
    			   SimpleShapefile.exportGeometriesToShapeFile(l.getGeometriclayer().getGeometries(),
    					   new File(targets),"Point",reader.getGeoTransform(),null);
    		   } catch (Exception e) {
 				  logger.error("Problem exporting the bounding box:"+e.getLocalizedMessage(),e);
 			   }

    		   if(l.getBand().equals("Merged")||layerResults.size()==1){
	    		   try{
	    			   String targetscsv=params.outputFolder+File.separator+"targets.csv";
	    			   List<Geometry> targets=new ArrayList<Geometry>(l.getGeometriclayer().getGeometries());
	    			   List<Geometry> ambi=new ArrayList<>();

	    			   if(l.getGeometriesByTag(ComplexEditVDSVectorLayer.AZIMUTH_AMBIGUITY_TAG)!=null)
	    				   ambi.addAll(l.getGeometriesByTag(ComplexEditVDSVectorLayer.AZIMUTH_AMBIGUITY_TAG).getGeometries());

	    			   if(l.getGeometriesByTag(ComplexEditVDSVectorLayer.ARTEFACTS_AMBIGUITY_TAG)!=null)
		    			   ambi.addAll(l.getGeometriesByTag(ComplexEditVDSVectorLayer.ARTEFACTS_AMBIGUITY_TAG).getGeometries());

		    		   //remove ambiguities
	    			   List<Geometry>toRemove=new ArrayList<>();
	    			   if(!ambi.isEmpty()){
		    			   for(Geometry geom:targets){
			    			   if(ambi.contains(geom)){
			    				   toRemove.add(geom);
			    			   }
		    			   }
	    			   }
	    			   targets.removeAll(toRemove);
	    			   GenericCSVIO.geomCsv(new File(targetscsv),targets,reader.getGeoTransform(),imageName,true);
	    		   }catch(Exception e ){
	    			   logger.error("Problem saving targets in csv:"+imageName,e);
	    		   }
    		   }
    	   }
		   //save the bound box as shape file
		   try{
			   String bbox=outfolder+File.separator+"bbox.shp";
			   List<Geometry> ggBbox=new ArrayList<Geometry>();

			   try {
	    		  ggBbox.add(reader.getBbox(PlatformConfiguration.getConfigurationInstance().getLandMaskMargin(0)));
				  SimpleShapefile.exportGeometriesToShapeFile(ggBbox, new File(bbox),"Polygon",null,null);
			   } catch (Exception e) {
				  logger.error("Problem exporting the bounding box:"+e.getLocalizedMessage(),e);
			   }

			   String bboxcsv=params.outputFolder+File.separator+"bbox.csv";
			   GenericCSVIO.geomCsv(new File(bboxcsv),ggBbox,null,imageName,true);
		   }catch(Exception e ){
			   logger.error("Problem saving bbox in csv:"+imageName,e);
		   }
		}
	}


	@Override
	public void startAnalysis(String imgName,String msg) {
		logger.info("Start Analysis:"+imgName+"  "+msg);

	}
	@Override
	public void performVDSAnalysis(String message, int numSteps) {
		logger.info(message);
	}
	@Override
	public void nextVDSAnalysisStep(String message,int step,int steps) {
		logger.info(step+"/"+steps);
	}
	@Override
	public void startBlackBorederAnalysis(String message) {
		logger.info(message);
	}
	@Override
	public void startAnalysisBand(String message) {
		logger.info(message);
	}
	@Override
	public void calcAzimuthAmbiguity(String message) {
		logger.info(message);
	}
	@Override
	public void agglomerating(String message) {
		logger.info(message);
	}
	@Override
	public void endAnalysis(String imgName) {
		logger.info("Start Analysis:"+imgName);

	}
	@Override
	public void layerReady(ILayer layer) {
		// TODO Auto-generated method stub

	}
}
