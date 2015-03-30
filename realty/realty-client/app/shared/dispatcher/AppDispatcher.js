/**
 * Created by dionis on 03/12/14.
 */
var Dispatcher = require('flux/lib/Dispatcher');
//var Dispatcher = require('flux').Dispatcher;
var assign = require('object-assign');

var AppDispatcher = assign(new Dispatcher(), {

    /**
     * A bridge function between the views and the dispatcher, marking the action
     * as a view action.  Another variant here could be handleServerAction.
     * @param  {object} action The data coming from the view.
     */
    handleViewAction: function (action) {
        this.dispatch({
            source: 'VIEW_ACTION',
            action: action
        });
    },

    scheduleOrExecute: function(fn) {
        if(!this.isDispatching()){
            console.log('Is not dispatching ');
            console.log('Will execute function');
            console.log(fn);
            fn();
        } else {
            var self = this;
            //schedule next attempt
            setTimeout(function(){
                return self.scheduleOrExecute(fn);
            }, 100);
        }
    }

});

module.exports = AppDispatcher;
