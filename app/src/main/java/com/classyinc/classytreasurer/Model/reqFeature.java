package com.classyinc.classytreasurer.Model;

public class reqFeature {

    private  String id,email,featurereq;

    public reqFeature() {


    }

    public reqFeature(String id, String email, String featurereq) {
        this.id = id;
        this.email = email;
        this.featurereq = featurereq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeaturereq() {
        return featurereq;
    }

    public void setFeaturereq(String featurereq) {
        this.featurereq = featurereq;
    }
}
