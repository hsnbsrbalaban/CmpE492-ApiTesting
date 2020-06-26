import axios from 'axios'
import store from '@/store'

const service = axios.create({
  baseURL: 'http://main.apitesting.info',
  timeout: 5000
})

export function getProjects() {
  const url = 'projects'
  return service.get(url)
    .then(response => {
      console.log(response.status)
      store.dispatch('projects/setProjects', response.data)
      return response.data
    })
    .catch(error => {
      console.log(error.toString())
    })
}

export function createProject(hostname) {
  const url = 'createProject?hostname=' + hostname
  return service.post(url)
    .then(response => {
      console.log('Create Project Success')
      return response.status
    })
    .catch(error => {
      console.log(error.toString())
    })
}

export function deleteProject(hostname) {
  const url = 'deleteProject?hostname=' + hostname
  return service.delete(url)
    .then(response => {
      console.log('Create Project Success')
      return response.status
    })
    .catch(error => {
      console.log(error.toString())
    })
}
