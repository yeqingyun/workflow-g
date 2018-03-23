package com.gionee.gniflow.sap.rfc;

import java.util.HashMap;
import java.util.Properties;

import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class JCOProvider implements DestinationDataProvider{
	private HashMap<String, Properties> secureDBStorage = new HashMap<String, Properties>();
    private DestinationDataEventListener eL;

    @Override
    public Properties getDestinationProperties(String destinationName) {
        try
        {
            //read the destination from DB
            Properties p = secureDBStorage.get(destinationName);

            if(p!=null)
            {
                //check if all is correct, for example
                if(p.isEmpty())
                    throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION, "destination configuration is incorrect", null);
                return p;
            }
            
            return null;
        }
        catch(RuntimeException re)
        {
            throw new DataProviderException(DataProviderException.Reason.INTERNAL_ERROR, re);
        }
    }

    @Override
    public void setDestinationDataEventListener(
            DestinationDataEventListener eventListener) {
        this.eL = eventListener;

    }

    @Override
    public boolean supportsEvents() {
        return true;
    }

    //implementation that saves the properties in a very secure way
    public void changePropertiesForABAP_AS(String destName, Properties properties) {
        synchronized(secureDBStorage)
        {
            if(properties==null)
            {
                if(secureDBStorage.remove(destName)!=null)
                    eL.deleted(destName);
            }
            else 
            {
                secureDBStorage.put(destName, properties);
                eL.updated(destName); // create or updated
            }
        }
    }
}
