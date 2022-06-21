package eu.profinit.stm.mapperMyBatis.place;

import eu.profinit.stm.configuration.MyBatisConfiguration;
import eu.profinit.stm.model.event.Place;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ContextConfiguration(classes = MyBatisConfiguration.class)
@TestPropertySource("/test.properties")
public class PlaceMapperMyBatisTest {

    @Autowired
    private PlaceMapperMyBatis placeMapperMyBatis;

    @Test
    public void insertPlace() {
        Place place = new Place("New place", "Insert", 8L);
        placeMapperMyBatis.insertPlace(place);
        Assert.assertNotNull(place.getEntityId());
        Assert.assertNotNull(placeMapperMyBatis.findPlaceById(place.getEntityId()));
    }

    @Test
    public void deletePlaceById() {
        Long placeId = 1L;
        placeMapperMyBatis.deletePlaceById(placeId);
        Assert.assertNull(placeMapperMyBatis.findPlaceById(placeId));
    }

    @Test
    public void findPlaceById() {
        Long placeId = 2L;
        Place place = placeMapperMyBatis.findPlaceById(placeId);
        Assert.assertNotNull(place);
    }

    /**
    @Test
    public void updateSubgroupName() {
        Long placeId = 1L;
        Place place = placeMapperMyBatis.findPlaceById(placeId);
        String prevName = place.getName();
        place.setName("New place name");
        placeMapperMyBatis.updatePlace(place);
        Assert.assertNotEquals(prevName, place.getName());
    }
    */
}
