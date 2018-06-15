package com.bmn.gm;

import com.bmn.gm.Service;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.net.URL;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.List;
import java.util.Map;


/**
 * jetty http 服务
 *
 * @author qvp
 */
public class JettyServer implements Service {
	private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);

	private int port = 8983;
	private String contextPath = "/";
	private String webPath;
	private int threadNum = 50;

	private Map<String, Filter> filters;		//配置filter
	private Map<String, Servlet> servlets;		//配置servlet
	private List<EventListener> listeners;	 	//配置listener

	private Server server;

	private void init() throws Exception {
		server = new Server();

		/*SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
        connector.setMaxIdleTime(30000);
        connector.setRequestHeaderSize(8192);
        QueuedThreadPool threadPool =  new QueuedThreadPool(threadNum);
        threadPool.setName("embed-jetty-http");
        connector.setThreadPool(threadPool);
*/

		//server.setConnectors(new Connector[] { connector });
		//Context context = null;
		ServletContextHandler context = null;

		if(webPath != null && !webPath.equals("")) {//webapp 可以支持 jsp
			logger.info("load webPath={}", webPath);
			final URL warUrl = new File(webPath).toURI().toURL();
			final String warUrlString = warUrl.toExternalForm();
			
			//ErrorPageErrorHandler errorPage = new ErrorPageErrorHandler();
			//errorPage.addErrorPage(400, 500, "/sys/login");
			
			context = new WebAppContext(warUrlString, contextPath);
			//((WebAppContext)context).setErrorHandler(errorPage);
			context.setResourceBase(webPath);
			
			((WebAppContext)context).setDescriptor("web/WEB-INF/web.xml");
			
			server.setHandler(context);
		} else {
			context = new ServletContextHandler(server, contextPath);
		}

		if(listeners != null) {
			for(EventListener listener : listeners) {
				context.addEventListener(listener);
			}
		}

		//add filter
		if(filters != null) {
			for(Map.Entry<String, Filter> eFilter : filters.entrySet()) {
				EnumSet<DispatcherType> d  = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.FORWARD);
				
				logger.info("add filter={}, path={}", eFilter.getValue().getClass(), eFilter.getKey());
				context.addFilter(new FilterHolder(eFilter.getValue()), eFilter.getKey(), d);
			}
		}

		//add servlet
		if(servlets != null) {
			for(Map.Entry<String, Servlet> eServlet : servlets.entrySet()) {
				logger.info("add servlet={}, path={}", eServlet.getValue().getClass(), eServlet.getKey());
				context.addServlet(new ServletHolder(eServlet.getValue()), eServlet.getKey());
			}
		}

//		context.addBean(errorPage);

		if(webPath == null) {
			context.addServlet(DefaultServlet.class, "/*");
		}
	}


	public void start() {
		try {
			init();
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("jetty embed server started, port={}", port);
	}

	public void stop() {
		try {
			server.stop();
			server.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void main(String[] args) {
		String contextFile = "classpath:spring-context.xml";
		if (args.length > 0) {
			contextFile = args[0];
		}
		ApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(contextFile);
		} catch (Exception e) {
			System.out.println("RunMain [spring-conf-file]");
			logger.warn("", e);
		}

		String jettyEmbedServerBeanName = "jettyEmbedServer";
		if (args.length > 1) {
			jettyEmbedServerBeanName = args[1];
		}

		final JettyServer jettyEmbedServer = (JettyServer) context.getBean(jettyEmbedServerBeanName);
		Runtime.getRuntime().addShutdownHook(new thread() {

			@Override
			public void run() {
				try {
					jettyEmbedServer.stop();
				} catch (Exception e) {
					logger.error("run main stop error!", e);
				}
			}

		});

		try {
			jettyEmbedServer.start();
			logger.info("server started");
		} catch (Throwable e) {
			logger.warn("has exception!", e);
			System.exit(-1);
		}
	}*/

	public void setPort(int port) {
		this.port = port;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public void setFilters(Map<String, Filter> filters) {
		this.filters = filters;
	}

	public void setServlets(Map<String, Servlet> servlets) {
		this.servlets = servlets;
	}

	public void setListeners(List<EventListener> listeners) {
		this.listeners = listeners;
	}
}
