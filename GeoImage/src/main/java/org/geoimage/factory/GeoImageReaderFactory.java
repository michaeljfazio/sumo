/*
 * 
 */
package org.geoimage.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geoimage.def.GeoImageReader;
import org.geoimage.impl.alos.AlosGeoTiff;
import org.geoimage.impl.alos.GDALAlosCeos;
import org.geoimage.impl.alos.prop.TiffAlosProperties;
import org.geoimage.impl.cosmo.AbstractCosmoSkymedImage;
import org.geoimage.impl.cosmo.CosmoSkyFactory;
import org.geoimage.impl.envi.EnvisatImage_SLC;
import org.geoimage.impl.radarsat.Radarsat1Image;
import org.geoimage.impl.radarsat.Radarsat2Image;
import org.geoimage.impl.radarsat.Radarsat2ImageGDAL;
import org.geoimage.impl.radarsat.Radarsat2Image_SLC;
import org.geoimage.impl.radarsat.Radarsat2Image_SLCGDAL;
import org.geoimage.impl.s1.GDALSentinel1;
import org.geoimage.impl.s1.Sentinel1;
import org.geoimage.impl.s1.Sentinel1Factory;
import org.geoimage.impl.s1.Sentinel1GRD;
import org.geoimage.impl.s1.Sentinel1SLC;
import org.geoimage.impl.tsar.TerrasarXImage;
import org.geoimage.impl.tsar.TerrasarXImage_SLC;

import it.geosolutions.imageio.gdalframework.GDALUtilities;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;


public class GeoImageReaderFactory {

    public final static List<String> FORMATS = new Vector<String>();
    private static Logger logger=LogManager.getLogger(GeoImageReaderFactory.class);
    
    static {
    	try{
    		String[] temp = java.util.ResourceBundle.getBundle("geoimage").getString("formats").split(",");
    		for (String f : temp) {
    			addFormat(f);
    		}
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    	}
    }

