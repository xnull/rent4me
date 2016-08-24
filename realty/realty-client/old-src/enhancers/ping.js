export const ping = store => next => action => {
      console.log(`ping by ${action.type}, payload: ${action.payload}`)
      return next(action)
    }
