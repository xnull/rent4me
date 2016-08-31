/**
 * Created by dionis on 31/08/16.
 */
import accounting from 'accounting'


export default class NumberUtils {
    static formatNumber(number) {
        return accounting.formatNumber(number, 0, " ")
    }

    static parseNumber(numberAsString) {
        numberAsString = (numberAsString || '') + ''
        numberAsString = numberAsString.replace(/\D/g,'')
        return numberAsString != '' ? parseInt(numberAsString) : null
    }

    static numBetween(num, startIncl, endIncl) {
        if(num) {
            if(num < startIncl) return startIncl
            else if(num > endIncl) return endIncl
            else return num
        } else {
            return ''
        }
    }
}
