package com.zhlzzz.together.system;

public interface AboutService {

    AboutEntity updateAbout(String company, String logo, String introduction);
    AboutEntity getAbout();
}
