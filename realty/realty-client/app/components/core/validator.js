/**
 * Created by null on 29.09.14.
 */
function Validator() {
    'use strict';

    return {
        checkUndefined: function (value) {
            if (typeof value === 'undefined') {
                throw new IllegalArgumentException('Property is undefined');
            }
        },

        checkUndefinedBatch: function (values) {
            if (typeof values === 'undefined') {
                throw new IllegalArgumentException('Property is undefined');
            }

            values.forEach(function (value) {
                if (typeof value === 'undefined') {
                    throw new IllegalArgumentException('Property is undefined');
                }
            });
        }
    };

    function IllegalArgumentException(message) {
        this.name = 'IllegalArgumentException';
        this.message = message;
    }
}

module.exports = function () {
    return Validator();
};