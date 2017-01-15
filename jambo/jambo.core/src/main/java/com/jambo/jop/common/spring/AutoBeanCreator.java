package com.jambo.jop.common.spring;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

/**
 * 用途：自动把符合条件的class注册为bean 作用：减少xml配置
 * 
 * @author Huang Baiming
 * @version 1.0 2007-12-4
 * @version 1.1 2007-12-11 正则表达式匹配改用spring自带的PatternMatchUtils
 */
public class AutoBeanCreator implements BeanFactoryPostProcessor, Ordered {
	
	private static Logger log = LoggerFactory.getLogger(AutoBeanCreator.class);
	// --------------------------------------------------------------
	private int order; // BeanFactoryPostProcessor初始化顺序（Spring容器）
	private boolean singleton;
	
	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	private List packageNames; // 要被自动建立bean的package列表

	public void setPackageNames(String[] names) {
		this.packageNames = new ArrayList(names.length);
		for (int i = 0; i < names.length; i++) {
			this.packageNames.add(StringUtils.trimWhitespace(names[i]));
		}
	}

	private List patterns = null; // 建立bean时的限制条件，要符合该模式的类才被建立为Bean

	public void setPatterns(String[] patterns) {
		this.patterns = new ArrayList(patterns.length);
		for (int i = 0; i < patterns.length; i++) {
			String pattern = StringUtils.trimWhitespace(patterns[i]);
			this.patterns.add(pattern);
		}
	}

	private boolean readJar = false; // 是否需要读取jar包，默认为false，优化启动速度

	public void setReadJar(boolean readJar) {
		this.readJar = readJar;
	}

	public boolean isReadJar() {
		return this.readJar;
	}

	// -- getter and setter ---------------------------------------------------

	/**
	 * 读取classpath,遍历所有class文件，把符合条件的注册为spring容器里面的bean
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {

		List beanNames = new ArrayList(); // 该list用于保存符合条件的bean名称（也就是类名）

		/* 1.查找class路径下所有符合条件的Bean */
		String[] classPaths = findClassPaths();
		java.net.URL webPath = this.getClass().getResource("/");
		Iterator iter = this.packageNames.iterator();
		while (iter.hasNext()) {
			try {
				String packageName = (String) iter.next();
				findBeanNamesFromClassPaths(classPaths, packageName, beanNames);
				findBeanNamesFromWebInfo(webPath, packageName, beanNames);
			} catch (Exception e) {
				throw new FatalBeanException(" Find bean names Error！", e);
			}
		}

		/* 2.注册Bean */
		DefaultListableBeanFactory bf = (DefaultListableBeanFactory) factory;
		Iterator iter2 = beanNames.iterator();
		while (iter2.hasNext()) {
			String beanName = (String) iter2.next();
			try {
				if (Class.forName(beanName).isInterface()) {// 忽略接口
					continue;
				}
				if (bf.containsBean(beanName)) { // 有重复时抛异常
					throw new FatalBeanException(" Repeated bean name Error！");
				}
				BeanDefinition beanDefinition = new RootBeanDefinition();
				beanDefinition.setBeanClassName(beanName);
				bf.registerBeanDefinition(beanName, beanDefinition);
				if(log.isDebugEnabled()) log.debug("注册bean: " + beanName);
			} catch (ClassNotFoundException e) {
				throw new FatalBeanException("Can't find class for the bean name:" + beanName, e);
			}
		}
	}

	protected String[] findClassPaths() {
		String classpath = System.getProperty("java.class.basepath");
		String split;
		if (classpath.indexOf(";") > 0) {
			split = ";";
		} else {
			split = ":";
		}
		return classpath.split(split);
	}

	protected void findBeanNamesFromClassPaths(String[] classPaths, String packageName, List beanNames) throws Exception {
		for (int i = 0; i < classPaths.length; ++i) {
			String path = classPaths[i];
			if (path.endsWith(".jar")) {
				if (this.readJar) {
					findFromJar(path, packageName, beanNames);
				}
			} else {
				File file = new File(path + File.separator + StringUtils.replace(packageName,".", File.separator));
				findFromFile(file, packageName, beanNames);
			}
		}
	}

	/**
	 * 暂时只支持查找WEB-INF/classes目录下的文件，不支持WEB-INF/lib目录
	 */
	protected void findBeanNamesFromWebInfo(java.net.URL webPath, String packageName, List beanNames) throws Exception {
		String filePath = webPath.toString();
		filePath = filePath.substring(5, filePath.length());
		if (filePath.indexOf("WEB-INF") >= 0) {
			File file = new File(filePath + File.separator + StringUtils.replace(packageName,".", File.separator));
			findFromFile(file, packageName, beanNames);
		}
	}

	/**
	 * ZipEntry表示ZIP文件条目
	 */
	protected void findFromJar(String jarPath, String packageName, List beanNames) throws Exception {
		ZipInputStream in = new ZipInputStream(new FileInputStream(new File(jarPath)));
		ZipEntry ze = null;
		while ((ze = in.getNextEntry()) != null) {
			String name = ze.getName();
			if (name.startsWith(  StringUtils.replace(packageName,".", "/")) && name.endsWith(".class")) {
				String className = name.substring(0, name.length() - 6);
				if (isMatch(className)) {
					beanNames.add(className);
				}
			}
		}
		in.close();
	}

	/**
	 * 递归方法
	 */
	protected void findFromFile(File file, String packageName, List beanNames) throws Exception {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; ++i) {
					findFromFile(files[i], packageName + "." + files[i].getName(), beanNames);
				}
			} else {
				if(log.isDebugEnabled()) log.debug("check class file:" + file.getName());
				if (file.getName().endsWith(".class")) {
					String className = packageName.substring(0, packageName.length() - 6); // 这里的packageName已经是class文件了
					if (isMatch(className)) {
						if(log.isDebugEnabled()) log.debug("add class file:" + file.getName());
						beanNames.add(className);
					}
				}
			}
		}
	}

	protected boolean isMatch(String name) {
		if (this.patterns == null) {
			return true;
		}
		Iterator iter = this.patterns.iterator();
		while (iter.hasNext()) {
			String pattern = (String) iter.next();
			if (PatternMatchUtils.simpleMatch(pattern, name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
}
