package com.gionee.gniflow.sap.rfc;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.ext.Environment;

public class RfcManager {
	
	private static Logger logger = Logger.getLogger(RfcManager.class);

    private static String ABAP_AS_POOLED = "TaskService";

    private static JCOProvider provider = null;

    private static JCoDestination destination = null;

    static {
        Properties properties = loadProperties();

        provider = new JCOProvider();

        // catch IllegalStateException if an instance is already registered
        try {
            Environment.registerDestinationDataProvider(provider);
        } catch (IllegalStateException e) {
            logger.debug(e);
        }

        provider.changePropertiesForABAP_AS(ABAP_AS_POOLED, properties);
    }

    public static Properties loadProperties() {
        RfcManager manager = new RfcManager();
        Properties prop = new Properties();
        try {
            prop.load(manager.getClass().getResourceAsStream(
                    "/SapService.properties"));
        } catch (IOException e) {
            logger.debug(e);
        }
        return prop;
    }

    public static JCoDestination getDestination() throws JCoException {
        if (destination == null) {
            destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        }
        return destination;
    }

    public static JCoFunction getFunction(String functionName) {
        JCoFunction function = null;
        try {
            function = getDestination().getRepository()
                    .getFunctionTemplate(functionName).getFunction();
        } catch (JCoException e) {
            logger.error(e);
        } catch (NullPointerException e) {
            logger.error(e);
        }
        return function;
    }

    public static void execute(JCoFunction function) {
        logger.debug("SAP Function Name : " + function.getName());
        JCoParameterList paramList = function.getImportParameterList();

        if (paramList != null) {
            logger.debug("Function Import Structure : " + paramList.toString());
        }else{
        	 paramList = function.getTableParameterList();
        }

        try {
            function.execute(getDestination());
        } catch (JCoException e) {
            logger.error(e);
        }
       
        if (paramList != null) {
            logger.debug("Function Export Structure : " + paramList.toString());
        }
    }
    public static String ping() {
        String msg = null;
        try {
            getDestination().ping();
            msg = "Destination " + ABAP_AS_POOLED + " is ok";
        } catch (JCoException ex) {
        	ex.printStackTrace();
        }
        logger.debug(msg);
        return msg;
    }

}
