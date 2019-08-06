package com.sindrethoresen.android.Rutetider;

/**
 * Created by st97m_000 on 13.08.2017.
 */

class Favoritt {
    private boolean stopp;
    private String text;
    private String dag;
    private int logo;

    Favoritt(boolean stopp, String text, String dag, int logo) {
        this.stopp = stopp;
        this.text = text;
        this.dag = dag;
        this.logo=logo;
    }

    boolean isStopp() {
        return stopp;
    }

    String getText() {
        return text;
    }

    String getDag() {
        return dag;
    }

    int getLogo() {
        return logo;
    }
}
