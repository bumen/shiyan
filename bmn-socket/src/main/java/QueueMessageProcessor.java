import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Modifier;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class QueueMessageProcessor implements Runnable, UncaughtExceptionHandler {

	private Logger logger = LogManager.getLogger(this.getClass());

	private String name = null;

	private int index = 0;

	private BlockingQueue<BaseMsg> queue = null;

	private Thread thread = null;

	private boolean stop = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public QueueMessageProcessor(String name) {
		MaintainTask.start(ServerConfigBean.getInstance().isPublic());

		this.name = name;

		this.queue = new LinkedBlockingQueue<BaseMsg>();
	}

	public QueueMessageProcessor(String name, int index) {
		MaintainTask.start(ServerConfigBean.getInstance().isPublic());

		this.name = name;
		this.index = index;

		this.queue = new LinkedBlockingQueue<BaseMsg>();
	}

	/**
	 * 写入消息到队列
	 * 
	 * @param msg
	 */
	public void put(BaseMsg msg) {
		if (stop) {// 关闭了，不再接受数据
			return;
		}

		queue.add(msg);
	}

	/**
	 * 阻塞式获取数据
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public BaseMsg get() throws InterruptedException {
		BaseMsg bean = queue.take();
		return bean;
	}

	@Override
	public void run() {
		BaseMsg msg = null;
		while (!stop) {
			try {
				msg = this.get();
//				Instances.getMsgRouter().execute(msg);
			} catch (Exception e) {
				logger.error(String.format("Get message Name[%s] in thread[%s] index[%d]", msg.getName(), this.name,
						this.index), e);
			}
		}
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		if (stop) {
			return;
		}

		logger.error("Uncaught exception in thread [" + t + "]", e);

		// 线程挂了,尝试重新启动一个
		System.gc();
		System.runFinalization();

		// 启动线程
		this.startThread();
	}

	private void startThread() {
		MaintainTask.start(ServerConfigBean.getInstance().isPublic());

		this.thread = new Thread(this);
		this.thread.setName(this.name);

		// 加入定义的ErrHandler
		this.thread.setUncaughtExceptionHandler(this);
		this.thread.start();

		logger.info(String.format("Thread [%s], index[%d] is working.", this.name, this.index));
	}

	/**
	 * 启动
	 */
	public void start() {
		this.startThread();
	}

	/**
	 * 停止线程
	 */
	public void stop() {
		this.stop = true;

		this.thread.interrupt();

		logger.info(String.format("Thread [%s], index[%d] is stopped.", this.name, this.index));
	}

	private class BaseMsg {

		public Object getName() {
			return null;
		}
	}

	private static class MaintainTask {

		public static void start(boolean ispublic) {

		}
	}

	private static class ServerConfigBean {

		public static ServerConfigBean getInstance() {
			return null;
		}

		public boolean isPublic() {
			return true;
		}
	}
}
