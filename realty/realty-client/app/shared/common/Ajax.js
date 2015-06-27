/**
 * Created by dionis on 09/12/14.
 */

var AuthStore = require('../stores/AuthStore');
var assign = require('object-assign');
var JSON = require('JSON2');
var Utils = require('rent4meUtil');
var $ = require('jquery');

function AjaxBuilder(httpMethod) {
    //initialize variables
    var _httpMethod = httpMethod;

    var _url = null;
    var _data = null;
    var _contentType = null;
    var _withAuth = false;
    var _responseType = null;
    var _succ = null;
    var _error = null;

    this.withUrl = function (url) {
        _url = url;

        return this;
    };

    this.withJsonResponse = function () {
        _responseType = 'json';
        return this;
    };

    this.withJsonBody = function (data) {
        _data = JSON.stringify(data);
        _contentType = 'application/json; charset=utf-8';

        return this;
    };

    this.authorized = function () {
        _withAuth = true;

        return this;
    };

    this.onSuccess = function (succ) {
        _succ = succ;

        return this;
    };

    this.onError = function (err) {
        _error = err;

        return this;
    };

    this.execute = function () {
        var resultingSettings = {};
        resultingSettings['type'] = _httpMethod;

        if (_url) {
            resultingSettings['url'] = _url;
        }

        if (_data) {
            resultingSettings['data'] = _data;
        }

        if (_contentType) {
            resultingSettings['contentType'] = _contentType;
        }

        if (_withAuth) {
            resultingSettings['beforeSend'] = function (request) {
                request.setRequestHeader("Authorization", "Basic " + AuthStore.getAuthHeader());
            };
        }

        if (_succ) {
            resultingSettings['success'] = _succ;
        }

        var errorHandler = function (xhr, status, err) {
            //default handling of error codes
            if (xhr.status == 401) {
                //Utils.navigateToStart();
                //return;
            }


            if (_error) {
                _error(xhr, status, err);
            }
        };

        resultingSettings['error'] = errorHandler;

        $.ajax(resultingSettings);
    }

}

var Ajax = {
    POST: function (url) {
        return new AjaxBuilder('POST').withUrl(url)
    },

    GET: function (url) {
        return new AjaxBuilder('GET').withUrl(url)
    },

    PUT: function (url) {
        return new AjaxBuilder('PUT').withUrl(url)
    },

    DELETE: function (url) {
        return new AjaxBuilder('DELETE').withUrl(url)
    },

    PATCH: function (url) {
        return new AjaxBuilder('PATCH').withUrl(url)
    }
};

module.exports = Ajax;
