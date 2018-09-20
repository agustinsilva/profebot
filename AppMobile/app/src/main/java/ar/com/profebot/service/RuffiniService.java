package ar.com.profebot.service;

import java.util.ArrayList;

import flanagan.complex.Complex;
import flanagan.math.Polynomial;

public class RuffiniService {

    //Este metodo devuelve el próximo paso para la ejercitación.
    public static String FactorizeToNextStep(Polynomial polynomialOriginal) throws Exception {
        Complex[] roots = polynomialOriginal.roots();
        String processedPoly = "";
        ArrayList<Double> rootsForNewPoly = new ArrayList<Double>();
        if (areRealRoots(roots))
        {
            rootsForNewPoly = getRealRoots(roots);
            if (rootsForNewPoly.size() == 2){
                processedPoly = "(x" + rootsForNewPoly.get(0).intValue() + ").(x" + rootsForNewPoly.get(1).intValue() + ")";
            }

        }
        else{
            throw new Exception("Polinomio Imaginario");
        }


        return processedPoly;
    }

    private static ArrayList<Double> getRealRoots(Complex[] roots) {
        ArrayList<Double> realRoots = new ArrayList<Double>();
        for (Complex root : roots)
        {
            if (root.getImag() > -0.01 && root.getImag() < 0.01){
                realRoots.add(root.getReal());
            }
        }
        return realRoots;
    }

    private static boolean areRealRoots(Complex[] roots) {
        boolean real = true;
        for (Complex root : roots)
        {
            if (!(root.getImag() > -0.01 && root.getImag() < 0.01)){
                real = false;
            }
        }
        return real;
    }
}
