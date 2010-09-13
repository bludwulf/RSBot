package org.rsbot.bot;

import org.rsbot.bot.injector.Injector;
import org.rsbot.client.Loader;
import org.rsbot.util.GlobalConfiguration;

import java.applet.Applet;
import java.awt.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Qauters
 */
public class RSLoader extends Applet implements Runnable, Loader {

	private final Logger log = Logger.getLogger(RSLoader.class.getName());

	private static final long serialVersionUID = 6288499508495040201L;

	/**
	 * The applet of the client
	 */
	private Applet client;
	private Runnable loadedCallback;

	/**
	 * The injector
	 */
	Injector injector;

	/**
	 * The game class loader
	 */
	public RSClassLoader classLoader;

	@Override
	public final synchronized void destroy() {
		if (client != null) {
			client.destroy();
		}
	}

	public Applet getClient() {
		return client;
	}

	@Override
	public final synchronized void init() {
		if (client != null) {
			client.init();
		}
	}

	@Override
	public final void paint(final Graphics graphics) {
		if (client != null) {
			client.paint(graphics);
		}
		else
		{
			Font font = new Font("Helvetica", 1,13);
			FontMetrics fontMetrics = getFontMetrics(font);
			graphics.setColor(Color.black);
            graphics.fillRect(0, 0, 768, 503);
            graphics.setColor(Color.RED);
            graphics.drawRect(232, 232, 303, 33);
            String s = "Loading...";
            graphics.setFont(font);
            graphics.setColor(Color.WHITE);
            graphics.drawString(s, (768 - fontMetrics.stringWidth(s)) / 2, 255);
		}
	}

	/**
	 * The run void of the loader
	 */
	public void run() {
		try {
			Class<?> c = classLoader.loadClass("client");
			client = (Applet) c.newInstance();
			loadedCallback.run();
			c.getMethod("provideLoaderApplet", new Class[] { java.applet.Applet.class }).invoke(null, new Object[] { this });
			client.init();
			client.start();
		} catch (final Exception e) {
			log.log(Level.SEVERE, "Unable to load client, please check your firewall and internet connection.");
			File versionFile = new File(GlobalConfiguration.Paths.getVersionCache());
			versionFile.delete();
		}
	}

	public void loadClasses() {
		injector = new Injector();
		classLoader = new RSClassLoader(injector);
	}
	
	public void setCallback(final Runnable r) {
		loadedCallback = r;
	}

	/**
	 * Overridden void start()
	 */
	@Override
	public final synchronized void start() {
		if (client != null) {
			client.start();
		}
	}

	/**
	 * Overridden void stop()
	 */
	@Override
	public final synchronized void stop() {
		if (client != null) {
			client.stop();
		}
	}

	/**
	 * Overridden void update(Graphics)
	 */
	@Override
	public final void update(Graphics graphics) {
		if (client != null) {
			client.update(graphics);
		}
	}

}