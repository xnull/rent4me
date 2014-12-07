/**
 * Created by dionis on 07/12/14.
 */

var _inProgressCount = 0;

var BlockUI = {
    blockUI: function() {
        var oldInProgressCount = _inProgressCount++;
        if(oldInProgressCount == 0) {
            $.blockUI({
                overlayCSS:  {
                    backgroundColor: '#000',
                    opacity:         0.0,
                    cursor:          'wait'
                },
                message: '<img src="images/spin.gif" width="64px" height="64px"/>',
                css: {
                    background: 'none',
                    border: 0,
                    opacity:         0.6,
                    cursor:          'wait'
                }
            });
        }
    },
    unblockUI: function() {
        var currInProgress = --_inProgressCount;
        if(currInProgress == 0) {
            $.unblockUI();
        }
    }
};

module.exports = BlockUI;