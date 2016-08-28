import { APARTMENT_LOAD_DETAILS, APARTMENT_LOAD_LIST, APARTMENT_SAVE_DETAILS } from '../constants/ApartmentConstants'

const initialState = {
  cache : new Map(),
  apartments: [],
  fetching: false,
  error: ''
};

export default function Apartment(state = initialState, action) {
  console.log('page reducer '+action.type)
  switch (action.type) {
    case APARTMENT_LOAD_LIST:
      // return { ...state, year: action.payload, fetching: true, error: '' }
    case APARTMENT_SAVE_DETAILS:
      // return { ...state, photos: action.payload, fetching: false, error: '' }
    case APARTMENT_LOAD_DETAILS:
      // return { ...state, error: action.payload.message, fetching: false}
    default:
      return state
  }
}
