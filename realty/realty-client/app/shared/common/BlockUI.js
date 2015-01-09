/**
 * Created by dionis on 07/12/14.
 */

var _inProgressCount = 0;

var BlockUI = {
    blockUI: function () {
        var oldInProgressCount = _inProgressCount++;
        if (oldInProgressCount == 0) {
            $.blockUI({
                overlayCSS: {
                    backgroundColor: '#000',
                    opacity: 0.0,
                    cursor: 'wait'
                },
                message: '<img src="images/spin.gif" width="64px" height="64px"/>',
                css: {
//                    background: 'none',
                    backgroundColor: '#000',
                    width: '70px',
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    border: '3px solid #fff',
                    opacity: 0.7,
                    cursor: 'wait',
                    'border-radius': '10px',
                    '-webkit-border-radius': '10px',
                    '-moz-border-radius': '10px',
                    'z-index': 1052
                }
            });
        }
    },
    unblockUI: function () {
        var currInProgress = --_inProgressCount;
        if (currInProgress == 0) {
            $.unblockUI();
        }
    }
};

module.exports = BlockUI;