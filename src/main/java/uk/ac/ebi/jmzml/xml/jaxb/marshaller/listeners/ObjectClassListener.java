package uk.ac.ebi.jmzml.xml.jaxb.marshaller.listeners;

import org.apache.log4j.Logger;

import javax.xml.bind.Marshaller;

/**
 * User: rcote
 * Date: 19-Jun-2008
 * Time: 11:31:37
 * $Id: $
 */
public class ObjectClassListener extends Marshaller.Listener{

    private static final Logger logger = Logger.getLogger(ObjectClassListener.class);

    public void beforeMarshal(Object source) {
        //this class will only be associated with a Marshaller when
        //the logging level is set to DEBUG 
        logger.debug("marshalling: " + source.getClass());
    }

}
