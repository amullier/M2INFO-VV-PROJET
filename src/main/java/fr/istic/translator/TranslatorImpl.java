package fr.istic.translator;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;

public class TranslatorImpl implements Translator {

    public void start(ClassPool classPool) throws NotFoundException, CannotCompileException {
        System.out.println("Starting");
    }

    public void onLoad(ClassPool classPool, String className) throws NotFoundException, CannotCompileException {
        if(className.contains("Addition")){
            CtMethod operate = classPool.get(className).getDeclaredMethod("operate");
            //operate.setBody("return FirstTerm - SecondTerm;");
        }

    }
}