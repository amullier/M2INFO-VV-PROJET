package fr.istic.vv.mutator;

import fr.istic.vv.common.MutantContainer;
import javassist.bytecode.Mnemonic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MutationDefinition {
    private static Logger logger = LoggerFactory.getLogger(MutationDefinition.class);

    public static List<Mutation> getMutations(){
        List<Mutation> mutations = new ArrayList<>();

        //Arithmetic mutations
        for(String prefix : Arrays.asList("i", "d", "f","l")) { // For all number types
            //Addition -> Subtraction
            mutations.add(new Mutation(prefix + "add", indexOf(prefix + "add"), prefix + "sub", indexOf(prefix + "sub"), MutantContainer.MutantType.ADDITION));

            //Subtraction -> Addition
            mutations.add(new Mutation(prefix + "sub", indexOf(prefix + "sub"), prefix + "add", indexOf(prefix + "add"), MutantContainer.MutantType.SUBTRACTION));

            //Multiplication -> Division
            mutations.add(new Mutation(prefix + "mul", indexOf(prefix + "mul"), prefix + "div",indexOf(prefix + "div"), MutantContainer.MutantType.MULTIPLICATION));

            //Division -> Multiplication
            mutations.add(new Mutation(prefix + "div", indexOf(prefix + "div"), prefix + "mul",indexOf(prefix + "mul"), MutantContainer.MutantType.DIVISION));
        }

        mutations.add(new Mutation("ifeq", indexOf("ifeq"), "ifneq",indexOf("ifneq"), MutantContainer.MutantType.CONDITION_EQ));

        mutations.add(new Mutation("ifneq", indexOf("ifneq"), "ifeq",indexOf("ifeq"), MutantContainer.MutantType.CONDITION_NEQ));


        return mutations;
    }

    private static int indexOf(String operation){
        int index = Arrays.asList(Mnemonic.OPCODE).indexOf(operation);
        if(index==-1){
            logger.error("The "+operation+" was not found in Mnemonic.OPCODE table");
        }
        return index;
    }

}
