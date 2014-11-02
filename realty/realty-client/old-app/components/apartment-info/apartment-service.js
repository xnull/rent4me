/**
 * Created by null on 9/24/14.
 */
var validator = require('../core/validator.js');

function ApartmentInfoService($resource, $log) {

    //validator().checkUndefinedBatch([$resource, $log]);
    /**
     * @type {Object}
     */
    var apartmentInfoDto = {
        id: 1,
        ownerId: 123,
        floor: 7,
        rooms: 1,
        square: 52,
        price: 50000,
        address: 'Москва, улица 800-летия Москвы, 4к2',
        features: {
            balcony: true,
            furniture: true,
            refrigerator: true,
            tv: true,
            conditioner: true //etc
        },
        images: [
            {
                img: 'components/apartment-info/images/photo1.jpg',
                isActive: 'active'
            },
            {
                img: 'components/apartment-info/images/photo2.jpg',
                isActive: ''
            }
        ],
        description: 'Комната в центре столицы, с хорошими соседями, в историческом доме - крепче современных.' +
            'До Чистых Прудов 5 минут пешком.' +
            'Все удовольствия культурной жизни в шаговой доступности, театры, кафе, антикафе, кинотеатры - ' +
            'мест, где можно общаться множество. В квартире есть WiFI.' +
            'В качестве жильца хочется видеть одного, адекватного человека. Не домоседа), ' +
            'без вредных привычек, аккуратного. Въехать можно уже 4-5 августа.'

    };

    /**
     * Get info about a apartment
     * @param apartmentId
     * @returns {Object} apartmentInfoDto
     * @param $resource
     */
    function getApartmentById(apartmentId) {
        $log.debug('Get apartment info by id: ' + apartmentId);
        //$log.debug($resource);
        return apartmentInfoDto;
    }

    /**
     *
     * @param address
     * @returns apartmentInfoDto
     */
    function getApartmentByAddress(address) {

    }

    return {
        getApartmentById: getApartmentById
    };
}

module.exports = function () {
    return ApartmentInfoService;
};