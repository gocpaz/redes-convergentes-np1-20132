package controller;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.impl.DaemonThreadFactory;
import org.directwebremoting.ui.dwr.Util;

import uk.ltd.getahead.dwr.WebContextFactory;


@SuppressWarnings("deprecation")
public class SMNPController implements Runnable{
    /**
     * Are we updating the clocks on all the pages?
     */
    protected transient boolean active = false;
    /**
     * The last time string
     */
    protected String timeString = "";
	
	public String getMessage(){
		String newTimeString = new Date().toString();
        // Call DWR's util which adds rows into a table.  peopleTable is the id of the tbody and 
        // data contains the row/column data.
    	Util.setValue("s", newTimeString);
		return newTimeString;
	}
	
	 public SMNPController()
	 {
	        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
	        executor.scheduleAtFixedRate(this, 1, 50, TimeUnit.MILLISECONDS);
	    }

	    /* (non-Javadoc)
	     * @see java.lang.Runnable#run()
	     */
	    @Override
	    public void run()
	    {
	        updateTableDisplay();
	    }

	    private class UpdatesEnabledFilter implements ScriptSessionFilter
		{
	    	private String attrName;
	    	
	    	public UpdatesEnabledFilter(String attrName)
		    {
	    		this.attrName = attrName;
	    	}
	    	
	        public boolean match(ScriptSession ss)
		    {
				Object check = ss.getAttribute(attrName);
		        return (check != null && check.equals(Boolean.TRUE));
			}    	
	    }
	    
	    public void updateTableDisplay()
	    {
//	        String page = ServerContextFactory.get().getContextPath() + "/index.jsp";
//	        ScriptSessionFilter attributeFilter = new AttributeScriptSessionFilter(SCRIPT_SESSION_ATTR);
	        Browser.withAllSessionsFiltered(new UpdatesEnabledFilter(UPDATES_ENABLED_ATTR), new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                // TODO recebe o objeto com as informacoes do ip atual.
//	            	SMNPModel smnp = new SMNPModel();
	            	String newTimeString = new Date().toString();
	                // Call DWR's util which adds rows into a table.  peopleTable is the id of the tbody and 
	                // data contains the row/column data.
	            	Util.setValue("s", newTimeString);
	            }
	        });
	    }
		/**
		 * Called from the client to add an attribute on the ScriptSession.  This
		 * attribute will be used so that only pages (ScriptSessions) that have 
		 * set this attribute will be updated.
		 */
	    public void addAttributeToScriptSession() {
			ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
	        scriptSession.setAttribute(SCRIPT_SESSION_ATTR, true);
	    }
	    
	    /**
		 * Called from the client to remove an attribute from the ScriptSession.  
		 * When called from a client that client will no longer receive updates (unless addAttributeToScriptSession)
		 * is called again.
		 */
	    public void removeAttributeToScriptSession() {
	        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
	        scriptSession.removeAttribute(SCRIPT_SESSION_ATTR);
	    }
	    
	    private static String UPDATES_ENABLED_ATTR = "UPDATES_ENABLED";
	    private final static String SCRIPT_SESSION_ATTR = "SCRIPT_SESSION_ATTR";
}
