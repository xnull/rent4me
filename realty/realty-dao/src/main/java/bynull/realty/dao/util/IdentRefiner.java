package bynull.realty.dao.util;

import bynull.realty.common.PhoneUtil;
import bynull.realty.data.business.ids.IdentType;

import java.util.Optional;

/**
 * Created by null on 8/13/15.
 */
public class IdentRefiner {

    public static String refine(String identValue, IdentType identType){
        if (identType == IdentType.PHONE){
            Optional<String> phone = PhoneUtil.findPhoneSafeRu(identValue).getPhoneNumber();
            if (!phone.isPresent()){
                return identValue;
            }

            return phone.get();
        }

        return identValue;
    }
}
