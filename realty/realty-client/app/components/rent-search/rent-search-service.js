/**
 * Created by null on 9/24/14.
 */
module.exports = function RentSearchService($log, $resource) {

    var searchRequest = {
        floorsNumber: 1,
        metro: 'begovaya',
        address: 'kremlin'
    };

    var searchResponse = [
        {
            id: 1,
            lat: 55.752020,
            lng: 37.617526
        }
    ];

    /**
     * Send request to the server
     * @param searchRequest
     */
    function searchByParams(searchRequest) {
        var User = $resource('/rest/rent-search/:userId', {userId:'@id'});
        User.get({userId:123}, function(u, getResponseHeaders){
            u.abc = true;
            u.$save(function(u, putResponseHeaders) {
                //u => saved user object
                //putResponseHeaders => $http header getter
            });
        });
    }


    return {
        searchByParams: searchByParams
    };
};