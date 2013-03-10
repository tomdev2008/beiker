package com.beike.util;

import java.util.Locale;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * <p>Title: </p> 
 * <p>Description: 
 * <p>Copyright: </p> 
 * <p>Company: 易宝支付</p>
 * @author xxxx
 * @version 1.0
 */
public class ServiceLocator
{
    /**
     * The bean reference factory location.
     */
    //private final static String BEAN_REFERENCE_LOCATION = "springContext/beanRefFactory.xml";
	// update by Lei for junit test.
	public static String BEAN_REFERENCE_LOCATION = "springContext/jointBeanRefFactory.xml";

    /**
     * The bean reference factory ID.
     */
    public static String BEAN_REFERENCE_ID = "beanRefFactory";



	private static ServiceLocator sInstance = null;
	private static Object lock=new Object();
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if(sInstance!=null)
					sInstance.shutdown();
			}
		});
	}

	/**
	 * Get the unique instance of this class.
	 */
	public static  ServiceLocator instance() {

		if (sInstance == null) {
			synchronized(lock){
				if(sInstance==null)
					sInstance = new ServiceLocator();
			}
		}

		return sInstance;

	}
	/**
	 * Private constuctor
	 */
	private ServiceLocator() {
        BeanFactoryLocator beanFactoryLocator =
            ContextSingletonBeanFactoryLocator.getInstance(
                BEAN_REFERENCE_LOCATION);
        beanFactoryReference = beanFactoryLocator.useBeanFactory(BEAN_REFERENCE_ID);
        messageSourceAccessor=new MessageSourceAccessor((ApplicationContext)beanFactoryReference.getFactory());
	}


    /**
     * The bean factory reference instance.
     */
    private BeanFactoryReference beanFactoryReference;
    private MessageSourceAccessor messageSourceAccessor;

    /**
     * Gets the Spring ApplicationContext.
     */
    public ApplicationContext getContext(){
    	return (ApplicationContext)beanFactoryReference.getFactory();
    }

    /**
     * Shuts down the ServiceLocator and releases any used resources.
     */
    public synchronized void shutdown()
    {
        if (this.beanFactoryReference != null)
        {
            this.beanFactoryReference.release();
            this.beanFactoryReference = null;
            sInstance=null;
        }
    }
	public String getMessage(String code,Object[] args){
		return messageSourceAccessor.getMessage(code,args);
	}
	public String getMessage(String code,Locale locale,Object[] args){
		return messageSourceAccessor.getMessage(code,args,locale);
	}
	public String getMessage(String code,String defaultMsg,Object[] args){
		return messageSourceAccessor.getMessage(code,args,defaultMsg);
	}
	public String getMessage(String code,String defaultMsg,Locale locale,Object[]args){
		return messageSourceAccessor.getMessage(code,args,defaultMsg,locale);
	}
}