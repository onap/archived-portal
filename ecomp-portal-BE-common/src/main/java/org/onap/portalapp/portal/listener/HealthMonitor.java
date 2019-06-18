/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
package org.onap.portalapp.portal.listener;

import java.time.Instant;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.onap.music.datastore.PreparedQueryObject;
import org.onap.music.exceptions.MusicServiceException;
import org.onap.music.main.MusicCore;
import org.onap.music.main.MusicUtil;
import org.onap.portalapp.music.util.MusicProperties;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.onap.portalapp.portal.logging.logic.EPLogUtil;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.Transactional;




@Transactional
@Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
@NoArgsConstructor
public class HealthMonitor {
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HealthMonitor.class);
	private Thread healthMonitorThread;
	private static SessionFactory sessionFactory;

	private static boolean databaseUp;
	private static boolean uebUp;
	private static boolean frontEndUp;
	private static boolean backEndUp;
	private static boolean dbPermissionsOk;
	private static boolean zookeeperStatusOk;
	private static boolean cassandraStatusOk;
	private static String application = "Portal";
	private static boolean isSuspended = false;

	@Autowired
	public HealthMonitor(SessionFactory sessionFactory) {
		HealthMonitor.sessionFactory = sessionFactory;
	}

	private static void monitorEPHealth() {

		int numIntervalsDatabaseHasBeenDown = 0;
		int numIntervalsDatabasePermissionsIncorrect = 0;
		int numIntervalsZookeeperNotHealthy = 0;
		int numIntervalsCassandraNotHealthy = 0;

		logger.debug(EELFLoggerDelegate.debugLogger, "monitorEPHealth thread started");
        

		long sleepInterval = (Long
				.parseLong(SystemProperties.getProperty(EPCommonSystemProperties.HEALTH_POLL_INTERVAL_SECONDS)) * 1000);
		long numIntervalsBetweenAlerts = Long
				.parseLong(SystemProperties.getProperty(EPCommonSystemProperties.HEALTHFAIL_ALERT_EVERY_X_INTERVALS));
		logger.debug(EELFLoggerDelegate.debugLogger,
				"monitorEPHealth: Polling health every " + sleepInterval + " milliseconds. Alerting every "
						+ (sleepInterval * numIntervalsBetweenAlerts) / 1000 + " seconds when component remains down.");
		
		while (true) {
			logger.debug(EELFLoggerDelegate.debugLogger,
					"monitorEPHealth: Test Connection to all");
			//
			// Get DB status. If down, signal alert once every X intervals.
			//
			databaseUp = checkIfDatabaseUp();
			if (databaseUp) {
				if ((numIntervalsDatabaseHasBeenDown % numIntervalsBetweenAlerts) == 0) {
					logger.debug(EELFLoggerDelegate.debugLogger,
							"monitorEPHealth: database down, logging to error log to trigger alert.");
					// Write a Log entry that will generate an alert
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHealthCheckMySqlError);
					numIntervalsDatabaseHasBeenDown++;
				} else {
					numIntervalsDatabaseHasBeenDown = 0;
				}
			}

			dbPermissionsOk = checkDatabasePermissions();
			if (!dbPermissionsOk) {
				if ((numIntervalsDatabasePermissionsIncorrect % numIntervalsBetweenAlerts) == 0) {
					logger.debug(EELFLoggerDelegate.debugLogger,
							"monitorEPHealth: database permissions incorrect, logging to error log to trigger alert.");
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHealthCheckMySqlError);
					numIntervalsDatabasePermissionsIncorrect++;
				} else {
					numIntervalsDatabasePermissionsIncorrect = 0;
				}
			}
			if(org.onap.portalapp.music.util.MusicUtil.isMusicEnable()){

				zookeeperStatusOk = checkZookeeperStatus();

				if (!zookeeperStatusOk) {
					if ((numIntervalsZookeeperNotHealthy % numIntervalsBetweenAlerts) == 0) {
						logger.debug(EELFLoggerDelegate.debugLogger,
								"monitorEPHealth: cluster nodes down, logging to error log to trigger alert.");
						EPLogUtil.logEcompError(logger, EPAppMessagesEnum.MusicHealthCheckZookeeperError);
						numIntervalsZookeeperNotHealthy++;
					} else {
						numIntervalsZookeeperNotHealthy = 0;
					}
				}

				cassandraStatusOk = checkCassandraStatus();
				if (!cassandraStatusOk) {
					if ((numIntervalsCassandraNotHealthy % numIntervalsBetweenAlerts) == 0) {
						logger.debug(EELFLoggerDelegate.debugLogger,
								"monitorEPHealth: cluster nodes down, logging to error log to trigger alert.");
						EPLogUtil.logEcompError(logger, EPAppMessagesEnum.MusicHealthCheckCassandraError);
						numIntervalsCassandraNotHealthy++;
					} else {
						numIntervalsCassandraNotHealthy = 0;
					}
				}
			}
			frontEndUp = true;
			backEndUp = true;

			if (Thread.interrupted()) {
				logger.info(EELFLoggerDelegate.errorLogger, "monitorEPHealth: thread interrupted");
				break;
			}

			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "monitorEPHealth: sleep interrupted", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	@PostConstruct
	public void initHealthMonitor() {
		healthMonitorThread = new Thread("EP HealthMonitor thread") {
			@Override
			public void run() {
				try {
					monitorEPHealth();
				}
				catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "healthMonitorThread failed", e);
				}
			}
		};
		healthMonitorThread.start();
		
	}

	@PreDestroy
	public void closeHealthMonitor() {
		this.healthMonitorThread.interrupt();
	}

	/**
	 * This routine checks whether the database can be read. In June 2017 we
	 * experimented with checking if the database can be WRITTEN. Writes failed
	 * with some regularity in a MariaDB Galera cluster, and in that
	 * environment, the resulting alerts in the log triggered a health monitor
	 * cron job to shut down the Tomcat instance. The root cause of the cluster
	 * write failures was not determined.
	 * 
	 * @return true if the database can be read.
	 */
	private static boolean checkIfDatabaseUp() {
		boolean isUp = false;
		Session localSession = null;
		try {
			localSession = sessionFactory.openSession();
			if (localSession != null) {
				String sql = "select app_name from fn_app where app_id=1";
				Query query = localSession.createSQLQuery(sql);
				@SuppressWarnings("unchecked")
				List<String> queryList = query.list();
				if (queryList != null) {
					isUp = true;
				}
			}
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, "checkIfDatabaseUp failed", e);
			isUp = false;
		} finally {
			if (localSession != null)
				localSession.close();
		}
		return isUp;
	}

	private static boolean checkZookeeperStatus() {

		String[] zookeeperNodes = MusicUtil.getMyZkHost().split(",");
		logger.info(EELFLoggerDelegate.applicationLogger, "MusicUtil.getMyZkHost()---- :" + MusicUtil.getMyZkHost());
		for (String zookeeperNode : zookeeperNodes) {
			try {
				logger.info(EELFLoggerDelegate.applicationLogger, "server ip--zookeeper  :" + zookeeperNode.trim());
				String[] iport = zookeeperNode.split(":");
				String zkNodeStatistics = FourLetterWordMain.send4LetterWord(iport[0].trim(),
					Integer.parseInt(iport[1].trim()), "stat");
				logger.info(EELFLoggerDelegate.applicationLogger,
					"Getting Status for Zookeeper zkNodeStatistics :" + zkNodeStatistics);
				if (StringUtils.isNotBlank(zkNodeStatistics)) {
					String state = zkNodeStatistics.substring(zkNodeStatistics.indexOf("Mode:"),
						zkNodeStatistics.indexOf("Node"));
					logger.info(EELFLoggerDelegate.applicationLogger,
						"Getting Status for zookeeper :" + zookeeperNode.trim() + ":------:" + state);
					if (state.contains("leader") || state.contains("follower")) {
						return true;
					}
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "ZookeeperStatus Service is not responding", e.getCause());
			}
		}

		return false;
	}


	private static boolean checkCassandraStatus() {
		logger.info(EELFLoggerDelegate.applicationLogger, "Getting Status for Cassandra");
		if (getAdminKeySpace()) {
			return true;
		} else {
			logger.error(EELFLoggerDelegate.errorLogger, "Cassandra Service is not responding");
			return false;
		}
	}
	
	private static Boolean getAdminKeySpace() {
		String musicKeySpace = MusicProperties.getProperty(MusicProperties.MUSIC_SESSION_KEYSPACE);
		Instant creationTime = Instant.now();
		PreparedQueryObject pQuery = new PreparedQueryObject();
		pQuery.appendQueryString(
				"UPDATE " + musicKeySpace + ".health_check  SET creation_time = ? WHERE primary_id = ?");
		pQuery.addValue(creationTime.toString());
		pQuery.addValue(application);
		try {
			MusicCore.nonKeyRelatedPut(pQuery, MusicUtil.CRITICAL);
		} catch (MusicServiceException e) {
			logger.error(EELFLoggerDelegate.errorLogger, e.getErrorMessage(), e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;

	}

	
	private static boolean checkDatabasePermissions() {
		boolean isUp = false;
		Session localSession = null;
		try {
			localSession = sessionFactory.openSession();
			if (localSession != null) {
				String sql = "SHOW GRANTS FOR CURRENT_USER";
				Query query = localSession.createSQLQuery(sql);
				@SuppressWarnings("unchecked")
				List<String> grantsList = query.list();
				for (String str : grantsList) {
					if ((str.toUpperCase().contains("ALL"))
							|| (str.toUpperCase().contains("DELETE") && str.toUpperCase().contains("SELECT")
									&& str.toUpperCase().contains("UPDATE") && str.toUpperCase().contains("INSERT"))) {
						isUp = true;
						break;
					}
				}
				if (!isUp) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"checkDatabasePermissions returning false.  SHOW GRANTS FOR CURRENT_USER being dumped:");
					for (String str : grantsList) {
						logger.error(EELFLoggerDelegate.errorLogger, "grants output item = [" + str + "]");
					}
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "checkDatabasePermissions failed", e);
			if ((e.getCause() != null) && (e.getCause().getMessage() != null)) {
				logger.error(EELFLoggerDelegate.errorLogger, "checkDatabasePermissions failure cause", e.getCause());
			}
			isUp = false;
		} finally {
			if (localSession != null) {
				localSession.close();
			}
		}
		return isUp;
	}

	public static boolean isDatabaseUp() {
		return databaseUp;
	}

	public static boolean isUebUp() {
		return uebUp;
	}

	public static boolean isFrontEndUp() {
		return frontEndUp;
	}

	public static boolean isBackEndUp() {
		return backEndUp;
	}

	public static boolean isDbPermissionsOk() {
		return dbPermissionsOk;
	}

	public static boolean isZookeeperStatusOk() {
		return zookeeperStatusOk;
	}

	public static boolean isCassandraStatusOk() {
		return cassandraStatusOk;
	}

	public static boolean isSuspended() {
		return isSuspended;
	}

	public static void setSuspended(boolean isSuspended) {
		HealthMonitor.isSuspended = isSuspended;
	}
}
