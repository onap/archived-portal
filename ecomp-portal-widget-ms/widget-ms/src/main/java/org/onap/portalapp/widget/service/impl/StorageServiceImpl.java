package org.onap.portalapp.widget.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.onap.portalapp.widget.constant.WidgetConstant;
import org.onap.portalapp.widget.domain.ValidationRespond;
import org.onap.portalapp.widget.domain.WidgetCatalog;
import org.onap.portalapp.widget.domain.WidgetFile;
import org.onap.portalapp.widget.excetpion.StorageException;
import org.onap.portalapp.widget.service.StorageService;
import org.onap.portalapp.widget.service.WidgetCatalogService;
import org.onap.portalapp.widget.utils.UnzipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

	private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);
	private final String TMP_PATH = "/tmp/";

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	WidgetCatalogService widgetCatalogService;

	@Override
	@Transactional
	public void deleteWidgetFile(long widgetId) {
		WidgetFile widgetFile = getWidgetFile(widgetId);
		logger.debug("StorageServiceImpl.deleteWidgetFile: deleting widget file {}", widgetId);
		if (widgetFile == null) {
			logger.debug(
					"StorageServiceImpl.deleteWidgetFile: No widget file found in database while performing StorageServiceImpl.deleteWidgetFile.");
			return;
		}
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.delete(widgetFile);
		tx.commit();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public WidgetFile getWidgetFile(long widgetId) {
		logger.debug("StorageServiceImpl.getWidgetFile: getting widget file {}", widgetId);
		WidgetFile widgetFile = null;
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(WidgetFile.class);
		criteria.add(Restrictions.eq("widgetId", widgetId));
		List<WidgetFile> widgetFiles = criteria.list();
		//session.flush();
		session.close();
		if (widgetFiles.size() > 0)
			widgetFile = widgetFiles.get(0);
		return widgetFile;
	}

	@Override
	public ValidationRespond checkZipFile(MultipartFile file) {
		StringBuilder error_msg = new StringBuilder();
		UnzipUtil unzipper = new UnzipUtil();
		Map<String, byte[]> map;
		File convFile;
		boolean isValid = true;
		if (!file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')).equals(".zip")) {
			isValid = false;
			error_msg.append(WidgetConstant.VALIDATION_MESSAGE_ZIP);
			logger.error("StorageServiceImpl.checkZipFile: invalid file format");
		}
		try {
			if (file.isEmpty()) {
				logger.error(
						"StorageServiceImpl.checkZipFile: Failed to store empty file " + file.getOriginalFilename());
				throw new StorageException(
						"StorageServiceImpl.checkZipFile: Failed to store empty file " + file.getOriginalFilename());
			}

			String fileLocation = TMP_PATH+file.getOriginalFilename();
			logger.debug("StorageServiceImpl.checkZipFile: store the widget to:" + fileLocation);
			convFile = new File(fileLocation);
			try(FileOutputStream fos = new FileOutputStream(convFile)){
				fos.write(file.getBytes());
			}
			map = unzipper.unzip_db(fileLocation, TMP_PATH, "tempWidgets");
			convFile.delete();
		} catch (IOException e) {
			logger.error("StorageServiceImpl.checkZipFile: Failed to store file " + file.getOriginalFilename(), e);
			throw new StorageException(
					"torageServiceImpl.checkZipFile: Failed to store file " + file.getOriginalFilename(), e);
		}

		for (byte[] b : map.values()) {
			if (isValid && b == null) {
				isValid = false;
				error_msg.append(WidgetConstant.VALIDATION_MESSAGE_FILES);
				break;
			}
		}
		return new ValidationRespond(isValid, error_msg.toString());
	}

	@Override
	@Transactional
	public void save(MultipartFile file, WidgetCatalog newWidget, long widgetId) {

		UnzipUtil unzipper = new UnzipUtil();
		Map<String, byte[]> map;
		File convFile;
		try {
			if (file.isEmpty()) {
				logger.error("Failed to store empty file " + file.getOriginalFilename());
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}
			String fileLocation = file.getOriginalFilename();
			logger.debug("StorageServiceImpl.save: store the widget to:" + fileLocation);
			convFile = new File(fileLocation);
			try(FileOutputStream fos = new FileOutputStream(convFile)){
				fos.write(file.getBytes());
			}
			map = unzipper.unzip_db(fileLocation, ".", "tempWidgets");
			convFile.delete();
		} catch (IOException e) {
			logger.error("StorageServiceImpl.save: Failed to store file " + file.getOriginalFilename(), e);
			throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
		}
		saveJsHelper(newWidget, widgetId, map);
	}

	@Override
	@Transactional
	public void initSave(File file, WidgetCatalog newWidget, long widgetId) {

		UnzipUtil unzipper = new UnzipUtil();
		Map<String, byte[]> map;

		try {
			String fileLocation = file.getPath();
			logger.debug("StorageServiceImpl.save: store the widget to:" + fileLocation);
			map = unzipper.unzip_db(fileLocation, ".", "tempWidgets");
		} catch (IOException e) {
			logger.error("StorageServiceImpl.save: Failed to store file " + file.getName(), e);
			throw new StorageException("Failed to store file " + file.getName(), e);
		}
		
		saveJsHelper(newWidget, widgetId, map);
	}

	/**
	 * Helper method for saving widget files (controller.js, framework.js,
	 * markup.html and style.css) to ep_widget_catalog_files table in database
	 * 
	 * @param newWidget
	 * @param widgetId
	 * @param map
	 */
	private void saveHelper(WidgetCatalog newWidget, long widgetId, Map<String, byte[]> map) {

		logger.debug("Going to save widget " + newWidget);
		WidgetFile widgetFile = new WidgetFile();
		widgetFile.setName(newWidget.getName());
		widgetFile.setWidgetId(widgetId);

		

		String sb = null;
		try(InputStream fileInputStream = this.getClass().getClassLoader().getResourceAsStream("framework-template.js")) {
			byte[] bytes = new byte[fileInputStream.available()];
			if(fileInputStream.read(bytes) > 0) {
				sb = new String(bytes, "UTF-8");
			}
		} catch (IOException e) {
			logger.error("StorageServiceImpl.save: Failed to load framework-template.js file ", e);
		}

		String namespace = "Portal" + widgetId + "Widget";
		String controllerName = "Portal" + widgetId + "Ctrl";
		String cssName = "portal" + widgetId + "-css-ready";
		String colorArg1 = "color: #fff";
		String framework="";
		if(sb!=null) {
			framework = sb.replaceAll("ARGUMENT1", namespace).replaceAll("ARGUMENT2", controllerName)
					.replaceAll("ARGUMENT3", cssName).replaceAll("CSS_ARG1", colorArg1)
					.replaceAll("MICROSERVICE_ID", newWidget.getServiceId().toString())
					.replaceAll("WIDGET_ID", Long.toString(widgetId));
		}
		widgetFile.setFramework(framework.getBytes());

		final byte[] controllerLoc = map.get(WidgetConstant.WIDGET_CONTROLLER_LOCATION);
		if (controllerLoc == null || controllerLoc.length == 0)
			throw new IllegalArgumentException(
					"Map is missing required key " + WidgetConstant.WIDGET_CONTROLLER_LOCATION);
		String javascript = new String(controllerLoc);
		String functionHeader = javascript.substring(javascript.indexOf("function"), javascript.indexOf(")") + 1);
		String functionName = functionHeader.substring(functionHeader.indexOf(" "), functionHeader.indexOf("(")).trim();
		javascript = javascript.replaceFirst(functionName, controllerName);
		String functionParam = functionHeader.substring(functionHeader.indexOf("(") + 1, functionHeader.indexOf(")"));
		List<String> paramList = Arrays.asList(functionParam.split(","));

		int left_bracket_index = javascript.indexOf("{") + 1;
		String widgetData = namespace + "=" + namespace + "||{};" + "var res = " + namespace + ".widgetData;";
		javascript = javascript.substring(0, left_bracket_index) + widgetData
				+ javascript.substring(left_bracket_index);

		StringBuilder injectStr = new StringBuilder().append("[");
		for (int i = 0; i < paramList.size(); i++) {
			if (i == paramList.size() - 1)
				injectStr.append("'" + paramList.get(i).trim() + "'];");
			else
				injectStr.append("'" + paramList.get(i).trim() + "',");
		}
		javascript = namespace + ".controller = " + javascript + ";" + namespace + ".controller.$inject = "
				+ injectStr.toString();

		String html = new String(map.get(WidgetConstant.WIDGET_MARKUP_LOCATION)).replaceFirst(functionName,
				controllerName);
		;

		Pattern cssPattern = Pattern.compile("#.*-css-ready");
		Matcher cssMatcher = cssPattern.matcher(new String(map.get(WidgetConstant.WIDGET_STYLE_LOCATION)));
		if (cssMatcher.find()) {
			widgetFile.setCss(new String(map.get(WidgetConstant.WIDGET_STYLE_LOCATION))
					.replace(cssMatcher.group(0), "#" + cssName).getBytes());
		}

		widgetFile.setMarkup(html.getBytes());
		widgetFile.setController(javascript.getBytes());
		Session session = sessionFactory.openSession();
		session.save(widgetFile);
		session.flush();
		session.close();
		// sessionFactory.getCurrentSession().save(widgetFile);
		logger.debug(
				"StorageServiceImpl.save: saved fraemwork.js controller.js, markup.html and style.css files to the database for widget {}",
				widgetId);

	}
	
	/**
	 * Helper method to UnZip File
	 * 
	 * @param file
	 */
	private Map<String, byte[]> unZipFile(MultipartFile file) {
		UnzipUtil unzipper = new UnzipUtil();
		Map<String, byte[]> map;
		File convFile;
		try {
			if (file.isEmpty()) {
				logger.error("StorageServiceImpl.update: Failed to store empty file " + file.getOriginalFilename());
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}
			String fileLocation = file.getOriginalFilename();
			logger.debug("StorageServiceImpl.update: store the widget to:" + fileLocation);
			convFile = new File(fileLocation);
			try(FileOutputStream fos = new FileOutputStream(convFile)){
				fos.write(file.getBytes());
			}
			map = unzipper.unzip_db(fileLocation, ".", "tempWidgets");
			convFile.delete();
			return map;
		} catch (IOException e) {
			logger.error("StorageServiceImpl.update: Failed to store file " + file.getOriginalFilename(), e);
			throw new StorageException("StorageServiceImpl.update: Failed to store file " + file.getOriginalFilename(),
					e);
		}
	}
	
	/**
	 * Helper method for saving widget file (controller.js) to ep_widget_catalog_files table in database
	 * 
	 * @param newWidget
	 * @param widgetId
	 * @param map
	 */
	private void saveJsHelper(WidgetCatalog newWidget, long widgetId, Map<String, byte[]> map) {

		logger.debug("Going to save controller.js " + newWidget);
		WidgetFile widgetFile = new WidgetFile();
		widgetFile.setName(newWidget.getName());
		widgetFile.setWidgetId(widgetId);
		final byte[] controllerLoc = map.get(WidgetConstant.WIDGET_CONTROLLER_LOCATION);
		if (controllerLoc == null || controllerLoc.length == 0)
			throw new IllegalArgumentException(
					"Map is missing required key " + WidgetConstant.WIDGET_CONTROLLER_LOCATION);
		String javascript = new String(controllerLoc);

		widgetFile.setController(javascript.getBytes());
		Session session = sessionFactory.openSession();
		session.save(widgetFile);
		session.flush();
		session.close();
		logger.debug(
				"StorageServiceImpl.save: saved controller.js file to the database for widget {}",
				widgetId);

	}
	
	@Override
	public void updateJsFile(MultipartFile file, WidgetCatalog newWidget, long widgetId) {
		Map<String, byte[]> map;
		map = unZipFile(file);
		//Get existing widget file from DB	
		WidgetFile widgetFile = getWidgetFile(widgetId);
		
		String javascript = new String(map.get(WidgetConstant.WIDGET_CONTROLLER_LOCATION));
		widgetFile.setController(javascript.getBytes());
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.update(widgetFile);
		tx.commit();
		session.flush();
		session.close();
		logger.debug(
				"StorageServiceImpl.save: updated controller.js file to the database for widget {}",
				widgetId);
	}
	
	
	
	

	@Override
	public void update(MultipartFile file, WidgetCatalog newWidget, long widgetId) {
		UnzipUtil unzipper = new UnzipUtil();
		Map<String, byte[]> map;
		File convFile;
		try {
			if (file.isEmpty()) {
				logger.error("StorageServiceImpl.update: Failed to store empty file " + file.getOriginalFilename());
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}
			String fileLocation = file.getOriginalFilename();
			logger.debug("StorageServiceImpl.update: store the widget to:" + fileLocation);
			convFile = new File(fileLocation);
			try(FileOutputStream fos = new FileOutputStream(convFile)){
				fos.write(file.getBytes());
			}
			map = unzipper.unzip_db(fileLocation, ".", "tempWidgets");
			convFile.delete();
		} catch (IOException e) {
			logger.error("StorageServiceImpl.update: Failed to store file " + file.getOriginalFilename(), e);
			throw new StorageException("StorageServiceImpl.update: Failed to store file " + file.getOriginalFilename(),
					e);
		}
		WidgetFile widgetFile = getWidgetFile(widgetId);

		String sb = null;
		try(InputStream fileInputStream = this.getClass().getClassLoader().getResourceAsStream("framework-template.js")){
			byte[] bytes = new byte[fileInputStream.available()];
			if(fileInputStream.read(bytes) > 0) {
				sb = new String(bytes, "UTF-8");
			}
		} catch (IOException e) {
			logger.error("StorageServiceImpl.save: Failed to load framework-template.js file ", e);
		}

		String namespace = "Portal" + widgetId + "Widget";
		String controllerName = "Portal" + widgetId + "Ctrl";
		String cssName = "portal" + widgetId + "-css-ready";
		String colorArg1 = "color: #fff";
		String framework="";
		if(sb!=null) {
			framework = sb.replaceAll("ARGUMENT1", namespace).replaceAll("ARGUMENT2", controllerName)
					.replaceAll("ARGUMENT3", cssName).replaceAll("CSS_ARG1", colorArg1)
					.replaceAll("MICROSERVICE_ID", newWidget.getServiceId().toString())
					.replaceAll("WIDGET_ID", Long.toString(widgetId));
		}
		widgetFile.setFramework(framework.getBytes());

		String javascript = new String(map.get(WidgetConstant.WIDGET_CONTROLLER_LOCATION));
		String functionHeader = javascript.substring(javascript.indexOf("function"), javascript.indexOf(")") + 1);
		String functionName = functionHeader.substring(functionHeader.indexOf(" "), functionHeader.indexOf("(")).trim();
		javascript = javascript.replaceFirst(functionName, controllerName);
		String functionParam = functionHeader.substring(functionHeader.indexOf("(") + 1, functionHeader.indexOf(")"));
		List<String> paramList = Arrays.asList(functionParam.split(","));

		int left_bracket_index = javascript.indexOf("{") + 1;
		String widgetData = namespace + "=" + namespace + "||{};" + "var res = " + namespace + ".widgetData;";
		javascript = javascript.substring(0, left_bracket_index) + widgetData
				+ javascript.substring(left_bracket_index);

		StringBuilder injectStr = new StringBuilder().append("[");
		for (int i = 0; i < paramList.size(); i++) {
			if (i == paramList.size() - 1)
				injectStr.append("'" + paramList.get(i).trim() + "'];");
			else
				injectStr.append("'" + paramList.get(i).trim() + "',");
		}
		javascript = namespace + ".controller = " + javascript + ";" + namespace + ".controller.$inject = "
				+ injectStr.toString();

		String html = new String(map.get(WidgetConstant.WIDGET_MARKUP_LOCATION)).replaceFirst(functionName,
				controllerName);
		;

		Pattern cssPattern = Pattern.compile("#.*-css-ready");
		Matcher cssMatcher = cssPattern.matcher(new String(map.get(WidgetConstant.WIDGET_STYLE_LOCATION)));
		if (cssMatcher.find()) {
			widgetFile.setCss(new String(map.get(WidgetConstant.WIDGET_STYLE_LOCATION))
					.replace(cssMatcher.group(0), "#" + cssName).getBytes());
		}

		widgetFile.setMarkup(html.getBytes());
		widgetFile.setController(javascript.getBytes());
		// widgetFile.setCss(map.get(WidgetConstant.WIDGET_STYLE_LOCATION));
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.update(widgetFile);
		tx.commit();
		session.flush();
		session.close();
		logger.debug(
				"StorageServiceImpl.save: updated fraemwork.js controller.js, markup.html and style.css files to the database for widget {}",
				widgetId);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public String getWidgetMarkup(long widgetId) throws UnsupportedEncodingException {
		String markup = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(WidgetFile.class);
		criteria.add(Restrictions.eq("widgetId", widgetId));
		List<WidgetFile> widgetFile = criteria.list();
		logger.debug("StorageServiceImpl.getWidgetMarkup: getting widget markup result={}", widgetFile);

		if (widgetFile.size() > 0)
			markup = new String(widgetFile.get(0).getMarkup(), "UTF-8");
		return markup;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public String getWidgetController(long widgetId) throws UnsupportedEncodingException {
		String controller = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(WidgetFile.class);
		criteria.add(Restrictions.eq("widgetId", widgetId));
		List<WidgetFile> widgetFile = criteria.list();
		logger.debug("StorageServiceImpl.getWidgetController: getting widget controller result={}", widgetFile);

		if (widgetFile.size() > 0)
			controller = new String(widgetFile.get(0).getController(), "UTF-8");
		return controller;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public String getWidgetFramework(long widgetId) throws UnsupportedEncodingException {
		String framework = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(WidgetFile.class);
		criteria.add(Restrictions.eq("widgetId", widgetId));
		List<WidgetFile> widgetFile = criteria.list();
		logger.debug("StorageServiceImpl.getWidgetFramework: getting widget framework result={}", widgetFile);

		if (widgetFile.size() > 0)
			framework = new String(widgetFile.get(0).getFramework(), "UTF-8");
		return framework;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public String getWidgetCSS(long widgetId) throws UnsupportedEncodingException {
		String css = null;
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(WidgetFile.class);
		criteria.add(Restrictions.eq("widgetId", widgetId));
		List<WidgetFile> widgetFile = criteria.list();
		logger.debug("StorageServiceImpl.getWidgetCSS: getting widget css result={}", widgetFile);

		if (widgetFile.size() > 0)
			css = new String(widgetFile.get(0).getCss(), "UTF-8");
		return css;
	}

	@Override
	@Transactional
	public byte[] getWidgetCatalogContent(long widgetId) throws Exception {

		WidgetCatalog widget = widgetCatalogService.getWidgetCatalog(widgetId);
		File f = File.createTempFile("temp", ".zip");
		try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f))){
			String javascript = getWidgetController(widgetId);
	
			ZipEntry e = new ZipEntry(widget.getName() + "/js/controller.js");
			out.putNextEntry(new ZipEntry(widget.getName() + "/js/"));
			out.putNextEntry(e);
			byte[] data = javascript.getBytes();
			out.write(data, 0, data.length);
			out.closeEntry();
			byte[] result = Files.readAllBytes(Paths.get(f.getPath()));
			f.delete();
			return result;
		}
	}

}
