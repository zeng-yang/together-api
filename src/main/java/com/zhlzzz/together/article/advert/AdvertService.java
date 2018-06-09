package com.zhlzzz.together.article.advert;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdvertService {

    Advert addAdvert(AdvertParam advertParam);
    Advert updateAdvert(Long id, AdvertParam advertParam);
    Optional<? extends Advert> getAdvertById(Long id);
    void deleteAdvert(Long id);
    List<AdvertEntity> findAll();
}
