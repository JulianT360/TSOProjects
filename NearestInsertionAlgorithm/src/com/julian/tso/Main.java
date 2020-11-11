package com.julian.tso;

import com.julian.tso.algorithms.NearestInsertion;

public class Main {

    private static NearestInsertion algorithm;

    public static void main(String[] args) {
	    algorithm = new NearestInsertion();
	    algorithm.solve();
    }
}
