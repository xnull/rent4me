package bynull.realty.components;

import bynull.realty.ServiceTest;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

public class FacebookHelperComponentTest extends ServiceTest {
    @Resource
    FacebookHelperComponent component;

    @Test
    public void loadPosts() {
        List<FacebookHelperComponent.FacebookPostItemDTO> dtos = component.doLoadPostsFromPage("kvartira.msk", new DateTime().minusDays(30).toDate());
        System.out.println("Size:"+dtos.size());
        System.out.println("======");
        for (FacebookHelperComponent.FacebookPostItemDTO dto : dtos) {
            System.out.println("-----");
            System.out.println(dto.getId());
            System.out.println(dto.getMessage());
            System.out.println(dto.getCreatedDtime()+"/"+dto.getUpdatedDtime());
            System.out.println("++++++");
        }
    }
}