--
-- ============LICENSE_START==========================================
-- ONAP Portal
-- ===================================================================
-- Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
-- ===================================================================
-- Modifications Copyright (c) 2019 Samsung
-- ===================================================================
--
-- Unless otherwise specified, all software contained herein is licensed
-- under the Apache License, Version 2.0 (the "License");
-- you may not use this software except in compliance with the License.
-- You may obtain a copy of the License at
--
--             http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
-- Unless otherwise specified, all documentation contained herein is licensed
-- under the Creative Commons License, Attribution 4.0 Intl. (the "License");
-- you may not use this documentation except in compliance with the License.
-- You may obtain a copy of the License at
--
--             https://creativecommons.org/licenses/by/4.0/
--
-- Unless required by applicable law or agreed to in writing, documentation
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
-- ============LICENSE_END============================================
--
--


-- MySQL dump 10.17  Distrib 10.3.14-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: portal
-- ------------------------------------------------------
-- Server version	10.3.14-MariaDB-1:10.3.14+maria~bionic

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

INSERT INTO `cr_report` (`rep_id`, `title`, `descr`, `public_yn`, `report_xml`, `create_id`, `create_date`, `maint_id`, `maint_date`, `menu_id`, `menu_approved_yn`, `owner_id`, `folder_id`, `dashboard_type_yn`, `dashboard_yn`) VALUES (15,'Application Usage Report Wid','',1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<customReport pageSize=\"200\" reportType=\"Linear\">\n    <reportName>Application Usage Report Wid</reportName>\n    <reportDescr></reportDescr>\n    <dbInfo>local</dbInfo>\n    <dbType>mysql</dbType>\n    <chartType>BarChart3D</chartType>\n    <chartWidth>700</chartWidth>\n    <chartHeight>500</chartHeight>\n    <showChartTitle>false</showChartTitle>\n    <public>false</public>\n    <hideFormFieldAfterRun>false</hideFormFieldAfterRun>\n    <createId>27</createId>\n    <createDate>2017-01-28-05:00</createDate>\n    <reportSQL>SELECT \n	l.date audit_date, \n	app_id app_id, \n	IF(CHAR_LENGTH(l.app_name) &gt;14, CONCAT(CONCAT(SUBSTR(l.app_name,1,7),\'...\'), SUBSTR(l.app_name, CHAR_LENGTH(l.app_name)-3,CHAR_LENGTH(l.app_name))) , l.app_name) app_name, \n	IFNULL(r.ct,0) ct \nfrom\n(\n	select a.Date, app_id, app_name\n	from (\n	    select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as Date\n	    from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n	    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n	    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n	) a, \n	(\n		SELECT  \n			app_id, app_name\n		from\n		(\n			select @rn := @rn+1 AS rowId, app_id, app_name from \n				(\n					select app_id, app_name, ct from \n					(\n						select affected_record_id, count(*) ct\n						from fn_audit_log l\n						where audit_date &gt; date_add( curdate(), interval -6 day)\n						and affected_record_id not in ( 1, -1)\n						and activity_cd in (\'tab_access\', \'app_access\')\n						and user_id = [USER_ID]\n						group by affected_record_id\n					) a, fn_app f\n					where a.affected_record_id = f.app_id\n					order by ct desc \n				) b,\n				(SELECT @rn := 0) t2\n		) mm where rowId &lt;= 4\n	)b\n	where a.Date between date_add( curdate(), interval -6 day) and  curdate()\n) l left outer join\n(\n	select app_name,  DATE(audit_date) audit_date_1 ,count(*) ct from fn_audit_log a, fn_app b\n	where user_id = [USER_ID]\n	and audit_date &gt; date_add( curdate(), interval -6 day)\n	and activity_cd in (\'tab_access\', \'app_access\')\n	and a.affected_record_id = b.app_id\n	and b.app_id &lt;&gt; 1\n	and b.app_id in \n	(\n		SELECT  \n			app_id\n		from\n		(\n			select @rn := @rn+1 AS rowId, app_id from \n				(\n					select app_id, ct from \n					(\n						select affected_record_id app_id, count(*) ct\n						from fn_audit_log \n						where audit_date &gt; date_add( curdate(), interval -6 day)\n						and affected_record_id not in ( 1, -1)\n						and activity_cd in (\'tab_access\', \'app_access\')\n						and user_id = [USER_ID]\n						group by affected_record_id\n					) a\n					order by ct desc \n				) b,\n				(SELECT @rn := 0) t2\n		) mm \n	)\n	group by app_name,  DATE(audit_date)\n) r\non l.Date = r.audit_date_1\nand l.app_name = r.app_name</reportSQL>\n    <reportTitle></reportTitle>\n    <reportSubTitle></reportSubTitle>\n    <reportHeader></reportHeader>\n    <frozenColumns>0</frozenColumns>\n    <emptyMessage>Your Search didn\'t yield any results.</emptyMessage>\n    <dataGridAlign>left</dataGridAlign>\n    <reportFooter></reportFooter>\n    <numFormCols>1</numFormCols>\n    <displayOptions>NNNNNNN</displayOptions>\n    <dataContainerHeight>100</dataContainerHeight>\n    <dataContainerWidth>100</dataContainerWidth>\n    <allowSchedule>N</allowSchedule>\n    <multiGroupColumn>N</multiGroupColumn>\n    <topDown>N</topDown>\n    <sizedByContent>N</sizedByContent>\n    <comment>N|</comment>\n    <dataSourceList>\n        <dataSource tableId=\"du0\">\n            <tableName>DUAL</tableName>\n            <tablePK></tablePK>\n            <displayName>DUAL</displayName>\n            <dataColumnList>\n                <dataColumn colId=\"audit_date\">\n                    <tableId>du0</tableId>\n                    <dbColName>l.date</dbColName>\n                    <colName>l.date</colName>\n                    <displayName>audit_date_1</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>1</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <colOnChart>LEGEND</colOnChart>\n                    <chartSeq>1</chartSeq>\n                    <chartColor></chartColor>\n                    <chartLineType></chartLineType>\n                    <chartSeries>false</chartSeries>\n                    <dbColType>VARCHAR2</dbColType>\n                    <chartGroup></chartGroup>\n                    <yAxis></yAxis>\n                </dataColumn>\n                <dataColumn colId=\"app_id\">\n                    <tableId>du0</tableId>\n                    <dbColName>app_id</dbColName>\n                    <colName>app_id</colName>\n                    <displayName>app_id</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>2</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <dbColType>VARCHAR2</dbColType>\n                </dataColumn>\n                <dataColumn colId=\"app_name\">\n                    <tableId>du0</tableId>\n                    <dbColName>IF(CHAR_LENGTH(l.app_name) &gt;14, CONCAT(CONCAT(SUBSTR(l.app_name,1,7),\'...\'), SUBSTR(l.app_name, CHAR_LENGTH(l.app_name)-3,CHAR_LENGTH(l.app_name))) , l.app_name)</dbColName>\n                    <colName>IF(CHAR_LENGTH(l.app_name) &gt;14, CONCAT(CONCAT(SUBSTR(l.app_name,1,7),\'...\'), SUBSTR(l.app_name, CHAR_LENGTH(l.app_name)-3,CHAR_LENGTH(l.app_name))) , l.app_name)</colName>\n                    <displayName>app_name</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>3</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <chartSeq>2</chartSeq>\n                    <chartColor></chartColor>\n                    <chartLineType></chartLineType>\n                    <chartSeries>true</chartSeries>\n                    <dbColType>VARCHAR2</dbColType>\n                    <chartGroup></chartGroup>\n                    <yAxis></yAxis>\n                </dataColumn>\n                <dataColumn colId=\"ct\">\n                    <tableId>du0</tableId>\n                    <dbColName>IFNULL(r.ct,0)</dbColName>\n                    <colName>IFNULL(r.ct,0)</colName>\n                    <displayName>ct</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>4</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <colOnChart>0</colOnChart>\n                    <chartSeq>1</chartSeq>\n                    <chartColor></chartColor>\n                    <chartLineType></chartLineType>\n                    <chartSeries>false</chartSeries>\n                    <dbColType>VARCHAR2</dbColType>\n                    <chartGroup></chartGroup>\n                    <yAxis></yAxis>\n                </dataColumn>\n            </dataColumnList>\n        </dataSource>\n    </dataSourceList>\n    <reportInNewWindow>false</reportInNewWindow>\n    <displayFolderTree>false</displayFolderTree>\n    <maxRowsInExcelDownload>500</maxRowsInExcelDownload>\n    <chartAdditionalOptions>\n        <chartOrientation>vertical</chartOrientation>\n        <hidechartLegend>N</hidechartLegend>\n        <legendPosition>bottom</legendPosition>\n        <labelAngle>up90</labelAngle>\n        <rangeAxisUpperLimit></rangeAxisUpperLimit>\n        <rangeAxisLowerLimit></rangeAxisLowerLimit>\n        <animate>true</animate>\n        <animateAnimatedChart>true</animateAnimatedChart>\n        <stacked>true</stacked>\n        <barControls>false</barControls>\n        <xAxisDateType>false</xAxisDateType>\n        <lessXaxisTickers>false</lessXaxisTickers>\n        <timeAxis>true</timeAxis>\n        <logScale>false</logScale>\n        <topMargin>30</topMargin>\n        <bottomMargin>50</bottomMargin>\n        <rightMargin>60</rightMargin>\n        <leftMargin>100</leftMargin>\n    </chartAdditionalOptions>\n    <folderId>NULL</folderId>\n    <isOneTimeScheduleAllowed>N</isOneTimeScheduleAllowed>\n    <isHourlyScheduleAllowed>N</isHourlyScheduleAllowed>\n    <isDailyScheduleAllowed>N</isDailyScheduleAllowed>\n    <isDailyMFScheduleAllowed>N</isDailyMFScheduleAllowed>\n    <isWeeklyScheduleAllowed>N</isWeeklyScheduleAllowed>\n    <isMonthlyScheduleAllowed>N</isMonthlyScheduleAllowed>\n</customReport>\n',1,'2019-08-08 08:43:27',1,'2019-08-08 08:43:27','',0,1,NULL,0,0),(18,'Application Usage bar Wid','',1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<customReport pageSize=\"200\" reportType=\"Linear\">\n    <reportName>Application Usage Line Wid</reportName>\n    <reportDescr></reportDescr>\n    <dbInfo>local</dbInfo>\n    <dbType>mysql</dbType>\n    <chartType>TimeSeriesChart</chartType>\n    <chartMultiSeries>N</chartMultiSeries>\n    <chartWidth>700</chartWidth>\n    <chartHeight>300</chartHeight>\n    <showChartTitle>false</showChartTitle>\n    <public>false</public>\n    <hideFormFieldAfterRun>false</hideFormFieldAfterRun>\n    <createId>27</createId>\n    <createDate>2017-01-28-05:00</createDate>\n    <reportSQL>SELECT \n	l.date audit_date, \n	IF(CHAR_LENGTH(l.app_name) &gt;14, CONCAT(CONCAT(SUBSTR(l.app_name,1,7),\'...\'), SUBSTR(l.app_name, CHAR_LENGTH(l.app_name)-3,CHAR_LENGTH(l.app_name))) , l.app_name) app_name, \n	IFNULL(r.ct,0) ct \nfrom\n(\n	select a.Date, app_id, app_name\n	from (\n	    select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as Date\n	    from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n	    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n	    cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c\n	) a, \n	(\n		SELECT  \n			app_id, app_name\n		from\n		(\n			select @rn := @rn+1 AS rowId, app_id, app_name from \n				(\n					select app_id, app_name, ct from \n					(\n						select affected_record_id, count(*) ct\n						from fn_audit_log l\n						where audit_date &gt; date_add( curdate(), interval -30 day)\n						and affected_record_id not in ( 1, -1)\n						and activity_cd in (\'tab_access\', \'app_access\')\n						and user_id = [USER_ID]\n						group by affected_record_id\n					) a, fn_app f\n					where a.affected_record_id = f.app_id\n					order by ct desc \n				) b,\n				(SELECT @rn := 0) t2\n		) mm where rowId &lt;= 4\n	)b\n	where a.Date between date_add( curdate(), interval -30 day) and  curdate()\n) l left outer join\n(\n	select app_name,  DATE(audit_date) audit_date_1 ,count(*) ct from fn_audit_log a, fn_app b\n	where user_id = [USER_ID]\n	and audit_date &gt; date_add( curdate(), interval -30 day)\n	and activity_cd in (\'tab_access\', \'app_access\')\n	and a.affected_record_id = b.app_id\n	and b.app_id &lt;&gt; 1\n	and b.app_id in \n	(\n		SELECT  \n			app_id\n		from\n		(\n			select @rn := @rn+1 AS rowId, app_id from \n				(\n					select app_id, ct from \n					(\n						select affected_record_id app_id, count(*) ct\n						from fn_audit_log \n						where audit_date &gt; date_add( curdate(), interval -30 day)\n						and affected_record_id not in ( 1, -1)\n						and activity_cd in (\'tab_access\', \'app_access\')\n						and user_id = [USER_ID]\n						group by affected_record_id\n					) a\n					order by ct desc \n				) b,\n				(SELECT @rn := 0) t2\n		) mm \n	)\n	group by app_name,  DATE(audit_date)\n) r\non l.Date = r.audit_date_1\nand l.app_name = r.app_name</reportSQL>\n    <reportTitle></reportTitle>\n    <reportSubTitle></reportSubTitle>\n    <reportHeader></reportHeader>\n    <frozenColumns>0</frozenColumns>\n    <emptyMessage>Your Search didn\'t yield any results.</emptyMessage>\n    <dataGridAlign>left</dataGridAlign>\n    <reportFooter></reportFooter>\n    <numFormCols>1</numFormCols>\n    <displayOptions>NNNNNNN</displayOptions>\n    <dataContainerHeight>100</dataContainerHeight>\n    <dataContainerWidth>100</dataContainerWidth>\n    <allowSchedule>N</allowSchedule>\n    <multiGroupColumn>N</multiGroupColumn>\n    <topDown>N</topDown>\n    <sizedByContent>N</sizedByContent>\n    <comment>N|</comment>\n    <dataSourceList>\n        <dataSource tableId=\"du0\">\n            <tableName>DUAL</tableName>\n            <tablePK></tablePK>\n            <displayName>DUAL</displayName>\n            <dataColumnList>\n                <dataColumn colId=\"audit_date\">\n                    <tableId>du0</tableId>\n                    <dbColName>l.date</dbColName>\n                    <colName>l.date</colName>\n                    <displayName>audit_date_1</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>1</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <colOnChart>LEGEND</colOnChart>\n                    <chartSeq>1</chartSeq>\n                    <chartSeries>false</chartSeries>\n                    <isRangeAxisFilled>false</isRangeAxisFilled>\n                    <drillinPoPUp>false</drillinPoPUp>\n                    <dbColType>VARCHAR2</dbColType>\n                    <enhancedPagination>false</enhancedPagination>\n                </dataColumn>\n                <dataColumn colId=\"app_name\">\n                    <tableId>du0</tableId>\n                    <dbColName>IF(CHAR_LENGTH(l.app_name) &gt;14, CONCAT(CONCAT(SUBSTR(l.app_name,1,7),\'...\'), SUBSTR(l.app_name, CHAR_LENGTH(l.app_name)-3,CHAR_LENGTH(l.app_name))) , l.app_name)</dbColName>\n                    <colName>IF(CHAR_LENGTH(l.app_name) &gt;14, CONCAT(CONCAT(SUBSTR(l.app_name,1,7),\'...\'), SUBSTR(l.app_name, CHAR_LENGTH(l.app_name)-3,CHAR_LENGTH(l.app_name))) , l.app_name)</colName>\n                    <displayName>app_name</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>2</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <colOnChart>0</colOnChart>\n                    <chartSeq>2</chartSeq>\n                    <chartColor></chartColor>\n                    <chartLineType></chartLineType>\n                    <chartSeries>true</chartSeries>\n                    <isRangeAxisFilled>false</isRangeAxisFilled>\n                    <drillinPoPUp>false</drillinPoPUp>\n                    <dbColType>VARCHAR2</dbColType>\n                    <chartGroup></chartGroup>\n                    <yAxis></yAxis>\n                    <enhancedPagination>false</enhancedPagination>\n                </dataColumn>\n                <dataColumn colId=\"ct\">\n                    <tableId>du0</tableId>\n                    <dbColName>IFNULL(r.ct,0)</dbColName>\n                    <colName>IFNULL(r.ct,0)</colName>\n                    <displayName>ct</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>3</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <colOnChart>0</colOnChart>\n                    <chartSeq>1</chartSeq>\n                    <chartColor></chartColor>\n                    <chartLineType></chartLineType>\n                    <chartSeries>false</chartSeries>\n                    <isRangeAxisFilled>false</isRangeAxisFilled>\n                    <drillinPoPUp>false</drillinPoPUp>\n                    <dbColType>VARCHAR2</dbColType>\n                    <chartGroup></chartGroup>\n                    <yAxis></yAxis>\n                    <enhancedPagination>false</enhancedPagination>\n                </dataColumn>\n            </dataColumnList>\n        </dataSource>\n    </dataSourceList>\n    <reportInNewWindow>false</reportInNewWindow>\n    <displayFolderTree>false</displayFolderTree>\n    <maxRowsInExcelDownload>500</maxRowsInExcelDownload>\n    <chartAdditionalOptions>\n        <chartOrientation>vertical</chartOrientation>\n        <hidechartLegend>N</hidechartLegend>\n        <legendPosition>bottom</legendPosition>\n        <labelAngle>down45</labelAngle>\n        <animate>true</animate>\n        <animateAnimatedChart>true</animateAnimatedChart>\n        <stacked>true</stacked>\n        <barControls>false</barControls>\n        <xAxisDateType>false</xAxisDateType>\n        <lessXaxisTickers>false</lessXaxisTickers>\n        <timeAxis>true</timeAxis>\n        <timeSeriesRender>line</timeSeriesRender>\n        <multiSeries>false</multiSeries>\n        <showXAxisLabel>false</showXAxisLabel>\n        <addXAxisTickers>false</addXAxisTickers>\n        <topMargin>30</topMargin>\n        <bottomMargin>50</bottomMargin>\n        <rightMargin>60</rightMargin>\n        <leftMargin>100</leftMargin>\n    </chartAdditionalOptions>\n    <folderId>NULL</folderId>\n    <drillURLInPoPUpPresent>false</drillURLInPoPUpPresent>\n    <isOneTimeScheduleAllowed>N</isOneTimeScheduleAllowed>\n    <isHourlyScheduleAllowed>N</isHourlyScheduleAllowed>\n    <isDailyScheduleAllowed>N</isDailyScheduleAllowed>\n    <isDailyMFScheduleAllowed>N</isDailyMFScheduleAllowed>\n    <isWeeklyScheduleAllowed>N</isWeeklyScheduleAllowed>\n    <isMonthlyScheduleAllowed>N</isMonthlyScheduleAllowed>\n</customReport>\n',1,'2019-08-08 08:43:27',1,'2019-08-08 08:43:27','',0,1,NULL,0,0),(20,'Average time spend on portal','',1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<customReport pageSize=\"200\" reportType=\"Linear\">\n    <reportName>Average time spend on portal</reportName>\n    <reportDescr></reportDescr>\n    <dbInfo>local</dbInfo>\n    <dbType>mysql</dbType>\n    <chartType>TimeSeriesChart</chartType>\n    <chartMultiSeries>N</chartMultiSeries>\n    <chartWidth>700</chartWidth>\n    <chartHeight>300</chartHeight>\n    <showChartTitle>false</showChartTitle>\n    <public>true</public>\n    <hideFormFieldAfterRun>false</hideFormFieldAfterRun>\n    <createId>27</createId>\n    <createDate>2017-01-28-05:00</createDate>\n    <reportSQL>SELECT \n	d.dat audit_date, \n	\'# of Minutes\' app, \n	coalesce(diff, null, 0) mins \nfrom\n(\n	select * from\n	(\n	select curdate() - INTERVAL (a.a + (10 * b.a) + (100 * c.a)) DAY as dat\n	from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a\n	cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b\n	cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c \n	) d where d.dat between date_add( curdate(), interval -30 day) and  curdate()\n) d left outer join\n(\n	select dat, mi, mx, TIMESTAMPDIFF(MINUTE, coalesce(mi, null, 0), coalesce(mx, null, 0)) + 30  diff\n	from\n	(\n		select DATE(audit_date) dat, coalesce(min(audit_date), null, 0) mi, coalesce(max(audit_date), null, 0) mx\n		from fn_audit_log \n		where user_id = [USER_ID] and DATE(audit_date) between CURDATE()-300 and CURDATE()\n		group by DATE(audit_date)\n	) a\n) a\non a.dat = d.dat\norder by 1</reportSQL>\n    <reportTitle></reportTitle>\n    <reportSubTitle></reportSubTitle>\n    <reportHeader></reportHeader>\n    <frozenColumns>0</frozenColumns>\n    <emptyMessage>Your Search didn\'t yield any results.</emptyMessage>\n    <dataGridAlign>left</dataGridAlign>\n    <reportFooter></reportFooter>\n    <numFormCols>1</numFormCols>\n    <displayOptions>NNNNNNN</displayOptions>\n    <dataContainerHeight>100</dataContainerHeight>\n    <dataContainerWidth>100</dataContainerWidth>\n    <allowSchedule>N</allowSchedule>\n    <multiGroupColumn>N</multiGroupColumn>\n    <topDown>N</topDown>\n    <sizedByContent>N</sizedByContent>\n    <comment>N|</comment>\n    <dataSourceList>\n        <dataSource tableId=\"du0\">\n            <tableName>DUAL</tableName>\n            <tablePK></tablePK>\n            <displayName>DUAL</displayName>\n            <dataColumnList>\n                <dataColumn colId=\"audit_date\">\n                    <tableId>du0</tableId>\n                    <dbColName>d.dat</dbColName>\n                    <colName>d.dat</colName>\n                    <displayName>audit_date_1</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>1</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <colOnChart>LEGEND</colOnChart>\n                    <chartSeq>1</chartSeq>\n                    <chartSeries>false</chartSeries>\n                    <isRangeAxisFilled>false</isRangeAxisFilled>\n                    <drillinPoPUp>false</drillinPoPUp>\n                    <dbColType>VARCHAR2</dbColType>\n                    <enhancedPagination>false</enhancedPagination>\n                </dataColumn>\n                <dataColumn colId=\"app\">\n                    <tableId>du0</tableId>\n                    <dbColName>\'# of Minutes\'</dbColName>\n                    <colName>\'# of Minutes\'</colName>\n                    <displayName>app</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>2</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <chartSeries>true</chartSeries>\n                    <dbColType>VARCHAR2</dbColType>\n                </dataColumn>\n                <dataColumn colId=\"mins\">\n                    <tableId>du0</tableId>\n                    <dbColName>coalesce(diff, null, 0)</dbColName>\n                    <colName>coalesce(diff, null, 0)</colName>\n                    <displayName>mins</displayName>\n                    <displayWidth>10</displayWidth>\n                    <displayWidthInPxls>nullpxpx</displayWidthInPxls>\n                    <displayAlignment>Left</displayAlignment>\n                    <orderSeq>3</orderSeq>\n                    <visible>true</visible>\n                    <calculated>true</calculated>\n                    <colType>VARCHAR2</colType>\n                    <groupBreak>false</groupBreak>\n                    <colOnChart>0</colOnChart>\n                    <chartSeq>1</chartSeq>\n                    <chartColor></chartColor>\n                    <chartLineType></chartLineType>\n                    <chartSeries>false</chartSeries>\n                    <dbColType>VARCHAR2</dbColType>\n                    <chartGroup></chartGroup>\n                    <yAxis></yAxis>\n                </dataColumn>\n            </dataColumnList>\n        </dataSource>\n    </dataSourceList>\n    <reportInNewWindow>false</reportInNewWindow>\n    <displayFolderTree>false</displayFolderTree>\n    <maxRowsInExcelDownload>500</maxRowsInExcelDownload>\n    <chartAdditionalOptions>\n        <chartOrientation>vertical</chartOrientation>\n        <hidechartLegend>N</hidechartLegend>\n        <legendPosition>bottom</legendPosition>\n        <labelAngle>down45</labelAngle>\n        <animate>true</animate>\n        <animateAnimatedChart>true</animateAnimatedChart>\n        <stacked>true</stacked>\n        <barControls>false</barControls>\n        <xAxisDateType>false</xAxisDateType>\n        <lessXaxisTickers>false</lessXaxisTickers>\n        <timeAxis>true</timeAxis>\n        <timeSeriesRender>line</timeSeriesRender>\n        <multiSeries>false</multiSeries>\n        <showXAxisLabel>false</showXAxisLabel>\n        <addXAxisTickers>false</addXAxisTickers>\n        <topMargin>30</topMargin>\n        <bottomMargin>50</bottomMargin>\n        <rightMargin>60</rightMargin>\n        <leftMargin>100</leftMargin>\n    </chartAdditionalOptions>\n    <folderId>NULL</folderId>\n    <drillURLInPoPUpPresent>false</drillURLInPoPUpPresent>\n    <isOneTimeScheduleAllowed>N</isOneTimeScheduleAllowed>\n    <isHourlyScheduleAllowed>N</isHourlyScheduleAllowed>\n    <isDailyScheduleAllowed>N</isDailyScheduleAllowed>\n    <isDailyMFScheduleAllowed>N</isDailyMFScheduleAllowed>\n    <isWeeklyScheduleAllowed>N</isWeeklyScheduleAllowed>\n    <isMonthlyScheduleAllowed>N</isMonthlyScheduleAllowed>\n</customReport>\n',1,'2019-08-08 08:43:27',1,'2019-08-08 08:43:27','',0,1,NULL,0,0);

LOCK TABLES `fn_app` WRITE;
/*!40000 ALTER TABLE `fn_app` DISABLE KEYS */;
INSERT INTO
  `fn_app` (
    `app_Id`,
    `app_name`,
    `app_image_url`,
    `app_description`,
    `app_notes`,
    `app_url`,
    `app_alternate_url`,
    `app_rest_endpoint`,
    `ml_app_name`,
    `ml_app_admin_id`,
    `mots_id`,
    `app_password`,
    `_open`,
    `_enabled`,
    `app_username`,
    `ueb_key`,
    `ueb_secret`,
    `ueb_topic_name`,
    `app_type`,
    `auth_central`,
    `auth_namespace`
  )
VALUES
  (
    1,
    'Default',
    'assets/images/tmp/portal1.png',
    'Some Default Description',
    'Some Default Note',
    'http://localhost',
    'http://localhost',
    'http://localhost:8080/ecompportal',
    'EcompPortal',
    '',
    NULL,
    'dR2NABMkxPaFbIbym87ZwQ==',
    0,
    0,
    'm00468@portal.onap.org',
    'EkrqsjQqZt4ZrPh6',
    NULL,
    NULL,
    1,
    1,
    'org.onap.portal'
  ),(
    2,
    'xDemo App',
    'images/cache/portal-222865671_37476.png',
    NULL,
    NULL,
    'http://portal-sdk.simpledemo.onap.org:30212/ONAPPORTALSDK/welcome.htm',
    NULL,
    'http://portal-sdk:8080/ONAPPORTALSDK/api/v3',
    '',
    '',
    NULL,
    '2VxipM8Z3SETg32m3Gp0FvKS6zZ2uCbCw46WDyK6T5E=',
    0,
    1,
    'Default',
    'ueb_key',
    'ueb_secret',
    'ECOMP-PORTAL-OUTBOX',
    1,
    0,
    NULL
  ),(
    3,
    'DMaaP Bus Ctrl',
    'images/cache/portal944583064_80711.png',
    NULL,
    NULL,
    'http://dmaap-bc.simpledemo.onap.org:/ECOMPDBCAPP/dbc#/dmaap',
    NULL,
    'http://dmaap-bc:8989/ECOMPDBCAPP/api/v2',
    '',
    '',
    NULL,
    'okYTaDrhzibcbGVq5mjkVQ==',
    0,
    0,
    'Default',
    'ueb_key',
    'ueb_secret',
    'ECOMP-PORTAL-OUTBOX',
    1,
    0,
    NULL
  ),(
    4,
    'SDC',
    'images/cache/portal956868231_53879.png',
    NULL,
    NULL,
    'http://sdc.api.fe.simpledemo.onap.org:30206/sdc1/portal',
    NULL,
    'http://sdc-be:8080/api/v3',
    '',
    '',
    NULL,
    'j85yNhyIs7zKYbR1VlwEfNhS6b7Om4l0Gx5O8931sCI=',
    0,
    1,
    'sdc',
    'ueb_key',
    'ueb_secret',
    'ECOMP-PORTAL-OUTBOX',
    1,
    1,
    'org.onap.sdc'
  ),(
    5,
    'Policy',
    'images/cache/portal1470452815_67021.png',
    NULL,
    NULL,
    'https://policy.api.simpledemo.onap.org:30219/onap/policy',
    NULL,
    'https://pap:8443/onap/api/v3',
    '',
    '',
    NULL,
    '2VxipM8Z3SETg32m3Gp0FvKS6zZ2uCbCw46WDyK6T5E=',
    0,
    1,
    'Default',
    'ueb_key_5',
    'ueb_secret',
    'ECOMP-PORTAL-OUTBOX',
    1,
    1,
    'org.onap.policy'
  ),(
    6,
    'Virtual Infrastructure Deployment',
    'images/cache/portal-345993588_92550.png',
    NULL,
    NULL,
    'https://vid.api.simpledemo.onap.org:30200/vid/welcome.htm',
    NULL,
    'https://vid:8443/vid/api/v3',
    '',
    '',
    NULL,
    '2VxipM8Z3SETg32m3Gp0FvKS6zZ2uCbCw46WDyK6T5E=',
    0,
    1,
    'Default',
    '2Re7Pvdkgw5aeAUD',
    'S31PrbOzGgL4hg4owgtx47Da',
    'ECOMP-PORTAL-OUTBOX-90',
    1,
    1,
    'org.onap.vid'
  ),(
    7,
    'A&AI UI',
    'images/cache/portal-345993588_92550.png',
    NULL,
    NULL,
    'https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html#/viewInspect',
    NULL,
    'https://aai-sparky-be.onap:8000/api/v2',
    '',
    '',
    NULL,
    '4LK69amiIFtuzcl6Gsv97Tt7MLhzo03aoOx7dTvdjKQ=',
    0,
    1,
    'aaiui',
    'ueb_key_7',
    'ueb_secret',
    'ECOMP-PORTAL-OUTBOX',
    1,
    1,
    'org.onap.aai'
  ),(
    8,
    'CLI',
    'images/cache/portal-345993588_92550.png',
    NULL,
    NULL,
    'http://cli.api.simpledemo.onap.org:30260/',
    NULL,
    NULL,
    '',
    '',
    NULL,
    '',
    1,
    1,
    '',
    '',
    '',
    '',
    1,
    0,
    NULL
  ),(
    9,
    'MSB',
    'images/cache/portal-345993588_92550.png',
    NULL,
    NULL,
    'http://msb.api.simpledemo.onap.org:30280/iui/microservices/default.html',
    NULL,
    NULL,
    '',
    '',
    NULL,
    '',
    1,
    1,
    '',
    '',
    '',
    '',
    2,
    0,
    NULL
  ),(
    10,
    'SO-Monitoring',
    'images/cache/portal-345993588_92550.png',
    NULL,
    NULL,
    'http://so-monitoring:30224',
    NULL,
    'http://so-monitoring:30224',
    '',
    '',
    NULL,
    'password',
    1,
    1,
    'user',
    '',
    '',
    '',
    1,
    0,
    'SO-Monitoring'
  ),(
    11,
    'LF Acumos Marketplace',
    'images/cache/portal_907838932_26954.png',
    NULL,
    NULL,
    'https://marketplace.acumos.org/#/home',
    NULL,
    NULL,
    '',
    '',
    NULL,
    '',
    1,
    1,
    '',
    '',
    '',
    '',
    2,
    0,
    NULL
  );

/*!40000 ALTER TABLE `fn_app` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `ep_app_function`
--

LOCK TABLES `ep_app_function` WRITE;
/*!40000 ALTER TABLE `ep_app_function` DISABLE KEYS */;
INSERT INTO `ep_app_function` (`app_id`, `function_cd`, `function_name`) VALUES (1,'menu|menu_acc_admin|*','Admin Account Menu'),(1,'menu|menu_admin|*','Admin Menu'),(1,'menu|menu_home|*','Home Menu'),(1,'menu|menu_logout|*','Logout Menu'),(1,'menu|menu_web_analytics|*','Web Analytics'),(1,'url|addWebAnalyticsReport|*','Add Web Analytics Report'),(1,'url|appsFullList|*','Apps Full List'),(1,'url|centralizedApps|*','Centralized Apps'),(1,'url|edit_notification|*','User Notification'),(1,'url|functionalMenu|*','Functional Menu'),(1,'url|getAdminNotifications|*','Admin Notifications'),(1,'url|getAllWebAnalytics|*','Get All Web Analytics'),(1,'url|getFunctionalMenuRole|*','Get Functional Menu Role'),(1,'url|getNotificationAppRoles|*','Get Notification App Roles'),(1,'url|getUserAppsWebAnalytics|*','Get User Apps Web Analytics'),(1,'url|getUserJourneyAnalyticsReport|*','Get User Journey Report'),(1,'url|get_roles%2f%2a|*','getRolesOfApp'),(1,'url|get_role_functions%2f%2a|*','Get Role Functions'),(1,'url|login|*','Login'),(1,'url|notification_code|*','Notification Code'),(1,'url|role_function_list%2fsaveRoleFunction%2f%2a|*','Save Role Function'),(1,'url|saveNotification|*','publish notifications'),(1,'url|syncRoles|*','SyncRoles'),(1,'url|url_role.htm|*','role page'),(1,'url|url_welcome.htm|*','welcome page'),(1,'url|userAppRoles|*','userAppRoles'),(1,'url|userApps|*','User Apps');
/*!40000 ALTER TABLE `ep_app_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_role`
--

LOCK TABLES `fn_role` WRITE;
/*!40000 ALTER TABLE `fn_role` DISABLE KEYS */;
INSERT INTO `fn_role` (`role_id`, `role_name`, `active_yn`, `priority`, `app_id`, `app_role_id`) VALUES (1,'System_Administrator',1,1,NULL,NULL),(16,'Standard_User',1,5,NULL,NULL),(900,'Restricted_App_Role',1,1,NULL,NULL),(950,'Portal_Notification_Admin',1,1,NULL,NULL),(999,'Account_Administrator',1,1,NULL,NULL),(1000,'System_Administrator',1,1,2,1),(1001,'Standard_User',1,1,2,16),(1002,'System_Administrator',1,1,3,1),(1003,'Standard_User',1,1,3,16),(1004,'ADMIN',1,1,4,0),(1005,'TESTOR',1,1,4,1),(1006,'System_Administrator',1,1,5,1),(1007,'Standard_User',1,1,5,16),(1008,'System_Administrator',1,1,6,1),(1009,'Standard_User',1,1,6,16),(1010,'Usage_Analyst',1,1,NULL,NULL),(1011,'View',1,1,7,1),(1012,'Standard_User',1,1,7,16),(2115,'Portal_Usage_Analyst',1,6,NULL,NULL);
/*!40000 ALTER TABLE `fn_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ep_app_role_function`
--

LOCK TABLES `ep_app_role_function` WRITE;
/*!40000 ALTER TABLE `ep_app_role_function` DISABLE KEYS */;
INSERT INTO `ep_app_role_function` (`id`, `app_id`, `role_id`, `function_cd`, `role_app_id`) VALUES (1,1,1,'url|login|*',NULL),(2,1,1,'menu|menu_admin|*',NULL),(3,1,1,'menu|menu_home|*',NULL),(4,1,1,'menu|menu_logout|*',NULL),(5,1,16,'url|login|*',NULL),(6,1,16,'menu|menu_home|*',NULL),(7,1,16,'menu|menu_logout|*',NULL),(8,1,950,'url|edit_notification|*',NULL),(9,1,950,'url|getAdminNotifications|*',NULL),(10,1,950,'url|saveNotification|*',NULL),(11,1,999,'url|userAppRoles|*',NULL),(12,1,999,'url|getAdminNotifications|*',NULL),(13,1,999,'url|userApps|*',NULL),(14,1,1010,'menu|menu_web_analytics|*',NULL),(15,1,2115,'menu|menu_web_analytics|*',NULL),(16,1,1,'menu|menu_acc_admin|*',NULL),(17,1,999,'menu|menu_acc_admin|*',NULL),(18,1,999,'url|centralizedApps|*',NULL),(19,1,999,'url|getAllWebAnalytics|*',NULL),(20,1,999,'url|getFunctionalMenuRole|*',NULL),(21,1,999,'url|getNotificationAppRoles|*',NULL),(22,1,999,'url|getUserAppsWebAnalytics|*',NULL),(23,1,999,'url|getUserJourneyAnalyticsReport|*',NULL),(24,1,999,'url|get_roles%2f%2a|*',NULL),(25,1,999,'url|get_role_functions%2f%2a|*',NULL),(26,1,999,'url|notification_code|*',NULL),(27,1,999,'url|role_function_list%2fsaveRoleFunction%2f%2a|*',NULL),(28,1,999,'url|syncRoles|*',NULL);
/*!40000 ALTER TABLE `ep_app_role_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ep_basic_auth_account`
--

LOCK TABLES `ep_basic_auth_account` WRITE;
/*!40000 ALTER TABLE `ep_basic_auth_account` DISABLE KEYS */;
INSERT INTO `ep_basic_auth_account` (`id`, `ext_app_name`, `username`, `password`, `active_yn`) VALUES (1,'JIRA','jira','6APqvG4AU2rfLgCvMdySwQ==',1);
/*!40000 ALTER TABLE `ep_basic_auth_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ep_microservice`
--

LOCK TABLES `ep_microservice` WRITE;
/*!40000 ALTER TABLE `ep_microservice` DISABLE KEYS */;
INSERT INTO `ep_microservice` (`id`, `name`, `description`, `app_Id`, `endpoint_url`, `security_type`, `username`, `password`, `active`) VALUES (1,'News Microservice','News',1,'http://portal-app:8989/ONAPPORTAL/commonWidgets','Basic Authentication','portal','6APqvG4AU2rfLgCvMdySwQ==',1),(2,'Events Microservice','Events',1,'http://portal-app:8989/ONAPPORTAL/commonWidgets','Basic Authentication','portal','6APqvG4AU2rfLgCvMdySwQ==',1),(3,'Resources Microservice','Resources',1,'http://portal-app:8989/ONAPPORTAL/commonWidgets','Basic Authentication','portal','6APqvG4AU2rfLgCvMdySwQ==',1),(4,'Portal-Common-Scheduler Microservice','Portal-Common-Scheduler',1,'http://portal-app:8989/ONAPPORTAL/commonWidgets','Basic Authentication','portal','6APqvG4AU2rfLgCvMdySwQ==',1);
/*!40000 ALTER TABLE `ep_microservice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ep_microservice_parameter`
--

LOCK TABLES `ep_microservice_parameter` WRITE;
/*!40000 ALTER TABLE `ep_microservice_parameter` DISABLE KEYS */;
INSERT INTO `ep_microservice_parameter` (`id`, `service_id`, `para_key`, `para_value`) VALUES (1,1,'resourceType','NEWS'),(2,2,'resourceType','EVENTS'),(3,3,'resourceType','IMPORTANTRESOURCES'),(4,4,'resourceType',NULL);
/*!40000 ALTER TABLE `ep_microservice_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ep_widget_catalog`
--

LOCK TABLES `ep_widget_catalog` WRITE;
/*!40000 ALTER TABLE `ep_widget_catalog` DISABLE KEYS */;
INSERT INTO `ep_widget_catalog` (`wdg_name`, `service_id`, `wdg_desc`, `wdg_file_loc`, `all_user_flag`) VALUES ('News',1,'News','news-widget.zip',1),('Events',2,'Events','events-widget.zip',1),('Resources',3,'Resources','resources-widget.zip',1),('Portal-Common-Scheduler',4,'Portal-Common-Scheduler','portal-common-scheduler-widget.zip',1);
/*!40000 ALTER TABLE `ep_widget_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ep_widget_catalog_files`
--

LOCK TABLES `ep_widget_catalog_files` WRITE;
/*!40000 ALTER TABLE `ep_widget_catalog_files` DISABLE KEYS */;
INSERT INTO `ep_widget_catalog_files` (`file_id`, `widget_id`, `widget_name`, `framework_js`, `controller_js`, `markup_html`, `widget_css`) VALUES (1,1,'News','var Portal1Widget = (function(window, undefined) {                                                                                      \n                                                                                                                                    \n	var Portal1Widget = Portal1Widget || {};                                                                                                \n	function extractHostPortApp(src) {	                                                                                            \n		\n		Portal1Widget.microserviceId = 1;                                                                                 \n		Portal1Widget.pathArray = src.split( \'/\' );                                                                                     \n			\n		Portal1Widget.widgetName = 1;\n		Portal1Widget.serviceSeperator = Portal1Widget.pathArray[Portal1Widget.pathArray.length - 4];                                           \n		Portal1Widget.commonUrl = src.substring(0, src.lastIndexOf(\"/\" + Portal1Widget.pathArray[Portal1Widget.pathArray.length - 2]));       \n		\n		Portal1Widget.recipientDivDataAttrib = \'data-\' + Portal1Widget.widgetName;                                                          \n		Portal1Widget.controllerName = \'Portal1Ctrl\';                                                                                     \n		Portal1Widget.readyCssFlag = \'portal1-css-ready\';                                                                                       \n		Portal1Widget.readyCssFlagExpectedValue = \'#bada55\';                                                                            \n		Portal1Widget.serviceURL = src.substring(0, src.lastIndexOf(\"/\" + Portal1Widget.serviceSeperator)) + \'/portalApi/microservice/proxy/parameter/\' + 1;  \n	}                                  \n	\n	extractHostPortApp(document.getElementsByTagName(\'script\')[0].src);\n	\n	function loadStylesheet(url) {                                                                                                 \n		var link = document.createElement(\'link\');                                                                                 \n		link.rel = \'stylesheet\';                                                                                                   \n		link.type = \'text/css\';                                                                                                    \n		link.href = url;                                                                                                           \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(link, entry);                                                                                \n	}                                                                                                                              \n                                                                                                                                   \n	function isCssReady(callback) {                                                                                                \n		var testElem = document.createElement(\'span\');                                                                             \n		testElem.id = Portal1Widget.readyCssFlag;	                                                                                   \n		testElem.style = \'color: #fff\';                                                                       \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(testElem, entry);                                                                            \n	                                                                                                                               \n		(function poll() {                                                                                                         \n			var node = document.getElementById(\'css-ready\');                                                                       \n			var value;                                                                                                             \n			if (window.getComputedStyle) {                                                                                         \n				value = document.defaultView.getComputedStyle(testElem, null)                                                      \n						.getPropertyValue(\'color\');                                                                                \n			}                                                                                                                      \n			else if (node.currentStyle) {                                                                                          \n				value = node.currentStyle.color;                                                                                   \n			}                                                                                                                      \n			if (value && value === \'rgb(186, 218, 85)\' || value.toLowerCase() === Portal1Widget.readyCssFlagExpectedValue) {           \n				callback();                                                                                                        \n			} else {                                                                                                               \n				setTimeout(poll, 500);                                                                                             \n			}                                                                                                                      \n		})();                                                                                                                      \n	}                                                                                                                              \n	                                                                                                                               \n	function injectCss(css) {                                                                                                      \n		var style = document.createElement(\'style\');                                                                               \n		style.type = \'text/css\';                                                                                                   \n 		css = css.replace(/\\}/g, \"}\\n\");                                                                                           \n	                                                                                                                               \n		if (style.styleSheet) {                                                                                                    \n			style.styleSheet.cssText = css;                                                                                        \n		} else {                                                                                                                   \n			style.appendChild(document.createTextNode(css));                                                                       \n		}                                                                                                                          \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(style, entry);                                                                               \n	}                                                                                                                              \n	                                                                                                                               \n	function loadScript(url, callback) {                                                                                           \n		var script = document.createElement(\'script\');                                                                             \n		script.src = url;                                                                                                          \n		\n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(script, entry);                                                                              \n		\n		script.onload = script.onreadystatechange = function() {   \n			var rdyState = script.readyState;                                                                                      \n			if (!rdyState || /complete|loaded/.test(script.readyState)) {                                                          \n				callback();                                                                                                        \n				script.onload = null;                                                                                              \n				script.onreadystatechange = null;                                                                                  \n			}                                                                                                                      \n		};                                                                                                                         \n	}                                                                                                                              \n                                                                                                                                   \n	function loadSupportingFiles(callback) {                                                                                       \n		callback();                                                                                                                \n	}                                                                                                                              \n	                                                                                                                               \n	function getWidgetParams() {                                                                                                   \n		                                                                                                                           \n	}                                                                                                                              \n	                                                                                                                               \n	function getWidgetData(widgetUrl, callback) {   	                                                                           \n	var responseData;                      																						   \n	try{                                   																						   \n		jQuery.ajax({                       																					   \n			url: widgetUrl,               																						   \n			method: \"GET\",           																							   \n			xhrFields: {              																							   \n				withCredentials: true       																					   \n			},    \n			crossDomain: true,			\n			success: function (result) {    																				 	   \n				if (result.isOk == false){  																					   \n			                     																							   \n				}else{                      																					   \n					callback(result);		  																					   \n				}                           																					   \n			}                             																					   \n		});                                 																				   \n	}                                      																						   \n	catch(e){                              																						   \n	\n	}                                      																						   \n																							   \n	}                                                                                                   \n	\n	function getMarkupContent(markupLocation, callback){                                                                             \n		                                                                                                                           \n		try{\n			jQuery.ajax({                                                                                                              \n		        url: markupLocation,\n		        method: \"GET\",   \n		        xhrFields: {              																							   \n					withCredentials: true       																					   \n				},   \n				crossDomain: true,		\n		        success: function (result) {                                                                                           \n		            if (result.isOk == false){                                                                                         \n		            	                                                                                                               \n		            }else{                                                                                                             \n		            	callback(result);                                                                                   \n					}                                                                                                                  \n		        }                                                                                                        \n			});       \n		}\n		catch(e){\n			\n		}\n	}                                                                                                                              \n	                                                                                                                               \n	function renderWidget(data, location, $controllerProvider) {                                                                   \n		var div = document.createElement(\'div\');                                                                                   \n		getMarkupContent(Portal1Widget.commonUrl + \"/markup/\" + Portal1Widget.widgetName, function(div){\n			location.append(div);                                                                                                      \n			Portal1Widget.widgetData = data;\n			app.controllerProvider.register(Portal1Widget.controllerName, Portal1Widget.controller);                                           \n			var mController = angular.element(document.getElementById(\"widgets\"));                                                   \n			mController.scope().activateThis(location);   \n		});\n		\n	}                                                                                                                              \n	                                                                                                                               \n	function printAllArtifacts(moduleName, controllerName) {                                                                       \n	    var queue = angular.module(moduleName)._invokeQueue;                                                                       \n	    for(var i=0;i<queue.length;i++) {                                                                                          \n	        var call = queue[i];                                                                                                   \n	        console.log(i + \'. \' + call[0] + \' - \' + call[1] + \' - \' + call[2][0]);                                                \n	    }                                                                                                                          \n	}                                                                                                                              \n	                                                                                                                               \n	function get(name){                                                                                                            \n	   if(name=(new RegExp(\'[?&]\'+encodeURIComponent(name)+\'=([^&]*)\')).exec(location.search))                                     \n	      return decodeURIComponent(name[1]);                                                                                      \n	}                                                                                                                              \n                                                                                                                                   \n	loadSupportingFiles(function() {                                                                                               \n		loadStylesheet(Portal1Widget.commonUrl + \'/\' + Portal1Widget.widgetName + \'/style.css\');                                           \n		loadScript(Portal1Widget.commonUrl + \'/\' + Portal1Widget.widgetName + \'/controller.js\',                                            \n			function() {                                                                                                           \n				$(\'[\'+ Portal1Widget.recipientDivDataAttrib + \']\').each(function() {                                                   \n					var location = jQuery(this);                                                                                   \n					location.removeAttr(Portal1Widget.recipientDivDataAttrib);                                                         \n					var id = location.attr(Portal1Widget.recipientDivDataAttrib);                                                      \n					getWidgetData(Portal1Widget.serviceURL, function(data) { \n						isCssReady(function(){                                                                                     \n							renderWidget(data, location);                                                                          \n						});								                                                                           \n					});                                                                                                            \n				});                                                                                                                \n			}                                                                                                                      \n		);                                                                                                                         \n	});                                                                                                                            \n	                                                                                                                               \n	return Portal1Widget;	                                                                                                           \n})(window);                                                                                                                        \n				','Portal1Widget.controller = function Portal1Ctrl($rootScope, applicationsService , $log,\n			$window, userProfileService, $scope, $cookies, $timeout, $interval,\n			$uibModal, dashboardService, ngDialog) {Portal1Widget=Portal1Widget||{};var res = Portal1Widget.widgetData;\n		\n		var _this = this;	\n\n		//activate spinner\n		this.isLoading = true;\n		$scope.getUserAppsIsDone = false;\n		this.userProfileService = userProfileService;\n		$scope.demoNum = 1;\n		$scope.event_content_show = false;\n		$scope.widgetData = [];\n\n		$scope.editWidgetModalPopup = function(availableData, resourceType) {\n\n			$scope.editData = JSON.stringify(availableData);\n			$scope.availableDataTemp = $scope.availableData;\n			\n		};\n		\n		/*Setting News data*/\n		$scope.newsData = [];\n		$scope.updateNews = function() {\n			$scope.newsData.length=0;\n			//dashboardService.getCommonWidgetData(\'NEWS\').then(function(res) {\n				// $log.info(res.message);\n				var newsDataJsonArray = res.response.items;\n				for (var i = 0; i < newsDataJsonArray.length; i++) {\n					$scope.newsData.push(newsDataJsonArray[i]);\n				}\n			//})[\'catch\'](function(err) {\n			//	$log.error(\'dashboard controller: failed to get news list\', err);\n			//	_this.isLoading = false;\n			//});\n		}\n		$scope.updateNews();\n\n	}\n\n;Portal1Widget.controller.$inject = [\'$rootScope\',\'applicationsService\',\'$log\',\'$window\',\'userProfileService\',\'$scope\',\'$cookies\',\'$timeout\',\'$interval\',\'$uibModal\',\'dashboardService\',\'ngDialog\'];','<div  id=\"widget-news\" ng-controller=\"Portal1Ctrl\" class=\"widget-news-main\">\n		<div att-gridster-item-body class=\"information-section-gridsterContent\">\n			<div class=\"resources\">\n				<ul ng-show=\"newsData.length!=0\">\n					<li ng-repeat=\"item in newsData\"><a id=\"new-widget-{{item.id}}\"\n												href=\"{{item.href}}\" target=\"_blank\" ng-bind=\"item.title\"></a></li>\n				</ul>\n							<div ng-hide=\"newsData.length!=0\">\n								<div class=\"activity-error-container\"\n									style=\"background: rgb(255, 255, 255); overflow: hidden !important; width: 100%;\">\n									<div class=\"activity-error-block\">\n										<i class=\"icon-information full-linear-icon-information\"\n										   style=\"margin-left: 125px; font-size: 90px\"></i> <br>\n										<div class=\"activity-error-msg1\">There\'s currently no\n														news available.</div>\n									</div>\n								</div>\n					</div>\n			</div>\n		</div>\n</div>\n','\n.portal-widget-panel-container {\n	margin-left:150px;\n	width:1500px;\n}\n\n\n.portal-widget-panel-fixed-panel {\n  min-height: 300px;\n  max-height: 1300px;\n  overflow: auto;\n}\n\n.portal-widget-panel-double-middle {\n  min-height: 660px;\n  max-height: 660px;\n  overflow: auto;\n}\n\n.portal-widget-panel-row {\n    margin-right: 0px;\n    margin-left:  0px;\n    width: 2800px;\n}\n\n/*Increases the width of the card/panel */\n.portal-widget-panel-panel-default {\n	width:450px\n}\n\n/*Controls the spacing between the cards */\n.portal-widget-panel-col-sm-3 {\n	width:20.5%\n}\n\n.portal-widget-panel-top {\n    top: 15px;\n	left: 15px;\n}\n\n#portal1-css-ready {\ncolor: #bada55 !important;\n}'),(2,2,'Events','var Portal2Widget = (function(window, undefined) {                                                                                      \n                                                                                                                                    \n	var Portal2Widget = Portal2Widget || {};                                                                                                \n	function extractHostPortApp(src) {	                                                                                            \n		\n		Portal2Widget.microserviceId = 2;                                                                                 \n		Portal2Widget.pathArray = src.split( \'/\' );                                                                                     \n			\n		Portal2Widget.widgetName = 2;\n		Portal2Widget.serviceSeperator = Portal2Widget.pathArray[Portal2Widget.pathArray.length - 4];                                           \n		Portal2Widget.commonUrl = src.substring(0, src.lastIndexOf(\"/\" + Portal2Widget.pathArray[Portal2Widget.pathArray.length - 2]));       \n		\n		Portal2Widget.recipientDivDataAttrib = \'data-\' + Portal2Widget.widgetName;                                                          \n		Portal2Widget.controllerName = \'Portal2Ctrl\';                                                                                     \n		Portal2Widget.readyCssFlag = \'portal2-css-ready\';                                                                                       \n		Portal2Widget.readyCssFlagExpectedValue = \'#bada55\';                                                                            \n		Portal2Widget.serviceURL = src.substring(0, src.lastIndexOf(\"/\" + Portal2Widget.serviceSeperator)) + \'/portalApi/microservice/proxy/parameter/\' + 2;  \n	}                                  \n	\n	extractHostPortApp(document.getElementsByTagName(\'script\')[0].src);\n	\n	function loadStylesheet(url) {                                                                                                 \n		var link = document.createElement(\'link\');                                                                                 \n		link.rel = \'stylesheet\';                                                                                                   \n		link.type = \'text/css\';                                                                                                    \n		link.href = url;                                                                                                           \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(link, entry);                                                                                \n	}                                                                                                                              \n                                                                                                                                   \n	function isCssReady(callback) {                                                                                                \n		var testElem = document.createElement(\'span\');                                                                             \n		testElem.id = Portal2Widget.readyCssFlag;	                                                                                   \n		testElem.style = \'color: #fff\';                                                                       \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(testElem, entry);                                                                            \n	                                                                                                                               \n		(function poll() {                                                                                                         \n			var node = document.getElementById(\'css-ready\');                                                                       \n			var value;                                                                                                             \n			if (window.getComputedStyle) {                                                                                         \n				value = document.defaultView.getComputedStyle(testElem, null)                                                      \n						.getPropertyValue(\'color\');                                                                                \n			}                                                                                                                      \n			else if (node.currentStyle) {                                                                                          \n				value = node.currentStyle.color;                                                                                   \n			}                                                                                                                      \n			if (value && value === \'rgb(186, 218, 85)\' || value.toLowerCase() === Portal2Widget.readyCssFlagExpectedValue) {           \n				callback();                                                                                                        \n			} else {                                                                                                               \n				setTimeout(poll, 500);                                                                                             \n			}                                                                                                                      \n		})();                                                                                                                      \n	}                                                                                                                              \n	                                                                                                                               \n	function injectCss(css) {                                                                                                      \n		var style = document.createElement(\'style\');                                                                               \n		style.type = \'text/css\';                                                                                                   \n 		css = css.replace(/\\}/g, \"}\\n\");                                                                                           \n	                                                                                                                               \n		if (style.styleSheet) {                                                                                                    \n			style.styleSheet.cssText = css;                                                                                        \n		} else {                                                                                                                   \n			style.appendChild(document.createTextNode(css));                                                                       \n		}                                                                                                                          \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(style, entry);                                                                               \n	}                                                                                                                              \n	                                                                                                                               \n	function loadScript(url, callback) {                                                                                           \n		var script = document.createElement(\'script\');                                                                             \n		script.src = url;                                                                                                          \n		\n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(script, entry);                                                                              \n		\n		script.onload = script.onreadystatechange = function() {   \n			var rdyState = script.readyState;                                                                                      \n			if (!rdyState || /complete|loaded/.test(script.readyState)) {                                                          \n				callback();                                                                                                        \n				script.onload = null;                                                                                              \n				script.onreadystatechange = null;                                                                                  \n			}                                                                                                                      \n		};                                                                                                                         \n	}                                                                                                                              \n                                                                                                                                   \n	function loadSupportingFiles(callback) {                                                                                       \n		callback();                                                                                                                \n	}                                                                                                                              \n	                                                                                                                               \n	function getWidgetParams() {                                                                                                   \n		                                                                                                                           \n	}                                                                                                                              \n	                                                                                                                               \n	function getWidgetData(widgetUrl, callback) {   	                                                                           \n	var responseData;                      																						   \n	try{                                   																						   \n		jQuery.ajax({                       																					   \n			url: widgetUrl,               																						   \n			method: \"GET\",           																							   \n			xhrFields: {              																							   \n				withCredentials: true       																					   \n			},    \n			crossDomain: true,			\n			success: function (result) {    																				 	   \n				if (result.isOk == false){  																					   \n			                     																							   \n				}else{                      																					   \n					callback(result);		  																					   \n				}                           																					   \n			}                             																					   \n		});                                 																				   \n	}                                      																						   \n	catch(e){                              																						   \n	\n	}                                      																						   \n																							   \n	}                                                                                                   \n	\n	function getMarkupContent(markupLocation, callback){                                                                             \n		                                                                                                                           \n		try{\n			jQuery.ajax({                                                                                                              \n		        url: markupLocation,\n		        method: \"GET\",   \n		        xhrFields: {              																							   \n					withCredentials: true       																					   \n				},   \n				crossDomain: true,		\n		        success: function (result) {                                                                                           \n		            if (result.isOk == false){                                                                                         \n		            	                                                                                                               \n		            }else{                                                                                                             \n		            	callback(result);                                                                                   \n					}                                                                                                                  \n		        }                                                                                                        \n			});       \n		}\n		catch(e){\n			\n		}\n	}                                                                                                                              \n	                                                                                                                               \n	function renderWidget(data, location, $controllerProvider) {                                                                   \n		var div = document.createElement(\'div\');                                                                                   \n		getMarkupContent(Portal2Widget.commonUrl + \"/markup/\" + Portal2Widget.widgetName, function(div){\n			location.append(div);                                                                                                      \n			Portal2Widget.widgetData = data;\n			app.controllerProvider.register(Portal2Widget.controllerName, Portal2Widget.controller);                                           \n			var mController = angular.element(document.getElementById(\"widgets\"));                                                   \n			mController.scope().activateThis(location);   \n		});\n		\n	}                                                                                                                              \n	                                                                                                                               \n	function printAllArtifacts(moduleName, controllerName) {                                                                       \n	    var queue = angular.module(moduleName)._invokeQueue;                                                                       \n	    for(var i=0;i<queue.length;i++) {                                                                                          \n	        var call = queue[i];                                                                                                   \n	        console.log(i + \'. \' + call[0] + \' - \' + call[1] + \' - \' + call[2][0]);                                                \n	    }                                                                                                                          \n	}                                                                                                                              \n	                                                                                                                               \n	function get(name){                                                                                                            \n	   if(name=(new RegExp(\'[?&]\'+encodeURIComponent(name)+\'=([^&]*)\')).exec(location.search))                                     \n	      return decodeURIComponent(name[1]);                                                                                      \n	}                                                                                                                              \n                                                                                                                                   \n	loadSupportingFiles(function() {                                                                                               \n		loadStylesheet(Portal2Widget.commonUrl + \'/\' + Portal2Widget.widgetName + \'/style.css\');                                           \n		loadScript(Portal2Widget.commonUrl + \'/\' + Portal2Widget.widgetName + \'/controller.js\',                                            \n			function() {                                                                                                           \n				$(\'[\'+ Portal2Widget.recipientDivDataAttrib + \']\').each(function() {                                                   \n					var location = jQuery(this);                                                                                   \n					location.removeAttr(Portal2Widget.recipientDivDataAttrib);                                                         \n					var id = location.attr(Portal2Widget.recipientDivDataAttrib);                                                      \n					getWidgetData(Portal2Widget.serviceURL, function(data) { \n						isCssReady(function(){                                                                                     \n							renderWidget(data, location);                                                                          \n						});								                                                                           \n					});                                                                                                            \n				});                                                                                                                \n			}                                                                                                                      \n		);                                                                                                                         \n	});                                                                                                                            \n	                                                                                                                               \n	return Portal2Widget;	                                                                                                           \n})(window);                                                                                                                        \n				','Portal2Widget.controller = function Portal2Ctrl($rootScope, applicationsService , $log,\n			$window, userProfileService, $scope, $cookies, $timeout, $interval,\n			$uibModal, dashboardService, ngDialog) {Portal2Widget=Portal2Widget||{};var res = Portal2Widget.widgetData;\n		var _this = this;\n\n		//activate spinner\n		this.isLoading = true;\n		$scope.getUserAppsIsDone = false;\n		this.userProfileService = userProfileService;\n		$scope.demoNum = 1;\n		$scope.event_content_show = false;\n		$scope.widgetData = [];\n\n		$scope.editWidgetModalPopup = function(availableData, resourceType) {\n\n			$scope.editData = JSON.stringify(availableData);\n			$scope.availableDataTemp = $scope.availableData;\n			\n		};\n		/*Setting News data*/\n		$scope.eventData = [];\n		$scope.updateEvents = function() {\n\n			$scope.eventData.length=0;\n			//dashboardService.getCommonWidgetData(\'EVENTS\').then(function(res) {\n				var eventDataJsonArray = res.response.items;	\n				for (var i = 0; i < eventDataJsonArray.length; i++) {\n					if(eventDataJsonArray[i].eventDate !=null) {\n						// yyyy-mm-dd\n						eventDataJsonArray[i].year = eventDataJsonArray[i].eventDate.substring(2,4);\n						eventDataJsonArray[i].mon  = eventDataJsonArray[i].eventDate.substring(5,7);\n						eventDataJsonArray[i].day  = eventDataJsonArray[i].eventDate.substring(8,10);\n					}\n					$scope.eventData.push(eventDataJsonArray[i]);\n				}\n			//})[\'catch\'](function(err) {\n			//	$log.error(\'dashboard controller: failed to get Events list\', err);\n			//	_this.isLoading = false;\n			//});\n		}\n		$scope.updateEvents();\n\n\n	}\n;Portal2Widget.controller.$inject = [\'$rootScope\',\'applicationsService\',\'$log\',\'$window\',\'userProfileService\',\'$scope\',\'$cookies\',\'$timeout\',\'$interval\',\'$uibModal\',\'dashboardService\',\'ngDialog\'];','<div  id=\"widget-events\" ng-controller=\"Portal2Ctrl\" class=\"widget-news-main\">\n		<div att-gridster-item-body\n			class=\"information-section-gridsterContent\">\n			<div class=\"events\">\n				<ul ng-show=\"eventData.length!=0\">\n					<li ng-repeat=\"event in eventData\">\n						<div ng-click=\"event_content_show=!event_content_show\">\n							<div class=\"events-date\">{{event.mon}}/{{event.day}}/{{event.year}}\n							</div>\n							<div>\n								<div class=\"event-title-div\">\n									<p ng-bind=\"event.title\"></p>\n								</div>\n								<div>\n									<span class=\"icon-chevron-up\" ng-if=\"event_content_show\"\n										style=\"color: #888; font-size: 22px;\"></span> <span\n										class=\"icon-chevron-down\" ng-if=\"!event_content_show\"\n										style=\"color: #888; font-size: 22px;\"></span>\n\n								</div>\n								<div style=\"clear: both;\"></div>\n							</div>\n						</div>\n						<div class=\"events-content\" ng-show=\"event_content_show\">\n							<div class=\"events-content-body\">\n								<a id=\"event-widget-{{event.id}}\" class=\"events-link\" ng-href=\"{{event.href}}\"\n									target=\"_blank\"> <span ng-bind=\"event.content\"></span>\n								</a>\n							</div>\n							<div></div>\n						</div>\n					</li>\n\n\n				</ul>\n				<div ng-hide=\"eventData.length!=0\">\n					<div class=\"activity-error-container\"\n						style=\"background: rgb(255, 255, 255); overflow: hidden !important; width: 100%;\">\n						<div class=\"activity-error-block\">\n							<i class=\"icon-information full-linear-icon-information\"\n								style=\"margin-left: 125px; font-size: 90px\"></i> <br>\n							<div class=\"activity-error-msg1\">There\'s currently no\n								event available.</div>\n						</div>\n					</div>\n				</div>\n			</div>\n	  </div>\n</div>\n','\n.portal-widget-panel-container {\n	margin-left:150px;\n	width:1500px;\n}\n\n\n.portal-widget-panel-fixed-panel {\n  min-height: 300px;\n  max-height: 1300px;\n  overflow: auto;\n}\n\n.portal-widget-panel-double-middle {\n  min-height: 660px;\n  max-height: 660px;\n  overflow: auto;\n}\n\n.portal-widget-panel-row {\n    margin-right: 0px;\n    margin-left:  0px;\n    width: 2800px;\n}\n\n/*Increases the width of the card/panel */\n.portal-widget-panel-panel-default {\n	width:450px\n}\n\n/*Controls the spacing between the cards */\n.portal-widget-panel-col-sm-3 {\n	width:20.5%\n}\n\n.portal-widget-panel-top {\n    top: 15px;\n	left: 15px;\n}\n\n#portal2-css-ready {\ncolor: #bada55 !important;\n}'),(3,3,'Resources','var Portal3Widget = (function(window, undefined) {                                                                                      \n                                                                                                                                    \n	var Portal3Widget = Portal3Widget || {};                                                                                                \n	function extractHostPortApp(src) {	                                                                                            \n		\n		Portal3Widget.microserviceId = 3;                                                                                 \n		Portal3Widget.pathArray = src.split( \'/\' );                                                                                     \n			\n		Portal3Widget.widgetName = 3;\n		Portal3Widget.serviceSeperator = Portal3Widget.pathArray[Portal3Widget.pathArray.length - 4];                                           \n		Portal3Widget.commonUrl = src.substring(0, src.lastIndexOf(\"/\" + Portal3Widget.pathArray[Portal3Widget.pathArray.length - 2]));       \n		\n		Portal3Widget.recipientDivDataAttrib = \'data-\' + Portal3Widget.widgetName;                                                          \n		Portal3Widget.controllerName = \'Portal3Ctrl\';                                                                                     \n		Portal3Widget.readyCssFlag = \'portal3-css-ready\';                                                                                       \n		Portal3Widget.readyCssFlagExpectedValue = \'#bada55\';                                                                            \n		Portal3Widget.serviceURL = src.substring(0, src.lastIndexOf(\"/\" + Portal3Widget.serviceSeperator)) + \'/portalApi/microservice/proxy/parameter/\' + 3;  \n	}                                  \n	\n	extractHostPortApp(document.getElementsByTagName(\'script\')[0].src);\n	\n	function loadStylesheet(url) {                                                                                                 \n		var link = document.createElement(\'link\');                                                                                 \n		link.rel = \'stylesheet\';                                                                                                   \n		link.type = \'text/css\';                                                                                                    \n		link.href = url;                                                                                                           \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(link, entry);                                                                                \n	}                                                                                                                              \n                                                                                                                                   \n	function isCssReady(callback) {                                                                                                \n		var testElem = document.createElement(\'span\');                                                                             \n		testElem.id = Portal3Widget.readyCssFlag;	                                                                                   \n		testElem.style = \'color: #fff\';                                                                       \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(testElem, entry);                                                                            \n	                                                                                                                               \n		(function poll() {                                                                                                         \n			var node = document.getElementById(\'css-ready\');                                                                       \n			var value;                                                                                                             \n			if (window.getComputedStyle) {                                                                                         \n				value = document.defaultView.getComputedStyle(testElem, null)                                                      \n						.getPropertyValue(\'color\');                                                                                \n			}                                                                                                                      \n			else if (node.currentStyle) {                                                                                          \n				value = node.currentStyle.color;                                                                                   \n			}                                                                                                                      \n			if (value && value === \'rgb(186, 218, 85)\' || value.toLowerCase() === Portal3Widget.readyCssFlagExpectedValue) {           \n				callback();                                                                                                        \n			} else {                                                                                                               \n				setTimeout(poll, 500);                                                                                             \n			}                                                                                                                      \n		})();                                                                                                                      \n	}                                                                                                                              \n	                                                                                                                               \n	function injectCss(css) {                                                                                                      \n		var style = document.createElement(\'style\');                                                                               \n		style.type = \'text/css\';                                                                                                   \n 		css = css.replace(/\\}/g, \"}\\n\");                                                                                           \n	                                                                                                                               \n		if (style.styleSheet) {                                                                                                    \n			style.styleSheet.cssText = css;                                                                                        \n		} else {                                                                                                                   \n			style.appendChild(document.createTextNode(css));                                                                       \n		}                                                                                                                          \n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(style, entry);                                                                               \n	}                                                                                                                              \n	                                                                                                                               \n	function loadScript(url, callback) {                                                                                           \n		var script = document.createElement(\'script\');                                                                             \n		script.src = url;                                                                                                          \n		\n		var entry = document.getElementsByTagName(\'script\')[0];                                                                    \n		entry.parentNode.insertBefore(script, entry);                                                                              \n		\n		script.onload = script.onreadystatechange = function() {   \n			var rdyState = script.readyState;                                                                                      \n			if (!rdyState || /complete|loaded/.test(script.readyState)) {                                                          \n				callback();                                                                                                        \n				script.onload = null;                                                                                              \n				script.onreadystatechange = null;                                                                                  \n			}                                                                                                                      \n		};                                                                                                                         \n	}                                                                                                                              \n                                                                                                                                   \n	function loadSupportingFiles(callback) {                                                                                       \n		callback();                                                                                                                \n	}                                                                                                                              \n	                                                                                                                               \n	function getWidgetParams() {                                                                                                   \n		                                                                                                                           \n	}                                                                                                                              \n	                                                                                                                               \n	function getWidgetData(widgetUrl, callback) {   	                                                                           \n	var responseData;                      																						   \n	try{                                   																						   \n		jQuery.ajax({                       																					   \n			url: widgetUrl,               																						   \n			method: \"GET\",           																							   \n			xhrFields: {              																							   \n				withCredentials: true       																					   \n			},    \n			crossDomain: true,			\n			success: function (result) {    																				 	   \n				if (result.isOk == false){  																					   \n			                     																							   \n				}else{                      																					   \n					callback(result);		  																					   \n				}                           																					   \n			}                             																					   \n		});                                 																				   \n	}                                      																						   \n	catch(e){                              																						   \n	\n	}                                      																						   \n																							   \n	}                                                                                                   \n	\n	function getMarkupContent(markupLocation, callback){                                                                             \n		                                                                                                                           \n		try{\n			jQuery.ajax({                                                                                                              \n		        url: markupLocation,\n		        method: \"GET\",   \n		        xhrFields: {              																							   \n					withCredentials: true       																					   \n				},   \n				crossDomain: true,		\n		        success: function (result) {                                                                                           \n		            if (result.isOk == false){                                                                                         \n		            	                                                                                                               \n		            }else{                                                                                                             \n		            	callback(result);                                                                                   \n					}                                                                                                                  \n		        }                                                                                                        \n			});       \n		}\n		catch(e){\n			\n		}\n	}                                                                                                                              \n	                                                                                                                               \n	function renderWidget(data, location, $controllerProvider) {                                                                   \n		var div = document.createElement(\'div\');                                                                                   \n		getMarkupContent(Portal3Widget.commonUrl + \"/markup/\" + Portal3Widget.widgetName, function(div){\n			location.append(div);                                                                                                      \n			Portal3Widget.widgetData = data;\n			app.controllerProvider.register(Portal3Widget.controllerName, Portal3Widget.controller);                                           \n			var mController = angular.element(document.getElementById(\"widgets\"));                                                   \n			mController.scope().activateThis(location);   \n		});\n		\n	}                                                                                                                              \n	                                                                                                                               \n	function printAllArtifacts(moduleName, controllerName) {                                                                       \n	    var queue = angular.module(moduleName)._invokeQueue;                                                                       \n	    for(var i=0;i<queue.length;i++) {                                                                                          \n	        var call = queue[i];                                                                                                   \n	        console.log(i + \'. \' + call[0] + \' - \' + call[1] + \' - \' + call[2][0]);                                                \n	    }                                                                                                                          \n	}                                                                                                                              \n	                                                                                                                               \n	function get(name){                                                                                                            \n	   if(name=(new RegExp(\'[?&]\'+encodeURIComponent(name)+\'=([^&]*)\')).exec(location.search))                                     \n	      return decodeURIComponent(name[1]);                                                                                      \n	}                                                                                                                              \n                                                                                                                                   \n	loadSupportingFiles(function() {                                                                                               \n		loadStylesheet(Portal3Widget.commonUrl + \'/\' + Portal3Widget.widgetName + \'/style.css\');                                           \n		loadScript(Portal3Widget.commonUrl + \'/\' + Portal3Widget.widgetName + \'/controller.js\',                                            \n			function() {                                                                                                           \n				$(\'[\'+ Portal3Widget.recipientDivDataAttrib + \']\').each(function() {                                                   \n					var location = jQuery(this);                                                                                   \n					location.removeAttr(Portal3Widget.recipientDivDataAttrib);                                                         \n					var id = location.attr(Portal3Widget.recipientDivDataAttrib);                                                      \n					getWidgetData(Portal3Widget.serviceURL, function(data) { \n						isCssReady(function(){                                                                                     \n							renderWidget(data, location);                                                                          \n						});								                                                                           \n					});                                                                                                            \n				});                                                                                                                \n			}                                                                                                                      \n		);                                                                                                                         \n	});                                                                                                                            \n	                                                                                                                               \n	return Portal3Widget;	                                                                                                           \n})(window);                                                                                                                        \n				','Portal3Widget.controller = function Portal3Ctrl($rootScope, applicationsService , $log,\n			$window, userProfileService, $scope, $cookies, $timeout, $interval,\n			$uibModal, dashboardService, ngDialog) {Portal3Widget=Portal3Widget||{};var res = Portal3Widget.widgetData;\n		\n		var _this = this;	\n\n		//activate spinner\n		this.isLoading = true;\n		$scope.getUserAppsIsDone = false;\n		this.userProfileService = userProfileService;\n		$scope.demoNum = 1;\n		$scope.event_content_show = false;\n		$scope.widgetData = [];\n\n		$scope.editWidgetModalPopup = function(availableData, resourceType) {\n\n			$scope.editData = JSON.stringify(availableData);\n			$scope.availableDataTemp = $scope.availableData;\n			\n		};\n		\n		/*Setting News data*/\n		$scope.newsData = [];\n		$scope.updateNews = function() {\n			$scope.newsData.length=0;\n			//dashboardService.getCommonWidgetData(\'NEWS\').then(function(res) {\n				// $log.info(res.message);\n				var newsDataJsonArray = res.response.items;\n				for (var i = 0; i < newsDataJsonArray.length; i++) {\n					$scope.newsData.push(newsDataJsonArray[i]);\n				}\n			//})[\'catch\'](function(err) {\n			//	$log.error(\'dashboard controller: failed to get news list\', err);\n			//	_this.isLoading = false;\n			//});\n		}\n		$scope.updateNews();\n\n	}\n\n;Portal3Widget.controller.$inject = [\'$rootScope\',\'applicationsService\',\'$log\',\'$window\',\'userProfileService\',\'$scope\',\'$cookies\',\'$timeout\',\'$interval\',\'$uibModal\',\'dashboardService\',\'ngDialog\'];','<div  id=\"widget-news\" ng-controller=\"Portal3Ctrl\" class=\"widget-news-main\">\n		<div att-gridster-item-body class=\"information-section-gridsterContent\">\n			<div class=\"resources\">\n				<ul ng-show=\"newsData.length!=0\">\n					<li ng-repeat=\"item in newsData\"><a id=\"resource-widget-{{item.id}}\"\n												href=\"{{item.href}}\" target=\"_blank\" ng-bind=\"item.title\"></a></li>\n				</ul>\n							<div ng-hide=\"newsData.length!=0\">\n								<div class=\"activity-error-container\"\n									style=\"background: rgb(255, 255, 255); overflow: hidden !important; width: 100%;\">\n									<div class=\"activity-error-block\">\n										<i class=\"icon-information full-linear-icon-information\"\n										   style=\"margin-left: 125px; font-size: 90px\"></i> <br>\n										<div class=\"activity-error-msg1\">There\'s currently no\n														news available.</div>\n									</div>\n								</div>\n					</div>\n			</div>\n		</div>\n</div>\n','\n.portal-widget-panel-container {\n	margin-left:150px;\n	width:1500px;\n}\n\n\n.portal-widget-panel-fixed-panel {\n  min-height: 300px;\n  max-height: 1300px;\n  overflow: auto;\n}\n\n.portal-widget-panel-double-middle {\n  min-height: 660px;\n  max-height: 660px;\n  overflow: auto;\n}\n\n.portal-widget-panel-row {\n    margin-right: 0px;\n    margin-left:  0px;\n    width: 2800px;\n}\n\n/*Increases the width of the card/panel */\n.portal-widget-panel-panel-default {\n	width:450px\n}\n\n/*Controls the spacing between the cards */\n.portal-widget-panel-col-sm-3 {\n	width:20.5%\n}\n\n.portal-widget-panel-top {\n    top: 15px;\n	left: 15px;\n}\n\n#portal3-css-ready {\ncolor: #bada55 !important;\n}');
/*!40000 ALTER TABLE `ep_widget_catalog_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_app_contact_us`
--

LOCK TABLES `fn_app_contact_us` WRITE;
/*!40000 ALTER TABLE `fn_app_contact_us` DISABLE KEYS */;
INSERT INTO `fn_app_contact_us` (`app_id`, `contact_name`, `contact_email`, `url`, `active_yn`, `description`) VALUES (2,'Portal SDK Team','portal@lists.onap.org','https://wiki.onap.org/display/DW/Approved+Projects',NULL,'xDemo Application'),(3,'DBC Team','portal@lists.onap.org','https://wiki.onap.org/display/DW/Approved+Projects',NULL,'DBC.'),(4,'SDC Team','sdc@lists.onap.org','https://wiki.onap.org/display/DW/Approved+Projects',NULL,'Service Design and Creation (SDC).'),(5,'Policy Team','policy@lists.onap.org','https://wiki.onap.org/display/DW/Approved+Projects',NULL,'Policy.'),(6,'VID Team','vid@lists.onap.org','https://wiki.onap.org/display/DW/Approved+Projects',NULL,'Virtual Infrastructure Design.'),(7,'AAI UI Team','aaiui@lists.onap.org','https://wiki.onap.org/display/DW/Approved+Projects',NULL,'AAI UI Application'),(8,'CLI Team','onap-discuss@lists.onap.org','https://wiki.onap.org/display/DW/Approved+Projects',NULL,'CLI Application'),(10,'SO Team','so@lists.onap.org','https://wiki.onap.org/display/DW/Approved+Projects',NULL,'Service Orchestration (SO).');
/*!40000 ALTER TABLE `fn_app_contact_us` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_common_widget_data`
--

LOCK TABLES `fn_common_widget_data` WRITE;
/*!40000 ALTER TABLE `fn_common_widget_data` DISABLE KEYS */;
INSERT INTO `fn_common_widget_data` (`id`, `category`, `href`, `title`, `content`, `event_date`, `sort_order`) VALUES (6,'NEWS','https://www.onap.org/announcement/2017/09/27/open-network-automation-platform-onap-project-continues-rapid-membership-growth','Open Network Automation Platform (ONAP) Project Continues Rapid Membership Growth',NULL,NULL,10),(7,'NEWS','https://www.onap.org/announcement/2017/02/23/the-linux-foundation-announces-the-formation-of-a-new-project-to-help-accelerate-innovation-in-open-networking-automation','The Linux Foundation Announces Merger of Open Source ECOMP and OPEN-O to Form New Open Network Automation Platform (ONAP) Project',NULL,NULL,20),(8,'NEWS','http://about.att.com/story/orange_testing_att_open_source_ecomp_platform.html','Orange Testing AT&Ts Open Source ECOMP Platform for Building Software-Defined Network Capabilities',NULL,NULL,30),(9,'NEWS','http://about.att.com/innovationblog/linux_foundation','Opening up ECOMP: Our Network Operating System for SDN',NULL,NULL,40),(10,'EVENTS','https://onapbeijing2017.sched.com/list/descriptions/','ONAP Beijing Release Developer Forum',NULL,'2017-12-11',1),(11,'EVENTS','https://events.linuxfoundation.org/events/open-networking-summit-north-america-2018','Open Networking Summit',NULL,'2018-03-26',2),(12,'IMPORTANTRESOURCES','http://onap.readthedocs.io/en/latest/guides/onap-developer/developing/index.html','Development Guides',NULL,NULL,1),(13,'IMPORTANTRESOURCES','https://wiki.onap.org/','ONAP Wiki',NULL,NULL,2),(14,'IMPORTANTRESOURCES','http://onap.readthedocs.io/en/latest/guides/onap-developer/developing/index.html#portal-platform','ONAP Portal Documentation',NULL,NULL,3),(15,'IMPORTANTRESOURCES','http://onap.readthedocs.io/en/latest/guides/onap-developer/architecture/index.html#architecture','ONAP Architecture',NULL,NULL,4);
/*!40000 ALTER TABLE `fn_common_widget_data` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `fn_display_text`
--

LOCK TABLES `fn_display_text` WRITE;
/*!40000 ALTER TABLE `fn_display_text` DISABLE KEYS */;
INSERT INTO `fn_display_text` (`id`, `language_id`, `text_id`, `text_label`) VALUES (1,1,2,'Home'),(2,1,3,'Application Catalog'),(3,1,4,'Widget Catalog'),(4,1,5,'Admins'),(5,1,6,'Roles'),(6,1,7,'Users'),(7,1,8,'Portal Admins'),(8,1,9,'Application Onboarding'),(9,1,10,'Widget Onboarding'),(10,1,11,'Edit Functional Menu'),(11,1,12,'User Notifications'),(12,1,13,'Microservice Onboarding'),(13,1,15,'App Account Management'),(14,2,2,'ä¸»é¡µ'),(15,2,3,'åº”ç”¨ç›®å½•'),(16,2,4,'éƒ¨ä»¶ç›®å½•'),(17,2,5,'ç®¡ç†å‘˜'),(18,2,6,'è§’è‰²'),(19,2,7,'ç”¨æˆ·'),(20,2,8,'é—¨æˆ·ç®¡ç†å‘˜'),(21,2,9,'åº”ç”¨ç®¡ç†'),(22,2,10,'éƒ¨ä»¶ç®¡ç†'),(23,2,11,'ç¼–è¾‘åŠŸèƒ½èœå•'),(24,2,12,'ç”¨æˆ·é€šçŸ¥'),(25,2,13,'å¾®æœåŠ¡ç®¡ç†'),(26,2,15,'åº”ç”¨è´¦æˆ·ç®¡ç†');
/*!40000 ALTER TABLE `fn_display_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_function`
--

LOCK TABLES `fn_function` WRITE;
/*!40000 ALTER TABLE `fn_function` DISABLE KEYS */;
INSERT INTO `fn_function` (`function_cd`, `function_name`) VALUES ('edit_notification','User Notification'),('getAdminNotifications','Admin Notifications'),('login','Login'),('menu_admin','Admin Menu'),('menu_ajax','Ajax Menu'),('menu_customer','Customer Menu'),('menu_customer_create','Customer Create'),('menu_feedback','Feedback Menu'),('menu_help','Help Menu'),('menu_home','Home Menu'),('menu_job','Job Menu'),('menu_job_create','Job Create'),('menu_job_designer','Process in Designer view'),('menu_logout','Logout Menu'),('menu_map','Map Menu'),('menu_notes','Notes Menu'),('menu_process','Process List'),('menu_profile','Profile Menu'),('menu_profile_create','Profile Create'),('menu_profile_import','Profile Import'),('menu_reports','Reports Menu'),('menu_sample','Sample Pages Menu'),('menu_tab','Sample Tab Menu'),('menu_task','Task Menu'),('menu_task_search','Task Search'),('menu_web_analytics','Web Analytics'),('saveNotification','publish notifications'),('view_reports','View Raptor reports');
/*!40000 ALTER TABLE `fn_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_language`
--

LOCK TABLES `fn_language` WRITE;
/*!40000 ALTER TABLE `fn_language` DISABLE KEYS */;
INSERT INTO `fn_language` (`language_name`, `language_alias`) VALUES ('English','EN'),('ç®€ä½“ä¸­æ–‡','CN');
/*!40000 ALTER TABLE `fn_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_lu_activity`
--

LOCK TABLES `fn_lu_activity` WRITE;
/*!40000 ALTER TABLE `fn_lu_activity` DISABLE KEYS */;
INSERT INTO `fn_lu_activity` (`activity_cd`, `activity`) VALUES ('add_child_role','add_child_role'),('add_role','add_role'),('add_role_function','add_role_function'),('add_user_role','add_user_role'),('apa','Add Portal Admin'),('app_access','App Access'),('dpa','Delete Portal Admin'),('eaaf','External auth add function'),('eaar','External auth add role'),('eadf','External auth delete function'),('eadr','External auth delete role'),('eauf','External auth update function'),('eaurf','External auth update role and function'),('functional_access','Functional Access'),('guest_login','Guest Login'),('left_menu_access','Left Menu Access'),('login','Login'),('logout','Logout'),('mobile_login','Mobile Login'),('mobile_logout','Mobile Logout'),('remove_child_role','remove_child_role'),('remove_role','remove_role'),('remove_role_function','remove_role_function'),('remove_user_role','remove_user_role'),('search','Search'),('tab_access','Tab Access'),('uaa','Update Account Admin'),('uu','Update User');
/*!40000 ALTER TABLE `fn_lu_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_lu_alert_method`
--

LOCK TABLES `fn_lu_alert_method` WRITE;
/*!40000 ALTER TABLE `fn_lu_alert_method` DISABLE KEYS */;
INSERT INTO `fn_lu_alert_method` (`alert_method_cd`, `alert_method`) VALUES ('EMAIL','Email'),('FAX','Fax'),('PAGER','Pager'),('PHONE','Phone'),('SMS','SMS');
/*!40000 ALTER TABLE `fn_lu_alert_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_lu_menu_set`
--

LOCK TABLES `fn_lu_menu_set` WRITE;
/*!40000 ALTER TABLE `fn_lu_menu_set` DISABLE KEYS */;
INSERT INTO `fn_lu_menu_set` (`menu_set_cd`, `menu_set_name`) VALUES ('APP','Application Menu');
/*!40000 ALTER TABLE `fn_lu_menu_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_lu_priority`
--

LOCK TABLES `fn_lu_priority` WRITE;
/*!40000 ALTER TABLE `fn_lu_priority` DISABLE KEYS */;
INSERT INTO `fn_lu_priority` (`priority_id`, `priority`, `active_yn`, `sort_order`) VALUES (10,'Low',1,10),(20,'Normal',1,20),(30,'High',1,30),(40,'Urgent',1,40),(50,'Fatal',1,50);
/*!40000 ALTER TABLE `fn_lu_priority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_lu_tab_set`
--

LOCK TABLES `fn_lu_tab_set` WRITE;
/*!40000 ALTER TABLE `fn_lu_tab_set` DISABLE KEYS */;
INSERT INTO `fn_lu_tab_set` (`tab_set_cd`, `tab_set_name`) VALUES ('APP','Application Tabs');
/*!40000 ALTER TABLE `fn_lu_tab_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_lu_timezone`
--

LOCK TABLES `fn_lu_timezone` WRITE;
/*!40000 ALTER TABLE `fn_lu_timezone` DISABLE KEYS */;
INSERT INTO `fn_lu_timezone` (`timezone_id`, `timezone_name`, `timezone_value`) VALUES (10,'US/Eastern','US/Eastern'),(20,'US/Central','US/Central'),(30,'US/Mountain','US/Mountain'),(40,'US/Arizona','America/Phoenix'),(50,'US/Pacific','US/Pacific'),(60,'US/Alaska','US/Alaska'),(70,'US/Hawaii','US/Hawaii');
/*!40000 ALTER TABLE `fn_lu_timezone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_menu`
--

LOCK TABLES `fn_menu` WRITE;
/*!40000 ALTER TABLE `fn_menu` DISABLE KEYS */;
INSERT INTO `fn_menu` (`menu_id`, `label`, `parent_id`, `sort_order`, `action`, `function_cd`, `active_yn`, `servlet`, `query_string`, `external_url`, `target`, `menu_set_cd`, `separator_yn`, `image_src`) VALUES (1,'root',NULL,10,NULL,'menu_home',0,NULL,NULL,NULL,NULL,'APP',0,NULL),(2,'Home',1,10,'root.applicationsHome','menu_home',1,NULL,NULL,NULL,NULL,'APP',0,'icon-building-home'),(3,'Application Catalog',1,15,'root.appCatalog','menu_home',1,NULL,NULL,NULL,NULL,'APP',0,'icon-retail-gallery'),(4,'Widget Catalog',1,20,'root.widgetCatalog','menu_home',1,NULL,NULL,NULL,NULL,'APP',0,'icon-retail-gallery'),(5,'Admins',1,40,'root.admins','menu_admin',1,NULL,NULL,NULL,NULL,'APP',0,'icon-content-star'),(6,'Roles',1,45,'root.roles','menu_acc_admin',1,NULL,NULL,NULL,NULL,'APP',0,'icon-people-groupcollaboration'),(7,'Users',1,50,'root.users','menu_acc_admin',1,NULL,NULL,NULL,NULL,'APP',0,'icon-people-groupcollaboration'),(8,'Portal Admins',1,60,'root.portalAdmins','menu_admin',1,NULL,NULL,NULL,NULL,'APP',0,'icon-controls-settingsconnectedactivity'),(9,'Application Onboarding',1,70,'root.applications','menu_admin',1,NULL,NULL,NULL,NULL,'APP',0,'icon-content-grid2'),(10,'Widget Onboarding',1,80,'root.widgetOnboarding','menu_admin',1,NULL,NULL,NULL,NULL,'APP',0,'icon-content-grid2'),(11,'Edit Functional Menu',1,90,'root.functionalMenu','menu_admin',1,NULL,NULL,NULL,NULL,'APP',0,'icon-misc-pen'),(12,'User Notifications',1,100,'root.userNotifications','edit_notification',1,NULL,NULL,NULL,NULL,'APP',0,'icon-controls-settingsconnectedactivity'),(13,'Microservice Onboarding',1,110,'root.microserviceOnboarding','menu_admin',1,NULL,NULL,NULL,NULL,'APP',0,'icon-content-grid2'),(15,'App Account Management',1,130,'root.accountOnboarding','menu_admin',1,NULL,NULL,NULL,NULL,'App',0,'icon-content-grid2');
/*!40000 ALTER TABLE `fn_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_menu_functional`
--

LOCK TABLES `fn_menu_functional` WRITE;
/*!40000 ALTER TABLE `fn_menu_functional` DISABLE KEYS */;
INSERT INTO
  `fn_menu_functional` (
    `menu_id`,
    `column_num`,
    `text`,
    `parent_menu_id`,
    `url`,
    `active_yn`,
    `image_src`
  )
VALUES
  (175, 1, 'Manage', NULL, '', 1, NULL),
  (178, 2, 'Support', NULL, '', 1, NULL),
  (1, 2, 'Design', 175, '', 1, NULL),
  (2, 8, 'ECOMP Platform Management', 175, '', 0, NULL),
  (3, 5, 'Technology Insertion', 175, '', 1, NULL),
  (5, 7, 'Performance Management', 175, '', 0, NULL),
  (6, 6, 'Technology Management', 175, '', 1, NULL),
  (7, 4, 'Capacity Planning', 175, '', 0, NULL),
  (8, 3, 'Operations Planning', 175, '', 1, NULL),
  (11, 1, 'Product Design', 1, '', 1, NULL),
  (12, 2, 'Resource/Service Design & Onboarding', 1, '', 1, NULL),
  (13, 3, 'Orchestration (recipe/Process) Design', 1, '', 0, NULL),
  (14, 4, 'Service Graph visualizer', 1, '', 0, NULL),
  (15, 5, 'Distribution', 1, '', 1, NULL),
  (16, 6, 'Testing', 1, '', 1, NULL),
  (17, 7, 'Simulation', 1, '', 0, NULL),
  (18, 8, 'Certification', 1, '', 0, NULL),
  (19, 9, 'Policy Creation/Management', 1, 'http://policy.api.simpledemo.onap.org:8443/onap/policy', 1, NULL),
  (20, 10, 'Catalog Browser', 1, '', 1, NULL),
  (24, 5, 'Create/Manage Policy', 12, 'http://policy.api.simpledemo.onap.org:8443/onap/policy', 1, NULL),
  (56, 1, 'Policy Engineering', 8, 'http://policy.api.simpledemo.onap.org:8443/onap/policy', 1, NULL),
  (115, 1, 'Test/Approve a Resource or Service', 16, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/dashboard', 1, NULL),
  (130, 1, 'Favorites', 175, '', 1, NULL),
  (139, 2, 'Approve a Service for distribution', 12, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/dashboard', 1,NULL),
  (142, 3, 'Create a License model', 12, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/onboardVendor', 1, NULL),
  (145, 1, 'Distribute a Service', 15, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/dashboard', 1, NULL),
  (181, 1, 'Contact Us', 178, '', 1, NULL),
  (184, 2, 'Get Access', 178, '', 1, NULL),
  (301, 1, 'Create a Product', 11, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/dashboard', 1, NULL),
  (304, 2, 'Create a Vendor Software Product', 11, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/onboardVendor', 1, NULL),
  (307, 1, 'Manage a Resource/Service', 20, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/catalog', 1, NULL),
  (310, 2, 'Manage a Product', 20, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/catalog', 1, NULL),
	(313, 3, 'View a Resource/Service/Product', 20, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/catalog', 1, NULL),
	(316, 11, 'Administration', 1, '', 1, NULL),
	(148, 1, 'User Management / Category Management', 316, 'http://sdc.api.simpledemo.onap.org:8181/sdc1/portal#/adminDashboard', 1, NULL),
	(317, 1, 'Message Bus Management', 6, 'http://portal.api.simpledemo.onap.org:8989/ECOMPDBCAPP/dbc#/dmaap', 1, NULL),
	(318, 1, 'Infrastructure Provisioning', 3, '', 1, NULL),
	(319, 1, 'Infrastructure VNF Provisioning', 318, 'https://vid.api.simpledemo.onap.org:8443/vid/welcome.htm', 1, NULL);

/*!40000 ALTER TABLE `fn_menu_functional` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_menu_functional_ancestors`
--

LOCK TABLES `fn_menu_functional_ancestors` WRITE;
/*!40000 ALTER TABLE `fn_menu_functional_ancestors` DISABLE KEYS */;
INSERT INTO `fn_menu_functional_ancestors` (`id`, `menu_id`, `ancestor_menu_id`, `depth`) VALUES (1,175,175,0),(2,178,178,0),(3,11,11,0),(4,12,12,0),(5,13,13,0),(6,14,14,0),(7,15,15,0),(8,16,16,0),(9,17,17,0),(10,18,18,0),(11,19,19,0),(12,20,20,0),(13,316,316,0),(14,318,318,0),(15,317,317,0),(16,56,56,0),(17,301,301,0),(18,304,304,0),(19,24,24,0),(20,139,139,0),(21,142,142,0),(22,145,145,0),(23,115,115,0),(24,307,307,0),(25,310,310,0),(26,313,313,0),(27,1,1,0),(28,2,2,0),(29,3,3,0),(30,5,5,0),(31,6,6,0),(32,7,7,0),(33,8,8,0),(34,130,130,0),(35,181,181,0),(36,184,184,0),(37,148,148,0),(38,319,319,0),(64,11,1,1),(65,12,1,1),(66,13,1,1),(67,14,1,1),(68,15,1,1),(69,16,1,1),(70,17,1,1),(71,18,1,1),(72,19,1,1),(73,20,1,1),(74,316,1,1),(75,318,3,1),(76,317,6,1),(77,56,8,1),(78,301,11,1),(79,304,11,1),(80,24,12,1),(81,139,12,1),(82,142,12,1),(83,145,15,1),(84,115,16,1),(85,307,20,1),(86,310,20,1),(87,313,20,1),(88,1,175,1),(89,2,175,1),(90,3,175,1),(91,5,175,1),(92,6,175,1),(93,7,175,1),(94,8,175,1),(95,130,175,1),(96,181,178,1),(97,184,178,1),(98,148,316,1),(99,319,318,1),(127,301,1,2),(128,304,1,2),(129,24,1,2),(130,139,1,2),(131,142,1,2),(132,145,1,2),(133,115,1,2),(134,307,1,2),(135,310,1,2),(136,313,1,2),(137,148,1,2),(138,319,3,2),(139,11,175,2),(140,12,175,2),(141,13,175,2),(142,14,175,2),(143,15,175,2),(144,16,175,2),(145,17,175,2),(146,18,175,2),(147,19,175,2),(148,20,175,2),(149,316,175,2),(150,318,175,2),(151,317,175,2),(152,56,175,2),(158,301,175,3),(159,304,175,3),(160,24,175,3),(161,139,175,3),(162,142,175,3),(163,145,175,3),(164,115,175,3),(165,307,175,3),(166,310,175,3),(167,313,175,3),(168,148,175,3),(169,319,175,3);
/*!40000 ALTER TABLE `fn_menu_functional_ancestors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_menu_functional_roles`
--

LOCK TABLES `fn_menu_functional_roles` WRITE;
/*!40000 ALTER TABLE `fn_menu_functional_roles` DISABLE KEYS */;
INSERT INTO `fn_menu_functional_roles` (`id`, `menu_id`, `app_id`, `role_id`) VALUES (1,19,5,1007),(2,19,5,1006),(3,24,5,1007),(4,24,5,1006),(5,56,5,1007),(6,56,5,1006),(8,115,4,1004),(9,115,4,1005),(10,139,4,1004),(11,139,4,1005),(12,142,4,1004),(13,142,4,1005),(14,145,4,1004),(15,145,4,1005),(16,148,4,1004),(17,148,4,1005),(18,301,4,1004),(19,301,4,1005),(20,304,4,1004),(21,304,4,1005),(22,307,4,1004),(23,307,4,1005),(24,310,4,1004),(25,310,4,1005),(26,313,4,1004),(27,313,4,1005),(39,319,6,1009),(40,319,6,1008),(42,317,3,1003),(43,317,3,1002);
/*!40000 ALTER TABLE `fn_menu_functional_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_qz_job_details`
--

LOCK TABLES `fn_qz_job_details` WRITE;
/*!40000 ALTER TABLE `fn_qz_job_details` DISABLE KEYS */;
INSERT INTO `fn_qz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`, `DESCRIPTION`, `JOB_CLASS_NAME`, `IS_DURABLE`, `IS_NONCONCURRENT`, `IS_UPDATE_DATA`, `REQUESTS_RECOVERY`, `JOB_DATA`) VALUES ('Scheduler_20190808_one','LogJob','AppGroup',NULL,'org.onap.portalapp.scheduler.LogJob','0','1','1','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0unitst\0bytesx\0'),('Scheduler_20190808_one','PortalSessionTimeoutFeedJob','AppGroup',NULL,'org.onap.portalapp.service.sessionmgt.TimeoutHandler','0','1','1','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xp\0sr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0\0x\0');
/*!40000 ALTER TABLE `fn_qz_job_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_qz_locks`
--

LOCK TABLES `fn_qz_locks` WRITE;
/*!40000 ALTER TABLE `fn_qz_locks` DISABLE KEYS */;
INSERT INTO `fn_qz_locks` (`SCHED_NAME`, `LOCK_NAME`) VALUES ('Scheduler_20190808_one','STATE_ACCESS'),('Scheduler_20190808_one','TRIGGER_ACCESS');
/*!40000 ALTER TABLE `fn_qz_locks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_qz_scheduler_state`
--

LOCK TABLES `fn_qz_scheduler_state` WRITE;
/*!40000 ALTER TABLE `fn_qz_scheduler_state` DISABLE KEYS */;
INSERT INTO `fn_qz_scheduler_state` (`SCHED_NAME`, `INSTANCE_NAME`, `LAST_CHECKIN_TIME`, `CHECKIN_INTERVAL`) VALUES ('Scheduler_20190808_one','portal-portal-app-76c9f7bfb5-s8rhd1565254283688',1565691615399,20000);
/*!40000 ALTER TABLE `fn_qz_scheduler_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_qz_triggers`
--

LOCK TABLES `fn_qz_triggers` WRITE;
/*!40000 ALTER TABLE `fn_qz_triggers` DISABLE KEYS */;
INSERT INTO `fn_qz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `JOB_NAME`, `JOB_GROUP`, `DESCRIPTION`, `NEXT_FIRE_TIME`, `PREV_FIRE_TIME`, `PRIORITY`, `TRIGGER_STATE`, `TRIGGER_TYPE`, `START_TIME`, `END_TIME`, `CALENDAR_NAME`, `MISFIRE_INSTR`, `JOB_DATA`) VALUES ('Scheduler_20190808_one','LogTrigger','AppGroup','LogJob','AppGroup',NULL,1565691660000,1565691600000,0,'WAITING','CRON',1565254275000,0,NULL,0,''),('Scheduler_20190808_one','PortalSessionTimeoutFeedTrigger','AppGroup','PortalSessionTimeoutFeedJob','AppGroup',NULL,1565691900000,1565691600000,0,'WAITING','CRON',1565254275000,0,NULL,0,'');
/*!40000 ALTER TABLE `fn_qz_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_qz_cron_triggers`
--

LOCK TABLES `fn_qz_cron_triggers` WRITE;
/*!40000 ALTER TABLE `fn_qz_cron_triggers` DISABLE KEYS */;
INSERT INTO `fn_qz_cron_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `CRON_EXPRESSION`, `TIME_ZONE_ID`) VALUES ('Scheduler_20190808_one','LogTrigger','AppGroup','0 * * * * ? *','GMT'),('Scheduler_20190808_one','PortalSessionTimeoutFeedTrigger','AppGroup','0 0/5 * * * ? *','GMT');
/*!40000 ALTER TABLE `fn_qz_cron_triggers` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `fn_restricted_url`
--

LOCK TABLES `fn_restricted_url` WRITE;
/*!40000 ALTER TABLE `fn_restricted_url` DISABLE KEYS */;
INSERT INTO `fn_restricted_url` (`restricted_url`, `function_cd`) VALUES ('async_test.htm','menu_home'),('attachment.htm','menu_admin'),('broadcast.htm','menu_admin'),('chatWindow.htm','menu_home'),('contact_list.htm','menu_home'),('customer_dynamic_list.htm','menu_home'),('event.htm','menu_home'),('event_list.htm','menu_home'),('file_upload.htm','menu_admin'),('gauge.htm','menu_tab'),('gmap_controller.htm','menu_tab'),('gmap_frame.htm','menu_tab'),('jbpm_designer.htm','menu_job_create'),('jbpm_drools.htm','menu_job_create'),('job.htm','menu_admin'),('map.htm','menu_tab'),('map_download.htm','menu_tab'),('map_grid_search.htm','menu_tab'),('mobile_welcome.htm','menu_home'),('process_job.htm','menu_job_create'),('profile.htm','menu_profile_create'),('raptor.htm','menu_reports'),('raptor.htm','view_reports'),('raptor2.htm','menu_reports'),('raptor_blob_extract.htm','menu_reports'),('raptor_blob_extract.htm','view_reports'),('raptor_email_attachment.htm','menu_reports'),('raptor_search.htm','menu_reports'),('report_list.htm','menu_reports'),('role.htm','menu_admin'),('role_function.htm','menu_admin'),('sample_animated_map.htm','menu_tab'),('sample_map.htm','menu_home'),('sample_map_2.htm','menu_tab'),('sample_map_3.htm','menu_tab'),('tab2_sub1.htm','menu_tab'),('tab2_sub2_link1.htm','menu_tab'),('tab2_sub2_link2.htm','menu_tab'),('tab2_sub3.htm','menu_tab'),('tab3.htm','menu_tab'),('tab4.htm','menu_tab'),('template.jsp','menu_home'),('test.htm','menu_admin');
/*!40000 ALTER TABLE `fn_restricted_url` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `fn_role_composite`
--

LOCK TABLES `fn_role_composite` WRITE;
/*!40000 ALTER TABLE `fn_role_composite` DISABLE KEYS */;
INSERT INTO `fn_role_composite` (`parent_role_id`, `child_role_id`) VALUES (1,16);
/*!40000 ALTER TABLE `fn_role_composite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_role_function`
--

LOCK TABLES `fn_role_function` WRITE;
/*!40000 ALTER TABLE `fn_role_function` DISABLE KEYS */;
INSERT INTO `fn_role_function` (`role_id`, `function_cd`) VALUES (1,'login'),(1,'menu_admin'),(1,'menu_ajax'),(1,'menu_customer'),(1,'menu_customer_create'),(1,'menu_feedback'),(1,'menu_help'),(1,'menu_home'),(1,'menu_job'),(1,'menu_job_create'),(1,'menu_logout'),(1,'menu_notes'),(1,'menu_process'),(1,'menu_profile'),(1,'menu_profile_create'),(1,'menu_profile_import'),(1,'menu_reports'),(1,'menu_sample'),(1,'menu_tab'),(16,'login'),(16,'menu_ajax'),(16,'menu_customer'),(16,'menu_customer_create'),(16,'menu_home'),(16,'menu_logout'),(16,'menu_map'),(16,'menu_profile'),(16,'menu_reports'),(16,'menu_tab'),(950,'edit_notification'),(950,'getAdminNotifications'),(950,'saveNotification'),(1010,'menu_web_analytics'),(2115,'menu_web_analytics');
/*!40000 ALTER TABLE `fn_role_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_shared_context`
--

LOCK TABLES `fn_shared_context` WRITE;
/*!40000 ALTER TABLE `fn_shared_context` DISABLE KEYS */;
INSERT INTO `fn_shared_context` (`id`, `create_time`, `context_id`, `ckey`, `cvalue`) VALUES (1,'2019-08-08 10:11:18','b999771d~2d60~4638~a670~d47d17219157','USER_FIRST_NAME','Jimmy'),(2,'2019-08-08 10:11:18','b999771d~2d60~4638~a670~d47d17219157','USER_LAST_NAME','Hendrix'),(3,'2019-08-08 10:11:18','b999771d~2d60~4638~a670~d47d17219157','USER_EMAIL','admin@onap.org'),(4,'2019-08-08 10:11:18','b999771d~2d60~4638~a670~d47d17219157','USER_ORG_USERID','jh0003'),(5,'2019-08-08 10:17:47','29cc8f94~5a7d~41f8~b359~432bb903a718','USER_FIRST_NAME','Demo'),(6,'2019-08-08 10:17:47','29cc8f94~5a7d~41f8~b359~432bb903a718','USER_LAST_NAME','User'),(7,'2019-08-08 10:17:47','29cc8f94~5a7d~41f8~b359~432bb903a718','USER_EMAIL','demo@openecomp.org'),(8,'2019-08-08 10:17:47','29cc8f94~5a7d~41f8~b359~432bb903a718','USER_ORG_USERID','demo'),(9,'2019-08-08 11:01:02','7e3ced0a~52a3~492a~be53~2885d2df5a43','USER_FIRST_NAME','Demo'),(10,'2019-08-08 11:01:02','7e3ced0a~52a3~492a~be53~2885d2df5a43','USER_LAST_NAME','User'),(11,'2019-08-08 11:01:02','7e3ced0a~52a3~492a~be53~2885d2df5a43','USER_EMAIL','demo@openecomp.org'),(12,'2019-08-08 11:01:02','7e3ced0a~52a3~492a~be53~2885d2df5a43','USER_ORG_USERID','demo');
/*!40000 ALTER TABLE `fn_shared_context` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_tab`
--

LOCK TABLES `fn_tab` WRITE;
/*!40000 ALTER TABLE `fn_tab` DISABLE KEYS */;
INSERT INTO `fn_tab` (`tab_cd`, `tab_name`, `tab_descr`, `action`, `function_cd`, `active_yn`, `sort_order`, `parent_tab_cd`, `tab_set_cd`) VALUES ('TAB1','Tab 1','Tab 1 Information','tab1.htm','menu_tab',1,10,NULL,'APP'),('TAB2','Tab 2','Tab 2 Information','tab2_sub1.htm','menu_tab',1,20,NULL,'APP'),('TAB2_SUB1','Sub Tab 1','Sub Tab 1 Information','tab2_sub1.htm','menu_tab',1,10,'TAB2','APP'),('TAB2_SUB1_S1','Left Tab 1','Sub - Sub Tab 1 Information','tab2_sub1.htm','menu_tab',1,10,'TAB2_SUB1','APP'),('TAB2_SUB2','Sub Tab 2','Sub Tab 2 Information','tab2_sub2.htm','menu_tab',1,20,'TAB2','APP'),('TAB2_SUB3','Sub Tab 3','Sub Tab 3 Information','tab2_sub3.htm','menu_tab',1,30,'TAB2','APP'),('TAB3','Tab 3','Tab 3 Information','tab3.htm','menu_tab',1,30,NULL,'APP'),('TAB4','Tab 4','Tab 4 Information','tab4.htm','menu_tab',1,40,NULL,'APP');
/*!40000 ALTER TABLE `fn_tab` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_tab_selected`
--

LOCK TABLES `fn_tab_selected` WRITE;
/*!40000 ALTER TABLE `fn_tab_selected` DISABLE KEYS */;
INSERT INTO `fn_tab_selected` (`selected_tab_cd`, `tab_uri`) VALUES ('TAB1','tab1'),('TAB2','tab2_sub1'),('TAB2','tab2_sub2'),('TAB2','tab2_sub3'),('TAB2_SUB1','tab2_sub1'),('TAB2_SUB1_S1','tab2_sub1'),('TAB2_SUB2','tab2_sub2'),('TAB2_SUB3','tab2_sub3'),('TAB3','tab3'),('TAB4','tab4');
/*!40000 ALTER TABLE `fn_tab_selected` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_user`
--

LOCK TABLES `fn_user` WRITE;
/*!40000 ALTER TABLE `fn_user` DISABLE KEYS */;
INSERT INTO
  `fn_user` (
    `org_id`,
    `manager_id`,
    `first_name`,
    `middle_name`,
    `last_name`,
    `phone`,
    `fax`,
    `cellular`,
    `email`,
    `address_id`,
    `alert_method_cd`,
    `hrid`,
    `org_user_id`,
    `org_code`,
    `login_id`,
    `login_pwd`,
    `last_login_date`,
    `active_yn`,
    `created_id`,
    `created_date`,
    `modified_id`,
    `modified_date`,
    `is_internal_yn`,
    `address_line_1`,
    `address_line_2`,
    `city`,
    `state_cd`,
    `zip_code`,
    `country_cd`,
    `location_clli`,
    `org_manager_userid`,
    `company`,
    `department_name`,
    `job_title`,
    `timezone`,
    `department`,
    `business_unit`,
    `business_unit_name`,
    `cost_center`,
    `fin_loc_code`,
    `silo_status`,
    `language_id`
  )
VALUES
  (
    NULL,
    NULL,
    'Demo',
    NULL,
    'User',
    NULL,
    NULL,
    NULL,
    'demo@openecomp.org',
    NULL,
    NULL,
    NULL,
    'demo',
    NULL,
    'demo',
    'demo123',
    '2019-08-08 12:18:17',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2019-08-08 12:18:17',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'Jimmy',
    NULL,
    'Hendrix',
    NULL,
    NULL,
    NULL,
    'admin@onap.org',
    NULL,
    NULL,
    NULL,
    'jh0003',
    NULL,
    'jh0003',
    'demo123',
    '2019-08-08 10:16:11',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2019-08-08 10:16:11',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'Carlos',
    NULL,
    'Santana',
    NULL,
    NULL,
    NULL,
    'designer@onap.org',
    NULL,
    NULL,
    NULL,
    'cs0008',
    NULL,
    'cs0008',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'Joni',
    NULL,
    'Mitchell',
    NULL,
    NULL,
    NULL,
    'tester@onap.org',
    NULL,
    NULL,
    NULL,
    'jm0007',
    NULL,
    'jm0007',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'Steve',
    NULL,
    'Regev',
    NULL,
    NULL,
    NULL,
    'ops@onap.org',
    NULL,
    NULL,
    NULL,
    'op0001',
    NULL,
    'op0001',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'David',
    NULL,
    'Shadmi',
    NULL,
    NULL,
    NULL,
    'governor@onap.org',
    NULL,
    NULL,
    NULL,
    'gv0001',
    NULL,
    'gv0001',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'Teddy',
    NULL,
    'Isashar',
    NULL,
    NULL,
    NULL,
    'pm1@onap.org',
    NULL,
    NULL,
    NULL,
    'pm0001',
    NULL,
    'pm0001',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'Eden',
    NULL,
    'Rozin',
    NULL,
    NULL,
    NULL,
    'ps1@onap.org',
    NULL,
    NULL,
    NULL,
    'ps0001',
    NULL,
    'ps0001',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'vid1',
    NULL,
    'user',
    NULL,
    NULL,
    NULL,
    'vid1@onap.org',
    NULL,
    NULL,
    NULL,
    'vid1',
    NULL,
    'vid1',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'vid2',
    NULL,
    'user',
    NULL,
    NULL,
    NULL,
    'vid2@onap.org',
    NULL,
    NULL,
    NULL,
    'vid2',
    NULL,
    'vid2',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'vid3',
    NULL,
    'user',
    NULL,
    NULL,
    NULL,
    'vid3@onap.org',
    NULL,
    NULL,
    NULL,
    'vid3',
    NULL,
    'vid3',
    'demo123',
    '2016-10-20 15:11:16',
    1,
    NULL,
    '2016-10-14 21:00:00',
    1,
    '2016-10-20 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  ),(
    NULL,
    NULL,
    'steve',
    NULL,
    'user',
    NULL,
    NULL,
    NULL,
    'steve@onap.org',
    NULL,
    NULL,
    NULL,
    'steve',
    NULL,
    'steve',
    'demo123',
    '2017-05-19 15:11:16',
    1,
    NULL,
    '2017-05-19 21:00:00',
    1,
    '2017-05-19 15:11:16',
    0,
    NULL,
    NULL,
    NULL,
    'NJ',
    NULL,
    'US',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    10,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    1
  );

/*!40000 ALTER TABLE `fn_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ep_pers_user_app_sort`
--

LOCK TABLES `ep_pers_user_app_sort` WRITE;
/*!40000 ALTER TABLE `ep_pers_user_app_sort` DISABLE KEYS */;
INSERT INTO `ep_pers_user_app_sort` (`id`, `user_id`, `sort_pref`) VALUES (1,1,0);
/*!40000 ALTER TABLE `ep_pers_user_app_sort` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_pers_user_app_sel`
--

LOCK TABLES `fn_pers_user_app_sel` WRITE;
/*!40000 ALTER TABLE `fn_pers_user_app_sel` DISABLE KEYS */;
INSERT INTO `fn_pers_user_app_sel` (`id`, `user_id`, `app_id`, `status_cd`) VALUES (1,1,7,'S'),(2,1,8,'S'),(10,1,10,'S'),(11,1,5,'S');
/*!40000 ALTER TABLE `fn_pers_user_app_sel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fn_audit_log`
--

LOCK TABLES `fn_audit_log` WRITE;
/*!40000 ALTER TABLE `fn_audit_log` DISABLE KEYS */;
INSERT INTO `fn_audit_log` (`log_id`, `user_id`, `activity_cd`, `audit_date`, `comments`, `affected_record_id_bk`, `affected_record_id`) VALUES (1,1,'app_access','2019-08-08 10:18:52','https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html',NULL,'7'),(2,1,'tab_access','2019-08-08 10:18:52','https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html?cc=1565259532115',NULL,'7'),(3,1,'tab_access','2019-08-08 10:18:56','Home',NULL,'1'),(4,1,'tab_access','2019-08-08 10:18:59','https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html?cc=1565259538769',NULL,'7'),(5,1,'app_access','2019-08-08 10:18:59','https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html',NULL,'7'),(6,1,'tab_access','2019-08-08 10:18:59','https://aai.ui.simpledemo.onap.org:30220/services/aai/webapp/index.html?cc=1565259538769',NULL,'7'),(7,1,'tab_access','2019-08-08 10:19:06','Home',NULL,'1');
/*!40000 ALTER TABLE `fn_audit_log` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `fn_user_role`
--

LOCK TABLES `fn_user_role` WRITE;
/*!40000 ALTER TABLE `fn_user_role` DISABLE KEYS */;
INSERT INTO `fn_user_role` (`user_id`, `role_id`, `priority`, `app_id`)
 VALUES
 (1,1,1,1),
 (1,950,1,1),
 (1,999,1,1),
 (1,999,1,2),(1,999,1,3),(1,999,1,4),(1,999,1,5),(1,999,1,6),(1,999,1,7),(1,1000,1,2),(1,1001,1,2),(1,1002,1,3),(1,1004,1,4),(1,1006,1,5),(1,1008,1,6),(2,999,1,4),(2,1004,1,4),(3,16,NULL,4),(3,1005,NULL,4),(4,16,NULL,4),(4,1005,NULL,4),(5,16,NULL,4),(5,1005,NULL,4),(6,16,NULL,4),(6,1005,NULL,4),(7,16,NULL,4),(7,1005,NULL,4),(8,16,NULL,4),(8,1005,NULL,4),(9,16,NULL,6),(9,999,NULL,1),(9,1008,NULL,6),(10,16,NULL,6),(10,1008,NULL,6),(10,1009,NULL,6),(11,16,NULL,6),(12,16,NULL,7),(12,1011,NULL,7),(12,1012,NULL,7);
/*!40000 ALTER TABLE `fn_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `schema_info`
--

LOCK TABLES `schema_info` WRITE;
/*!40000 ALTER TABLE `schema_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `schema_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-13 10:20:25
