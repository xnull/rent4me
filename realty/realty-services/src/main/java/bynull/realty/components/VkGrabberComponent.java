package bynull.realty.components;

import bynull.realty.dto.vk.ItemDTO;
import bynull.realty.services.vk.VkGroupPostsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by trierra on 1/4/15.
 */
@Component
public class VkGrabberComponent {

//    @Resource
////    private VkAuth vkAuth;
//
//    @Resource
    private VkGroupPostsService vkGroupPostsService;

    private String accessToken;

    @PostConstruct
//    private void init() throws EmptyHiddenVkValue, IOException, URISyntaxException {
//        accessToken = vkAuth.receiveToken();
//    }

    public List<ItemDTO> receiveWallPosts() throws URISyntaxException {
//       List<ItemJSON> fromVk =  vkGroupPostsService.getWallPostsList("club22062158", accessToken);
//        List<ItemDTO> json = fromVk.stream().map(ItemJSON::to).collect(Collectors.toList());
        return null;

    }
}
