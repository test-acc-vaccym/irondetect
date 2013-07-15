package de.fhhannover.inform.trust.irondetect.util;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.inform.fh-hannover.de/
 * 
 * This file is part of irongui, version 0.0.3, implemented by the Trust@FHH
 * research group at the Hochschule Hannover, a program to visualize the content
 * of a MAP Server (MAPS), a crucial component within the TNC architecture.
 * 
 * The development was started within the bachelor
 * thesis of Tobias Ruhe at Hochschule Hannover (University of
 * Applied Sciences and Arts Hannover). irongui is now maintained
 * and extended within the ESUKOM research project. More information
 * can be found at the Trust@FHH website.
 * %%
 * Copyright (C) 2010 - 2013 Trust@FHH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Ralf Steuerwald
 * @author Bastian Hellmann
 * 
 */
public class Configuration {

	private static Logger logger = Logger.getLogger(Configuration.class);

	private static String CONFIG_FILE = "/configuration.properties";

	private static Properties properties;

	// begin of parameter block
	private static final String IFMAP_AUTH_METHOD = "ifmap.server.auth.method";
	private static final String IFMAP_URL_BASIC = "ifmap.server.url.basic";
	private static final String IFMAP_URL_CERT = "ifmap.server.url.cert";

	private static final String IFMAP_MAX_RESULT_SIZE = "ifmap.maxresult.size";

	private static final String IRONDETECT_PDPSUBSCRIBER_USER = "irondetect.pdpsubscriber.user";
	private static final String IRONDETECT_PDPSUBSCRIBER_PASSWORD = "irondetect.pdpsubscriber.password";
	private static final String IRONDETECT_DEVICESUBSCRIBER_USER = "irondetect.devicesubscriber.user";
	private static final String IRONDETECT_DEVICESUBSCRIBER_PASSWORD = "irondetect.devicesubscriber.password";
	private static final String IRONDETECT_STATUSPUBLISHER_USER = "irondetect.statuspublisher.user";
	private static final String IRONDETECT_STATUSPUBLISHER_PASSWORD = "irondetect.statuspublisher.password";

	private static final String KEYSTORE_PATH = "keystore.path";
	private static final String KEYSTORE_PASSWORD = "keystore.password";

	private static final String SUBSCRIBER_PDP = "irondetect.subscriber.pdp";

	private static final String STATUS_IDENTIFIER = "irondetect.publish.status.identifier";

	private static final String POLICY_FILE = "irondetect.policy.filename";

	private static final String PROCEDURE_DIRECTORY = "irondetect.procedure.directory";

	private static final String DB4O_DATABASE = "irondetect.db4o.database";

	private static final String ACTION_AS_IFMAP_EVENT = "irondetect.publisher.actionasifmapevent";
	private static final String PUBLISH_NOTIFY = "irondetect.publisher.notify";

	private static final String IRONDETECT_GUI = "irondetect.gui";
	private static final String IRONDETECT_PUBLISH_STATUS = "irondetect.publish.status";
	// end of parameter block

	/**
	 * Loads the configuration file. Every time this method is called the
	 * file is read again.
	 */
	public static void init() {
		// support user specific config files without rebuilding the whole thing
		String config = System.getProperty("config");
		if (config != null) {
			CONFIG_FILE = config;
		}
		logger.info("Trying to read in configuration file: " + CONFIG_FILE);

		properties = new Properties();
		//		InputStream is = Configuration.class.getResourceAsStream(CONFIG_FILE);
		InputStream is = null;
		try {
			is = Helper.getInputStreamForFile(CONFIG_FILE);
			properties.load(is);
		} catch (FileNotFoundException e) {
			logger.error("Could not find " + CONFIG_FILE);
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			logger.error("Error while reading " + CONFIG_FILE);
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the value assigned to the given key. If the configuration has
	 * not been loaded jet this method loads it.
	 * 
	 * @param key
	 * @return the value assigned to key or null if the is none
	 */
	private static String get(String key) {
		if (properties == null) {
			init();
		}

		String result = properties.getProperty(key);
		if (result == null) {
			logger.error("Could not find configuration entry for '" + key + "'");
			return "";
		} else {
			logger.info("Found configuration entry for '" + key + "': " + result);
			return result;
		}
	}

	public static String ifmapAuthMethod() {
		return get(IFMAP_AUTH_METHOD);
	}

	public static String ifmapUrlBasic() {
		return get(IFMAP_URL_BASIC);
	}

	public static String ifmapUrlCert() {
		return get(IFMAP_URL_CERT);
	}

	public static String irondetectPdpSubscriberUser() {
		return get(IRONDETECT_PDPSUBSCRIBER_USER);
	}

	public static String irondetectPdpSubscriberPassword() {
		return get(IRONDETECT_PDPSUBSCRIBER_PASSWORD);
	}

	public static String irondetectDeviceSubscriberUser() {
		return get(IRONDETECT_DEVICESUBSCRIBER_USER);
	}

	public static String irondetectDeviceSubscriberPassword() {
		return get(IRONDETECT_DEVICESUBSCRIBER_PASSWORD);
	}

	public static String irondetectStatusPublisherUser() {
		return get(IRONDETECT_STATUSPUBLISHER_USER);
	}

	public static String irondetectStatusPublisherPassword() {
		return get(IRONDETECT_STATUSPUBLISHER_PASSWORD);
	}

	public static String keyStorePath() {
		return get(KEYSTORE_PATH);
	}

	public static String keyStorePassword() {
		return get(KEYSTORE_PASSWORD);
	}

	public static String subscriberPdp() {
		return get(SUBSCRIBER_PDP);
	}

	public static String getStatusIdentifier() {
		return get(STATUS_IDENTIFIER);
	}

	public static String policyFile() {
		return get(POLICY_FILE);
	}

	public static String procedureDirectory() {
		return get(PROCEDURE_DIRECTORY);
	}

	public static String db4oDatabase() {
		return get(DB4O_DATABASE);
	}

	public static boolean actionAsIfmapEvent() {
		return get(ACTION_AS_IFMAP_EVENT).equalsIgnoreCase("true");
	}

	public static boolean publishNotify() {
		return get(PUBLISH_NOTIFY).equalsIgnoreCase("true");
	}

	public static int ifmapMaxResultSize() {
		return Integer.parseInt(get(IFMAP_MAX_RESULT_SIZE));
	}

	public static boolean loadGUI() {
		return Boolean.parseBoolean(get(IRONDETECT_GUI));
	}

	public static boolean publishStatus() {
		return Boolean.parseBoolean(get(IRONDETECT_PUBLISH_STATUS));
	}
}
