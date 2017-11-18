package fr.istic.vv.common;

import fr.istic.vv.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Tool to access class object from path directory
 */
public class ClassParser {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String PACKAGE_SEPARATOR = ".";

    /**
     * Gets class located in directoryPath
     * Ensures that classes are loaded in classLoader
     * @param directoryPath
     * @return classes list
     */
    public List<Class> getClassesFromDirectory(String directoryPath){
        List<Class> classList = new ArrayList<>();

        logger.info("Class parsing into {}",directoryPath);
        File classDirectory = new File(directoryPath);

        List<String> classesName = getClassesNameFromDirectory(classDirectory);

        loadClassFromDirectory(classDirectory,classesName);

        return classList;
    }

    /**
     * Load in classLoader classes from classesName located in directory
     * @param directoryClass
     * @param classesName
     * @return classes list
     */
    private List<Class> loadClassFromDirectory(File directoryClass,List<String> classesName){
        List<Class> loadedClasses = new ArrayList<>();
        URLClassLoader classLoader = null;
        try{

            logger.trace("Get URL from directory");
            URL url = directoryClass.toURI().toURL();
            URL[] urls = new URL[]{url};

            logger.trace("Loading folder into classLoader");
            classLoader = new URLClassLoader(urls);

            logger.trace("Loading classes located in {}",directoryClass.getAbsolutePath());
            for(String className : classesName){
                logger.info("Loading class : {}",className);
                loadedClasses.add(classLoader.loadClass(className));
            }

        } catch (ClassNotFoundException e) {
            logger.error("Class can not be load",e);
        } catch (MalformedURLException e) {
            logger.error("Directory can not be transform into URL object",e);
        } finally {
            if(classLoader!=null) {
                try {
                    classLoader.close();
                } catch (IOException e) {
                    logger.warn("Errors occured during ClassLoader closing",e);
                }
            }
        }

        return loadedClasses;
    }

    /**
     * Returns classes name located in directory
     * String format include also package architecture (ex: fr.istic.vv.Operation)
     * @param directory : root classes directory
     * @return List of classes name, if there is not file in directory returns a empty list
     */
    public List<String> getClassesNameFromDirectory(File directory){
        return getClassesNameFromDirectory(directory,"");
    }

    /**
     * Method same as last but with a parentPackage parameter to ensure recursive execution
     * @param directory
     * @param parentPackage
     * @return
     */
    private List<String> getClassesNameFromDirectory(File directory, String parentPackage){

        List<String> classesName = new ArrayList<>();
        if (directory.isDirectory()){
            for (File classFile : directory.listFiles()) {
                if(classFile.isDirectory()){
                    String packageArchitecture = parentPackage.equals("") ? classFile.getName() : parentPackage + PACKAGE_SEPARATOR + classFile.getName();
                    classesName.addAll(getClassesNameFromDirectory(classFile,packageArchitecture));
                }
                else if(classFile.isFile()){
                    classesName.add(parentPackage+PACKAGE_SEPARATOR+getClassName(classFile));
                }
                else{
                    logger.trace("File {} is not directory and not a file",classFile.getName());
                }
            }
        }
        return classesName;

    }

    /**
     * Get the class name from
     * @param classFile
     * @return
     */
    private String getClassName(File classFile){
        return classFile.getName().substring(0, classFile.getName().lastIndexOf('.'));
    }
}
