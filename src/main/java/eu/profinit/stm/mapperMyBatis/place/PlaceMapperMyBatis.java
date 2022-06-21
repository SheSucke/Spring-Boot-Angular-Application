package eu.profinit.stm.mapperMyBatis.place;

import eu.profinit.stm.model.event.Place;

public interface PlaceMapperMyBatis {

    /**
     * insert Place to database and update it's entityId
     * @param place to insert
     */
    void insertPlace(Place place);

    /**
     *
     * @param id to find Place by
     */
    void deletePlaceById(Long id);

    /**
     *
     * @param id to find Place by
     * @return found Place or null
     */
    Place findPlaceById(Long id);

    /**
     *
     * @param place to update
     */
    //void updatePlace(Place place);
}
