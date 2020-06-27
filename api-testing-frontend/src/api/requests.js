import axios from 'axios'

const service = axios.create({
  baseURL: 'http://main.apitesting.info',
  timeout: 5000
})

export function getRequests(hostname) {
  const url = 'requests/' + hostname
  return service.get(url)
    .then(response => {
      console.log(response.status)
      return response.data
    })
    .catch(error => {
      console.log(error.toString())
    })
}

export function getSingleRequest(hostname, id) {
  const url = 'requests/' + hostname + '/id/' + id
  return service.get(url)
    .then(response => {
      console.log(response.status)
      return response.data
    })
    .catch(error => {
      console.log(error.toString())
    })
}

export function modifyRequest(request) {
  const url = 'fuzz'
  const headers = {
    'Content-Type': 'application/json'
  }

  return service.post(url, request, {
    "headers": headers
  }).then(response => {
      console.log(response.status)
      return response
    })
    .catch(error => {
      console.log(error.toString())
    })
}
