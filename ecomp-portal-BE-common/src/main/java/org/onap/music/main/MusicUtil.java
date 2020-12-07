/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright Â© 2017 AT&T Intellectual Property. All rights reserved.
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
 *.
 */

package org.onap.music.main;

import com.datastax.driver.core.DataType;

import locate.v1_1.Configuration.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.onap.music.datastore.PreparedQueryObject;
import org.onap.music.eelf.logging.EELFLoggerDelegate;

public class MusicUtil {
    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MusicUtil.class);
    public static final String ATOMIC = "atomic";
    public static final String EVENTUAL = "eventual";
    public static final String CRITICAL = "critical";
    public static final String ATOMICDELETELOCK = "atomic_delete_lock";
    public static final String DEFAULTKEYSPACENAME = "TBD";
    private static final String XLATESTVERSION = "X-latestVersion";
    private static final String XMINORVERSION = "X-minorVersion";
    private static final String XPATCHVERSION = "X-patchVersion";
    private static final String LOCALHOST = "localhost";
    private static final String PROPERTIES_FILE = "/opt/app/music/etc/music.properties";
    private static int myId = 0;
    private static ArrayList<String> allIds = new ArrayList();
    private static String publicIp = "";
    private static ArrayList<String> allPublicIps = new ArrayList();
    private static String myZkHost = "localhost";
    private static String myCassaHost = "localhost";
    private static String defaultMusicIp = "localhost";
    private static boolean debug = true;
    private static String version = "2.3.0";
    private static String musicRestIp = "localhost";
    private static String musicPropertiesFilePath = "/opt/app/music/etc/music.properties";
    private static long defaultLockLeasePeriod = 6000L;
    private static final String[] propKeys = new String[]{"zookeeper.host", "cassandra.host", "music.ip", "debug", "version", "music.rest.ip", "music.properties", "lock.lease.period", "id", "all.ids", "public.ip", "all.pubic.ips", "cassandra.user", "cassandra.password", "aaf.endpoint.url"};
    private static String cassName = "cassandra";
    private static String cassPwd = "";
    private static String aafEndpointUrl = null;
    private static int cassandraPort = 9042;
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

    private MusicUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static String getCassName() {
        return cassName;
    }

    public static String getCassPwd() {
        return cassPwd;
    }

    public static String getAafEndpointUrl() {
        return aafEndpointUrl;
    }

    public static void setAafEndpointUrl(String aafEndpointUrl) {
        MusicUtil.aafEndpointUrl = aafEndpointUrl;
    }

    public static int getMyId() {
        return myId;
    }

    public static void setMyId(int myId) {
        MusicUtil.myId = myId;
    }

    public static List<String> getAllIds() {
        return allIds;
    }

    public static void setAllIds(List<String> allIds) {
        MusicUtil.allIds = (ArrayList)allIds;
    }

    public static String getPublicIp() {
        return publicIp;
    }

    public static void setPublicIp(String publicIp) {
        MusicUtil.publicIp = publicIp;
    }

    public static List<String> getAllPublicIps() {
        return allPublicIps;
    }

    public static void setAllPublicIps(List<String> allPublicIps) {
        MusicUtil.allPublicIps = (ArrayList)allPublicIps;
    }

    public static String[] getPropkeys() {
        return propKeys;
    }

    public static String getMusicRestIp() {
        return musicRestIp;
    }

    public static void setMusicRestIp(String musicRestIp) {
        MusicUtil.musicRestIp = musicRestIp;
    }

    public static String getMusicPropertiesFilePath() {
        return musicPropertiesFilePath;
    }

    public static void setMusicPropertiesFilePath(String musicPropertiesFilePath) {
        MusicUtil.musicPropertiesFilePath = musicPropertiesFilePath;
    }

    public static long getDefaultLockLeasePeriod() {
        return defaultLockLeasePeriod;
    }

    public static void setDefaultLockLeasePeriod(long defaultLockLeasePeriod) {
        MusicUtil.defaultLockLeasePeriod = defaultLockLeasePeriod;
    }

    public static void setDebug(boolean debug) {
        MusicUtil.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setVersion(String version) {
        MusicUtil.version = version;
    }

    public static String getVersion() {
        return version;
    }

    public static String getMyZkHost() {
        return myZkHost;
    }

    public static void setMyZkHost(String myZkHost) {
        MusicUtil.myZkHost = myZkHost;
    }

    public static String getMyCassaHost() {
        return myCassaHost;
    }

    public static void setMyCassaHost(String myCassaHost) {
        MusicUtil.myCassaHost = myCassaHost;
    }

    public static String getDefaultMusicIp() {
        return defaultMusicIp;
    }

    public static void setDefaultMusicIp(String defaultMusicIp) {
        MusicUtil.defaultMusicIp = defaultMusicIp;
    }

    public static int getCassandraPort() {
        return cassandraPort;
    }

    public static void setCassandraPort(int cassandraPort) {
        MusicUtil.cassandraPort = cassandraPort;
    }

    public static String getTestType() {
        String testType = "";

        try {
            Scanner fileScanner = new Scanner(new File(""));
            testType = fileScanner.next();
            String batchSize = fileScanner.next();
            fileScanner.close();
        } catch (FileNotFoundException var3) {
            logger.error(EELFLoggerDelegate.errorLogger, var3.getMessage());
        }

        return testType;
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException var3) {
            logger.error(EELFLoggerDelegate.errorLogger, var3.getMessage());
            Thread.currentThread().interrupt();
        }

    }

    public static boolean isValidQueryObject(boolean withparams, PreparedQueryObject queryObject) {
        if (withparams) {
            int noOfValues = queryObject.getValues().size();
            int noOfParams = 0;
            char[] temp = queryObject.getQuery().toCharArray();

            for(int i = 0; i < temp.length; ++i) {
                if (temp[i] == '?') {
                    ++noOfParams;
                }
            }

            return noOfValues == noOfParams;
        } else {
            return !queryObject.getQuery().isEmpty();
        }
    }

    public static void setCassName(String cassName) {
        MusicUtil.cassName = cassName;
    }

    public static void setCassPwd(String cassPwd) {
        MusicUtil.cassPwd = cassPwd;
    }

    public static String convertToCQLDataType(DataType type, Object valueObj) throws Exception {
        String value = "";
        switch(type.getName()) {
            case UUID:
                value = valueObj + "";
                break;
            case TEXT:
            case VARCHAR:
                String valueString = valueObj + "";
                valueString = valueString.replace("'", "''");
                value = "'" + valueString + "'";
                break;
            case MAP:
                Map<String, Object> otMap = (Map)valueObj;
                value = "{" + jsonMaptoSqlString(otMap, ",") + "}";
                break;
            default:
                value = valueObj + "";
        }

        return value;
    }

    public static Object convertToActualDataType(DataType colType, Object valueObj) throws Exception {
        String valueObjString = valueObj + "";
        switch(colType.getName()) {
            case UUID:
                return UUID.fromString(valueObjString);
            case TEXT:
            case VARCHAR:
            default:
                return valueObjString;
            case MAP:
                return (Map)valueObj;
            case VARINT:
                return BigInteger.valueOf(Long.parseLong(valueObjString));
            case BIGINT:
                return Long.parseLong(valueObjString);
            case INT:
                return Integer.parseInt(valueObjString);
            case FLOAT:
                return Float.parseFloat(valueObjString);
            case DOUBLE:
                return Double.parseDouble(valueObjString);
            case BOOLEAN:
                return Boolean.parseBoolean(valueObjString);
        }
    }

    public static String jsonMaptoSqlString(Map<String, Object> jMap, String lineDelimiter) throws Exception {
        StringBuilder sqlString = new StringBuilder();
        int counter = 0;

        for(Iterator var4 = jMap.entrySet().iterator(); var4.hasNext(); ++counter) {
            Entry<String, Object> entry = (Entry)var4.next();
            Object ot = entry.getValue();
            String value = ot + "";
            if (ot instanceof String) {
                value = "'" + value.replace("'", "''") + "'";
            }

            sqlString.append("'" + (String)entry.getKey() + "':" + value);
            if (counter != jMap.size() - 1) {
                sqlString.append(lineDelimiter);
            }
        }

        return sqlString.toString();
    }

    public static String buildVersion(String major, String minor, String patch) {
        if (minor != null) {
            major = major + "." + minor;
            if (patch != null) {
                major = major + "." + patch;
            }
        }

        return major;
    }

    public static ResponseBuilder buildVersionResponse(String major, String minor, String patch) {
        ResponseBuilder response = Response.noContent();
        String versionIn = buildVersion(major, minor, patch);
        String version = getVersion();
        String[] verArray = version.split("\\.", 3);
        if (minor != null) {
            response.header("X-minorVersion", minor);
        } else {
            response.header("X-minorVersion", verArray[1]);
        }

        if (patch != null) {
            response.header("X-patchVersion", patch);
        } else {
            response.header("X-patchVersion", verArray[2]);
        }

        response.header("X-latestVersion", version);
        logger.info(EELFLoggerDelegate.applicationLogger, "Version In:" + versionIn);
        return response;
    }

    public static void loadProperties() throws Exception {
        if(INITIALIZED.get()) {
           logger.info("Initialized already");
            return;
        }
        CipherUtil.readAndSetKeyString();
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = MusicUtil.class.getClassLoader().getResourceAsStream("music.properties");
            prop.load(input);
        } catch (Exception var11) {
            logger.error(EELFLoggerDelegate.errorLogger, "Unable to find properties file.");
            throw new Exception();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }
            
        }

        String cassPwd = prop.getProperty("cassandra.password");
        String isEncrypted = prop.getProperty("cassandra.password.isencrypted", "true");
        
        if ("true".equals(isEncrypted)) {
            logger.debug(EELFLoggerDelegate.applicationLogger, "Decrypting....");
            cassPwd = CipherUtil.decryptPKC(cassPwd);
            logger.debug(EELFLoggerDelegate.applicationLogger, "Password Decrypted");
            setCassPwd(cassPwd);
        } else {
            setCassPwd(cassPwd);
        }

        setMyCassaHost(prop.getProperty("cassandra.host"));
        String zkHosts = prop.getProperty("zookeeper.host");
        setMyZkHost(zkHosts);
        setCassName(prop.getProperty("cassandra.user"));
        String cassPort = prop.getProperty("cassandra.port");
        if (cassPort != null) {
            setCassandraPort(Integer.parseInt(cassPort));
        }
        INITIALIZED.compareAndSet(false, true);
    }
}

