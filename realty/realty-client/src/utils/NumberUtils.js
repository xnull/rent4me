/**
 * Created by dionis on 31/08/16.
 */
import accounting from 'accounting'


export default class NumberUtils {
    static formatNumber(number) {
        return accounting.formatNumber(number, 0, " ")
    }
}
