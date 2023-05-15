const host = 'http://127.0.0.1:8000'
const state = {
  dbsource: [
  ],
  tempsource: {},
  uid: 1,
}
const mutations = {
  CLEAR_DBSOURCE(state) {
    state.dbsource = []
  },
  SET_DBSOURCE(state, value) {
    state.dbsource.push(
      {
        'index': state.dbsource.length + 1,
        'id': value.linkId ,
        'sysname': value.linkSystemName,
        'dbtype': value.linkDbType,
        'ip': value.linkDbIp,
        'dbname': value.linkDbName,
        'username': value.linkUserName,
        'pwd': value.linkPassword,
        'port': value.linkPort,
        'addon': value.linkComment,
      }
    )
    // console.log(state.tempsource)
  },
  SET_TEMPSOURCE(state, value){
    state.tempsource.userId = value.uid
    state.tempsource.linkId = value.id
    state.tempsource.linkSystemName = value.sysname
    state.tempsource.linkDbType = value.dbtype
    state.tempsource.linkDbIp = value.ip
    state.tempsource.linkDbName = value.dbname
    state.tempsource.linkUserName = value.username
    state.tempsource.linkPassword = value.pwd
    state.tempsource.linkPort = value.port
    state.tempsource.linkComment = value.addon
  }
}
const actions = {
  setdbsource({commit, rootState}, value) {
    value.forEach(item => {
      if(item.userId === rootState.user.id){
        commit('SET_DBSOURCE',item)
      }
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}