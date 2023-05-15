import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'
//数据库查询使用的vuex文件
import dbsource from './modules/dbsource'
import sqlquery from './modules/sqlquery'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user,
    dbsource,
    sqlquery,
  },
  getters
})

export default store
