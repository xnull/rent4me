package bynull.realty.dao.util;

import bynull.realty.common.PhoneUtil;
import bynull.realty.data.business.ids.IdentType;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Created by null on 8/13/15.
 */
@Slf4j
public class IdentRefiner {

    public static String refine(String identValue, IdentType identType){
        log.trace("Refine ident: {}, {}", identValue, identType);
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
