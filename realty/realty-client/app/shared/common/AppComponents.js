/**
 * Created by null on 6/23/15.
 */

module.exports = {
    UserStore: require('../../shared/stores/UserStore'),
    UserActions: require('../../shared/actions/UserActions'),
    AppAjax: require('rent4meAjax'),
    SocialNetStore: require('../../shared/stores/SocialNetStore'),

    SocialNetActions: require('../../shared/actions/SocialNetActions'),

    AdvertWidget: require('../../personal/components/left-pane/socialnet/advert-widget.js'),
    AdvertWidgetHelper: require('../../personal/components/left-pane/socialnet/advert-widget-helper.js'),
    AdvertInfoPage: require('../../personal/components/left-pane/advert/advert-info')

};
