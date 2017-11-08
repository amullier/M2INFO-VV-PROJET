package fr.istic.main;

import fr.istic.translator.TranslatorImpl;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.Translator;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import java.io.File;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args )
    {
        try {
            ClassPool pool = ClassPool.getDefault();

            Loader loader = new Loader(pool);
            Translator translator = new TranslatorImpl();
            File classDir = new File("../TargetProject/target/classes");
            File testDir = new File("../TargetProject/target/test-classes");
            loader.addTranslator(pool, translator);
            pool.appendClassPath(classDir.getPath());
            pool.appendClassPath(testDir.getPath());
            loader.run("fr.istic.vv.TargetApp", args);
            JUnitCore jUnitCore= new JUnitCore();
            String[] classes = {"fr.istic.vv.AdditionTest",
                    "fr.istic.vv.MultiplicationTest",
                    "fr.istic.vv.DivisionTest",
                    "fr.istic.vv.SubtractionTest"};
            for(CtClass ctClass : pool.get(classes)){
                Request request = Request.aClass(ctClass.toClass());
                Result r = jUnitCore.run(request);
                System.out.println("Tests ran : " + r.getRunCount() + ", failed : " + r.getFailureCount());
                System.out.println(r.getFailures());
                System.out.println(r.wasSuccessful());
            }

        }

        catch(Throwable exc) {
            System.out.println("Oh, no! Something went wrong.");
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        }

    }
}