    /**
     * 
     * @param gir
     * @return
     */
    public static GeoImageReader cloneReader(GeoImageReader gir){
    	//TODO : add "if" for other reader with multiple images (cosmoskymed)
    	GeoImageReader clone=null;
    	if(gir instanceof Sentinel1){
    		if(gir instanceof Sentinel1GRD)
    			clone=new Sentinel1GRD(((Sentinel1GRD) gir).getSwath(),((Sentinel1GRD)gir).getManifestFile(),((Sentinel1GRD)gir).getGeolocationAlgorithm());
    		else if(gir instanceof GDALSentinel1)
    			clone=new GDALSentinel1(((GDALSentinel1) gir).getSwath(),((GDALSentinel1)gir).getManifestFile(),((GDALSentinel1)gir).getGeolocationAlgorithm());
    		else
    			clone=new Sentinel1SLC(((Sentinel1SLC) gir).getSwath(),((Sentinel1SLC)gir).getManifestFile(),((Sentinel1GRD)gir).getGeolocationAlgorithm());
			clone.initialise();
    	}else {
    		clone=createReaderForName(gir.getFilesList()[0],null).get(0);
    	}
    	return clone;
    }
    
    
    /**
     * try to create the correct reader starting from file name
     * @param file
     * @return
     */
    public static List<GeoImageReader> createReaderForName(String file,String geoAlgorithm){
    	List<GeoImageReader> girList=new ArrayList<GeoImageReader>();

        try {
        	File f=new File(file);
	        String parent=f.getParent();
	        //le cosmosky possono avere immagini multiple (mosaic)
	        if(parent!=null&&(parent.contains("CSKS")||file.contains("CSKS"))){
	    		H5File h5file = new H5File(file, H5File.READ);
	    		H5Group group=((H5Group)h5file.get("/"));
	        	List<HObject>hObjs=group.getMemberList();
	        	Iterator<HObject> it=hObjs.iterator();

	        	while(it.hasNext()){
	        		HObject obj=it.next();
	        		AbstractCosmoSkymedImage cosmo=null;
	        		if(obj.getName().equals("MBI")){
	        			cosmo=CosmoSkyFactory.instanceCosmoSkymed(h5file,obj.getName(),null);
	        		}else{
	        			//complex images
	        			if(h5file.get(obj.getName()+"/SBI")!=null)
	        				cosmo=CosmoSkyFactory.instanceCosmoSkymed(h5file,obj.getName()+"/SBI",obj.getName());
	        		}	
	        		if(cosmo!=null){
		                if (cosmo.initialise()) {
		                	girList.add(cosmo);
		                    logger.info("Successfully reading {0} as {1}...", new Object[]{file,cosmo.getClass()});
		                }
		                if(girList.size()>1)
		                	cosmo.setContainsMultipleImage(true);
	        		}
	        	}
	        }else if(parent.contains("S1A")||parent.contains("S1B")){//sentinel 1
	        	girList=Sentinel1Factory.instanceS1Reader(f, geoAlgorithm);
        	}else{
        		GeoImageReader gir=null;
        		if(parent.contains("TDX1_SAR__MGD")){
        			gir= new TerrasarXImage(f);
        		}else if(parent.contains("TSX1_SAR__MGD")){
	        		gir= new TerrasarXImage(f);
	        	}else if(parent.contains("TSX1_SAR__SSC")||parent.contains("TDX1_SAR__SSC")){
	        		gir=new TerrasarXImage_SLC(f);
	        	}else if(parent.contains("RS2")&& parent.contains("SLC")){
	        		if(GDALUtilities.isGDALAvailable())
	        			gir=new Radarsat2Image_SLCGDAL(f);
	        		else
	        			gir=new Radarsat2Image_SLC(f);
	        	}else if(parent.contains("RS2")&& !parent.contains("SLC")){
	        		if(GDALUtilities.isGDALAvailable())
	        			gir=new Radarsat2ImageGDAL(f);
	        		else
	        			gir=new Radarsat2Image(f);
	        	}else if(parent.contains("RS1")){
	        		gir=new Radarsat1Image(f);
	        	}else if(parent.contains("ASA_")){
	        		gir=new EnvisatImage_SLC(f);
	        	}else if(new File(file).getName().equalsIgnoreCase("summary.txt")){
	        		TiffAlosProperties props=new TiffAlosProperties(file);
	        		if(props.getProductFormat().equalsIgnoreCase("CEOS")){
	        			gir=new GDALAlosCeos(f);
	        		}else{
	        			gir=new AlosGeoTiff(f);
	        		}
	        		
	        	}else{
	        		return null;
	        	}
	            if (gir.initialise()) {
	               logger.info("Successfully reading {0} as {1}...", new Object[]{file, gir.getClass()});
	                girList.add(gir);
	            }else{
	            	girList=null;
	            }
        	}
        } catch (Exception ex) {
            logger.error("Error reading:"+file,  ex);
            girList=null;
        }
        finally{
           return girList;
        }
    }

    /*
     *
     * @param file
     * @param format
     * @return
     *
    public static List<GeoImageReader> create(String file, String format) {
    	List<GeoImageReader> girList=new ArrayList<GeoImageReader>();

        try {
        	File f=new File(file);
    		GeoImageReader gir;
	        Class<?> clazz = Class.forName(format);
	        Object o= clazz.newInstance();
	        gir = (GeoImageReader)o;
            if (gir.initialise(f)) {
                logger.info("Successfully reading {0} as {1}...", new Object[]{file, format});
                girList.add(gir);
            }else{
            	girList=null;
            }
        } catch (Exception ex) {
            logger.error( "Class " + format + " not found.", ex);
            girList=null;
        }
        finally{
           return girList;
        }

    }*/

    private static void addFormat(String f) {
        FORMATS.add(f);
    }
}

