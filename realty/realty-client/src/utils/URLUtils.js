/**
 * Created by dionis on 31/08/16.
 */

function kVPair(k, v) {
    return encodeURIComponent(k)+"="+encodeURIComponent(v)
}

class Pair {
    constructor(k,v) {
        this.k = k
        this.v = v
    }
}

function flattenArrayIfNeeded(k, v) {
    if(Array.isArray(v)) {
        return v.map(_v=>kVPair(k, _v)).join('&')
    } else {
        return kVPair(k, v)
    }
}

function tuplify(queryParams = {}) {
    let arr = []
    for(var k in queryParams) {
        if(queryParams.hasOwnProperty(k)) {
            arr.push(new Pair(k,queryParams[k]))
        }
    }
    // console.log('tuplified array:',arr)
    return arr
}

export default class URLUtils {
    static buildUrl(host, path, queryParams = {}) {
        // console.log('query params', queryParams)
        var url = host
            + path + '?'
            + (tuplify(queryParams).map((pair) => flattenArrayIfNeeded(pair.k, pair.v)).join('&'))
        return url
    }
}
