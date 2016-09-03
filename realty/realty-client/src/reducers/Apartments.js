import {
    APARTMENT_LOAD_DETAILS,
    APARTMENT_LOAD_LIST_PART,
    APARTMENT_LIST_CLEAR_LOADING,
    APARTMENT_SAVE_DETAILS,
    APARTMENT_LIST_NEXT_PAGE_LOADING,
    APARTMENT_ERROR_LOADING,
    APARTMENT_SEARCH_INPUT_GEO_ERROR,
} from '../constants/ApartmentConstants'

const initialState = {
    cache: new Map(),
    apartments: [],
    hasNextPage: false,
    initialSearch: true,
    loadInProgress: false,
    error: '',
    geoErrorInParams: ''
};

export default function Apartments(state = initialState, action) {
    //console.log('page reducer, state:', action.type, state, action.payload)
    switch (action.type) {
        case APARTMENT_LOAD_LIST_PART:{
            const apartments = action.payload;
            const newCache = new Map(state.cache)
            apartments.forEach((apartment) => {
                newCache.set(apartment.id, apartment)
            })
            // let newApartments = [...state.apartments]
            // newApartments = newApartments.push(apartments)
            return {...state, apartments: [...state.apartments, ...apartments], cache: newCache, hasNextPage: apartments.length > 0, loadInProgress: false}
        }
        case APARTMENT_LIST_CLEAR_LOADING:
            return {...state, apartments: [], cache: new Map(), hasNextPage: false, loadInProgress: true, error: '', geoErrorInParams: ''}
        case APARTMENT_LIST_NEXT_PAGE_LOADING:
            return {...state, hasNextPage: false, loadInProgress: true, error: '', geoErrorInParams: ''}
        case APARTMENT_SAVE_DETAILS:
            return {...state}
        case APARTMENT_LOAD_DETAILS:
            return {...state}
        case APARTMENT_ERROR_LOADING:
            return {...state, error: 'Ошибка загрузки страницы', loadInProgress: false}
        case APARTMENT_SEARCH_INPUT_GEO_ERROR:
            return {...state, geoErrorInParams: 'Выберите адрес/город/район где вы хотите искать квартиру'}
        default:
            return state
    }
}
