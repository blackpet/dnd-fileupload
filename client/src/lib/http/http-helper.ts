import axios from 'axios';

////////////// common axios config ///////////////

axios.defaults.headers.post['Content-Type'] = 'appilcation/json'
axios.defaults.headers.put['Content-Type'] = 'appilcation/json'
axios.defaults.headers.delete['Content-Type'] = 'appilcation/json'

axios.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    // TODO simjaeho: error handling 하자!
    // 401: Unauthorized
    if (error?.response?.status === 401) {
      console.error('[axios response] 401: Unauthorized')
    }
    // 403: Forbidden
    else if (error?.response?.status === 403) {
      console.error('[axios response] 403: Forbidden')
    }

    return Promise.reject(error)
  }
)

export default axios


////////////// [client] remote API endpoint ///////////////
export const api = axios.create({
  baseURL: import.meta.env.VITE_API_PATH,
  timeout: 10000,
})

api.interceptors.request.use((config) => {
  // request intercept codes here...
  // config.headers!['Authorization'] = 'Bearer abcd'
  return config
})

api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    // error response intercept codes here...
    // error?.response?.status === 401

  }
)
