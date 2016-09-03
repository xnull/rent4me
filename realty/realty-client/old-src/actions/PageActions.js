import { GET_PHOTOS_REQUEST, GET_PHOTOS_SUCCESS, GET_PHOTOS_FAIL } from '../constants/Page'

let photosArr = []
let cached = false

function makeYearPhotos(photos, selectedYear) {
  let createdYear, yearPhotos = []
  photos.forEach((item) => {
    createdYear = new Date(item.created*1000).getFullYear()
    if( createdYear === selectedYear ) {
      yearPhotos.push(item)
    }
  })

  yearPhotos.sort((a, b) => b.likes.count - a.likes.count)

  return yearPhotos
}

function getMorePhotos(offset, count, year, dispatch) {
  console.log(`Get more photos ${offset}`)
  VK.Api.call('photos.getAll', {extended: 1, count: count, offset: offset}, (r) => {
    alert(42)
  })
  if(1==1) return
  // if(1==1) return
  // VK.Api.call('users.get', {user_ids: 6492}, function(r) {
  //   if(r.response) {
  //     alert('Привет, ' + r.response[0].first_name);
  //   }
  // });
  // if(1==1) return
  VK.Api.call('photos.getAll', {extended: 1, count: count, offset: offset}, (r) => {
    console.log('Callback')
    try {
      if( offset <= r.response[0] - count ) {
        photosArr = photosArr.concat(r.response)
        getMorePhotos(offset + 1, count, year, dispatch)
      } else {
        let photos = makeYearPhotos(photosArr, year)
        cached = true
        dispatch({
          type: GET_PHOTOS_SUCCESS,
          payload: photos
        })
      }
    } catch (e) {
      dispatch({
        type: GET_PHOTOS_FAIL,
        error: true,
        payload: new Error(e)
      })
    }
  })
}

export function getPhotos(year) {
  return (dispatch) => {
    dispatch({
      type: GET_PHOTOS_REQUEST,
      payload: year
    })

    console.log('cached?' + cached)
    if(cached) {
      let photos = makeYearPhotos(photosArr, year)
      dispatch({
        type: GET_PHOTOS_SUCCESS,
        payload: photos
      })
    } else {
      getMorePhotos(0, 3, year, dispatch)
    }

    // setTimeout(() => {
    //   dispatch({
    //     type: GET_PHOTOS_SUCCESS,
    //     payload: [1, 2, 3, 4, 5]
    //   })
    // }, 1000)
  }
}
