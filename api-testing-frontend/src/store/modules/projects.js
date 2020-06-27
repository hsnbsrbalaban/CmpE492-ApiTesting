const state = {
  projects: []
}

const mutations = {
  SET_PROJECTS: (state, projects) => {
    state.projects = projects
  }
}

const actions = {
  setProjects({ commit }, projects) {
    commit('SET_PROJECTS', projects)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
