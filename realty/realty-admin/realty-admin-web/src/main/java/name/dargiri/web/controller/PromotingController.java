package name.dargiri.web.controller;

import bynull.realty.components.VKHelperComponent;
import bynull.realty.data.business.configs.ServerSetting;
import bynull.realty.dto.ServerSettingDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.ServerSettingsService;
import lombok.Getter;
import lombok.Setter;
import name.dargiri.web.Constants;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * Created by dionis on 04/06/15.
 */
@Controller
@RequestMapping("/secure/promoting")
public class PromotingController {

    @Resource
    ServerSettingsService serverSettingsService;

    @Resource
    VKHelperComponent vkHelperComponent;

    @Resource
    ApartmentService apartmentService;

    @RequestMapping("")
    public ModelAndView index() {
        Optional<ServerSettingDTO> vkTokenSettingDTO = serverSettingsService.getById(ServerSetting.Id.VK_TOKEN);
        String vkToken = vkTokenSettingDTO.map(ServerSettingDTO::getValue).orElse(null);

        ModelAndView modelAndView = new ModelAndView("promoting/index");
        modelAndView.addObject("vkToken", vkToken);
        return modelAndView;
    }

    @Getter
    @Setter
    public static class CodeForm {
        private String code;
    }

    @RequestMapping(value = "vk_token", method = RequestMethod.POST)
    public ModelAndView vkCodeExchange(CodeForm codeForm, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Token replaced");

        String token = vkHelperComponent.exchangeAdminCodeToPermanentAccessToken(codeForm.code);

        Assert.notNull(token);

        ServerSettingDTO dto = new ServerSettingDTO();
        dto.setId(ServerSetting.Id.VK_TOKEN);
        dto.setValue(token);
        serverSettingsService.save(dto);

        return new ModelAndView("redirect:/secure/promoting/");
    }


    @Getter
    @Setter
    public static class VkPostForm {
        private String groupId;
        private String text;
    }

    @RequestMapping(value = "vk_post", method = RequestMethod.POST)
    public ModelAndView vkPostToGroup(VkPostForm vkPostForm, RedirectAttributes redirectAttributes) {
        vkHelperComponent.sendMessageToGroup(vkPostForm.getGroupId(), vkPostForm.getText());

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Message published to VK");

        return new ModelAndView("redirect:/secure/promoting/");
    }

    @Getter
    @Setter
    public static class VkBulkPostForHours {
        private int hoursStart;
        private int hoursEnd;
    }

    @RequestMapping(value = "vk_bulk_post", method = RequestMethod.POST)
    public ModelAndView vkPostToGroupBulk(VkBulkPostForHours vkBulkPostForHours, RedirectAttributes redirectAttributes) {
        apartmentService.publishFBApartmentsOnVkPage(new DateTime().minusHours(vkBulkPostForHours.hoursStart).toDate(), new DateTime().minusHours(vkBulkPostForHours.hoursEnd).toDate());

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Messages published to VK from FB");

        return new ModelAndView("redirect:/secure/promoting/");
    }
}
