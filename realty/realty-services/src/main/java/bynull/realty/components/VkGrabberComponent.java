package bynull.realty.components;

import bynull.realty.dto.vk.ItemDTO;
import bynull.realty.exeptions.EmptyHiddenVkValue;
import bynull.realty.grabber.VkAuth;
import bynull.realty.grabber.services.api.VkGroupPostsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by trierra on 1/4/15.
 */
@Component
public class VkGrabberComponent {

    @Resource
    private VkAuth vkAuth;

    @Resource
    private VkGroupPostsService vkGroupPostsService;

    private String accessToken;

    @PostConstruct
    private void init() throws EmptyHiddenVkValue, IOException, URISyntaxException {
        accessToken = vkAuth.receiveToken();
    }

    public List<ItemDTO> receiveWallPosts() throws URISyntaxException {
        return vkGroupPostsService.getWallPostsList("club22062158", accessToken);

    }
}
