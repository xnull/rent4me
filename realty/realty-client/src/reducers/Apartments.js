import { APARTMENT_LOAD_DETAILS, APARTMENT_LOAD_LIST_PART, APARTMENT_LIST_CLEAR_LOADING, APARTMENT_SAVE_DETAILS } from '../constants/ApartmentConstants'

const initialState = {
  cache : new Map(),
  apartments: [],
  fetching: false,
  error: ''
};

export default function Apartments(state = initialState, action) {
  console.log('page reducer, state:', action.type, state, action.payload)
  switch (action.type) {
    case APARTMENT_LOAD_LIST_PART:
      return { ...state, apartments: [...action.payload] }
    case APARTMENT_LIST_CLEAR_LOADING:
      return { ...state, apartments: [] }
    case APARTMENT_SAVE_DETAILS:
      return { ...state }
    case APARTMENT_LOAD_DETAILS:
      return { ...state }
    default:
      return state
  }
}
