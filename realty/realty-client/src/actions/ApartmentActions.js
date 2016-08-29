/**
 * Created by dionis on 28/08/16.
 */
import {
    APARTMENT_SAVE_DETAILS,
    APARTMENT_LOAD_LIST_PART,
    APARTMENT_LOAD_DETAILS,
    APARTMENT_LIST_CLEAR_LOADING
} from '../constants/ApartmentConstants'

import 'whatwg-fetch';

var cache = new Map()
var listCache = []

export function clearApartmentSearch() {
    return (dispatch) => {
        dispatch({
            type: APARTMENT_LIST_CLEAR_LOADING
        })

        const newId = () => new Date().getTime() + ""

        fetch('http://rent4.me/rest/cities/search?lng=24.6817121&lat=59.397131900000005')
            .then(function (response) {
                console.log('then')
                return response.json()
            })
            .then(function (json) {
                dispatch({
                    type: APARTMENT_LOAD_LIST_PART,
                    payload: [
                        {
                            id: "1",
                            address: "Bla bla street",
                            description: "Lorem ipsum",
                            rented: true,
                            previewImage: 'http://unsplash.it/600/300?image=1081'
                        },

                        {
                            id: "2",
                            address: "Ololo",
                            description: "Ararara",
                            rented: false,
                            previewImage: 'http://unsplash.it/600/300?image=1080'
                        },

                        {
                            id: "3",
                            address: "Bla bla street asdasd",
                            description: "Lorem ipsum",
                            rented: true,
                            previewImage: 'http://unsplash.it/600/300?image=1079'
                        },

                        {
                            id: "4",
                            address: "Hahahah",
                            description: "Ararara",
                            rented: false,
                            previewImage: 'http://unsplash.it/600/300?image=1078'
                        },

                        {
                            id: "5",
                            address: "Elm street",
                            description: "Lorem ipsum",
                            rented: true,
                            previewImage: 'http://unsplash.it/600/300?image=1077'
                        },

                        {
                            id: "6",
                            address: "Chichuahua",
                            description: "Ararara",
                            rented: false,
                            previewImage: 'http://unsplash.it/600/300'
                        }
                    ]
                })
                console.log('parsed json', json)
            }).catch(function (ex) {
            console.log('parsing failed', ex)
        });
    }
}
