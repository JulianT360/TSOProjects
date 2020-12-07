package com.julian.tso;

import com.julian.tso.algorithms.FarthestInsertion;

public class Main {

    private static FarthestInsertion algorithm;

    public static void main(String[] args) {
	    algorithm = new FarthestInsertion();
	    algorithm.solve();
    }
}
